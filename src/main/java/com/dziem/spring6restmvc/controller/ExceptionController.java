package com.dziem.spring6restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //works globally
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
