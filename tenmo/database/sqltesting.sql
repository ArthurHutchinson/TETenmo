SELECT *
FROM transfer;

SELECT *
FROM account;

SELECT *
FROM tenmo_user;

UPDATE account SET balance = balance - 250 WHERE account_id = 2001;
UPDATE account SET balance = balance + 250 WHERE account_id = 2002;

SELECT transfer_id, from_user_id, from_account_id, to_user_id, to_account_id, transfer_amount
FROM transfer
WHERE from_user_id = 1001;



