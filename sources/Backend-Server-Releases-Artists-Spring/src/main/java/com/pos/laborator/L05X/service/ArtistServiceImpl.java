package com.pos.laborator.L05X.service;

import com.pos.laborator.L05X.controller.ArtistController;
import com.pos.laborator.L05X.controller.ReleaseController;
import com.pos.laborator.L05X.model.entity.Artist;
import com.pos.laborator.L05X.repository.ArtistRepository;
import com.pos.laborator.L05X.service.interfaces.ArtistService;
import com.pos.laborator.L05X.service.other.TokenValidation;
import exceptions.ArtistExceptions.ArtistExistsException;
import exceptions.ArtistExceptions.ArtistMissingFieldException;
import exceptions.ArtistExceptions.ArtistNotFoundException;
import exceptions.TokenExceptions.Forbidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public ResponseEntity<List<Artist>> getAllArtists(Integer page, Integer items_per_page, String name, String match) {
        List<Artist> artists = null;

        if(page == null) // Show all responses
        {
            artists = artistRepository.findAll();

            if(name == null)
                artists = artistRepository.findAll();
            else if(match == null)
                artists = artistRepository.searchAll(name);
            else
                artists = artistRepository.searchStrictAll(name);
        }
        else // Show page with {items_per_page} elements
        {
            Pageable p = PageRequest.of(page, items_per_page);
            artists = artistRepository.getPageOfArtists(p);
        }


        for(Artist a : artists)
        {
            Link selfLink = linkTo(methodOn(ArtistController.class).getArtistById(a.getId())).withSelfRel();
            Link parentLink = linkTo(methodOn(ArtistController.class).getAllArtists(page, items_per_page, name, match)).withRel("artists");
            a.add(selfLink);
            a.add(parentLink);
        }
        return ResponseEntity.ok().body(artists);
    }

    @Override
    public ResponseEntity<Artist> getArtistById(String id) throws ArtistNotFoundException {
        Artist artist = artistRepository.findById(id);
        if(artist == null)
            throw new ArtistNotFoundException(id);

        Link selfLink = linkTo(methodOn(ArtistController.class).getArtistById(id)).withSelfRel();
        Link parentLink = linkTo(methodOn(ArtistController.class).getAllArtists(null, null, null, null)).withRel("artists");
        artist.add(selfLink);
        artist.add(parentLink);

        return ResponseEntity.ok().body(artist);
    }

    @Override
    public ResponseEntity<Artist> createArtist(Artist artist, String token) throws Exception {
        try {
            if(!TokenValidation.validate(token,"content manager"))
                throw new Forbidden();

            Artist ar = artistRepository.save(artist);
            Link selfLink = linkTo(methodOn(ArtistController.class).getArtistById(ar.getId())).withSelfRel().expand(ar.getId());
            ar.add(selfLink);
            return ResponseEntity.status(HttpStatus.CREATED).body(ar);
        }
        catch(DataIntegrityViolationException e){
            if(e.getCause().getClass().equals(org.hibernate.exception.ConstraintViolationException.class))
                throw new ArtistExistsException(e.getMessage());
            else if(e.getCause().getClass().equals(org.hibernate.PropertyValueException.class))
                throw new ArtistMissingFieldException(e.getMessage());
            else
                throw new Exception("Unknown Error");
        }
    }

    @Override
    public ResponseEntity<Artist> updateArtist(Artist newArtist, String id, String token) throws Exception {
        try {
            if(!TokenValidation.validate(token,"content manager"))
                throw new Forbidden();

            Artist artist = artistRepository.findById(id);
            if(artist == null)
                throw new ArtistNotFoundException(id);

            // Update an entry
            artist.setName(newArtist.getName());
            artist.setActive(newArtist.isActive());

            try {
                artistRepository.save(artist);
            }
            catch(DataIntegrityViolationException e){
                if(e.getCause().getClass().equals(org.hibernate.exception.ConstraintViolationException.class))
                    throw new ArtistExistsException(e.getMessage());
                else if(e.getCause().getClass().equals(org.hibernate.PropertyValueException.class))
                    throw new ArtistMissingFieldException(e.getMessage());
                else
                    throw new Exception("Unknown Error");
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        catch(ArtistNotFoundException e)
        {
            // Create a new entry
            newArtist.setId(id);
            try {
                return ResponseEntity.status(HttpStatus.CREATED).body(artistRepository.save(newArtist));
            }
            catch(DataIntegrityViolationException einner){
                if(einner.getCause().getClass().equals(org.hibernate.exception.ConstraintViolationException.class))
                    throw new ArtistExistsException(einner.getMessage());
                else if(einner.getCause().getClass().equals(org.hibernate.PropertyValueException.class))
                    throw new ArtistMissingFieldException(einner.getMessage());
                else
                    throw new Exception("Unknown Error");
            }
        }
    }

    @Override
    public ResponseEntity<Artist> deleteArtist(String id, String token) {
        if(!TokenValidation.validate(token,"content manager"))
            throw new Forbidden();

        Artist artist = artistRepository.findById(id);

        if(artist == null)
            throw new ArtistNotFoundException(id);

        artistRepository.delete(artist);

        Link selfLink = linkTo(methodOn(ArtistController.class).getArtistById(artist.getId())).withSelfRel();
        artist.add(selfLink);
        return ResponseEntity.ok().body(artist);
    }
}
