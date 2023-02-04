package com.mongodb_example.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A release with the same required parameters already exists")
public class UserExistsException extends RuntimeException {
    public UserExistsException(String id){
        super("User exists with the same unique fields: " + id);
    }
}
