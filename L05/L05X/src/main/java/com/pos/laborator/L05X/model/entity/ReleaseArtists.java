package com.pos.laborator.L05X.model.entity;

import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@Entity
@Table(name = "release_artists")
public class ReleaseArtists extends RepresentationModel<ReleaseArtists> {

    @Id
    private Integer releaseId;

    @Column(name = "artist_id", nullable = false)
    private String artistId;

    public Integer getReleaseId(){
        return this.releaseId;
    }

    public void setReleaseId(int releaseId){
        this.releaseId = releaseId;
    }

    public String getArtistId(){
        return this.artistId;
    }

    public void setArtistId(String artistId)
    {
            this.artistId = artistId;
    }
}
