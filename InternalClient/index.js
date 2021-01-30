const express = require('express');
const app = express();

app.set('view engine','ejs');
app.use(express.static('public'));

app.use('/login',require('./routes/pages/login.js'));
app.use('/logout',require('./routes/pages/logout.js'));
app.use('/controlPanel',require('./routes/pages/controlPanel.js'));

app.use('/api/handlePremiumRequest',require('./routes/api/handlePremiumRequest.js'));

app.use('/admin/createAdmin',require('./routes/pages/createAdmin.js'));
app.use('/admin/deleteAdmin',require('./routes/pages/deleteAdmin.js'));

app.get('/',(req,res)=>{
    res.redirect('/controlPanel');
});

app.listen(5001);