package exceptions.ReleaseArtistsExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Releases not found for this artist")
public class ArtistReleasesNotFoundException  extends RuntimeException{
    public ArtistReleasesNotFoundException(String uuid){
        super("Releases not found for artist with uuid: " + uuid);
    }
}
