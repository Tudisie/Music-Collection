package exceptions.ReleaseExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Some required parameters are missing")
public class ReleaseMissingFieldException extends RuntimeException{

    public ReleaseMissingFieldException(String message){
        super(message);
    }
}
