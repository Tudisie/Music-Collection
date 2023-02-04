package com.pos.laborator.L05X.service.interfaces;

import com.pos.laborator.L05X.model.DTO.ReleaseAndArtistNames;
import com.pos.laborator.L05X.model.entity.ReleaseArtists;
import exceptions.ReleaseArtistsExceptions.ArtistReleasesNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReleaseArtistsService {
    ResponseEntity<List<ReleaseAndArtistNames>> getAllArtistsReleases();

    ResponseEntity<List<String>> getArtistReleases(String uuid);

    ResponseEntity<List<String>> getReleaseArtists(Integer id);

    ResponseEntity<ReleaseArtists> createArtistRelease(ReleaseArtists ra, String token) throws Exception;

    ResponseEntity<ReleaseArtists> deleteArtistRelease(ReleaseArtists ra, String token) throws ArtistReleasesNotFoundException;
}
