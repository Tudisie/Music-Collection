package com.mongodb_example.main.exceptions.TokenExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "unauthorized")
public class Unauthorized  extends RuntimeException{
    public Unauthorized(){
        super("Unauthorized");
    }
}
