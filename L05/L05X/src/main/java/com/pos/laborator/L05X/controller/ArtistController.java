package com.pos.laborator.L05X.controller;

import com.pos.laborator.L05X.model.entity.Artist;
import com.pos.laborator.L05X.service.interfaces.ArtistService;
import exceptions.ArtistExceptions.ArtistNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/songcollection")
public class ArtistController {

    @Autowired
    ArtistService artistService;

    @GetMapping("/artists")
    public ResponseEntity<List<Artist>> getAllArtists(@RequestParam(value="page", required = false) Integer page,
                                                      @RequestParam(value="items_per_page", defaultValue = "5") Integer items_per_page,
                                                      @RequestParam(value="name", required = false) String name,
                                                      @RequestParam(value="match", required = false) String match){
        return artistService.getAllArtists(page, items_per_page, name, match);
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable(value = "id") String id)
            throws ArtistNotFoundException {
        return artistService.getArtistById(id);
    }

    @PostMapping("/artists")
    public ResponseEntity<Artist> createArtist(@Validated @RequestBody Artist artist, @RequestHeader(value="Authorization",required=false) String token)
        throws Exception{
        return artistService.createArtist(artist, token);
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> updateArtist(@RequestBody Artist newArtist, @PathVariable(value = "id") String id, @RequestHeader(value="Authorization",required=false) String token)
            throws Exception {
        return artistService.updateArtist(newArtist, id, token);
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Artist> deleteArtist(@PathVariable(value = "id") String id, @RequestHeader(value="Authorization",required=false) String token)
            throws ArtistNotFoundException{
        return artistService.deleteArtist(id, token);
    }
}
