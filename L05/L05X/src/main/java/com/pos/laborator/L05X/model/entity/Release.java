package com.pos.laborator.L05X.model.entity;

import org.springframework.hateoas.RepresentationModel;
import javax.persistence.*;

@Entity
@Table(name = "releases")
public class Release extends RepresentationModel<Release> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "genre", nullable = false)
    private String genre;
    @Column(name = "release_year")
    private Integer releaseYear;
    @Column(name = "category", nullable = false)
    private String category;
    @Column(name = "album_id")
    private Integer album_id;

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getGenre(){
        return genre;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public Integer getReleaseYear(){
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear){
        this.releaseYear = releaseYear;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public Integer getAlbumId(){
        return album_id;
    }

    public void setAlbumId(Integer album_id){
        this.album_id = album_id;
    }
}
