let usernameInput = document.getElementById("usernameInput");
let emailInput = document.getElementById("emailInput");
let firstnameInput = document.getElementById("firstnameInput");
let lastnameInput = document.getElementById("lastnameInput");
let passwordAInput = document.getElementById("passwordAInput");
let passwordBInput = document.getElementById("passwordBInput");
let statusArea = document.getElementById("statusArea");
let adminTypeInput = document.getElementById("adminTypes");

document.getElementById('registerButton').addEventListener('click',()=>{
    let username = usernameInput.value;
    let email = emailInput.value;
    let firstname = firstnameInput.value;
    let lastname = lastnameInput.value;
    let adminType = adminTypeInput.value;
    let passwordA = passwordAInput.value;
    let passwordB = passwordBInput.value;
    if(username && email && firstname && lastname && adminType && passwordA && passwordB){
        if(passwordA == passwordB){
            let body = {username:username,email:email,firstName:firstname,lastName:lastname,summonerName:"",password:passwordA,role:adminType}
            axios.post('/admin/createAdmin',body)
                .then(response=>{
                    statusArea.innerHTML = "Registration Successful";
                })
                .catch(()=>{
                    statusArea.innerHTML = "Registration Failed";
                });
        }
    }


});