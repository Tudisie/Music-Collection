import React from 'react';
import './TopBar.css';
import './UserDropdown.css';
import { useNavigate } from 'react-router-dom';

const UserDropdown = ({setToken, displayName}) => {
    const navigate = useNavigate();

    const logout = () =>{
        setToken("");
        navigate('/');
    }

    return(
        <div className="dropdown">
            <button className="dropbtn">{displayName}</button>
            <div className="dropdown-content">
                <button onClick={() => navigate('/account')}>Account</button>
                <button onClick={() => navigate('/change-password')}>Change Password</button>
                <button onClick={() => logout()}>Log out</button>
            </div>
        </div>
    );
};

export default UserDropdown;
