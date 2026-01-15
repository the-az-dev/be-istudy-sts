package com.cnl.istd_sts.common.managers;

import com.cnl.istd_sts.common.exceptions.CredentialsNotValid;
import com.cnl.istd_sts.common.exceptions.UserExistsExceptions;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionManager {
    @ExceptionHandler(UserExistsExceptions.class)
    public ResponseEntity<Object> handleUserExists(UserExistsExceptions ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage()); // Текст помилки
        body.put("status", HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler(CredentialsNotValid.class)
    public ResponseEntity<Object> handleAuthCredError(CredentialsNotValid ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED); // 401
    }
}
