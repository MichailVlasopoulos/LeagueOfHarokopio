const express = require('express');
const cookieParser = require('cookie-parser');
const router = express.Router();

router.use(cookieParser());

router.route('/')
    .get((req,res)=>{
        res.render('logout');
    })
    .post((req,res)=>{
        res.cookie('LOHTOKEN',"");
        res.redirect('/login');
    });


module.exports = router;