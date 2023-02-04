import React, { useEffect, useState } from 'react';
import './Artist.css';
import user_icon from './resources/user.png'

function Artist({artist, handleDeleteArtist, token}) {

    const [artist_songs, setArtistSongs] = useState([]);
    const [release_name, setReleaseName] = useState("");
    const [error, setError] = useState("");

    const fetchData = () => {
        return fetch("http://localhost:8080/api/songcollection/artists/" + artist.id +"/releases")
            .then((response) => response.json())
            .then((data) => {
                if(data.status !== 404){
                    setArtistSongs(data);
                }
            });
    }

    const handleRemoveRelease = (s) => {
        return fetch("http://localhost:8080/api/songcollection/releases/name=" + s)
            .then((response) => response.json())
            .then((data) => {
                const bodyreq = {
                    "releaseId": data.id,
                    "artistId": artist.id
                }
        
                const requestOptions = {
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
                    body: JSON.stringify(bodyreq)
                };
        
                fetch('http://localhost:8080/api/songcollection/artists/releases', requestOptions)
                    .then(response => {
                        if(response.status === 200){
                            fetchData();
                        }
                        else{
                            setError("Unhandled error status!");
                        }
                    });
            });
    }

    const handleAddRelease = () => {
        return fetch("http://localhost:8080/api/songcollection/releases/name=" + release_name)
            .then((response) => response.json())
            .then((data) => {
                if(data.status === 201 || data.status === undefined){
                    const bodyreq = {
                        "releaseId": data.id,
                        "artistId": artist.id
                    }
            
                    const requestOptions = {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
                        body: JSON.stringify(bodyreq)
                    };
            
                    fetch('http://localhost:8080/api/songcollection/artists/releases', requestOptions)
                        .then(response => {
                            if(response.status === 201){
                                setError("");
                                fetchData();
                            }
                            else if(response.status === 409){
                                setError("Song is already added for this artist!");
                            }
                            else{
                                console.log("Unhandled error status!");
                            }
                        });
                }else if(data.status === 404){
                    setError("Song not found!")
                }
            });
    }

    useEffect(() => {
        fetchData();
    }, [])
    
    return(
        <div>
            <div className="Artist">
                <div className ="user-img-wrapper">
                    <img alt="user icon" className = "user-img" src={user_icon} />
                </div>
                <div className="user-details">
                    <div className="block-info-left">
                        <p><span>Artist name:</span> {artist.name}</p>
                    </div>
                
                    <br/><br/>
                    <div className="remove-margin-top">
                        {(artist.active) ? <span className="grey">He is still active</span>: <span className="grey">He is no longer active</span>}
                    </div>
                    <button className="add-release-artist-btn" onClick={() => handleAddRelease()}>Add Song</button>
                    <input className="add-release-artist-input" onChange={(e) => setReleaseName(e.target.value)} />
                </div>
                <div className="artist-songs">Songs: {artist_songs && artist_songs.length > 0 && 
                    artist_songs.map(s => (
                        <div className="artist-release-block">
                            <button className="delete-song-from-artist-btn" onClick={() => handleRemoveRelease(s)}>-</button>
                            <p>{s}</p>
                        </div>
                    ))}
                </div>
                <button className="delete-artist" onClick={() => {handleDeleteArtist(artist.id)}}>Delete artist</button>
            </div>
            <p className="p-margin-left">{error}</p>
        </div>
    )
}

export default Artist;