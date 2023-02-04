package com.pos.laborator.L05X.repository;

import com.pos.laborator.L05X.model.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Integer> {

    @Query("SELECT r FROM Release r ORDER BY name")
    public List<Release> getPageOfReleases(Pageable pageable);

    @Query("SELECT r FROM Release r WHERE name LIKE %?1%")
    List<Release> searchAll(String name);

    @Query("SELECT r FROM Release r WHERE release_year = ?1")
    List<Release> searchAllByYear(Integer name);

    @Query("SELECT r FROM Release r WHERE genre = ?1")
    List<Release> searchAllByGenre(String name);

    @Query("SELECT r FROM Release r WHERE name=?1")
    List<Release> searchStrictAll(String name);

    @Query("SELECT r FROM Release r WHERE name=?1")
    List<Release> findReleaseByName(String name);

    @Query("SELECT r FROM Release r WHERE name=?1 AND category='album'")
    Release findAlbumByName(String name);

    @Query("SELECT r FROM Release r WHERE id=?1")
    Release findReleaseById(Integer id);
}
