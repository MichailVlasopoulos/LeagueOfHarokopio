select * from hua.User;
select * from hua.Authorities;
select * from hua.Request;

-- Update users role 
UPDATE hua.Authorities 
SET 
   role = "Main Admin"
WHERE
    username = "username";

-- Create user with id 1.

INSERT INTO hua.User VALUES (7,"Jojoc" , "Jojoc", "zed","jojoc@zed.gr",NULL,NULL); -- 0 = user_id PK
INSERT INTO hua.User_Password VALUES ("HASH7",7);
INSERT INTO hua.Authorities VALUES ("Simple User","Jojoc"); 

-- Create dummy "GET_LAST_GAME" request for user with id 1.
INSERT INTO hua.Request VALUES (125,"GET_LAST_GAME",NULL,now(),3); -- 123 = request_id PK 
INSERT INTO hua.Requests_Results VALUES (NULL,"PENDING",125);

-- Create dummy "GO_PREM" request for user with id 1.
INSERT INTO hua.Subscription_Requests VALUES (236,"GO_PREMIUM","MYPIN1G23GHJ13HS13",now(),3); -- 234 = sub_req_id PK
INSERT INTO hua.Subscription_Requests_Results VALUES ("PENDING_GO_PREM_REQ",236);