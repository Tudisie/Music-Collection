package com.pos.laborator.L05X.model.DTO;

import org.springframework.hateoas.RepresentationModel;

public class ReleaseAndArtistNames extends RepresentationModel<ReleaseAndArtistNames> {
    private String releaseName;
    private String artistName;

    public ReleaseAndArtistNames(String releaseName, String artistName){
        this.releaseName = releaseName;
        this.artistName = artistName;
    }

    public String getReleaseName(){
        return this.releaseName;
    }

    public void setReleaseName(String releaseName){
        this.releaseName = releaseName;
    }

    public String getArtistName(){
        return this.artistName;
    }

    public void setArtistName(String artistName){
        this.artistName = artistName;
    }
}
