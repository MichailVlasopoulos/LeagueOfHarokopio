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

const services = require('./routes/services.js');
app.use('/services',services);

const resolved = require('./routes/resolved.js');
app.use('/resolved',resolved);

const pending = require('./routes/pending.js');
app.use('/pending',pending);

const premium = require('./routes/premium.js');
app.use('/premium',premium);

const admin = require('./routes/admin.js');
app.use('/admin',admin);

const profile = require('./routes/profile.js');
app.use('/profile',profile);

app.get('/',(_req,res)=>{
    res.render('index');
});

app.listen(5000);