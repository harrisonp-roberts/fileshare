package dev.hroberts.fileshare.fileupload.controllers.exception;

import dev.hroberts.fileshare.fileupload.domain.domainexceptions.IDomainException;
import dev.hroberts.fileshare.fileupload.application.services.exceptions.InvalidHashAlgorithmException;
import dev.hroberts.fileshare.fileupload.application.services.exceptions.UploadAlreadyCompletedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class UserExceptionHandlers {
    @ExceptionHandler(UploadAlreadyCompletedException.class)
    public ResponseEntity<ErrorResponse> handleException(UploadAlreadyCompletedException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(InvalidHashAlgorithmException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidHashAlgorithmException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(IDomainException.class)
    public ResponseEntity<ErrorResponse> handleException(IDomainException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

}
