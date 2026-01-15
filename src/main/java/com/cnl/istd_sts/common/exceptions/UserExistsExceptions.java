package com.cnl.istd_sts.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsExceptions extends RuntimeException{
    public UserExistsExceptions(String email) {
        super("User with email " + email + " already exists");
    }
}
