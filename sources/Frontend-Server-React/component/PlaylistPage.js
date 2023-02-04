import React from 'react';
import './PlaylistPage.css';
import { useState, useEffect } from 'react';
import PlaylistPageRelease from './PlaylistPageRelease';
import song_icon from './resources/song.png'
import { useNavigate } from 'react-router-dom';

const PlaylistPage = ({token, currentPlaylistName, currentPlaylistId}) => {

    const [releases, setReleases] = useState([]);
    const [refresh, setRefresh] = useState(false);
    const navigate = useNavigate();

    const handleDeleteReleaseFromPlaylist = (release_id) => {
        const requestOptions = {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token }
        };

        const url = 'http://localhost:8081/api/songcollection/playlists/' + currentPlaylistId +"/" + release_id;

        fetch(url, requestOptions)
            .then(response => {
                (response.status === 401) && navigate('/unauthorized');
                (response.status === 403) && navigate('/forbidden');
                setRefresh(true);
            });
            
    }

    const checkCredentials =  () => {
        return token !== "";
    }

    const fetchData = async (e) => {
        const req = "http://localhost:8081/api/songcollection/playlists/" + window.location.pathname.split('/')[2];
        return fetch(req)
              .then((response) => response.json())
              .then((data) => setReleases(data.releases));
    }

    useEffect(() => {
        checkCredentials() ? fetchData() : navigate('/unauthorized');
    },[]);

    useEffect(() => {
        if(refresh === true){
            fetchData();
            setRefresh(false);
        }
    }, [refresh])
    
    return(
        <div className="Search">
            <h1>{currentPlaylistName}</h1>
            {
                releases && releases.length > 0 && releases.map((r, index) => (
                    <PlaylistPageRelease key={index} release={r} handleDeleteReleaseFromPlaylist={handleDeleteReleaseFromPlaylist}/>
                ))
            }

            <div className="PlaylistPageRelease">
                <div className ="playlist-release-img-wrapper">
                    <img  className = "playlist-release-img" src={song_icon} />
                </div>
                <button className="add-release-to-playlist" onClick={() => {navigate('/all-songs')}}>
                    <div className="playlist-add-release-details"> <i>Add a new song to playlist </i> </div>
                </button>
            </div>

            <button className="back-button" onClick={() => navigate('/playlists')}>Back</button>
        </div>
    );
};


export default PlaylistPage;