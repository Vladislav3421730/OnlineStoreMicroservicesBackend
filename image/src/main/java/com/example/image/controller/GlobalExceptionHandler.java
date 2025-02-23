package com.example.image.controller;

import com.example.image.dto.AppErrorDto;
import com.example.image.exception.ImageNotFoundException;
import com.example.image.exception.ImageUploadingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<AppErrorDto> handleEntityNotFoundException(ImageNotFoundException imageNotFoundException) {
        return new ResponseEntity<>(new AppErrorDto(imageNotFoundException.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImageUploadingException.class)
    public ResponseEntity<AppErrorDto> handleImageUploadingException(ImageUploadingException imageUploadingException) {
        return new ResponseEntity<>(new AppErrorDto(imageUploadingException.getMessage(), 400), HttpStatus.BAD_REQUEST);
    }


}
