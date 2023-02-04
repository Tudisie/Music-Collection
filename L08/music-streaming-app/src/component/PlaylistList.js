import React, { useEffect, useState } from 'react';
import './PlaylistList.css';
import Playlist from './Playlist';
import playlist_icon from './resources/playlist2.png'
import { useNavigate } from 'react-router-dom';

function PlaylistList({user_id, token, setCurrentPlaylistName, setCurrentPlaylistId}) {

    const [playlists, setPlaylists] = useState([]);
    const [playlist_name, setPlaylistName] = useState("");
    const navigate = useNavigate();

    const checkCredentials =  () => {
        return token !== "";
    }

    const createNewPlaylist = () => {
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body: JSON.stringify({ title: playlist_name, user_id: user_id })
        };

        fetch('http://localhost:8081/api/songcollection/playlists', requestOptions)
            .then(response => {
                if(response.status === 201){
                    //navigate(0);
                    fetchData();
                }
                else if(response.status === 401){
                    navigate('/unauthorized');
                }
                else if(response.status === 403){
                    navigate('/forbidden');
                }
                else{
                    console.log("Unhandled error code");
                }
            });
        
    }

    const fetchData = async () => {
        if(user_id)
            return fetch("http://localhost:8081/api/songcollection/playlists?user=" + user_id)
                .then((response) => response.json())
                .then((data) => setPlaylists(data));
    }

    useEffect(() =>{
        checkCredentials() ? fetchData() : navigate('/unauthorized');
    }, [user_id])

    return(
        <div className="PlaylistList">
                {
                playlists && playlists.length > 0 && playlists.map((pl, index) => (
                    <Playlist key = {index} pl = {pl} fetchData={fetchData} setCurrentPlaylistName={setCurrentPlaylistName} setCurrentPlaylistId={setCurrentPlaylistId} token={token} />
                    ))
                }

                <div className="Playlist">
                    <div className="picture">
                    <img src={playlist_icon} className="playlist-image" />
                    </div>
                    <h3>Create New</h3>
                    <input className = "playlist-create-input" 
                        onChange={e => setPlaylistName(e.target.value)} />
                    <button className="playlist-button" onClick={() => createNewPlaylist()}>Create</button>
                </div>
        </div>
    )
}

export default PlaylistList;