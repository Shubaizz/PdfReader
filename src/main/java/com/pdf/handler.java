package com.pdf;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class handler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public String error(HttpServletRequest request, Exception e){
        return "error";
    }
}
