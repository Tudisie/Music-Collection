import React, {useEffect, useState } from 'react';
import { getSOAPUserBody, SOAPtoJSON } from '../utils/SOAPResponses';
import './Account.css';

const Account = ({token, displayName }) => {

    const [user, setUser] = useState();


    useEffect(() => {
        var sr = getSOAPUserBody(displayName, token);
        
        var xhr = new XMLHttpRequest();
        var url = "http://localhost:8000";
        
        xhr.open('POST', url);
        xhr.onreadystatechange = function(){
            if (xhr.readyState === 4) {
                if(xhr.status === 200)
                {
                    var XMLParser = require('react-xml-parser');
                    var xml = new XMLParser().parseFromString(xhr.response);
                    const usr = SOAPtoJSON(xml.getElementsByTagName('tns:get_user_with_rolesResult'));
                    setUser(usr);
                }
                else
                {
                    // nothing
                }
            }
        }
        xhr.send(sr);
    }, [displayName, token])

    return(
        <div className="Account">
            <h1>{displayName}</h1>
            <div className="distance-account"></div>
            <div className="table-account">
                <div className="row-account">
                    <span className="cell-account">Username:</span>
                    <span className="cell-account">{user && JSON.parse(user)["username"]}</span>
                </div>
                <div className="row-account">
                    <span className="cell-account">Full Name:</span>
                    <span className="cell-account">{user && JSON.parse(user)["full_name"]}</span>
                </div>
                <div className="row-account">
                    <span className="cell-account">e-mail:</span>
                    <span className="cell-account">{user && JSON.parse(user)["email"]}</span>
                </div>
                <div className="row-account">
                    <span className="cell-account">Age:</span>
                    <span className="cell-account">{user && JSON.parse(user)["age"]}</span>
                </div>
                <div className="row-account">
                    <span className="cell-account">Roles:</span>
                    <span className="cell-account">{user && JSON.parse(user)["roles"].map((x) => <div key={x} className="roles-account">{x}</div>)}</span>
                </div>
            </div>
        </div>
    )
};

export default Account;