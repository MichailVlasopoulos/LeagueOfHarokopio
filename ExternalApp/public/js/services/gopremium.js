let paysafeInput = document.getElementById('paysafeInput');
let requestStatusField = document.getElementById('requestStatus');

document.getElementById('applyButton').addEventListener('click',()=>{
    let pin = paysafeInput.value.trim();
    if(pin){
        let body = {paysafe_pin:pin};
        axios.post('/api/applyForPremium',body)
            .then(response=>{
                requestStatusField.innerHTML = "Request Successful";
            })
            .catch(error=>{
                requestStatusField.innerHTML = "Request Failed"
            });
    } 
});