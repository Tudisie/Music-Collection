package exceptions.ReleaseExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Release not found")
public class ReleaseNotFoundException  extends RuntimeException{
    public ReleaseNotFoundException(Integer id){
        super("Resource not found for id: " + id);
    }
}
