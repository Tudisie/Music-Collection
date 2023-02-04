import React, { useEffect } from 'react';
import './ReleasesPage.css';
import ReleaseList from './ReleaseList';
import { useState } from 'react';
import Popup from './Popup';

const ReleasesPage = ({user_id, token}) => {

    const [releases, setReleases] = useState([]);
    const [page, setPage] = useState(0);
    const [noofpages, setNoOfPages] = useState(0);
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const [previousPage, setPreviousPage] = useState();
    const [nextPage, setNextPage] = useState();
    const [popup, setPopup] = useState();

    const fetchReleases = async (pg) => {
        const req = "http://localhost:8080/api/songcollection/releases?page=" + pg + "&items_per_page=" + itemsPerPage;
        return fetch(req)
              .then((response) => response.json())
              .then((data) => {
                //HATEOAS included
                setReleases(data["_embedded"]["releases"]);
                data["_links"]["previous_page"] ? setPreviousPage(data["_links"]["previous_page"]["href"]) : setPreviousPage(null);
                data["_links"]["next_page"] ? setNextPage(data["_links"]["next_page"]["href"]) : setNextPage(null);
              });
    }

    const fetchData = async () => {
        const req = "http://localhost:8080/api/songcollection/releases?page=0&items_per_page=" + itemsPerPage;
        return fetch(req)
              .then((response) => response.json())
              .then((data) => {
                //HATEOAS included
                setReleases(data["_embedded"]["releases"]);
                data["_links"]["previous_page"] ? setPreviousPage(data["_links"]["previous_page"]["href"]) : setPreviousPage(null);
                data["_links"]["next_page"] ? setNextPage(data["_links"]["next_page"]["href"]) : setNextPage(null);
              });
    }

    const fetchNumberOfPages = async () => {
        const req = "http://localhost:8080/api/songcollection/releases/pages?items_per_page=" + itemsPerPage;
        return fetch(req)
              .then((response) => response.json())
              .then((data) => setNoOfPages(data));
    }

    const fetchPreviousPageHATEOAS = async() => {
        setPage(page-1);
        return fetch(previousPage)
              .then((response) => response.json())
              .then((data) => {
                data["_embedded"] && setReleases(data["_embedded"]["releases"])
                data["_links"]["previous_page"] ? setPreviousPage(data["_links"]["previous_page"]["href"]) : setPreviousPage(null);
                data["_links"]["next_page"] ? setNextPage(data["_links"]["next_page"]["href"]) : setNextPage(null);
            });
    }

    const fetchNextPageHATEOAS = async() => {
        setPage(page+1);
        return fetch(nextPage)
              .then((response) => response.json())
              .then((data) => {
                data["_embedded"] && setReleases(data["_embedded"]["releases"]);
                data["_links"]["previous_page"] ? setPreviousPage(data["_links"]["previous_page"]["href"]) : setPreviousPage(null);
                data["_links"]["next_page"] ? setNextPage(data["_links"]["next_page"]["href"]) : setNextPage(null);
            });
    }

    useEffect(() =>{
        fetchReleases(0);
    }, [])

    useEffect(() =>{
        fetchNumberOfPages();
    }, [])
    
    return(
        <div className="ReleasesPage">
            <h1> All songs </h1>
            <p id="items-per-page-p">Items per page:</p>
            <input type="number" id="noofpages" min="0" defaultValue="5" onChange={e => setItemsPerPage(e.target.value)}/>
            <button id="save-items-per-page" onClick={() => {fetchReleases(0); setPage(0); fetchNumberOfPages()}}>Save</button>

            <div className="min-height">
                <ReleaseList releases={releases} setPopup={setPopup} token={token} fetchData={fetchData}/>
            </div>
            
            <div className="search-page">
                <button onClick={() => {setPage(0); fetchReleases(0)}}>
                    <span className="previous page-btn">&laquo; First</span>
                </button>

                <button onClick={() => {previousPage && fetchPreviousPageHATEOAS()}}>
                    <span className="previous round page-btn">&#8249;</span>
                </button>
                <div className="page-number">{page+1}</div>
                <button onClick={() => {nextPage && fetchNextPageHATEOAS()}}>
                    <span className="next round page-btn">&#8250;</span>
                </button>

                <button onClick={() => {setPage(noofpages-1); fetchReleases(noofpages-1)}}>
                    <span className="next page-btn">Last &raquo;</span>
                </button>
            </div>

            <br/><br/>
            {(popup && <Popup popup={popup} setPopup={setPopup} user_id={user_id} token={token}/>)}
        </div>
    );
};


export default ReleasesPage;