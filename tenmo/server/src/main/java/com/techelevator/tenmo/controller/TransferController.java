package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

//TODO: Add Authentication and Authorization Tags

@RestController
public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferController (TransferDao transferDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }


    // TODO: This is currently passing the User's ID through the URL. We need to fix this.
    @RequestMapping(value = "/transfer/{userId}", method = RequestMethod.GET)
    public List<Transfer> findTransfersByUserId(@PathVariable int userId) {
        return transferDao.findTransfersByUserId(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public boolean createTransfer(@RequestBody Transfer newTransfer) {
        try {
            transferDao.createTransfer(newTransfer);
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

}
