package exceptions.ArtistExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Some required parameters are missing")
public class ArtistMissingFieldException extends RuntimeException{

    public ArtistMissingFieldException(String message){
        super(message);
    }
}
