const express = require('express');
const app = express();

app.set('view engine','ejs');
app.use(express.static('public'));

app.use('/login',require('./routes/pages/login.js'));
app.use('/logout',require('./routes/pages/logout.js'));
app.use('/services',require('./routes/pages/services.js'));
app.use('/resolved',require('./routes/pages/resolved.js'));
app.use('/pending',require('./routes/pages/pending.js'));
app.use('/premium',require('./routes/pages/premium.js'));
app.use('/admin',require('./routes/pages/admin.js'));
app.use('/profile',require('./routes/pages/profile.js'));
app.use('/gopremium',require('./routes/pages/gopremium.js'));

app.use('/register',require('./routes/pages/register.js'));

app.use('/api/getMatchHistory',require('./routes/api/user/getMatchHistory.js'));
app.use('/api/getLeaderboards',require('./routes/api/user/getLeaderboards.js'));
app.use('/api/getChampions',require('./routes/api/user/getChampions.js'));
app.use('/api/applyForPremium',require('./routes/api/user/applyForPremium.js'));

app.use('/api/getPendings',require('./routes/api/user/getPendings.js'));
app.use('/api/getResolved',require('./routes/api/user/getResolved.js'));

app.use('/api/getTopPlayers',require('./routes/api/premium/getTopPlayers.js'));
app.use('/api/getGeneralChampionStats',require('./routes/api/premium/getGeneralChampionStats.js'));
app.use('/api/getgetLiveStats',require('./routes/api/premium/getLiveStats.js'));

app.use('/api/handleRequest',require('./routes/api/admin/handleRequest.js'));

app.get('/',(_req,res)=>{
    res.render('index');
});

app.listen(5000);