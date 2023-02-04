package exceptions.ArtistExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Release not found")
public class ArtistNotFoundException  extends RuntimeException{
    public ArtistNotFoundException(String id){
        super("Resource not found for id: " + id);
    }
}
