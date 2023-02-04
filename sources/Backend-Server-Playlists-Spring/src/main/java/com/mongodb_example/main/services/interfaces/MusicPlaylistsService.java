package com.mongodb_example.main.services.interfaces;

import com.mongodb_example.main.exceptions.PlaylistExistsException;
import com.mongodb_example.main.exceptions.PlaylistMissingFieldException;
import com.mongodb_example.main.exceptions.PlaylistNotFoundException;
import com.mongodb_example.main.music_playlists.models.Playlist;
import com.mongodb_example.main.music_playlists.models.DTO.PlaylistHateoas;
import com.mongodb_example.main.music_playlists.models.Release;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface MusicPlaylistsService {
    String getIdByTitle(String title);

    List<PlaylistHateoas> getAllPlaylists();

    List<PlaylistHateoas> getUserPlaylists(Integer user_id);

    PlaylistHateoas getPlaylistById(String id);

    List<PlaylistHateoas> getPlaylistsByTitle(String name);

    List<PlaylistHateoas> getUserPlaylistsByTitle(Integer user_id, String name);

    ResponseEntity<PlaylistHateoas> createPlaylist(Playlist playlist, String token) throws PlaylistExistsException, PlaylistMissingFieldException;

    ResponseEntity<Playlist> updatePlaylist(Playlist newPlaylist, String id, String token)
            throws PlaylistMissingFieldException, PlaylistNotFoundException, PlaylistExistsException, IOException;

    ResponseEntity<Playlist> updatePartiallyPlaylist(Playlist newPlaylist, String id, String token)
            throws PlaylistNotFoundException, PlaylistExistsException, IOException;

    Playlist removePlaylist(String id, String token) throws PlaylistNotFoundException;

    ResponseEntity<Playlist> addReleaseToPlaylist(Release release, String id, String token) throws IOException;

    ResponseEntity<Playlist> removeReleaseFromPlaylist(String playlistId, Integer releaseId, String token);
}
