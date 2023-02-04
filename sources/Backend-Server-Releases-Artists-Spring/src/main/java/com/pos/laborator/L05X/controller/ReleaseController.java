package com.pos.laborator.L05X.controller;

import com.pos.laborator.L05X.model.DTO.ReleaseDTO;
import com.pos.laborator.L05X.service.interfaces.ReleaseService;
import exceptions.ReleaseExceptions.ReleaseNotFoundException;
import com.pos.laborator.L05X.model.entity.Release;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/songcollection")
public class ReleaseController {

    @Autowired
    ReleaseService releaseService;

    @GetMapping("/releases")
    public ResponseEntity<CollectionModel<List<ReleaseDTO>>> getAllReleases(@RequestParam(value="page", required = false) Integer page,
                                                                            @RequestParam(value="items_per_page", defaultValue = "5") Integer items_per_page,
                                                                            @RequestParam(value="name", required = false) String name,
                                                                            @RequestParam(value="match", required = false) String match,
                                                                            @RequestParam(value="search_by", required = false) String search_by){
        return releaseService.getAllReleases(page, items_per_page, name, match, search_by);
    }

    @GetMapping("/releases/pages")
    public ResponseEntity<Integer> getNumberOfPages(@RequestParam(value="items_per_page", defaultValue = "5") Integer items_per_page){
        return releaseService.getNumberOfPages(items_per_page);
    }

    @GetMapping("/releases/{id}")
    public ResponseEntity<Release> getReleaseById(@PathVariable(value = "id") Integer id)
            throws ReleaseNotFoundException{
        return releaseService.getReleaseById(id);
    }

    @GetMapping("/releases/name={name}")
    public ResponseEntity<Release> getReleaseByName(@PathVariable(value="name") String name){
        return releaseService.getReleaseByName(name);
    }

    @GetMapping("/albums")
    public ResponseEntity<Release> getAlbumByName(@RequestParam(value="name") String name){
        return releaseService.getAlbumByName(name);
    }

    @GetMapping("/releases/genres")
    public List<String> getReleaseClassGenres(){
        return releaseService.getReleaseClassGenres();
    }

    @PostMapping("/releases")
    public ResponseEntity<Release> createRelease(@RequestBody Release release, @RequestHeader(value="Authorization",required=false) String token)
            throws Exception {
        return releaseService.createRelease(release, token);
    }

    @PutMapping("/releases/{id}")
    public ResponseEntity<Release> updateRelease(@RequestBody Release newRelease, @PathVariable(value = "id") Integer id, @RequestHeader(value="Authorization",required=false) String token)
            throws Exception {
        return releaseService.updateRelease(newRelease, id, token);
    }

    @DeleteMapping("/releases/{id}")
    public ResponseEntity<Release> deleteRelease(@PathVariable(value = "id") Integer id, @RequestHeader(value="Authorization",required=false) String token)
            throws ReleaseNotFoundException{
        return releaseService.deleteRelease(id, token);
    }
}
