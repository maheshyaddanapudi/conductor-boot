package com.github.maheshyaddanapudi.netflix.conductorboot.config.rest.exception.handler;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler 
  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
      = { ZuulException.class , HttpHostConnectException.class})
    protected ResponseEntity<Object> handleConflict(
      RuntimeException ex, WebRequest request) {

        if(request.getContextPath().equalsIgnoreCase("/api/health"))
        {
            String bodyOfResponse = "Integrated Conductor Server is still starting. Please retry after few seconds.";
            return handleExceptionInternal(ex, bodyOfResponse,
                    new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        }
        else
        {
            String bodyOfResponse = "Internal Server Error - "+ex.getMessage();
            return handleExceptionInternal(ex, bodyOfResponse,
                    new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }

    }
}