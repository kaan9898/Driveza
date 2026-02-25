package com.team3.driveza.controller;

import com.team3.driveza.exception.ErrorResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class ErrorPageController {

    private final ErrorResponseFactory errorResponseFactory;

    public ErrorPageController(ErrorResponseFactory errorResponseFactory) {
        this.errorResponseFactory = errorResponseFactory;
    }

    @RequestMapping("/403")
    public String accessDenied(HttpServletRequest request,
                               HttpServletResponse response,
                               Model model) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        response.setStatus(status.value());
        AccessDeniedException exception = (AccessDeniedException) request.getAttribute(WebAttributes.ACCESS_DENIED_403);
        List<String> details = exception != null
                ? Collections.singletonList(exception.getMessage())
                : Collections.emptyList();
        model.addAttribute("error", errorResponseFactory.build(status, "Access denied.", request, details));
        return "error/error";
    }
}
