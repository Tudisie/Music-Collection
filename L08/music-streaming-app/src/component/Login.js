import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';
import './Login.css';
import { getSOAPLoginBody } from '../utils/SOAPResponses';

async function loginUser(username, password, handleTokenChange, handleTokenError){
    var sr = getSOAPLoginBody(username, password);

    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8000";

    xhr.open('POST', url);
    xhr.onreadystatechange = function(){
        if (xhr.readyState === 4) 
        {
            if(xhr.status === 200)
            {
                var XMLParser = require('react-xml-parser');
                var xml = new XMLParser().parseFromString(xhr.response);
                const jws = xml.getElementsByTagName('tns:loginResult')[0].value;
                handleTokenChange(jws);
            }
            else
            {
                handleTokenError(true);
            }
        }
    }
    xhr.send(sr);
}

function Login({handleTokenChange, token}){
    const [username, setUsername] = useState();
    const [password, setPassword] = useState();
    const [error, setError] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if(token)
            navigate("/");
    }, [error, token])

    const handleTokenError = (err) => {
        setError(err);
    }

    const handleSubmit = async e => {
        e.preventDefault();
        await loginUser(username, password, handleTokenChange, handleTokenError);
    }

    return(
    <div className="Login">
        <h1>Log in</h1>
        <form onSubmit={handleSubmit}>
            <div className="row">
                <div className="col-25">
                    <label className="create-song-label" htmlFor="usrname">Username:</label>
                </div>
                <div className="col-75">
                    <input type="text" id="usrname" name="username" placeholder="Your username.."
                        onChange={e => setUsername(e.target.value)}/>
                </div>
            </div>
            <div className="row">
                <div className="col-25">
                    <label className="create-song-label" htmlFor="passwd">Password:</label>
                </div>
                <div className="col-75">
                    <input type="password" id="passwd" name="password" placeholder="Your password.."
                        onChange={e => setPassword(e.target.value)}/>
                </div>
            </div>
            
            <br />
            <div className="row">
                <input type="submit" value="Submit" />
            </div>
        </form>

        {error && (<div className='error-message'> Username or password incorrect!</div>)}
        
    </div>
    )
};

Login.propTypes = {
    handleTokenChange: PropTypes.func.isRequired
}

export default Login;