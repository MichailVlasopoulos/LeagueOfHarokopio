CREATE TABLE hua.User(
    user_id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
    PRIMARY KEY(user_id),

	username VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    summoner_name VARCHAR(50),
    summoner_id VARCHAR(50)
);

CREATE TABLE hua.Request(
    request_id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
    PRIMARY KEY(request_id),

    request_type VARCHAR(64) NOT NULL, 
    request_body TEXT,
    created_at DATETIME(6),

    user_id INT(10) UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE hua.Requests_Results(
    results TEXT,
    request_status VARCHAR(16) NOT NULL,

    request_id INT(10) UNSIGNED NOT NULL,
    PRIMARY KEY(request_id),
    FOREIGN KEY (request_id) REFERENCES Request(request_id) ON DELETE CASCADE
);

CREATE TABLE hua.Subscription_Requests(
    subscription_request_id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
    PRIMARY KEY(subscription_request_id),

    request_type VARCHAR(64) NOT NULL,
    paysafe_pin VARCHAR(64) NOT NULL,
    created_at DATETIME(6) ,
    

    user_id INT(10) UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE hua.Subscription_Requests_Results(
    request_status VARCHAR(16) NOT NULL,

    subscription_request_id INT(10) UNSIGNED NOT NULL,
    PRIMARY KEY(subscription_request_id),
    FOREIGN KEY (subscription_request_id) REFERENCES Subscription_Requests(subscription_request_id) ON DELETE CASCADE
);

CREATE TABLE hua.Authorities(
    role VARCHAR(32) NOT NULL,

    username VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY(username),
    FOREIGN KEY (username) REFERENCES User(username) ON DELETE CASCADE

);

CREATE TABLE hua.User_Password(
    password_hash VARCHAR(128) NOT NULL,

    user_id INT(10) UNSIGNED NOT NULL,
    PRIMARY KEY (user_id), 
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);
