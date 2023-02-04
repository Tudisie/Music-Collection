import React, { useEffect } from 'react';
import './ReleasesPage.css';
import ReleaseList from './ReleaseList';
import { useState } from 'react';
//I've changed only ReleasesPage with ReleasesPageX

const ReleasesPageX = () => {

    const [releases, setReleases] = useState([]);
    const [page, setPage] = useState(0);
    const [noofpages, setNoOfPages] = useState(0);
    const [itemsPerPage, setItemsPerPage] = useState(5);
    const [previousPage, setPreviousPage] = useState();
    const [nextPage, setNextPage] = useState();

    const fetchReleases = async (e) => {
        const req = "http://localhost:8080/api/songcollection/releases?page=" + page + "&items_per_page=" + itemsPerPage;
        return fetch(req)
              .then((response) => response.json())
              .then((data) => {
                //HATEOAS included
                setReleases(data["_embedded"]["releases"]);
                setPreviousPage(data["_links"]["previous_page"]);
                setNextPage(data["_links"]["next_page"]);
              });
    }

    const fetchNumberOfPages = async (e) => {
        const req = "http://localhost:8080/api/songcollection/releases/pages?items_per_page=" + itemsPerPage;
        return fetch(req)
              .then((response) => response.json())
              .then((data) => setNoOfPages(data));
    }

    useEffect(() =>{
        fetchReleases();
    }, [page])

    useEffect(() =>{
        fetchNumberOfPages();
    }, [])
    
    return(
        <div className="ReleasesPage">
            <h1> All songs </h1>
            <p id="items-per-page-p">Items per page:</p>
            <input type="number" id="noofpages" min="0" defaultValue="5" onChange={e => setItemsPerPage(e.target.value)}/>
            <button id="save-items-per-page" onClick={() => {fetchReleases(); fetchNumberOfPages()}}>Save</button>

            <div className="min-height">
                <ReleaseList releases={releases}/>
            </div>
            
            <div className="search-page">
                <button onClick={() => {setPage(0)}}>
                    <span className="previous page-btn">&laquo; First</span>
                </button>

                <button onClick={() => {(page > 0) && setPage(page-1)}}>
                    <span className="previous round page-btn">&#8249;</span>
                </button>
                <div className="page-number">{page+1}</div>
                <button onClick={() => {(page < noofpages-1) && setPage(page+1)}}>
                    <span className="next round page-btn">&#8250;</span>
                </button>

                <button onClick={() => {setPage(noofpages-1)}}>
                    <span className="next page-btn">Last &raquo;</span>
                </button>
            </div>
        </div>
    );
};


export default ReleasesPageX;