package com.mongodb_example.main.music_playlists.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Release {
    private Integer Id;
    private String name;

    private String link;
}
