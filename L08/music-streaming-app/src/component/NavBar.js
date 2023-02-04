import React from 'react';
import './NavBar.css';
import music_title from './resources/music-title.png'
import collection_title from './resources/collection-title.png'
import { useNavigate } from 'react-router-dom';

const NavBar = ({token}) => {
    const navigate = useNavigate()

    return (
        <div className="NavBar">
            <img id="title1" src={music_title} alt="logo"></img>
            <img id="title2" src={collection_title} alt="logo"></img>
            <button onClick={() => navigate('/')}>Home</button>
            <button onClick={() => navigate('/search')}>Search</button>
            {token ? (
            <button className="auth-required" onClick={() => navigate('/playlists')}>Your Playlists</button>
            ) : (
            <button className="auth-required" disabled>Your Playlists</button>
            )}
            
            <button onClick={() => navigate('/all-songs')}>All songs</button>

            <button onClick={() => navigate('/all-artists')}>All artists</button>

            <div className="y-spacer"></div>
            {token ? (
            <div>
                <button className="auth-required" onClick={() => navigate('/create-song')}>Create song</button>
                <button className="auth-required" onClick={() => navigate('/users')}>Users</button>
            </div>
            ) : (
            <button className="auth-required" disabled>Create song</button>
            )}
        </div>
    )
};

export default NavBar;
