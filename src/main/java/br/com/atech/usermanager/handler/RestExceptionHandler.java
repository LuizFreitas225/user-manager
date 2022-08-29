package br.com.atech.usermanager.handler;

import br.com.atech.usermanager.constant.ErrorMessage;
import br.com.atech.usermanager.exception.EmailInUseException;
import br.com.atech.usermanager.exception.ExceptionDetails;
import br.com.atech.usermanager.exception.ShortPasswordException;
import br.com.atech.usermanager.exception.UserIsDeletedException;
import br.com.atech.usermanager.exception.UserIsInactiveException;
import br.com.atech.usermanager.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserIsDeletedException.class)
    public ResponseEntity<Object> userIsDeletedException(UserIsDeletedException exception) {
        return new ResponseEntity(new ExceptionDetails(ErrorMessage.DELETED_STATUS, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsInactiveException.class)
    public ResponseEntity<Object> userIsInactive(UserIsDeletedException exception) {
        return new ResponseEntity(new ExceptionDetails(ErrorMessage.INACTIVE_STATUS, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> userNotFoundException(UserIsDeletedException exception) {
        return new ResponseEntity(new ExceptionDetails(ErrorMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailInUseException.class)
    public ResponseEntity<Object> emailInUseException(EmailInUseException exception) {
        return new ResponseEntity(new ExceptionDetails(ErrorMessage.EMAIL_IN_USE, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ShortPasswordException.class)
    public ResponseEntity<Object> invalidPasswordException(ShortPasswordException exception) {
        return new ResponseEntity(new ExceptionDetails(ErrorMessage.SHORT_PASSWORD, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errors = ex.getBindingResult().getAllErrors().stream().map(
                error -> error.getDefaultMessage()).collect(Collectors.toList()).toString();
        return new ResponseEntity(new ExceptionDetails(errors, status.value()), status);
    }

}
