package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {

    private String  toUserName;
    private String fromUserName;
    private BigDecimal transferAmount;

    public TransferDTO() {
    }

    public TransferDTO(String toUserName, String fromUserName, BigDecimal transferAmount) {
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
        this.transferAmount = transferAmount;
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

    @Override
    public String toString() {
        return "TransferDTO{" +
                "toUserName='" + toUserName + '\'' +
                ", fromUserName='" + fromUserName + '\'' +
                ", transferAmount=" + transferAmount +
                '}';
    }
}
