import React, { useEffect, useState } from 'react';
import './User.css';
import user_icon from './resources/user.png'
import { getSOAPAddRoleBody, getSOAPDeleteRoleBody, getSOAPDeleteUserBody } from '../utils/SOAPResponses';

function User({user, token, fetch}) {

    const [added_role, setAddedRole] = useState();
    const [deleted_role, setDeletedRole] = useState();

    const handleAddRole = async (e) => {
        e.preventDefault();
        var sr = getSOAPAddRoleBody(user.username, [added_role], token);

        var xhr = new XMLHttpRequest();
        var url = "http://localhost:8000";

        xhr.open('POST', url);
        xhr.onreadystatechange = function(){
            if (xhr.readyState === 4) {
                if(xhr.status === 200)
                {
                    fetch();
                }
                else
                {
                    // nothing
                }
            }
        }
        xhr.send(sr);
    }

    const handleDeleteRole = async (e) => {
        e.preventDefault();
        var sr = getSOAPDeleteRoleBody(user.username, [deleted_role], token);
        var xhr = new XMLHttpRequest();
        var url = "http://localhost:8000";

        xhr.open('POST', url);
        xhr.onreadystatechange = function(){
            if (xhr.readyState === 4) {
                if(xhr.status === 200)
                {
                    fetch();
                }
                else
                {
                    // nothing
                }
            }
        }
        xhr.send(sr);
    }

    const handleDeleteUser = async () => {
        var sr = getSOAPDeleteUserBody(user.username, token);
        var xhr = new XMLHttpRequest();
        var url = "http://localhost:8000";

        xhr.open('POST', url);
        xhr.onreadystatechange = function(){
            if (xhr.readyState === 4) {
                if(xhr.status === 200)
                {
                    fetch();
                }
                else
                {
                    // nothing
                }
            }
        }
        xhr.send(sr);
    }

    return(
        <div className="User">
            <div className ="user-img-wrapper">
                <img  className = "user-img" src={user_icon} />
            </div>
            <div className="user-details">
                <div className="block-info-left">
                    <p><span>Username:</span> {user.username}</p>
                </div>
                <div className="block-info-right">
                    <p className="first-p-block-info"><span>email:</span> {user.email}</p>
                    <p><span>Full Name:</span> {user.full_name}</p>
                    <p><span>Age:</span> {user.age}</p>
                </div>
                <br/><br/>
                <div className="remove-margin-top"><span>Roles: </span>{user.roles.map(r => r.concat("; "))}</div>
            </div>
            <div className="add-role-user">
                <form onSubmit={handleAddRole}>
                    <label htmlFor="add-role">Add Role:</label>
                    <input type="text" id="add-role" name="add-role" onChange={(e) => setAddedRole(e.target.value)}/>
                    <input type="submit" id="submit-add-role" value="Add"/>
                </form>

                <br/>
                <form onSubmit={handleDeleteRole}>
                    <label htmlFor="delete-role">Delete Role:</label>
                    <input type="text" id="delete-role" name="delete-role" onChange={(e) => setDeletedRole(e.target.value)}/>
                    <input type="submit" id="submit-delete-role" value="Delete"/>
                </form>
            </div>
            <button className="delete-user" onClick={() => {handleDeleteUser()}}>Delete user</button>
        </div>
    )
}

export default User;