import React from 'react';
import './ReleaseList.css';
import Release from './Release';

function ReleaseList({releases, setPopup, token, fetchData}){

  return(
      <div className="ReleaseList">
            {
            releases && releases.length > 0 && releases.map((r, index) => (
                <Release key = {index} r = {r} setPopup={setPopup} token={token} fetchData={fetchData} />
                ))
            }
      </div>
  )
}

export default ReleaseList;