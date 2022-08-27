package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account createAccount (Account account);

    //    TODO build out this method.
    int getAccountIdByUsername(String username);

    BigDecimal getBalance (int accountId);



}
