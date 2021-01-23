const matchHistoryStatusBox = document.getElementById("matchHistoryStatusBox");
const leaderboardsStatusBox = document.getElementById("leaderboardsStatusBox");
const championstatsStatusBox = document.getElementById("championstatsStatusBox");

document.getElementById("matchHistoryButton").addEventListener('click',()=>{
    axios.get('/api/getMatchHistory')
        .then(response=>{
            matchHistoryStatusBox.innerText = response.data.Status;
        })
        .catch(()=>{
            matchHistoryStatusBox.innerText = "Request Failed";
        });
});

document.getElementById("leaderboardsButton").addEventListener('click',()=>{
    axios.get('/api/getLeaderboards')
    .then(response=>{
        leaderboardsStatusBox.innerText = response.data.Status;
    })
    .catch(()=>{
        leaderboardsStatusBox.innerText = "Request Failed";
    });
});

document.getElementById("championstatsButton").addEventListener('click',()=>{
    axios.get('/api/getChampions')
    .then(response=>{
        championstatsStatusBox.innerText = response.data.Status;
    })
    .catch(()=>{
        championstatsStatusBox.innerText = "Request Failed";
    });
});
