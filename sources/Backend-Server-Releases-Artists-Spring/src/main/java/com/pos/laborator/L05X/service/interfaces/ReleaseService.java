package com.pos.laborator.L05X.service.interfaces;

import com.pos.laborator.L05X.model.DTO.ReleaseDTO;
import com.pos.laborator.L05X.model.entity.Release;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReleaseService {
    ResponseEntity<CollectionModel<List<ReleaseDTO>>> getAllReleases(Integer page, Integer items_per_page, String name, String match, String search_by);

    ResponseEntity<Integer> getNumberOfPages(Integer items_per_page);
    ResponseEntity<Release> getReleaseById(Integer id);
    ResponseEntity<Release> getReleaseByName(String name);
    ResponseEntity<Release> getAlbumByName(String name);
    List<String> getReleaseClassGenres();
    ResponseEntity<Release> createRelease(Release release, String token) throws Exception;
    ResponseEntity<Release> updateRelease(Release newRelease, Integer id, String token) throws Exception;
    ResponseEntity<Release> deleteRelease(Integer id, String token);
}
