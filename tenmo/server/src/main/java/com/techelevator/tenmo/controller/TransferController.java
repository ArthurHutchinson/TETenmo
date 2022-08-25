package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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


    @RequestMapping(value = "/transfer/userid", method = RequestMethod.GET)
    public List<Transfer> findTransfersByUserId(@RequestBody int userId) {
        return transferDao.getTransfersByUserId(userId);
    }

    @RequestMapping(value = "/transfer/transferid", method = RequestMethod.GET)
    public List<Transfer> getTransferById(@RequestBody int transferId) {
        return transferDao.getTransferByTransferId(transferId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public boolean createTransfer(@RequestBody Transfer newTransfer) {

              if(transferDao.createTransfer(newTransfer) == null){
                  return false;
              }return true;
    }

}
