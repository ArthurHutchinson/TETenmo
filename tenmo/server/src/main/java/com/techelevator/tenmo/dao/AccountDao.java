package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

    Account createAccount (Account account);

    Account getBalance (int accountId);

}
