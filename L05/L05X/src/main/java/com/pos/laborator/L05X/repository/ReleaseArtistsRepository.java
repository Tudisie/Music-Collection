package com.pos.laborator.L05X.repository;

import com.pos.laborator.L05X.model.DTO.ReleaseAndArtistNames;
import com.pos.laborator.L05X.model.entity.ReleaseArtists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReleaseArtistsRepository extends JpaRepository<ReleaseArtists, Integer> {

    String DTOpath = "com.pos.laborator.L05X.model.DTO.ReleaseAndArtistNames";
    String getAll = "SELECT new " + DTOpath + "(r.name, a.name) FROM ReleaseArtists ra, Release r, Artist a WHERE r.id = ra.releaseId AND ra.artistId = a.id";

    @Query(getAll)
    public List<ReleaseAndArtistNames> getAllNames();

    @Query(getAll + " AND ra.artistId = ?1")
    public List<ReleaseAndArtistNames> searchNamesByArtistName(String uuid);

    @Query(getAll + " AND ra.releaseId = ?1")
    public List<ReleaseAndArtistNames> searchNamesByReleaseName(Integer id);

    @Query("SELECT COUNT(*) > 0 FROM ReleaseArtists ra WHERE ra.releaseId = ?1 AND ra.artistId = ?2")
    public Boolean recordExists(Integer release_id, String artist_id);
}
