package exceptions.ReleaseExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A release with the same required parameters already exists")
public class ReleaseExistsException extends RuntimeException{

    public ReleaseExistsException(String message){
        super(message);
    }
}
