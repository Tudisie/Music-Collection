package com.mongodb_example.main.repositories;

import com.mongodb_example.main.music_playlists.models.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MusicPlaylistsRepository extends MongoRepository<Playlist, String> {

    @Query("{title:'?0'}")
    List<Playlist> findPlaylistsByTitle(String title);

    @Query("{user_id:?0}")
    List<Playlist> findPlaylistsByUser(Integer user_id);

    @Query("{user_id:?0, title:'?1'}")
    List<Playlist> findPlaylistsByUser(Integer user_id, String title);

    @Query("{id:'?0'}")
    Playlist findPlaylistById(String id);

}
