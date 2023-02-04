import './App.css';
import Home from './component/Home';
import Login from './component/Login';
import Search from './component/Search';
import Register from './component/Register';
import PlaylistPage from './component/PlaylistPage';
import ChangePassword from './component/ChangePassword';
import React, { useEffect, useState } from "react";
import NavBar from './component/NavBar'
import TopBar from './component/TopBar'
import PlaylistList from './component/PlaylistList';
import Account from './component/Account';
import { getSOAPUserIDBody, getSOAPUsernameBody, getSOAPUserRolesBody } from './utils/SOAPResponses';
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import ReleasesPage from './component/ReleasesPage';
import CreateSongPage from './component/CreateSongPage';
import UserList from './component/UserList';
import { Unauthorized } from './component/Unauthorized';
import { Forbidden } from './component/Forbidden';
import { ArtistList } from './component/ArtistList';

function App() {

  const [token, setToken] = useState('');
  const [displayName, setDisplayName] = useState();
  const [user_id, setUserID] = useState();
  const [darkmode, setDarkmode] = useState(false);
  const [style, setStyle] = useState("");


  const getRoles = (token, setRoles) =>{
    var sr = getSOAPUserRolesBody(token);

    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8000";

    xhr.open('POST', url);
    xhr.onreadystatechange = function(){
        if (xhr.readyState === 4) {
            if(xhr.status === 200)
            {
                var XMLParser = require('react-xml-parser');
                var xml = new XMLParser().parseFromString(xhr.response);
                const roles = xml.getElementsByTagName('tns:get_username_by_jwsResult');
                //setRoles(username);
            }
            else
            {
                // nothing
            }
        }
    }
    xhr.send(sr);
  }

  async function insertUsernameProp(jws, handleUsername) {
    var sr = getSOAPUsernameBody(jws);

    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8000";

    xhr.open('POST', url);
    xhr.onreadystatechange = function(){
        if (xhr.readyState === 4) {
            if(xhr.status === 200)
            {
                var XMLParser = require('react-xml-parser');
                var xml = new XMLParser().parseFromString(xhr.response);
                const username = xml.getElementsByTagName('tns:get_username_by_jwsResult')[0].value;
                handleUsername(username);
            }
            else
            {
                // nothing
            }
        }
    }
    xhr.send(sr);
  }

  async function insertUserIDProp(jws, handleUserID) {
    var sr = getSOAPUserIDBody(jws);

    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8000";

    xhr.open('POST', url);
    xhr.onreadystatechange = function(){
        if (xhr.readyState === 4) {
            if(xhr.status === 200)
            {
                var XMLParser = require('react-xml-parser');
                var xml = new XMLParser().parseFromString(xhr.response);
                const user_id = xml.getElementsByTagName('tns:get_user_id_by_jwsResult')[0].value;
                handleUserID(user_id);
            }
            else
            {
                // nothing
            }
        }
    }
    xhr.send(sr);
  }

  const handleTokenChange = (jws) => {
    setToken(jws);
    insertUsernameProp(jws,handleUsername);
    insertUserIDProp(jws, handleUserID);
  }

  const handleUsername = (username) =>{
    setDisplayName(username);
  }

  const handleUserID = (usr_id) =>{
    setUserID(usr_id);
  }

  const [currentPlaylistName, setCurrentPlaylistName] = useState("");
  const [currentPlaylistId, setCurrentPlaylistId] = useState("");

  // mandatory login
  /*if(!token){
    return <Login handleTokenChange={handleTokenChange} token={token} />
  }*/

  useEffect(() => {
    if(darkmode){
      document.body.style.background = "rgb(127, 127, 127)";
      setStyle("App-light");
    }
    else{
      document.body.style.background = "#222";
      setStyle("App");
    }
  },[darkmode])

  return (
    <Router>
      <div className={style}>
          
          <NavBar token={token}/>
          <main className="main_container">

            <TopBar token={token} setToken={setToken} displayName={displayName} darkmode = {darkmode} setDarkmode = {setDarkmode}/>
            <hr></hr>
            <div className="main">
              <Routes>
                <Route path="/" element={<Home user_id={user_id} token={token} displayName={displayName} setCurrentPlaylistName={setCurrentPlaylistName} setCurrentPlaylistId={setCurrentPlaylistId}/>} />
                <Route path="/login" element={<Login handleTokenChange={handleTokenChange} token={token}/>} />
                <Route path="/register" element={<Register/>} />
                <Route path="/change-password" element={<ChangePassword username={displayName} token={token}/>} />
                <Route path="/account" element={<Account token={token} displayName={displayName} />} />
                <Route path="/search" element={<Search user_id={user_id} token={token}/>} />
                <Route path="/all-songs" element={<ReleasesPage user_id={user_id} token={token}/>} />
                <Route path="/playlists" element={<PlaylistList user_id={user_id} token={token} setCurrentPlaylistName={setCurrentPlaylistName} setCurrentPlaylistId={setCurrentPlaylistId}/>} />
                <Route exact path="/playlists/:id" element={<PlaylistPage token={token} currentPlaylistName={currentPlaylistName} currentPlaylistId={currentPlaylistId}/>} />
                <Route path="/create-song" element={<CreateSongPage token={token}/>} />
                <Route path="/users" element={<UserList token={token} />} />
                <Route path="/unauthorized" element={<Unauthorized/>} />
                <Route path="/forbidden" element={<Forbidden/>} />
                <Route path="/all-artists" element={<ArtistList token={token}/>} />
              </Routes>
            </div>

          </main>
      </div>
    </Router>
  );
}

export default App;
