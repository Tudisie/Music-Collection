package com.mongodb_example.main.music_playlists.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "playlists")
public class Playlist {

    @Id
    private String id;

    private String title;

    private Integer user_id;

    private List<Release> releases;

    public Playlist(String title, Integer user_id, List<Release> releases){
        super();
        this.title = title;
        this.user_id = user_id;
        this.releases = releases;
    }
}
