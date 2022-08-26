package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

//TODO: Add Authentication and Authorization Tags

@PreAuthorize("isAuthenticated()")
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

    // TODO: TEST THIS
    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
    public List<Transfer> findTransfersByUserId(Principal principal) {
        return transferDao.getTransfersByUserId(userDao.findIdByUsername(principal.getName()));

    }

    // TODO: TEST THIS
    @RequestMapping(value = "/transfer/{transferId}", method = RequestMethod.GET)
    public List<Transfer> getTransferById(@PathVariable int transferId, Principal principal) {
        return transferDao.getTransferByTransferId(transferId, userDao.findIdByUsername(principal.getName()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public boolean createTransfer(@RequestBody Transfer newTransfer) {
// use accountDao to find out (from/to)_account_id for given username
        //then set newTransfer to_account and from_account
              if(transferDao.createTransfer(newTransfer) == null){
                  return false;

              }
        transferDao.setStatusToApproved(newTransfer);
              return true;
    }

}
