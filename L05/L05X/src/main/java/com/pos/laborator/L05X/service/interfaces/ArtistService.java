package com.pos.laborator.L05X.service.interfaces;

import com.pos.laborator.L05X.model.entity.Artist;
import exceptions.ArtistExceptions.ArtistNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ArtistService {
    ResponseEntity<List<Artist>> getAllArtists(Integer page, Integer items_per_page, String name, String match);

    ResponseEntity<Artist> getArtistById(String id) throws ArtistNotFoundException;

    ResponseEntity<Artist> createArtist(Artist artist, String token) throws Exception;

    ResponseEntity<Artist> updateArtist(Artist newArtist, String id, String token) throws Exception;

    ResponseEntity<Artist> deleteArtist(String id, String token);
}
