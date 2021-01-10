const statusBox = document.getElementById("statusBox");

document.getElementById("matchHistoryButton").addEventListener('click',()=>{
    axios.get('/api/getMatchHistory')
        .then(response=>{
            statusBox.innerText = response.data.status_message;
        })
        .catch(()=>{
            statusBox.innerText = "Request Failed";
        });
});

document.getElementById("leaderboardsButton").addEventListener('click',()=>{
    axios.get('/api/getLeaderboards')
    .then(response=>{
        statusBox.innerText = response.data.status_message;
    })
    .catch(()=>{
        statusBox.innerText = "Request Failed";
    });
});

document.getElementById("championstatsButton").addEventListener('click',()=>{
    axios.get('/api/getChampions')
    .then(response=>{
        statusBox.innerText = response.data.status_message;
    })
    .catch(()=>{
        statusBox.innerText = "Request Failed";
    });
});

document.getElementById("getprofileButton").addEventListener('click',()=>{
    axios.get('/api/getLeagueProfile')
    .then(response=>{
        statusBox.innerText = response.data.status_message;
    })
    .catch(()=>{
        statusBox.innerText = "Request Failed";
    });
});

