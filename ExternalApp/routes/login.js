const express = require('express');
const jwtSecurity = require('../security/jwt.js'); 
const bodyParser = require('body-parser');
const { default: Axios } = require('axios');

const router = express.Router();

const internalSystemEndpoint = "http://localhost:8080/authenticate";

router.use(bodyParser.json());
router.use((error,_req,res,next)=>{
    if(error instanceof SyntaxError){
        res.status(501).send("Json Syntax Error");
    }
    else{
        next();
    }
});

router.route('/')
    .get((_req,res)=>{
        res.sendFile('/html/login.html',{root:'public'});
    })
    .post((req,res)=>{
       let username = req.body.username;
       let password = req.body.password;
       if(username && password){
           let request_body = {username:username,password:password};
           let headers = {'Content-Type':'application/json'};
           Axios.post(internalSystemEndpoint,request_body,headers)
           .then(response =>{
                res.cookie('LOHTOKEN',response.data.jwt,{maxAge:900000,httpOnly:true,sameSite:true});
                res.send({status:"OK"});
           })
           .catch(error=>{
                res.status(501).send({status:"Failed to authorize."});
           });
       }
    });

module.exports = router;
