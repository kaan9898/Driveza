package com.team3.driveza.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class GlobalControllerExceptionHandler {
    private static final String ERROR_ATTR = "error";
    private final ErrorResponseFactory errorResponseFactory;

    // This controller advice catches problems thrown inside any @Controller so we can reuse the same error view.

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex,
                                      HttpServletRequest request,
                                      HttpServletResponse response,
                                      Model model) {
        // When Spring can't bind form data to the DTO, we gather which fields failed.
        List<String> details = ex.getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        return handleValidation(request, response, model, details);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgument(MethodArgumentNotValidException ex,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       Model model) {
        // @Valid on controller arguments also triggers this: treat it like any other validation failure.
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        return handleValidation(request, response, model, details);
    }

    private String handleValidation(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Model model,
                                    List<String> details) {
        // Shared helper so every validation error yields 400 and a consistent error payload.
        HttpStatus status = HttpStatus.BAD_REQUEST;
        response.setStatus(status.value());
        model.addAttribute(ERROR_ATTR, errorResponseFactory.build(status, "Validation failed.", request, details));
        return "error/error";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParameter(MissingServletRequestParameterException ex,
                                         HttpServletRequest request,
                                         HttpServletResponse response,
                                         Model model) {
        // Skip parameter? We still want to tell the user which request value is absent.
        HttpStatus status = HttpStatus.BAD_REQUEST;
        response.setStatus(status.value());
        model.addAttribute(ERROR_ATTR, errorResponseFactory.build(status, ex.getMessage(), request, Collections.emptyList()));
        return "error/error";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(ConstraintViolationException ex,
                                            HttpServletRequest request,
                                            HttpServletResponse response,
                                            Model model) {
        // Any thrown ConstraintViolationException (often from services) maps to the same 400 experience.
        HttpStatus status = HttpStatus.BAD_REQUEST;
        response.setStatus(status.value());
        List<String> details = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());
        model.addAttribute(ERROR_ATTR, errorResponseFactory.build(status, "Invalid input.", request, details));
        return "error/error";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model) {
        // Missing entity? We map ResourceNotFoundException to 404 and explain which resource was missing.
        HttpStatus status = HttpStatus.NOT_FOUND;
        response.setStatus(status.value());
        model.addAttribute(ERROR_ATTR, errorResponseFactory.build(status, ex.getMessage(), request, Collections.emptyList()));
        return "error/error";
    }

    @ExceptionHandler(ConflictException.class)
    public String handleConflict(ConflictException ex,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model) {
        // Business rules such as "already rented" land here and render 409 so templates can show a clear message.
        HttpStatus status = HttpStatus.CONFLICT;
        response.setStatus(status.value());
        model.addAttribute(ERROR_ATTR, errorResponseFactory.build(status, ex.getMessage(), request, Collections.emptyList()));
        return "error/error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleDenied(AccessDeniedException ex,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Model model) {
        // Spring Security forwards to this when the current user lacks permissions; return 403 and a short reason.
        HttpStatus status = HttpStatus.FORBIDDEN;
        response.setStatus(status.value());
        model.addAttribute(ERROR_ATTR, errorResponseFactory.build(status, "Access denied.", request, Collections.singletonList(ex.getMessage())));
        return "error/error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex,
                                        HttpServletRequest request,
                                        HttpServletResponse response,
                                        Model model) {
        // Other illegal arguments become 400 so the UI always sees the same layout.
        HttpStatus status = HttpStatus.BAD_REQUEST;
        response.setStatus(status.value());
        model.addAttribute(ERROR_ATTR, errorResponseFactory.build(status, ex.getMessage(), request, Collections.emptyList()));
        return "error/error";
    }

}
