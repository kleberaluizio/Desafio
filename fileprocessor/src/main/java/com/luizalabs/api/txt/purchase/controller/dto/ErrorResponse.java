package com.luizalabs.api.txt.purchase.controller.dto;

import com.luizalabs.api.txt.purchase.exception.InvalidFilterParameterFormatException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ErrorResponse {
    String timestamp;
    int status;
    String error;
    String message;
    List<String> details;

    private ErrorResponse(String timestamp, int status, String error, String message, List<String> details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    public static ErrorResponse from (RuntimeException e) {
        String message = e.getClass().equals(InvalidFilterParameterFormatException.class) ?
                "Invalid filter parameter format" :
                "Invalid entries found in the file";

        return new ErrorResponse(
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                List.of(e.getMessage().split("\n"))
        );
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}
