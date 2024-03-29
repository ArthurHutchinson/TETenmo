BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transfer;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance decimal(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

CREATE SEQUENCE seq_transfer_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;
  
 CREATE TABLE transfer (
 	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
  	from_account_id int NOT NULL,
 	to_account_id int NOT NULL,
	transfer_amount decimal(13,2) NOT NULL,
	is_approved boolean,
 	CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT FK_from_account_id FOREIGN KEY (from_account_id) REFERENCES account (account_id),
	CONSTRAINT FK_to_account_id FOREIGN KEY (to_account_id) REFERENCES account (account_id)
  );

 INSERT INTO tenmo_user (username, password_hash)
 VALUES ('TestUser1', '$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2'),
        ('TestUser2', '$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy');

 INSERT INTO account (user_id, balance)
 VALUES ((SELECT user_id FROM tenmo_user WHERE username = 'TestUser1'), 1000);
 INSERT INTO account (user_id, balance)
 VALUES ((SELECT user_id FROM tenmo_user WHERE username = 'TestUser2'), 2000);

 INSERT INTO transfer (from_account_id, to_account_id, transfer_amount)
 VALUES ((SELECT a.account_id FROM account AS a JOIN tenmo_user AS u ON u.user_id = a.user_id WHERE u.username = 'TestUser1' AND a.account_id = 2001),
 (SELECT a.account_id FROM account AS a JOIN tenmo_user AS u ON u.user_id = a.user_id WHERE u.username = 'TestUser2' AND a.account_id = 2002),
 50);

COMMIT;
