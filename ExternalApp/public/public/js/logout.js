const logoutButton = document.getElementById('logoutButton');

logoutButton.addEventListener('click',()=>{
    axios.post('/logout')
        .then(resp=>{
            document.location = '/login';
        })
        .catch(err=>{
        });
});