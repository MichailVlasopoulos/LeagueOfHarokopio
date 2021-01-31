const express = require('express');
const router = express.Router();
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const jwt = require('../../security/jwt.js');
const hasRole = require('../../security/roleChecker.js');
const { default: Axios } = require('axios');

router.use(bodyParser.json());
router.use((error,_req,res,next)=>{
    if(error instanceof SyntaxError){
        res.status(501).send("Json Syntax Error");
    }
    else{
        next();
    }
});

router.use(cookieParser());

router.route('/')
    .get(jwt.authenticateToken,(req,res)=>{
        if(hasRole(res.locals.payload.roles,["ROLE_MAIN_ADMIN"])){
            res.render('adminControlPanel');
        }
        else if(hasRole(res.locals.payload.roles,["ROLE_PREMIUM_ADMIN"])){
            let requests;
            let headers = {'Authorization':`Bearer ${req.cookies.LOHTOKEN}`};
            Axios.get('http://localhost:8080/premiumAdmin/getRequestsByType?requestType=PENDING',{headers:headers})
                .then(response=>{
                    requests = response.data;
                    res.render('premiumControlPanel',requests);
                })
                .catch((error)=>{
                    requests = {Requests:[]};
                    res.render('premiumControlPanel',requests);
                });
        }
        else{
            res.redirect('/login');
        }
});

module.exports = router;