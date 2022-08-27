package br.com.atech.usermanager.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionDetails {
    private final String message;
    private final int status;
    private final LocalDateTime timestamp;

    public ExceptionDetails(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
