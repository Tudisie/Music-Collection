import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Artist from './Artist';
import user_icon from './resources/user.png'
import './ArtistList.css';

export const ArtistList = ({token}) => {

    const [artists, setArtists] = useState();
    const [artist_name, setArtistName] = useState("");
    const [artist_active, setArtistActive] = useState(true);
    const [error, setError] = useState('.');

    const navigate = useNavigate();

    const fetchData = () => {
        return fetch("http://localhost:8080/api/songcollection/artists")
        .then((response) => response.json())
        .then((data) => setArtists(data));
    }

    const handleDeleteArtist = (id) => {
        const requestOptions = {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + token
              }
        };

        fetch('http://localhost:8080/api/songcollection/artists/' + id, requestOptions)
            .then(response => {
                if(response.status === 200){
                    fetchData();
                }
                else if(response.status === 401){
                    navigate('/unauthorized');
                }
                else if(response.status === 403){
                    navigate('/forbidden');
                }
                else {
                    console.log("Unhandled: " + response.status);
                }
            });
    }

    const handleAddArtist = () => {
        if(artist_name.length < 3){
            setError("Album name too short!");
        }else{
            
            const bodyreq = {
                "name": artist_name,
                "active": artist_active
            }

            const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token 
                },
                body: JSON.stringify(bodyreq)
            };

            fetch('http://localhost:8080/api/songcollection/artists', requestOptions)
                .then(response => {
                    if(response.status === 201){
                        fetchData();
                    }
                    else if(response.status === 409){
                        setError("An artist with the same name exists!");
                    }
                });
        }
    }

    useEffect(() =>{
        fetchData();
    }, [])

    return(
        <div>
                <h1>All artists</h1>
                {
                artists && artists.length > 0 && artists.map((a, index) => (
                    <Artist key = {index} artist = {a} handleDeleteArtist={handleDeleteArtist} token={token}/>
                    ))
                }

                <div className="Artist">
                    <div className ="user-img-wrapper">
                        <img  className = "user-img" src={user_icon} />
                    </div>
                    <div className="user-details">

                        <div className="block-artist-info-left">
                            <p>Add New Artist</p>
                        </div>

                        <div className="new-artist-input">
                            <label htmlFor="artist-name">Name:</label>
                            <input type="text" id="add-role" name="artist-name" onChange={(e) => setArtistName(e.target.value)}/>
                            <br/><br/>
                            <label htmlFor="artist-status">Is active:</label>
                            <select className="artist-active-select" onChange={(e) => {setArtistActive(e.target.value)}}>
                                <option value="true">True</option>
                                <option value="false">False</option>
                            </select>
                        </div>

                        <br/><br/>
                    </div>
                    <p className="artist-add-error">{error}</p>
                    <button className="add-user" onClick={() => {handleAddArtist()}}>Add artist</button>
                </div>

                <div className="vertical-space"/>

        </div>
    )
}