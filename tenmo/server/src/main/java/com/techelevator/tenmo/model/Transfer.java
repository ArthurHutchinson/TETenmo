package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private String fromUsername;
    private int transferId;
    private int fromAccountId;
    private String toUsername;
    private int toAccountId;
    private boolean isApproved;
    private BigDecimal transferAmount = new BigDecimal("0.00");

    public Transfer() {
    }

    public Transfer(int fromAccountId, int toAccountId, BigDecimal transferAmount, boolean isApproved) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.transferAmount = transferAmount;
        this.isApproved = isApproved();
    }

    public Transfer(int transferId, int fromAccountId, int toAccountId, BigDecimal transferAmount, boolean isApproved) {
        this.transferId = transferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.transferAmount = transferAmount;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
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

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "fromUsername='" + fromUsername + '\'' +
                ", transferId=" + transferId +
                ", fromAccountId=" + fromAccountId +
                ", toUsername='" + toUsername + '\'' +
                ", toAccountId=" + toAccountId +
                ", isApproved=" + isApproved +
                ", transferAmount=" + transferAmount +
                '}';
    }
}
