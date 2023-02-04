package com.mongodb_example.main.controllers;

import com.mongodb_example.main.exceptions.PlaylistExistsException;
import com.mongodb_example.main.exceptions.PlaylistMissingFieldException;
import com.mongodb_example.main.exceptions.PlaylistNotFoundException;
import com.mongodb_example.main.music_playlists.models.Playlist;
import com.mongodb_example.main.music_playlists.models.DTO.PlaylistHateoas;
import com.mongodb_example.main.music_playlists.models.Release;
import com.mongodb_example.main.services.interfaces.MusicPlaylistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/songcollection")
public class PlaylistController {

    @Autowired
    MusicPlaylistsService musicPlaylistsService;

    @GetMapping("/playlists")
    public List<PlaylistHateoas> getAllPlaylists(@RequestParam(value="name", required = false) String title,
                                                 @RequestParam(value="user", required = false) Integer user_id) {

        if(title == null && user_id == null)
            return musicPlaylistsService.getAllPlaylists();
        if(user_id != null && title != null)
            return musicPlaylistsService.getUserPlaylistsByTitle(user_id, title);
        if(user_id != null)
            return musicPlaylistsService.getUserPlaylists(user_id);
        //only title param
        return musicPlaylistsService.getPlaylistsByTitle(title);
    }

    @GetMapping("/playlists/{id}")
    public PlaylistHateoas getPlaylistById(@PathVariable String id){
        return musicPlaylistsService.getPlaylistById(id);
    }

    @PostMapping("/playlists")
    public ResponseEntity<PlaylistHateoas> createPlaylist(@Validated @RequestBody Playlist playlist, @RequestHeader(value="Authorization",required=false) String token) throws PlaylistExistsException, PlaylistMissingFieldException {
        return musicPlaylistsService.createPlaylist(playlist, token);
    }

    @PutMapping("/playlists/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@RequestBody Playlist newPlaylist, @PathVariable String id, @RequestHeader(value="Authorization",required=false) String token)
            throws PlaylistMissingFieldException, PlaylistNotFoundException, PlaylistExistsException, IOException {
        return musicPlaylistsService.updatePlaylist(newPlaylist, id, token);
    }

    @PatchMapping("/playlists/{id}")
    public ResponseEntity<Playlist> updatePartiallyPlaylist(@RequestBody Playlist newPlaylist, @PathVariable String id, @RequestHeader(value="Authorization",required=false) String token)
            throws PlaylistNotFoundException, PlaylistExistsException, IOException {
        return musicPlaylistsService.updatePartiallyPlaylist(newPlaylist, id, token);
    }

    @DeleteMapping("/playlists/{id}")
    public Playlist deletePlaylist(@PathVariable String id, @RequestHeader(value="Authorization",required=false) String token){
        return musicPlaylistsService.removePlaylist(id, token);
    }

    //Add release to playlist
    @PostMapping("/playlists/{id}")
    public ResponseEntity<Playlist> addReleaseToPlaylist(@Validated @RequestBody Release release, @PathVariable String id, @RequestHeader(value="Authorization",required=false) String token) throws IOException {
        return musicPlaylistsService.addReleaseToPlaylist(release, id, token);
    }

    //Remove release from playlist
    @DeleteMapping("/playlists/{playlistId}/{releaseId}")
    public ResponseEntity<Playlist> removeReleaseFromPlaylist(@PathVariable String playlistId, @PathVariable Integer releaseId, @RequestHeader(value="Authorization",required=false) String token){
        return musicPlaylistsService.removeReleaseFromPlaylist(playlistId, releaseId, token);
    }

}
