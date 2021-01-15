const express = require('express');
const app = express();

app.set('view engine','ejs');
app.use(express.static('public'));

app.use('/login',require('./routes/login.js'));
app.use('/logout',require('./routes/logout.js'));
app.use('/controlPanel',require('./routes/controlPanel.js'));

app.get('/',(req,res)=>{
    res.redirect('/controlPanel');
});

app.listen(5001);