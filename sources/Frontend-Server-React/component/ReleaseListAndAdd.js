import React from 'react';
import './ReleaseList.css';
import Release from './Release';
import NewRelease from './NewRelease';
import { useEffect, useState } from 'react';

function ReleaseListAndAdd ({token, setPopup}) {

  const [releases, setReleases] = useState([]);

  const fetchData = () => {
    return fetch("http://localhost:8080/api/songcollection/releases?page=0&items_per_page=4")
          .then((response) => response.json())
          .then((data) => setReleases(data["_embedded"]["releases"]));
  }

  useEffect(() => {
    fetchData();
  },[]);

  return(
    <div className="ReleaseList">
          {
          releases && releases.length > 0 && releases.map((r, index) => (
              <Release key = {index} r = {r} setPopup={setPopup} token={token} fetchData={fetchData}/>
              ))
          }
          <NewRelease token={token}/>
    </div>
  )
};

export default ReleaseListAndAdd;