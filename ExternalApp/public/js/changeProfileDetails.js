let usernameArea = document.getElementById("usernameInput");
let firstnameArea = document.getElementById("firstnameInput");
let lastnameArea = document.getElementById("lastnameInput");
let summonernameArea = document.getElementById("summonernameInput");

let updateButtonArea = document.getElementById("updateButtonArea");
let statusArea = document.getElementById("statusArea");

// Get field details.
let username = usernameArea.innerText;
let firstname = firstnameArea.innerText;
let lastname = lastnameArea.innerText;
let summonername = summonernameArea.innerText;

document.getElementById("changeDetailsButton").addEventListener('click',()=>{
    // Replace with inputs.
    let tmp = document.createElement("input");
    tmp.setAttribute("id","newusernameInput");
    usernameArea.innerHTML = '';
    tmp.value = username;
    usernameArea.appendChild(tmp);

    tmp = document.createElement("input");
    tmp.setAttribute("id","newfirstnameInput");
    firstnameArea.innerHTML = '';
    tmp.value = firstname;
    firstnameArea.appendChild(tmp);

    tmp = document.createElement("input");
    tmp.setAttribute("id","newlastnameInput");
    lastnameArea.innerHTML = '';
    tmp.value = lastname;
    lastnameArea.appendChild(tmp);

    tmp = document.createElement("input");
    tmp.setAttribute("id","newsummonernameInput");
    summonernameArea.innerHTML = '';
    tmp.value = summonername;
    summonernameArea.appendChild(tmp);

    // Create update button.
    let updateButton = document.createElement("button");
    updateButton.classList.add("btn");
    updateButton.classList.add("btn-secondary");
    
    updateButton.innerText = "Update Details"
    updateButton.addEventListener('click',()=>{
        let newusername = document.getElementById("newusernameInput").value;
        let newfirstname = document.getElementById("newfirstnameInput").value;
        let newlastname = document.getElementById("newlastnameInput").value;
        let newsummonername = document.getElementById("newsummonernameInput").value;
        let requestJson = {username:newusername,firstname:newfirstname,lastname:newlastname,summonername:newsummonername};
        let proceedWithRequest = true;        
        
        // Check if details are valid.
        for(item in requestJson){
            let value = requestJson[item].trim();
            if(value==''){
                proceedWithRequest = false;
                break;
            };
        }
        // If valid proceed with request.s
        if(proceedWithRequest){
            console.log(requestJson);
        }
    });
    
  
    updateButtonArea.innerHTML = '';
    updateButtonArea.appendChild(updateButton);

});

