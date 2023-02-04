package com.pos.laborator.L05X.controller;

import com.pos.laborator.L05X.model.DTO.ReleaseAndArtistNames;
import com.pos.laborator.L05X.model.entity.ReleaseArtists;
import com.pos.laborator.L05X.service.interfaces.ReleaseArtistsService;
import exceptions.ReleaseArtistsExceptions.ArtistReleasesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/songcollection")
public class ReleaseArtistsController {

    @Autowired
    private ReleaseArtistsService releaseArtistsService;

    /*@GetMapping("/release-artists")
    public ResponseEntity<List<ReleaseArtists>> getAllReleaseArtists() {
        List<ReleaseArtists> releaseArtists = releaseArtistsRepository.findAll();
        return ResponseEntity.ok().body(releaseArtists);
    }*/

    @GetMapping("/artists/releases")
    public ResponseEntity<List<ReleaseAndArtistNames>> getAllArtistsReleases(){
        return releaseArtistsService.getAllArtistsReleases();
    }

    @GetMapping("/artists/{uuid}/releases")
    public ResponseEntity<List<String>> getArtistReleases(@PathVariable(value = "uuid") String uuid){
        return releaseArtistsService.getArtistReleases(uuid);
    }

    @GetMapping("/releases/{id}/artists")
    public ResponseEntity<List<String>> getReleaseArtists(@PathVariable(value = "id") Integer id){
        return releaseArtistsService.getReleaseArtists(id);
    }

    @PostMapping("/artists/releases")
    public ResponseEntity<ReleaseArtists> createArtistRelease(@RequestBody ReleaseArtists ra, @RequestHeader(value="Authorization",required=false) String token)
            throws Exception {
        return releaseArtistsService.createArtistRelease(ra, token);
    }

    @DeleteMapping("/artists/releases")
    public ResponseEntity<ReleaseArtists> deleteArtistRelease(@RequestBody ReleaseArtists ra, @RequestHeader(value="Authorization",required=false) String token)
            throws ArtistReleasesNotFoundException{
        return releaseArtistsService.deleteArtistRelease(ra, token);
    }
}