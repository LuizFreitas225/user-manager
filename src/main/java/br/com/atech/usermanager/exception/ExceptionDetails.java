package br.com.atech.usermanager.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionDetails {
    private String message;
    private String title;
    private int status;
    private LocalDateTime timestamp;
}
