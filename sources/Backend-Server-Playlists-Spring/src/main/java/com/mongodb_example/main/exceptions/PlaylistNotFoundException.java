package com.mongodb_example.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Playlist not found")
public class PlaylistNotFoundException extends RuntimeException {
    public PlaylistNotFoundException(String id){
        super("Playlist not found for id: " + id);
    }
}
