package com.team3.driveza.Dto.Common;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ErrorResponseDto {
    private int status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;
    private List<String> details; // for more detailed, richer feedback (OPTIONAL)
}
