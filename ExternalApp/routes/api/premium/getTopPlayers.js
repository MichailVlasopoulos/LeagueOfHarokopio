const express = require('express');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const jwtSecurity = require('../../../security/jwt.js');
const { default: Axios } = require('axios');
const hasRole = require('../../../security/roleChecker.js');

const router = express.Router();
const internalSystemEndpoint = "http://localhost:8080/premiumUser/requestTopPlayersProfiles";  

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
    .get(jwtSecurity.authenticateToken,(req,res)=>{
        if(hasRole(res.locals.payload.roles,["ROLE_PREMIUM_USER"])){
            let responseJson = {Status:"Failed"};
            let headers = {'Authorization':`Bearer ${req.cookies.LOHTOKEN}`};
            Axios.get(internalSystemEndpoint,{headers:headers})
            .then(response =>{
                responseJson = response.data;
            })
            .finally(()=>{
                res.json(responseJson);
            });
        }
        else{
            res.status(403).json({Status:"Unauthorized"});
        }
});

module.exports = router;