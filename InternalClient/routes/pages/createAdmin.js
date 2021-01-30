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

let internalSystemEndpoint = "http://localhost:8080/mainAdmin/registerAdmin";

router.use(cookieParser());

router.route('/')
    .get(jwt.authenticateToken,(req,res)=>{
        if(hasRole(res.locals.payload.roles,["ROLE_MAIN_ADMIN"])){
            res.render('createAdmin');
        }
        else{
            res.redirect('/login');
        }
    })
    .post((req,res)=>{
        if(req.body){
            let headers = {'Authorization':`Bearer ${req.cookies.LOHTOKEN}`};
            Axios.post(internalSystemEndpoint,req.body,{headers:headers})
                .then(response=>{
                    res.sendStatus(200);
                })
                .catch(error=>{
                    res.sendStatus(500);
                });
        }
});

module.exports = router;