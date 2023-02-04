package com.pos.laborator.L05X.service;

import antlr.Token;
import com.pos.laborator.L05X.controller.ReleaseController;
import com.pos.laborator.L05X.model.DTO.ReleaseDTO;
import com.pos.laborator.L05X.model.entity.Release;
import com.pos.laborator.L05X.repository.ReleaseRepository;
import com.pos.laborator.L05X.service.interfaces.ReleaseService;
import com.pos.laborator.L05X.service.other.TokenValidation;
import exceptions.ReleaseExceptions.ReleaseExistsException;
import exceptions.ReleaseExceptions.ReleaseMissingFieldException;
import exceptions.ReleaseExceptions.ReleaseNotFoundException;
import exceptions.TokenExceptions.Forbidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    @Autowired
    private ReleaseRepository releaseRepository;

    @Override
    public ResponseEntity<CollectionModel<List<ReleaseDTO>>> getAllReleases(Integer page, Integer items_per_page, String name, String match, String search_by) {
        List<Release> releases = null;

        if(page == null) // Show all responses
        {
            if(name == null)
                releases = releaseRepository.findAll();
            else if(Objects.equals(search_by, "year")){
                Integer year = Integer.parseInt(name);
                releases = releaseRepository.searchAllByYear(year);
            }
            else if(Objects.equals(search_by, "genre")){
                releases = releaseRepository.searchAllByGenre(name);
            }
            else if(match == null) {
                    releases = releaseRepository.searchAll(name);
            }
            else{
                releases = releaseRepository.searchStrictAll(name);
            }
        }
        else // Show page with {items_per_page} elements
        {
            Pageable p = PageRequest.of(page, items_per_page);
            releases = releaseRepository.getPageOfReleases(p);
        }

        List<ReleaseDTO> releasesDTO = new ArrayList<ReleaseDTO>();
        for (Release r : releases){
            ReleaseDTO rdto = new ReleaseDTO(r);
            String rdto_name = "-";
            if(Objects.equals(rdto.getCategory(), "song")) {
                Release album = releaseRepository.findReleaseById(r.getAlbumId());
                if(album != null)
                    rdto_name = album.getName();
            }
            rdto.setAlbumName(rdto_name);
            releasesDTO.add(rdto);
        }

        for (ReleaseDTO r : releasesDTO) {
            Link selfLink = linkTo(methodOn(ReleaseController.class).getReleaseById(r.getId())).withSelfRel();
            Link parentLink = linkTo(methodOn(ReleaseController.class).getAllReleases(page, items_per_page, name, match, search_by)).withRel("releases");
            r.add(selfLink);
            r.add(parentLink);
        }

        if(releasesDTO.size() == 0){
            return ResponseEntity.ok().body(CollectionModel.empty());
        }

        CollectionModel<List<ReleaseDTO>> releasesLinked = CollectionModel.of(Collections.singleton(releasesDTO),
                linkTo(methodOn(ReleaseController.class).getAllReleases(page, items_per_page, null,null, null)).withSelfRel());

        if(page != null)
            if(page > 0){
                Link prev = linkTo(methodOn(ReleaseController.class).getAllReleases(page-1, items_per_page, "", "", "")).withRel("previous_page");
                releasesLinked.add(prev);
            }

        List<Release> all = releaseRepository.findAll();
        int noSongs = all.size();
        int noPages = (noSongs % items_per_page == 0) ? noSongs/items_per_page : (noSongs/items_per_page + 1);
        if(page != null)
            if(page < noPages - 1) {
                Link next = linkTo(methodOn(ReleaseController.class).getAllReleases(page + 1, items_per_page, "", "", "")).withRel("next_page");
                releasesLinked.add(next);
            }

        return ResponseEntity.ok().body(releasesLinked);
    }

    public ResponseEntity<Integer> getNumberOfPages(Integer items_per_page){
        if(items_per_page == 0)
            return ResponseEntity.ok().body(0);

        float count_songs = releaseRepository.findAll().size();
        Integer res = (int)(count_songs/items_per_page);

        if(count_songs % items_per_page != 0)
            res = res + 1;
        return ResponseEntity.ok().body(res);
    }

    @Override
    public ResponseEntity<Release> getReleaseById(Integer id) {
        Release release = releaseRepository.findById(id).orElseThrow(() -> new ReleaseNotFoundException(id));

        Link selfLink = linkTo(methodOn(ReleaseController.class).getReleaseById(id)).withSelfRel();
        Link parentLink = linkTo(methodOn(ReleaseController.class).getAllReleases(null, null, null, null, null)).withRel("releases");
        release.add(selfLink);
        release.add(parentLink);

        return ResponseEntity.ok().body(release);
    }

    @Override
    public List<String> getReleaseClassGenres(){
        return Arrays.asList("rock","metal","blues","classical","thrash metal","hard rock","heavy metal","metalcore","progressive metal","alternative metal");
    }

    @Override
    public ResponseEntity<Release> getReleaseByName(String name){
        name = name.replace("+", " ");
        List<Release> releases = releaseRepository.findReleaseByName(name);

        if(releases.isEmpty())
            throw new ReleaseNotFoundException(0);

        for(Release release: releases)
            if(!Objects.equals(release.getCategory(), "album"))
                return ResponseEntity.ok().body(release);

        return ResponseEntity.ok().body(releases.get(0));
    }

    @Override
    public ResponseEntity<Release> getAlbumByName(String name){
        Release release = releaseRepository.findAlbumByName(name);

        if(release == null)
            throw new ReleaseNotFoundException(0);
        return ResponseEntity.ok().body(release);
    }

    @Override
    public ResponseEntity<Release> createRelease(Release release, String token) throws Exception {
        try {
            if(!TokenValidation.validate(token,"content manager") && !TokenValidation.validate(token, "artist"))
                throw new Forbidden();
            Release r = releaseRepository.save(release);
            Link selfLink = linkTo(methodOn(ReleaseController.class).getReleaseById(r.getId())).withSelfRel().expand(r.getId());
            r.add(selfLink);
            return ResponseEntity.status(HttpStatus.CREATED).body(r);
        }
        catch(DataIntegrityViolationException e){
            if(e.getCause().getClass().equals(org.hibernate.exception.ConstraintViolationException.class))
                throw new ReleaseExistsException(e.getMessage());
            else if(e.getCause().getClass().equals(org.hibernate.PropertyValueException.class))
                throw new ReleaseMissingFieldException(e.getMessage());
            else
                throw new Exception("Unknown Error");
        }
    }

    @Override
    public ResponseEntity<Release> updateRelease(Release newRelease, Integer id, String token) throws Exception {
        try {
            if(!TokenValidation.validate(token,"content manager") && !TokenValidation.validate(token, "artist"))
                throw new Forbidden();

            Release release = releaseRepository.findById(id).orElseThrow(() -> new ReleaseNotFoundException(id));

            // Update an entry
            release.setName(newRelease.getName());
            release.setGenre(newRelease.getGenre());
            release.setReleaseYear(newRelease.getReleaseYear());
            release.setCategory(newRelease.getCategory());
            release.setAlbumId(newRelease.getAlbumId());

            try {
                releaseRepository.save(release);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            catch(DataIntegrityViolationException e){
                if(e.getCause().getClass().equals(org.hibernate.exception.ConstraintViolationException.class))
                    throw new ReleaseExistsException(e.getMessage());
                else if(e.getCause().getClass().equals(org.hibernate.PropertyValueException.class))
                    throw new ReleaseMissingFieldException(e.getMessage());
                else
                    throw new Exception("Unknown Error");
            }
        }
        catch(ReleaseNotFoundException e)
        {
            // Create a new entry
            newRelease.setId(id);
            try {
                return ResponseEntity.status(HttpStatus.CREATED).body(releaseRepository.save(newRelease));
            }
            catch(DataIntegrityViolationException einner){
                if(einner.getCause().getClass().equals(org.hibernate.exception.ConstraintViolationException.class))
                    throw new ReleaseExistsException(einner.getMessage());
                else if(einner.getCause().getClass().equals(org.hibernate.PropertyValueException.class))
                    throw new ReleaseMissingFieldException(einner.getMessage());
                else
                    throw new Exception("Unknown Error");
            }
        }
    }

    @Override
    public ResponseEntity<Release> deleteRelease(Integer id, String token) {

        if(!TokenValidation.validate(token,"content manager") && !TokenValidation.validate(token, "artist"))
            throw new Forbidden();
        Release release = releaseRepository.findById(id).orElseThrow(() -> new ReleaseNotFoundException(id));
        Link selfLink = linkTo(methodOn(ReleaseController.class).getReleaseById(release.getId())).withSelfRel();
        release.add(selfLink);

        //send HTTP DELETE Request release(id) from all playlists

        releaseRepository.delete(release);
        return ResponseEntity.ok().body(release);
    }
}
