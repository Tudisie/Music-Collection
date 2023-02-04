package exceptions.ReleaseArtistsExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A release has assigned an artist")
public class ArtistReleasesExistsException extends RuntimeException{
    public ArtistReleasesExistsException(String uuid){
            super("A release is already set for artist with uuid: " + uuid);
        }
}
