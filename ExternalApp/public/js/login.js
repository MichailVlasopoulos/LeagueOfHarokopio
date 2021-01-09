let usernameField = document.getElementById("usernameInputField");
let passwordField = document.getElementById("passwordInputField");
let resultField = document.getElementById('resultField');

document.getElementById("loginButton").addEventListener('click',()=>{
    let username = usernameField.value.trim();
    let password = passwordField.value.trim();
    if(username && password){
        axios.post('/login',{
            username:username,
            password:password
        })
        .then(response=>{
            document.location = '/';
        })
        .catch(error=>{
            resultField.innerHTML = '';
            resultField.append('Login Failed.');
        });
    }
});
