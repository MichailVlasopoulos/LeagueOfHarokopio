DELIMITER |
CREATE PROCEDURE insertUser (IN username VARCHAR(50),IN email VARCHAR(50),summoner_name VARCHAR(50), password_hash VARCHAR(128))
BEGIN
	DECLARE userID User_Password.user_id%type;
    
    insert into User (username,email,summoner_name) values (username ,email,summoner_name);
    insert into Authorities (username, role) values (username, "Simple User");
    
	SELECT user_id INTO userID FROM User WHERE username=username;
    insert into User_Password (password_hash, user_id) values (password_hash, user_id);
END |

delimiter ;



DELIMITER |
CREATE PROCEDURE insertAdmin(IN username VARCHAR(50), first_name VARCHAR(30), last_name VARCHAR(30),IN email VARCHAR(50), password_hash VARCHAR(128), admin_role VARCHAR(32))
BEGIN
	DECLARE user_id User_Password.user_id%type;
    
    insert into User(username,first_name, last_name,email) values (username ,first_name, last_name, email);
    insert into Authorities(username, role) values(username, admin_role); -- TODO CHANGE 
    
	SELECT user_id INTO userID FROM User WHERE username=username;
    insert into User_Password(password_hash, user_id) values(password_hash, user_id);
END |

delimiter ;


