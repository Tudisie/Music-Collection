
import React, { useState } from 'react';
import './Release.css'
import song_icon from './resources/song.png'
import album_icon from './resources/album.png'
import { useNavigate } from 'react-router-dom';

const Release = ({ r , setPopup, token, fetchData}) => {

  const [release_info, setReleaseInfo] = useState(false);
  const [release_artists, setReleaseArtists] = useState([]);
  const navigate = useNavigate();

  const handleDelete = () => {
    const requestOptions = {
      method: 'DELETE',
      headers: {
        'Authorization': 'Bearer ' + token
      }
    };
    fetch('http://localhost:8080/api/songcollection/releases/' + r.id, requestOptions)
      .then(response => {
        if(response.status === 200){fetchData(); navigate('/')};
        (response.status === 401) && navigate('/unauthorized');
        (response.status === 403) && navigate('/forbidden');
      });
  }

  const handleReleaseArtists = () => {
    release_info === false &&
      fetch('http://localhost:8080/api/songcollection/releases/' + r.id + "/artists")
        .then((response) => response.json())
        .then((data) => setReleaseArtists(data));

    setReleaseInfo(!release_info);
  }

  return(
    <div className="Release">
        <div className="release-info-btn" onClick={() => handleReleaseArtists()}>i</div>
        {release_info === true && 
          <div className="release-info-artists">
            <h3>Artists</h3>
            {release_artists && release_artists.length > 0 && release_artists.map((a,index) => (
              <p key={index}>{a}</p>
            ))}
          </div>
        }
        <div className="picture">
          <img src={r.category === "album" ? album_icon: song_icon} className="song-image" />
        </div>
        <h3>{r.name}</h3>
        <p className="release-type-text"><i>{r.category === "album" ? "(Album)" : r.category === "song" ? "(Song)": "(Single)"}</i></p>
        <hr />  
        <p><span>Genre:</span> {r.genre}</p>
        <p><span>Release Year:</span> {r.releaseYear || "Unknown"}</p>
        <p><span>Album:</span> {r.albumName || "-"}</p>
        <br/><br/><br/>
        <button className="release-button" onClick={() => setPopup(r)}>+ Playlist</button>
        <button className="release-button right-btn-delete" onClick={() => handleDelete()}>Delete</button>
    </div>
  )
};

export default Release;