const express = require('express');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const jwtSecurity = require('../../security/jwt.js');
const { default: Axios } = require('axios');

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
        let responseJson;
        let headers = {'Authorization':`Bearer ${req.cookies.LOHTOKEN}`};
        Axios.get(internalSystemEndpoint,{headers:headers})
        .then(response =>{
            responseJson = response.data;
        })
        .catch(error=>{
            console.error(error);
            responseJson = null;
        })
        .finally(()=>{
            res.render('home',{username:res.locals.payload.sub,internalSystemResponse:responseJson});
        }); 
});

module.exports = router;