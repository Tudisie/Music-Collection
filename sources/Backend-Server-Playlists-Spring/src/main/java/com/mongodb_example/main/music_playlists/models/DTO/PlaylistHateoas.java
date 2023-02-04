package com.mongodb_example.main.music_playlists.models.DTO;

import com.mongodb_example.main.music_playlists.models.Release;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

// Playlist is an Entity which maps a document in NoSQL - so it can't extend RepresentationModel<> as we used in Releases/Albums entities
public class PlaylistHateoas extends RepresentationModel<PlaylistHateoas> {

    private String id;

    private String title;

    private Integer user_id;

    private List<Release> releases;

    public PlaylistHateoas(String id, String title, Integer user_id, List<Release> releases){
        super();
        this.id = id;
        this.title = title;
        this.user_id = user_id;
        this.releases = releases;
    }

    public String getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public Integer getUser_id(){
        return this.user_id;
    }

    public List<Release> getReleases(){
        return this.releases;
    }
}
