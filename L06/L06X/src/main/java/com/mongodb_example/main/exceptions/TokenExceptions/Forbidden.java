package com.mongodb_example.main.exceptions.TokenExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "forbidden")
public class Forbidden  extends RuntimeException{
    public Forbidden(){
        super("Forbidden");
    }
}
