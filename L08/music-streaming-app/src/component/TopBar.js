import React from 'react';
import { useNavigate } from 'react-router-dom';
import UserDropdown from './UserDropdown';

const TopBar = ({token, setToken, displayName, darkmode, setDarkmode}) => {

    const navigate = useNavigate();

    return(
        <div className="TopBar">
            <input id="toggle" className="toggle" type="checkbox" onClick={() => {setDarkmode(!darkmode)}}/>
            <label htmlFor="toggle" className="title">Toggle dark mode</label>
            
            <div className="dropdown">
                {token ? (
                    <UserDropdown setToken={setToken} displayName={displayName}/>
                ): (
                    <div>
                        <button className="login-btn" onClick={() => {navigate("/register")}}>Register</button>
                        <button className="login-btn" onClick={() => {navigate("/login")}}>Log in</button>
                    </div>
                )}
            </div>
        </div>
    )
};

export default TopBar;
