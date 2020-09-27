/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steinacoz.tixx.tixxbayserver.errorhandles;

import com.steinacoz.tixx.tixxbayserver.errormodels.AccessDeniedException;
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

    @ExceptionHandler
    public ResponseEntity<AccessDeniedException> handleAccessDeniedException(
      Exception ex, WebRequest request) {
        AccessDeniedException ade = new AccessDeniedException();
        ade.setErrorCode(HttpStatus.FORBIDDEN);
        ade.setMessage("You are not allowed to access this route");
        ade.setStatus("failed");
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ade);
    }
    
    
    
    
}




