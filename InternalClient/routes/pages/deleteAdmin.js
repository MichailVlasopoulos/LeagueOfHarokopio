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
        if(hasRole(res.locals.payload.roles,["ROLE_MAIN_ADMIN"])){
            let allAdmins = {Admins:[]};
            let headers = {'Authorization':`Bearer ${req.cookies.LOHTOKEN}`};
            Axios.get("http://localhost:8080/mainAdmin/getAllAdmins",{headers:headers})
                .then(response=>{
                    allAdmins = {Admins:response.data};
                })
                .catch(error=>{
                })
                .finally(()=>{
                    res.render('deleteAdmin',allAdmins);
                });
        }
        else{
            res.redirect('/login');
        }
    })
    .post((req,res)=>{
        if(!isNaN(req.body.admin_id)){
            let headers = {'Authorization':`Bearer ${req.cookies.LOHTOKEN}`};
            Axios.get(`http://localhost:8080/mainAdmin/deleteAdmin?user_id=${req.body.admin_id}`,{headers:headers})
                .then(response=>{
                    res.sendStatus(200);
                })
                .catch(error=>{
                    res.sendStatus(500);
                });
        }
});

module.exports = router;