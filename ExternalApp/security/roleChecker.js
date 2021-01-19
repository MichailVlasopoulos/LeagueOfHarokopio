function hasRole(currentAuthorities,desiredRoles){
    // desiredRoles = [PREMIUM_USER,ADMIN,...]
    // currentAuthorities = // [{"authority": "ROLE_MAIN_ADMIN"}]
    for(item of currentAuthorities){
        if(desiredRoles.includes(item.authority)){
            return true;
        }
    }
    return false;
}
module.exports = hasRole;