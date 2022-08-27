package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
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

    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
    public List<TransferDTO> findTransfersByUserId(Principal principal) {

        List<TransferDTO> transfers = transferDao.getTransfersByUserId(userDao.findIdByUsername(principal.getName()), principal.getName(), accountDao.getAccountIdByUsername(principal.getName()));

        return transfers;

    }

    @RequestMapping(value = "/transfer/getTransferByTransferId", method = RequestMethod.GET)
    public List<TransferDTO> getTransferById(Principal principal, @RequestBody int transferId) {
        List<TransferDTO> transfers = transferDao.getTransferByTransferId(userDao.findIdByUsername(principal.getName()),
                principal.getName(),
                accountDao.getAccountIdByUsername(principal.getName()),
                transferId
                );
        return transfers;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String sendTransfer(@RequestBody Transfer newTransfer, Principal principal) {
        /* transfer format for postman:
         *       "toUsername" : "User1"
         *       "transferAmount : 0.00" */
        newTransfer.setFromUsername(principal.getName());
        newTransfer.setToAccountId(accountDao.getAccountIdByUsername(newTransfer.getToUsername()));
        newTransfer.setFromAccountId(accountDao.getAccountIdByUsername(principal.getName()));
        if (transferDao.verifyTransferIsLegit(newTransfer) == 1) {
            if (transferDao.createTransfer(newTransfer) == null) {
                return "Please review the details of your transaction";
            }
            transferDao.updateAccountsForTransfer(newTransfer);
            transferDao.setStatusToApproved(newTransfer);
            return "Transfer of " + newTransfer.getTransferAmount() + " to " + newTransfer.getToUsername() + " is complete.";
        }
        return "Please review the details of your transaction";
    }
}
