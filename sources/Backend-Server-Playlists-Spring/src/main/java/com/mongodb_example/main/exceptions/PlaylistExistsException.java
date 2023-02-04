package com.mongodb_example.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A playlist with the same required parameters already exists")
public class PlaylistExistsException extends RuntimeException {
    public PlaylistExistsException(String id){
        super("Playlist exists with the same unique fields: " + id);
    }
}
