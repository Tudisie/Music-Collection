import React from 'react';
import './Search.css';
import ReleaseList from './ReleaseList';
import { useState } from 'react';
import Popup from './Popup';

const Search = ({user_id, token}) => {

    const [releases, setReleases] = useState([]);
    const [searched, setSearched] = useState("");
    const [match_exact, setMatchExact] = useState(false);
    const [search_by, setSearchBy] = useState("Name");
    const [disabled_match, setDisabledMatch] = useState("");
    const [popup, setPopup] = useState();

    const fetchData = async (e) => {
        e.preventDefault();
        if(searched !== "" && search_by === "year" && isNaN(searched))
            return;
        let req = "http://localhost:8080/api/songcollection/releases?name=" + searched;
        (searched !== "") && (search_by === "year") && (req = req.concat("&search_by=year"));
        (searched !== "") && (search_by === "genre") && (req = req.concat("&search_by=genre"));
        (match_exact === true) && (req = req.concat("&match=exact"));
        return fetch(req)
              .then((response) => response.json())
              .then((data) => data["_embedded"] ? (setReleases(data["_embedded"]["releases"])) : setReleases([]));
    }

    const handleSearchBy = (value) => {
        setSearchBy(value);
        (value !== "name") ? setDisabledMatch(" - button disabled") : setDisabledMatch("");
    }
    
    return(
        <div className="Search">
            <h1> Search </h1>
            
            <div className="search-container">
                <form onSubmit={fetchData}>
                    <input type="search" placeholder="Search.." name="search" className="input-bar" 
                        onChange={e => setSearched(e.target.value)}/>
                    <button type="submit" className="search-btn"><i className="fa fa-search"></i></button>
                </form>
            </div>

            <input type="checkbox" id="match-exact-cb" name="match-exact" onChange={(e) => setMatchExact(e.target.checked)}/>
            <label htmlFor="match-exact" id="match-exact-label">match exact</label>
            <span className="disabled-text">{disabled_match}</span>

            <br /> <br/>

            <span>Search by:</span>

            <div className="container-cb" onChange={(e) => {handleSearchBy(e.target.value)}}>
                <input type="radio" value="name" name="searchby" defaultChecked /> Name
                <input type="radio" value="year" name="searchby" /> Year
                <input type="radio" value="genre" name="searchby" /> Genre
            </div>

            <br/><br/>

            <ReleaseList releases={releases} setPopup={setPopup}/>

            <br/><br/>
            {(popup && <Popup popup={popup} setPopup={setPopup} user_id={user_id} token={token}/>)}
        </div>
    );
};


export default Search;