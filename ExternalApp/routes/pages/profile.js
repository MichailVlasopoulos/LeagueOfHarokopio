const express = require('express');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const jwtSecurity = require('../../security/jwt.js');
const { default: Axios } = require('axios');
const hasRole = require('../../security/roleChecker.js');

const router = express.Router();
const internalSystemEndpoint = "http://localhost:8080/user"; 

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
        if(hasRole(res.locals.payload.roles,["ROLE_USER","ROLE_PREMIUM"])){
            res.render('profile',{});
        }
        else{
            res.redirect('/login');
        }
});

module.exports = router;