const express = require('express');
const app = express();

app.set('view engine','ejs');
app.use(express.static('public'));

const login = require('./routes/login.js');
app.use('/login',login);

const user = require('./routes/user.js');
app.use('/user',user);

const logout = require('./routes/logout.js');
app.use('/logout',logout);

app.get('/',(_req,res)=>{
    res.send(':)');
});

app.listen(5000);