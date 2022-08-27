package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;

import java.util.List;

public interface TransferDao {

    Transfer createTransfer(Transfer transfer);

    int verifyTransferIsLegit(Transfer newTransfer);

    void updateAccountsForTransfer(Transfer pendingTransfer);

    Transfer setStatusToApproved(Transfer pendingTransfer);

    List<TransferDTO> getTransfersByUserId(int userId, String username, int accountId);

    List<TransferDTO> getTransferByTransferId(int userId, String username, int accountId, int transfer_id);
}
