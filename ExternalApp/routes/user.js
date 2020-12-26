const express = require('express');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const jwtSecurity = require('../security/jwt.js');

const router = express.Router();

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
    .get(jwtSecurity.authenticateToken,(_req,res)=>{
    res.render('home',{username:res.locals.payload.sub});
});

module.exports = router;