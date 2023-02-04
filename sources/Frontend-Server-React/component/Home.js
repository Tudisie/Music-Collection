import React, { useState } from 'react';
import './Home.css';
import PropTypes from 'prop-types';
import ReleaseListAndAdd from './ReleaseListAndAdd';
import { useNavigate } from 'react-router-dom';
import PlaylistList from './PlaylistList';
import Popup from './Popup';

const Home = ({user_id, token, displayName, setCurrentPlaylistName, setCurrentPlaylistId }) => {

    const [popup, setPopup] = useState();

    const navigate = useNavigate();
    return(
        <div className="Home">
            {token ? (<h1> Welcome, {displayName}! </h1>): (<h1> Welcome! </h1>)}

            <h1 className="inline-block">Songs</h1>
            <button className="view-all-btn" onClick={() => navigate('/all-songs')}>(View all songs)</button>
            <ReleaseListAndAdd token={token} setPopup={setPopup} />

            <br/><br/>
            <h1 className='inline-block'>Your playlists</h1>

            {token ? (
                <span>
                    <button className="view-all-btn" onClick={() => navigate('/playlists')}>(View your playlists)</button>
                    <PlaylistList user_id={user_id} token={token} setCurrentPlaylistName={setCurrentPlaylistName} setCurrentPlaylistId={setCurrentPlaylistId}/>
                </span>
            ): (
                <p>You have to log in to view this content!</p>
            )}

            <br/><br/>
            {(popup && <Popup popup={popup} setPopup={setPopup} user_id={user_id} token={token}/>)}
            
        </div>
    );
};

Home.propTypes = {
    token: PropTypes.string.isRequired
}

export default Home;