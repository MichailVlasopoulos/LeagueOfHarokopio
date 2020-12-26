const express = require('express');
const cookieParser = require('cookie-parser');
const router = express.Router();

router.use(cookieParser());

router.route('/')
    .get((req,res)=>{
        res.sendFile('/html/logout.html',{root:'public'});
    })
    .post((req,res)=>{
        res.cookie('LOHTOKEN',"");
        res.redirect('/login');
    });


module.exports = router;