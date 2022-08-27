package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public boolean createAccount(@RequestBody Account newAccount, Principal principal){
        int id = userDao.findIdByUsername(principal.getName());
        newAccount = new Account();
        try {
            newAccount.setUserId(id);
            accountDao.createAccount(newAccount);
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/account/getBalance", method = RequestMethod.GET)
    public BigDecimal getBalance (@RequestBody Integer accountId) {
        return accountDao.getBalance(accountId);
    }

}
