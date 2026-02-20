package com.team3.driveza.exception;

import com.team3.driveza.Dto.Common.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ErrorResponseFactory {

    public ErrorResponseDto build(HttpStatus status,
                                  String message,
                                  HttpServletRequest request,
                                  List<String> details) {
        return ErrorResponseDto.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .details(details)
                .build();
    }
}
