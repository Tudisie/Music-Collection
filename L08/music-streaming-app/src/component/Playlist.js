
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Playlist.css'
import playlist_icon from './resources/playlist2.png'

const Playlist = ({ pl, fetchData, setCurrentPlaylistName, setCurrentPlaylistId, token}) => {
    
  const navigate = useNavigate();

  const delete_playlist = () => {
    const requestOptions = {
        method: 'DELETE',
        headers: {
          'Authorization': 'Bearer ' + token
        }
    };

    fetch('http://localhost:8081/api/songcollection/playlists/' +  pl.id, requestOptions)
        .then(response => {
            if(response.status === 200){
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
                console.log("Unhandled error");
            }
        });
  
  }

  return(
    <div className="Playlist">
        <div className="picture">
          <img src={playlist_icon} className="playlist-image" />
        </div>
        <h3>{pl.title}</h3>
        <hr />  
        <p><span>No. of Songs: </span>{pl.releases ? pl.releases.length : 0}</p>
        <button className="playlist-button" onClick={() => {setCurrentPlaylistName(pl.title); setCurrentPlaylistId(pl.id); navigate('/playlists/' + pl.id);}}>Open</button>
        <button className="playlist-button" onClick={() => delete_playlist()}>Delete</button>
    </div>
  );
};


export default Playlist;