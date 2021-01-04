const jwt = require('jsonwebtoken');
const TOKEN_SECRET = "I_<3_RNS_GR33K_FAN_PAGE";

module.exports = {
    authenticateToken:(req,res,next)=>{
        try{
            let token = req.cookies.LOHTOKEN;
            res.locals.payload = jwt.verify(token,TOKEN_SECRET);
            next();
        }
        catch{
            res.status(401).redirect('/login');
        }
    },
    signToken:(payload)=>{
        return jwt.sign(payload,TOKEN_SECRET);
    }
}