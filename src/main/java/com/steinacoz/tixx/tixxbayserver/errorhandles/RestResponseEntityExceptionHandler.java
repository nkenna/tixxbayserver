/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.errorhandles;

import com.steinacoz.tixx.tixxbayserver.errormodels.AccessDeniedException;
import com.steinacoz.tixx.tixxbayserver.errormodels.NotAuthorizedException;
import com.steinacoz.tixx.tixxbayserver.errormodels.NotFoundException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author nkenn
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler//(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      Exception ex, WebRequest request) {
        ErrorResponse ade = new ErrorResponse();
        ade.setErrorCode(HttpStatus.FORBIDDEN.value());
        ade.setMessage("You are not allowed to access this route: " + ex.getLocalizedMessage());
        ade.setStatus("failed");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ade);
    }
    
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotfoundException(
      Exception ex, WebRequest request) {
        ErrorResponse ade = new ErrorResponse();
        ade.setErrorCode(HttpStatus.NOT_FOUND.value());
        ade.setMessage("This resource was not found: " + ex.getLocalizedMessage());
        ade.setStatus("failed");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ade);
    }
    
    @ExceptionHandler//(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
      Exception ex, WebRequest request) {
        ErrorResponse ade = new ErrorResponse();
        ade.setErrorCode(HttpStatus.UNAUTHORIZED.value());
        ade.setMessage("You are not authoried: " + ex.getLocalizedMessage());
        ade.setStatus("failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ade);
    }

    
    
    
    
    
    
    
    
}

















