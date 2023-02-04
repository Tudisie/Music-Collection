package com.mongodb_example.main.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb_example.main.controllers.PlaylistController;
import com.mongodb_example.main.exceptions.ReleaseNotFoundException;
import com.mongodb_example.main.exceptions.TokenExceptions.Forbidden;
import com.mongodb_example.main.music_playlists.models.Playlist;
import com.mongodb_example.main.music_playlists.models.DTO.PlaylistHateoas;
import com.mongodb_example.main.music_playlists.models.Release;
import com.mongodb_example.main.services.other.TokenValidation;
import org.springframework.dao.DuplicateKeyException;
import com.mongodb_example.main.exceptions.PlaylistExistsException;
import com.mongodb_example.main.exceptions.PlaylistMissingFieldException;
import com.mongodb_example.main.exceptions.PlaylistNotFoundException;
import com.mongodb_example.main.repositories.MusicPlaylistsRepository;
import com.mongodb_example.main.services.interfaces.MusicPlaylistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb_example.main.services.HTTPRequests.sendHttpGETRequest;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MusicPlaylistsServiceImpl implements MusicPlaylistsService {

    @Autowired
    private MusicPlaylistsRepository playlistRepository;

    private static Playlist trySetReleasesFields(Playlist playlist) throws IOException {
        if(playlist.getReleases() == null)
            return playlist;
        for(Release release : playlist.getReleases())
        {
            String res = sendHttpGETRequest("http://localhost:8080/api/songcollection/releases?name=" + release.getName().replace(" ","+") + "&match=exact");

            JsonObject jsobj = new Gson().fromJson(res,JsonObject.class);
            JsonArray jsarr = jsobj.getAsJsonObject("_embedded").getAsJsonArray("releases");

            if(Objects.equals(jsarr, "[]"))
                throw new PlaylistMissingFieldException();

            //daca avem mai multe release-uri cu acelasi nume, cautam melodia (sarim peste album)
            int saved_index = 0;
            for(int i = 0; i < jsarr.size(); ++i)
            {
                JsonObject js = (JsonObject) jsarr.get(i);
                String category = js.get("category").toString().replace("\"","");
                if(category.equals("song") || category.equals("single"))
                    saved_index = i;
            }
            JsonObject js = (JsonObject) jsarr.get(saved_index);
            release.setId(Integer.parseInt(js.get("id").toString()));

            String href = js.getAsJsonObject("_links").getAsJsonObject("self").get("href").getAsString();
            release.setLink(href.replace("\"",""));
        }
        return playlist;
    }

    @Override
    public String getIdByTitle(String Title) {
        return null;
    }

    @Override
    public List<PlaylistHateoas> getAllPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();

        List<PlaylistHateoas> plharr = new ArrayList<>();

        for( Playlist playlist: playlists){
            PlaylistHateoas plh = new PlaylistHateoas(playlist.getId(), playlist.getTitle(), playlist.getUser_id(), playlist.getReleases());
            plh.add(linkTo(methodOn(PlaylistController.class).getPlaylistById(playlist.getId())).withSelfRel());
            plh.add(linkTo(methodOn(PlaylistController.class).getAllPlaylists(null,null)).withRel("playlists"));
            plharr.add(plh);
        }
        return plharr;
    }

    @Override
    public List<PlaylistHateoas> getUserPlaylists(Integer user_id){
        List<Playlist> playlists = playlistRepository.findPlaylistsByUser(user_id);
        if(playlists == null)
            throw new PlaylistNotFoundException("user_id: " + user_id.toString());

        List<PlaylistHateoas> plharr = new ArrayList<>();

        for( Playlist playlist: playlists){
            PlaylistHateoas plh = new PlaylistHateoas(playlist.getId(), playlist.getTitle(), playlist.getUser_id(), playlist.getReleases());
            plh.add(linkTo(methodOn(PlaylistController.class).getPlaylistById(playlist.getId())).withSelfRel());
            plh.add(linkTo(methodOn(PlaylistController.class).getAllPlaylists(null,null)).withRel("playlists"));
            plharr.add(plh);
        }
        return plharr;
    }

    @Override
    public PlaylistHateoas getPlaylistById(String id){
        Playlist playlist =  playlistRepository.findPlaylistById(id);
        if(playlist == null)
            throw new PlaylistNotFoundException(id);


        PlaylistHateoas plh = new PlaylistHateoas(playlist.getId(), playlist.getTitle(), playlist.getUser_id(), playlist.getReleases());
        plh.add(linkTo(methodOn(PlaylistController.class).getPlaylistById(playlist.getId())).withSelfRel());
        plh.add(linkTo(methodOn(PlaylistController.class).getAllPlaylists(null,null)).withRel("playlists"));

        return plh;
    }

    @Override
    public List<PlaylistHateoas> getPlaylistsByTitle(String title){
        List<Playlist> playlists = playlistRepository.findPlaylistsByTitle(title);
        if(playlists == null)
            throw new PlaylistNotFoundException(title);

        List<PlaylistHateoas> plharr = new ArrayList<>();

        for( Playlist playlist: playlists){
            PlaylistHateoas plh = new PlaylistHateoas(playlist.getId(), playlist.getTitle(), playlist.getUser_id(), playlist.getReleases());
            plh.add(linkTo(methodOn(PlaylistController.class).getPlaylistById(playlist.getId())).withSelfRel());
            plh.add(linkTo(methodOn(PlaylistController.class).getAllPlaylists(null,null)).withRel("playlists"));
            plharr.add(plh);
        }
        return plharr;
    }

    @Override
    public List<PlaylistHateoas> getUserPlaylistsByTitle(Integer user_id, String name){
        List<Playlist> playlists = playlistRepository.findPlaylistsByUser(user_id, name);
        if(playlists == null)
            throw new PlaylistNotFoundException("user_id: " + user_id.toString());

        List<PlaylistHateoas> plharr = new ArrayList<>();

        for( Playlist playlist: playlists){
            PlaylistHateoas plh = new PlaylistHateoas(playlist.getId(), playlist.getTitle(), playlist.getUser_id(), playlist.getReleases());
            plh.add(linkTo(methodOn(PlaylistController.class).getPlaylistById(playlist.getId())).withSelfRel());
            plh.add(linkTo(methodOn(PlaylistController.class).getAllPlaylists(null,null)).withRel("playlists"));
            plharr.add(plh);
        }
        return plharr;
    }

    @Override
    public ResponseEntity<PlaylistHateoas> createPlaylist(Playlist playlist, String token) throws PlaylistExistsException, PlaylistMissingFieldException {
        if(!TokenValidation.validate(token,"client"))
            throw new Forbidden();

        if (playlist.getTitle() == null)
            throw new PlaylistMissingFieldException();
        try {
            playlistRepository.save(trySetReleasesFields(playlist));

            PlaylistHateoas plh = new PlaylistHateoas(playlist.getId(), playlist.getTitle(), playlist.getUser_id(), playlist.getReleases());
            plh.add(linkTo(methodOn(PlaylistController.class).getPlaylistById(playlist.getId())).withSelfRel());
            plh.add(linkTo(methodOn(PlaylistController.class).getAllPlaylists(null,null)).withRel("playlists"));

            return ResponseEntity.status(HttpStatus.CREATED).body(plh);
        } catch (DuplicateKeyException ex) {
            throw new PlaylistExistsException(playlist.getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Playlist> updatePlaylist(Playlist newPlaylist, String id, String token)
            throws PlaylistMissingFieldException, PlaylistNotFoundException, PlaylistExistsException, IOException {
        if(!TokenValidation.validate(token,"client"))
            throw new Forbidden();

        if(newPlaylist.getTitle() == null || newPlaylist.getTitle().equals(""))
            throw new PlaylistMissingFieldException();

        try{
            Playlist playlist = playlistRepository.findPlaylistById(id);
            if(playlist == null)
                throw new PlaylistNotFoundException(id);

            // Update an entry
            newPlaylist = trySetReleasesFields(newPlaylist);

            playlist.setTitle(newPlaylist.getTitle());
            playlist.setReleases(newPlaylist.getReleases());

            try{
                playlistRepository.save(trySetReleasesFields(newPlaylist));
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } catch(Exception exinner){
                throw new PlaylistExistsException(playlist.getId());
            }

        } catch(PlaylistNotFoundException ex){

            // Create a new entry
            newPlaylist.setId(id);
            newPlaylist = trySetReleasesFields(newPlaylist);
            try{
                return ResponseEntity.status(HttpStatus.CREATED).body(playlistRepository.save(newPlaylist));
            } catch(Exception exinner){
                System.out.println(ex.toString());
                throw new PlaylistExistsException(newPlaylist.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Playlist> updatePartiallyPlaylist(Playlist newPlaylist, String id, String token)
            throws PlaylistNotFoundException, PlaylistExistsException, IOException {
        if(!TokenValidation.validate(token,"client"))
            throw new Forbidden();

        Playlist playlist = playlistRepository.findPlaylistById(id);
        if(playlist == null)
            throw new PlaylistNotFoundException(id);

        newPlaylist = trySetReleasesFields(newPlaylist);

        if(newPlaylist.getTitle() != null)
            playlist.setTitle(newPlaylist.getTitle());
        if(newPlaylist.getReleases() != null)
            playlist.setReleases(newPlaylist.getReleases());

        try{
            playlistRepository.save(trySetReleasesFields(playlist));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch(Exception exinner){
            throw new PlaylistExistsException(playlist.getId());
        }
    }

    @Override
    public Playlist removePlaylist(String id, String token) throws PlaylistNotFoundException {
        if(!TokenValidation.validate(token,"client"))
            throw new Forbidden();

        Playlist playlist = playlistRepository.findPlaylistById(id);
        if(playlist == null) {
            throw new PlaylistNotFoundException(id);
        }
        playlistRepository.deleteById(id);
        return playlist;
    }

    @Override
    public ResponseEntity<Playlist> addReleaseToPlaylist(Release release, String id, String token) throws IOException {
        if(!TokenValidation.validate(token,"client"))
            throw new Forbidden();

        Playlist playlist = playlistRepository.findPlaylistById(id);
        if(playlist == null)
            throw new PlaylistNotFoundException(id);

        List<Release> releases = playlist.getReleases();

        if(releases == null)
            releases = new ArrayList<>();

        //if it already exists in playlist
        for(Release r : releases) {
            if(Objects.equals(r.getName(), release.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(playlist);
            }
        }
        releases.add(release);
        playlist.setReleases(releases);

        playlist = trySetReleasesFields(playlist);

        try{
            playlistRepository.save(playlist);
            return ResponseEntity.status(HttpStatus.OK).body(playlist);
        } catch(Exception exinner){
            throw new PlaylistNotFoundException(playlist.getId());
        }
    }

    @Override
    public ResponseEntity<Playlist> removeReleaseFromPlaylist(String playlistId, Integer releaseId, String token) throws PlaylistNotFoundException{
        if(!TokenValidation.validate(token,"client"))
            throw new Forbidden();

        Playlist playlist = playlistRepository.findPlaylistById(playlistId);
        if(playlist == null)
            throw new PlaylistNotFoundException(playlistId);

        if(!playlist.getReleases().removeIf(r -> Objects.equals(r.getId(), releaseId)))
            throw new ReleaseNotFoundException(releaseId);

        try{
            playlistRepository.save(playlist);
            return ResponseEntity.status(HttpStatus.OK).body(playlist);
        } catch(Exception exinner){
            throw new PlaylistNotFoundException(playlist.getId());
        }
    }
}
