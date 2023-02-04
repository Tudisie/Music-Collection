package com.mongodb_example.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Some required parameters are missing")
public class PlaylistMissingFieldException extends RuntimeException {
    public PlaylistMissingFieldException(){
        super("Missing fields for requested resource");
    }
}
