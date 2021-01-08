-- Creating b+ trees sorted in descending order on the id , because the bigger the id the furthest it has been submitted to the system
--

CREATE UNIQUE INDEX req_res_idx ON Requests_Results (request_id DESC , request_status );
-- DROP INDEX req_res_idx;

CREATE UNIQUE INDEX sub_req_res_idx ON Subscription_Requests_Results (subscription_request_id DESC , request_status );
-- DROP INDEX sub_req_res_idx;

CREATE UNIQUE INDEX auth_idx ON Authorities (username DESC, role);
-- DROP INDEX auth_idx;

CREATE UNIQUE INDEX sub_req_idx ON Subscription_Requests (subscription_request_id DESC, request_type);
-- DROP INDEX sub_req_idx;

CREATE UNIQUE INDEX req_idx ON Request (request_id DESC, request_type);
-- DROP INDEX req_idx;

-- CREATE ON JOIN INDEX FOR PASSWORDS AND USER , because default index on user is a unique index on user_id , 
-- but the join on passwords happens on username column

