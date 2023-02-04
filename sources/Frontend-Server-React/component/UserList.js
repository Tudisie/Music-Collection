import React, { useEffect, useState } from 'react';
import './UserList.css';
import User from './User';
import { TableRoles } from './TableRoles';
import { getSOAPUsers, SOAPListToJSON } from '../utils/SOAPResponses';
import { useNavigate } from 'react-router-dom';

function UserList({token}) {

    const [users, setUsers] = useState();
    const navigate = useNavigate();

    const checkCredentials =  () => {
        return token !== "";
    }

    const fetchData = () => {
        var sr = getSOAPUsers(token);
        
        var xhr = new XMLHttpRequest();
        var url = "http://localhost:8000";
        
        xhr.open('POST', url);
        xhr.onreadystatechange = function(){
            if (xhr.readyState === 4) {
                if(xhr.status === 200)
                {
                    var users_aux = [];
                    var XMLParser = require('react-xml-parser');
                    var xml = new XMLParser().parseFromString(xhr.response);
                    const res = xml.getElementsByTagName('tns:get_all_users_with_rolesResult');
                    res.map((el) => users_aux.push((SOAPListToJSON(el))));
                    
                    setUsers(users_aux);
                }
                else
                {
                    // nothing
                }
            }
        }
        xhr.send(sr);
    }

    useEffect(() =>{
        checkCredentials() ? fetchData() : navigate('/unauthorized');
    }, [])

    return(
        <div>
                {
                users && users.length > 0 && users.map((usr, index) => (
                    <User key = {index} user = {JSON.parse(usr)} token={token} fetch={fetchData}/>
                    ))
                }
                <div className="vertical-space"/>
                <p>Information about roles</p>

                <TableRoles />
        </div>
    )
}

export default UserList;