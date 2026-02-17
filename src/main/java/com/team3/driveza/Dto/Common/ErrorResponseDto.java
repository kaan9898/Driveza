package com.team3.driveza.Dto.Common;

import java.util.Date;

public class ErrorResponseDto {
    private String message;
    private String error;
    private enum statusCode {
        SUCCESS, ERROR
    }
    private statusCode status;
    private Date timestamp;
    private String path;
}
