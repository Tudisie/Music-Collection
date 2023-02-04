package com.pos.laborator.L05X.model.DTO;

import com.pos.laborator.L05X.model.entity.Release;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;

@Relation(collectionRelation = "releases", itemRelation = "release")
public class ReleaseDTO extends RepresentationModel<ReleaseDTO> {

    private Integer id;
    private String name;
    private String genre;
    private Integer releaseYear;
    private String category;
    private String album_name;

    public ReleaseDTO(Integer id, String name, String genre, Integer releaseYear, String category, String album_name){
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.category = category;
        this.album_name = album_name;
    }

    public ReleaseDTO(Release r){
        this.id = r.getId();
        this.name = r.getName();
        this.genre = r.getGenre();
        this.releaseYear = r.getReleaseYear();
        this.category = r.getCategory();
    }

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

    public String getAlbumName(){
        return album_name;
    }

    public void setAlbumName(String album_name){
        this.album_name = album_name;
    }
}
