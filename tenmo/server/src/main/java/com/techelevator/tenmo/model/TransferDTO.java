package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {

    private String  toUserName;
    private String fromUserName;
    private BigDecimal transferAmount;
    private int transferId;

    public TransferDTO() {
    }

    public TransferDTO(String fromUserName, String toUserName, BigDecimal transferAmount, int transferId) {
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
        this.transferAmount = transferAmount;
        this.transferId = transferId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
                "toUserName='" + toUserName + '\'' +
                ", fromUserName='" + fromUserName + '\'' +
                ", transferAmount=" + transferAmount +
                ", transferId=" + transferId +
                '}';
    }
}
