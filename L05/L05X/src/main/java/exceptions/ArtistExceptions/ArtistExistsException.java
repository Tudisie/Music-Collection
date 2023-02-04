package exceptions.ArtistExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A release with the same required parameters already exists")
public class ArtistExistsException extends RuntimeException{

    public ArtistExistsException(String message){
        super(message);
    }
}
