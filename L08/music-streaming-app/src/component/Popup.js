import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Popup.css';

// popup contains the actual release who called the popup!
const Popup = ({popup, setPopup, user_id, token}) => {

    const [playlist_index, setPlaylistIndex] = useState(0);
    const [playlists, setPlaylists] = useState();
    const [error, setError] = useState("");

    const navigate = useNavigate();

    const handleAddToPlaylist = () => {
        const bodyreq = {
            "name": popup.name
        }

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json','Authorization': 'Bearer ' + token },
            body: JSON.stringify(bodyreq)
        };

        fetch('http://localhost:8081/api/songcollection/playlists/' + playlists[playlist_index].id, requestOptions)
            .then(response => {
                if(response.status === 200){
                    setPopup();
                }
                else if(response.status === 401){
                    navigate('/unauthorized');
                }
                else if(response.status === 403){
                    navigate('/forbidden');
                }
                else if(response.status === 409){
                    setError("A release with the same name exists in this playlist!");
                }
            });
    }

    const fetchData = () => {
        return fetch("http://localhost:8081/api/songcollection/playlists?user=" + user_id)
              .then((response) => response.json())
              .then((data) => {
                if(data.status === 400){ //TODO 401 403
                    navigate('/unauthorized');
                }
                else{
                    setPlaylists(data);
                }
              });
    }

    useEffect(() => {
        fetchData();
    }, [])

    return(
        <div className="popup-box">
            <div className="box">
                <button className="popup-x" onClick={() => setPopup()}>X</button>
                <br/>
                <h3>Playlist name:</h3>
                <select className="popup-select" onChange={(e) => setPlaylistIndex(e.target.selectedIndex)}>
                    {playlists && playlists.map((pl, index) => 
                    <option key={index}>{pl.title}</option>)}
                </select>
                <div className="height-spacer"></div>
                <button className="popup-add-button" onClick={() => handleAddToPlaylist()}>Add</button>
                <div className="popup-error">{error}</div>
            </div>
        </div>
    );
};


export default Popup;