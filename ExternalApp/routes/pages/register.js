const express = require('express');
const bodyParser = require('body-parser');
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

router.route('/')
    .get((req,res)=>{
        res.render('register');
});

module.exports = router;