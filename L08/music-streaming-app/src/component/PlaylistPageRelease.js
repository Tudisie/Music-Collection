import React from 'react';
import './PlaylistPageRelease.css';
import song_icon from './resources/song.png'

const PlaylistPageRelease = ({release, handleDeleteReleaseFromPlaylist}) => {

    return(
        <div className="PlaylistPageRelease">
            <div className ="playlist-release-img-wrapper">
                <img  className = "playlist-release-img" src={song_icon} />
            </div>
            <div className="playlist-release-details"> {release.name} </div>
            <button className="playlist-release-delete-btn" onClick={() => {handleDeleteReleaseFromPlaylist(release.id)}}>Delete</button>
        </div>
    );
};


export default PlaylistPageRelease;