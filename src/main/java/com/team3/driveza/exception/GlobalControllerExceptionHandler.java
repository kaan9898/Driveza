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

    // This advice intercepts exceptions thrown by any @Controller and prepares the shared error page.

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex,
                                      HttpServletRequest request,
                                      HttpServletResponse response,
                                      Model model) {
        // Happens when Thymeleaf binding can't obtain user input into the form object.
        // We collect each field error so the UI can show the user what to fix.
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
        // This fires when @Valid validation annotations fail on controller method arguments.
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        return handleValidation(request, response, model, details);
    }

    private String handleValidation(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Model model,
                                    List<String> details) {
        // Shared helper for any validation-type failure so we return 400 and the same error template.
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
        // When a required request parameter is missing, explain that in the error page with 400.
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
        // If we manually throw ConstraintViolationException (e.g. in service-layer validation), treat it like other bad requests.
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
        // Any time a controller asks for an entity and it isn't found we throw ResourceNotFoundException, so this turns that into 404 + friendly view.
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
        // Business rules like "vehicle already rented" or "email taken" should render 409 so the UI can explain why the request failed.
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
        // When Spring Security decides the current user is forbidden, show the error page with status 403.
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
        // Fall back to 400 for other validation-type problems where an IllegalArgumentException is thrown.
        HttpStatus status = HttpStatus.BAD_REQUEST;
        response.setStatus(status.value());
        model.addAttribute(ERROR_ATTR, errorResponseFactory.build(status, ex.getMessage(), request, Collections.emptyList()));
        return "error/error";
    }

}
