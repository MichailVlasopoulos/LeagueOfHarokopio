let buttons = document.getElementsByClassName('view-button');
let modalBody = document.getElementById('modal-div');

Array.prototype.slice.call(buttons).forEach(button=>{
    button.addEventListener('click',()=>{
        let request_id = button.id;
        let request = getRequestByID(request_id);
        if(request!=null){
            modalBody.innerHTML = "";
            if(request.type == "Champion Statistics"){
                formatChampStats(request);
            }
            else if(request.type == "Match History"){
                formatMatchHistory(request);
            }
            else if(request.type == "Leaderboards"){
                formatLeaderboards(request);
            }
        }
    });
});

function getRequestByID(id){
   for(let i=0;i<=all_requests.length;i++){
     if(all_requests[i].request_id == id){
         return {type:all_requests[i].request.request_type,results:all_requests[i].results};
     }        
   }
   return null;
}

function formatChampStats(request){
    let json = JSON.parse(request.results);
    console.log(json);
    let table = document.createElement('table');

    let th = document.createElement('th');
    th.appendChild(document.createTextNode("Champion ID"));
    table.appendChild(th);
    
    th = document.createElement('th');
    th.appendChild(document.createTextNode("Champion Level"));
    table.appendChild(th);
    
    th = document.createElement('th');
    th.appendChild(document.createTextNode("Champion Points"));
    table.appendChild(th);
 
    for(let item of json){
        let tr = document.createElement('tr');
        let td = document.createElement('td');
        
        td.appendChild(document.createTextNode(item.championId));
        tr.appendChild(td);
        
        td = document.createElement('td');
        td.appendChild(document.createTextNode(item.championLevel));
        tr.appendChild(td);
        
        td = document.createElement('td');
        td.appendChild(document.createTextNode(item.championPoints));
        tr.appendChild(td);

        table.appendChild(tr);
    }

    modalBody.appendChild(table);
}

function formatLeaderboards(request){
    let json = JSON.parse(request.results);
    let table = document.createElement('table');

    let th = document.createElement('th');
    th.appendChild(document.createTextNode("Summoner Name"));
    table.appendChild(th);
    
    th = document.createElement('th');
    th.appendChild(document.createTextNode("League Points"));
    table.appendChild(th);
    
 
    for(let item of json.entries){
        let tr = document.createElement('tr');
        let td = document.createElement('td');
        
        td.appendChild(document.createTextNode(item.summonerName));
        tr.appendChild(td);
        
        td = document.createElement('td');
        td.appendChild(document.createTextNode(item.leaguePoints));
        tr.appendChild(td);
        
        table.appendChild(tr);
    }

    modalBody.appendChild(table);
}

function formatMatchHistory(request){
    let json = JSON.parse(request.results);
    let table = document.createElement('table');

    let th = document.createElement('th');
    th.appendChild(document.createTextNode("Champion ID"));
    table.appendChild(th);
    
    th = document.createElement('th');
    th.appendChild(document.createTextNode("Lane"));
    table.appendChild(th);
    
    th = document.createElement('th');
    th.appendChild(document.createTextNode("Game ID"));
    table.appendChild(th);
 
    for(let item of json.matches){
        let tr = document.createElement('tr');
        let td = document.createElement('td');
        
        td.appendChild(document.createTextNode(item.champion));
        tr.appendChild(td);
        
        td = document.createElement('td');
        td.appendChild(document.createTextNode(item.lane));
        tr.appendChild(td);
        
        td = document.createElement('td');
        td.appendChild(document.createTextNode(item.gameId));
        tr.appendChild(td);

        table.appendChild(tr);
    }

    modalBody.appendChild(table);
    
}