const express = require('express');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const jwtSecurity = require('../../../security/jwt.js');
const { default: Axios } = require('axios');
const hasRole = require('../../../security/roleChecker.js');

const router = express.Router();
const internalSystemEndpoint = "http://localhost:8080/user/goPremium"; 

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
    .post(jwtSecurity.authenticateToken,(req,res)=>{
        if(hasRole(res.locals.payload.roles,["ROLE_USER"])){
            let pin = req.body.paysafe_pin.trim();
            if(pin){
                let headers = {'Authorization':`Bearer ${req.cookies.LOHTOKEN}`};
                let body = {paysafe_pin:pin};
                let status = 500;
                Axios.post(internalSystemEndpoint,body,{headers:headers})
                .then(response =>{
                    status = 200;
                })
                .catch(error=>{
                    console.error(error);
                })
                .finally(()=>{
                    res.sendStatus(status);
                });
            }
            else{
                res.sendStatus(400);
            }
        }
        else{
            res.status(403).json({Status:"Unauthorized"});
        }
});

module.exports = router;