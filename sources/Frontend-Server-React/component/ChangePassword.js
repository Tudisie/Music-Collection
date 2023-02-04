import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import './ChangePassword.css';
import { getSOAPChangePasswordBody } from '../utils/SOAPResponses';

async function changePasswordUser(username, password, token, handleError){
    var sr = getSOAPChangePasswordBody(username, password, token);

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
                xml.getElementsByTagName('tns:loginResult');
            }
            else
            {
                handleError(true);
            }
        }
    }
    xhr.send(sr);
}

function ChangePassword({username, token}){
    const [password, setPassword] = useState();
    const [repassword, setRePassword] = useState();
    const [error, setError] = useState(false);

    const navigate = useNavigate();

    const handleError = (err) => {
        setError(err);
    }

    const handleSubmit = async e => {
        e.preventDefault();
        if(password !== repassword)
            setError("Passwords don't match!");
        else if(password.length < 5)
            setError("Password is too short!");
        else{
            await changePasswordUser(username, password, token, handleError);
            navigate('/');
        }
    }

    return(
    <div className="Login">
        <h1>Change password</h1>
        <form onSubmit={handleSubmit}>
            <div className="row">
                <div className="col-25">
                    <label htmlFor="passwd">Password:</label>
                </div>
                <div className="col-75">
                    <input type="password" id="passwd" name="password" placeholder="Your password.."
                        onChange={e => setPassword(e.target.value)}/>
                </div>
            </div>

            <div className="row">
                    <div className="col-25">
                        <label htmlFor="repasswd">Re-enter password:</label>
                    </div>
                    <div className="col-75">
                        <input type="password" id="repasswd" name="repassword" placeholder="Repeat password.."
                            onChange={e => setRePassword(e.target.value)}/>
                    </div>
                </div>
            
            <br />
            <div className="row">
                <input type="submit" value="Submit" />
            </div>
        </form>

        {error && (<div className='error-message'>{error}</div>)}
        
    </div>
    )
};

export default ChangePassword;