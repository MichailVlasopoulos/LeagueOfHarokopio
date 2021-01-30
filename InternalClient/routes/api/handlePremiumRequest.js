const express = require('express');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const jwtSecurity = require('../../security/jwt.js');
const { default: Axios } = require('axios');
const hasRole = require('../../security/roleChecker.js');

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
    .post(jwtSecurity.authenticateToken,(req,res)=>{
        if(hasRole(res.locals.payload.roles,["ROLE_PREMIUM_ADMIN"])){
            if((req.body.action == "accept" || req.body.action == "deny") && !isNaN(req.body.request_id)){
                let internalSystemEndpoint = `http://localhost:8080/admin/updateRequest/${req.body.action}?requestId=${req.body.request_id}`;
                let headers = {'Authorization':`Bearer ${req.cookies.LOHTOKEN}`}; 
                Axios.get(internalSystemEndpoint,{headers:headers}) //TODO DO POST
                .then(response=>{
                    status = response.status;
                })
                .catch(error=>{
                    status = 500;
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
            res.status(403).json({response:"Unauthorized"});
        }
});

module.exports = router;