import React from 'react';
import './CreateSongPage.css';
import { useState , useEffect} from 'react';
import { useNavigate } from 'react-router-dom';

const CreateSongPage = ({token}) => {

    const [name, setName] = useState();
    const [albumName, setAlbumName] = useState();
    const [albumId, setAlbumId] = useState(null);
    const [genre, setGenre] = useState("rock");
    const [releaseYear, setReleaseYear] = useState();
    const [type, setType] = useState("song");

    const [error, setError] = useState("");

    const [genres, setGenres] = useState();

    const navigate = useNavigate();

    const handleSubmit = async e => {
        e.preventDefault();
        setError(""); //aynchronous - so i will use err to check in real time
        let err = false;
        if(name === undefined || name === ""){
            err = true;
            setError("Please fill all the required fields!")
        }else{
            releaseYear &&  (releaseYear < 1800 || releaseYear > 2023) && (err = true);
            releaseYear &&  (releaseYear < 1800 || releaseYear > 2023) && setError("Release Year is not defined correct!");
            (albumName && albumName.length < 3) && (err = true);
            (albumName && albumName.length < 3) && setError("Album name too short!");
        }

        if(err === false){
            (type === "song") && fetch('http://localhost:8080/api/songcollection/albums?name=' + albumName)
                .then(response => response.json())
                .then(response => {
                    if(response.status === 404) setError("Album doesn't exist!");
                    else{
                        const bodyreq = {
                            name:  name,
                            genre: genre,
                            releaseYear: releaseYear,
                            category: type,
                            albumId: response.id
                        };
                
                        const requestOptions = {
                            method: 'POST',
                            headers: { 
                                'Content-Type': 'application/json',
                                'Authorization': 'Bearer ' + token
                            },
                            body: JSON.stringify(bodyreq)
                        };

                        fetch('http://localhost:8080/api/songcollection/releases', requestOptions)
                        .then(response => {
                            (response.status === 401) && navigate('/unauthorized');
                            (response.status === 403) && navigate('/forbidden');
                            (parseInt(response.status/100,10) == 4) && setError("Song with this name exists!");
                            (parseInt(response.status/100,10) == 2) && navigate("/");
                        });
                    }
                    setAlbumId(response.id);
                });
            const bodyreq = {
                name:  name,
                genre: genre,
                releaseYear: releaseYear,
                category: type,
                albumId: null
            };
    
            const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token},
                body: JSON.stringify(bodyreq)
            };

            
            (type !== "song") && await fetch('http://localhost:8080/api/songcollection/releases', requestOptions)
                        .then(response => {
                            (response.status === 401) && navigate('/unauthorized');
                            (response.status === 403) && navigate('/forbidden');
                            (parseInt(response.status/100,10) == 4) && setError("Song with this name exists!");
                            (parseInt(response.status/100,10) == 2) && navigate("/");
                        });
        }
    }

    useEffect(() =>{
        const fetchGenres = () => {
            return fetch("http://localhost:8080/api/songcollection/releases/genres")
                  .then((response) => response.json())
                  .then((data) => setGenres(data));
        };
        
        fetchGenres();
    }, [])

    return (
        <div className="CreateSongPage">
        <h1>Create</h1>
        <form onSubmit={handleSubmit}>
            <div className="row">
                <div className="col-35">
                    <label className="create-song-label" htmlFor="name">Name:</label>
                </div>
                <div className="col-65">
                    <input type="text" id="name" name="name" placeholder="Insert a name.."
                        onChange={e => setName(e.target.value)}/>
                </div>
            </div>

            <div className="row">
                <div className="col-35">
                    <label className="create-song-label" htmlFor="aname">Album name:</label>
                </div>
                <div className="col-65">
                    {type === "song" ? (
                        <input type="text" id="aname" name="aname" placeholder="Insert album name.." onChange={e => setAlbumName(e.target.value)}/>
                    ):(
                        <input type="text" id="aname" name="aname" placeholder="Insert album name.." onChange={e => setAlbumName(e.target.value)} disabled/>
                    )}
                
                </div>
            </div>

            <div className="row">
                <div className="col-35">
                    <label className="create-song-label" htmlFor="genres">Genre:</label>
                </div>
                <div className="col-65">
                    <select name="genres" id="genres" onChange={(e) => {setGenre(e.target.value)}}>
                        {genres && genres.length > 0 && genres.map((g, index) => (
                             <option key={index} value={g}>{g}</option>
                            ))}
                    </select>
                </div>
            </div>

            <div className="row">
                <div className="col-35">
                    <label className="create-song-label" htmlFor="releaseyear">Release year:</label>
                </div>
                <div className="col-65">
                    <input type="number" id="releaseyear" name="releaseyer" onChange={e => setReleaseYear(e.target.value)}/>
                </div>
            </div>


            <div className="row">
                <div className="col-35">
                    <label className="create-song-label" htmlFor="type">Type:</label>
                </div>
                <div className="col-65">
                    <select name="type" id="type" onChange={(e) => {setType(e.target.value)}}>
                        <option>song</option>
                        <option>single</option>
                        <option>album</option>
                    </select>
                </div>
            </div>
            
            
            <br />
            <div className="row">
                <input type="submit" value="Submit" />
            </div>

            {error && (<div className='error-message'>{error}</div>)}
        </form>
    </div>
    )
};

export default CreateSongPage;
