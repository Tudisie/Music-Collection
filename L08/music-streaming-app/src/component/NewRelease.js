
import React, { Component } from 'react';
import './NewRelease.css'
import add_icon from './resources/plus.png'
import { useNavigate } from 'react-router-dom';

const NewRelease = ({token}) => {

    const navigate = useNavigate();


    return(
        <button className="new-release-btn" onClick={() => {navigate("/create-song")}}>
            <a className="NewRelease">
                <div>
                    <img src={add_icon} className="song-image" />
                </div>
                <h3>Add a New Song</h3>
            </a>
        </button>
    );
};


export default NewRelease;