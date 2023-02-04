package com.pos.laborator.L05X.repository;

import com.pos.laborator.L05X.model.entity.Artist;
import com.pos.laborator.L05X.model.entity.Release;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Artist findById(String id);

    @Query("SELECT a FROM Artist a ORDER BY name")
    List<Artist> getPageOfArtists(Pageable pageable);

    @Query("SELECT a FROM Artist a WHERE name LIKE %?1%")
    List<Artist> searchAll(String name);

    @Query("SELECT a FROM Artist a WHERE name=?1")
    List<Artist> searchStrictAll(String name);
}
