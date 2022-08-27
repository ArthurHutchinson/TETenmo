package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer createTransfer(Transfer transfer);

    void updateAccountsForTransfer(Transfer pendingTransfer);

    Transfer setStatusToApproved(Transfer pendingTransfer);

    List<Transfer> getTransfersByUserId(int userId);

    List<Transfer> getTransferByTransferId(int transferId, int userId);

}
