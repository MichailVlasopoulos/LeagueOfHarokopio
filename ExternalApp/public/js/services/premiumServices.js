const liveMatchStatusBox = document.getElementById("liveMatchStatusBox");
const topPlayersStatusBox = document.getElementById("topPlayersStatusBox");
const generalChampionStatsStatusBox = document.getElementById("generalChampionStatsStatusBox");

document.getElementById("liveMatchButton").addEventListener('click',()=>{
    axios.get('/api/getMatchHistory')
        .then(response=>{
            liveMatchStatusBox.innerText = response.data.Status;
        })
        .catch(()=>{
            liveMatchStatusBox.innerText = "Request Failed";
        });
});

document.getElementById("topPlayersButton").addEventListener('click',()=>{
    axios.get('/api/getLeaderboards')
    .then(response=>{
        topPlayersStatusBox.innerText = response.data.Status;
    })
    .catch(()=>{
        topPlayersStatusBox.innerText = "Request Failed";
    });
});

document.getElementById("generalChampionStatsButton").addEventListener('click',()=>{
    axios.get('/api/getChampions')
    .then(response=>{
        generalChampionStatsStatusBox.innerText = response.data.Status;
    })
    .catch(()=>{
        generalChampionStatsStatusBox.innerText = "Request Failed";
    });
});
