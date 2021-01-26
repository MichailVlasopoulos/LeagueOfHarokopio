let buttons = document.getElementsByClassName("action-button");

Array.prototype.slice.call(buttons).forEach(button=>{
    button.addEventListener('click',()=>{
        let button_id = button.getAttribute('id');
        let action = button_id.split("-")[0];
        let request_id = button_id.split("-")[1];
        // let headers = {'Content-Type':'application/json'}
        let body = {action:action,request_id:request_id};        
        axios.post('/api/handleRequest',body)
        .then(response=>{
            document.location = '/';
        })
        .catch(()=>{
            
        });
    })
})
