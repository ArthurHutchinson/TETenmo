package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private int fromUserId;
    private int fromAccountId;
    private int toUserId;
    private int toAccountId;
    private BigDecimal transferAmount = new BigDecimal("0.00");

    public Transfer() {
    }

    public Transfer(int fromUserId, int fromAccountId, int toUserId, int toAccountId, BigDecimal transferAmount) {
        this.fromUserId = fromUserId;
        this.fromAccountId = fromAccountId;
        this.toUserId = toUserId;
        this.toAccountId = toAccountId;
        this.transferAmount = transferAmount;
    }

    public Transfer(int transferId, int fromUserId, int fromAccountId, int toUserId, int toAccountId, BigDecimal transferAmount) {
        this.transferId = transferId;
        this.fromUserId = fromUserId;
        this.fromAccountId = fromAccountId;
        this.toUserId = toUserId;
        this.toAccountId = toAccountId;
        this.transferAmount = transferAmount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", fromUserId=" + fromUserId +
                ", fromAccountId=" + fromAccountId +
                ", toUserId=" + toUserId +
                ", toAccountId=" + toAccountId +
                ", transferAmount=" + transferAmount +
                '}';
    }

}
