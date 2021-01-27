let usernameInput = document.getElementById("usernameInput");
let emailInput = document.getElementById("emailInput");
let firstnameInput = document.getElementById("firstnameInput");
let lastnameInput = document.getElementById("lastnameInput");
let summonernameInput = document.getElementById("summonernameInput");
let passwordAInput = document.getElementById("passwordAInput");
let passwordBInput = document.getElementById("passwordBInput");
let statusArea = document.getElementById("statusArea");

document.getElementById('registerButton').addEventListener('click',()=>{
    let username = usernameInput.value;
    let email = emailInput.value;
    let firstname = firstnameInput.value;
    let lastname = lastnameInput.value;
    let summonername = summonernameInput.value;
    let passwordA = passwordAInput.value;
    let passwordB = passwordBInput.value;

    if(username && email && firstname && lastname && summonername && passwordA && passwordB){
        if(passwordA == passwordB){
            let body = {username:username,email:email,firstName:firstname,lastName:lastname,summonerName:summonername,password:passwordA}
            axios.post('/register',body)
                .then(response=>{
                    statusArea.innerHTML = "Registration Successful";
                    document.location = "/login";
                })
                .catch(()=>{
                    statusArea.innerHTML = "Registration Failed";
                });
        }
    }


});