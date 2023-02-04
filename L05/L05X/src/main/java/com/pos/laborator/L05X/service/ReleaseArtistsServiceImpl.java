package com.pos.laborator.L05X.service;

import com.pos.laborator.L05X.controller.ReleaseArtistsController;
import com.pos.laborator.L05X.model.DTO.ReleaseAndArtistNames;
import com.pos.laborator.L05X.model.entity.ReleaseArtists;
import com.pos.laborator.L05X.repository.ReleaseArtistsRepository;
import com.pos.laborator.L05X.service.interfaces.ReleaseArtistsService;
import com.pos.laborator.L05X.service.other.TokenValidation;
import exceptions.ReleaseArtistsExceptions.ArtistReleasesExistsException;
import exceptions.ReleaseArtistsExceptions.ArtistReleasesNotFoundException;
import exceptions.TokenExceptions.Forbidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ReleaseArtistsServiceImpl implements ReleaseArtistsService {

    @Autowired
    private ReleaseArtistsRepository releaseArtistsRepository;

    @Override
    public ResponseEntity<List<ReleaseAndArtistNames>> getAllArtistsReleases() {
        List<ReleaseAndArtistNames> artistReleases = releaseArtistsRepository.getAllNames();
        for(ReleaseAndArtistNames ar: artistReleases) {
            //Link selfLink = linkTo(methodOn(ReleaseArtistsController.class).getArtistReleases()).withSelfRel();
            Link parentLink = linkTo(methodOn(ReleaseArtistsController.class).getAllArtistsReleases()).withRel("artist_releases");
            //ar.add(selfLink);
            ar.add(parentLink);
        }
        return ResponseEntity.ok().body(artistReleases);
    }

    @Override
    public ResponseEntity<List<String>> getReleaseArtists(Integer id){
        List<String> artists = new ArrayList<>();
        List<ReleaseAndArtistNames> artistReleases = releaseArtistsRepository.searchNamesByReleaseName(id);

        for(ReleaseAndArtistNames ar: artistReleases){
            artists.add(ar.getArtistName());
        }

        return ResponseEntity.ok().body(artists);
    }

    @Override
    public ResponseEntity<List<String>> getArtistReleases(String uuid) {
        List<ReleaseAndArtistNames> artistReleases = releaseArtistsRepository.searchNamesByArtistName(uuid);
        List<String> releases = new ArrayList<>();

        if(artistReleases.isEmpty())
            throw new ArtistReleasesNotFoundException(uuid);

        for(ReleaseAndArtistNames ar: artistReleases) {
            //Link selfLink = linkTo(methodOn(ReleaseArtistsController.class).getArtistReleases(uuid)).withSelfRel();
            //Link parentLink = linkTo(methodOn(ReleaseArtistsController.class).getAllArtistsReleases()).withRel("artist_releases");
            //ar.add(selfLink);
            //ar.add(parentLink);
            releases.add(ar.getReleaseName());
        }
        return ResponseEntity.ok().body(releases);
    }

    @Override
    public ResponseEntity<ReleaseArtists> createArtistRelease(ReleaseArtists ra, String token) throws Exception {
        if(!TokenValidation.validate(token,"content manager"))
            throw new Forbidden();

        if (releaseArtistsRepository.recordExists(ra.getReleaseId(), ra.getArtistId()))
            throw new ArtistReleasesExistsException(ra.getArtistId());

        return ResponseEntity.status(HttpStatus.CREATED).body(releaseArtistsRepository.save(ra));

    }

    @Override
    public ResponseEntity<ReleaseArtists> deleteArtistRelease(ReleaseArtists ra, String token) throws ArtistReleasesNotFoundException {
        if(!TokenValidation.validate(token,"content manager"))
            throw new Forbidden();

        if (!releaseArtistsRepository.recordExists(ra.getReleaseId(), ra.getArtistId()))
            throw new ArtistReleasesNotFoundException(ra.getArtistId());

        releaseArtistsRepository.delete(ra);
        return ResponseEntity.ok().body(ra);
    }
}
