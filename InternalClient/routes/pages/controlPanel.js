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
        if(hasRole(res.locals.payload.role,"ROLE_MAIN_ADMIN")){
            res.render('adminControlPanel');
        }
        else if(hasRole(res.locals.payload.role,"ROLE_PREMIUM_ADMIN")){
            let requests;
            Axios.get('http://localhost:8080/premiumAdmin/getRequestsByType?requestType=PENDING')
                .then(response=>{
                    
                })
                .catch(()=>{
                    requests = {};
                })
                .finally(()=>{
                    res.render('premiumControlPanel',{requests:premium_requests});
                });
        }
        else{
            res.redirect('/login');
        }
    })
    .post((req,res)=>{
        res.send("OK");
});

module.exports = router;