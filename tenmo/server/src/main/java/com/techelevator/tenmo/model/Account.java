package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {

    @JsonProperty("account_id")
    private int accountId;
    private int userId;
    @JsonProperty("balance")
    private BigDecimal balance = new BigDecimal("0.00");

    public Account() {
    }

    public Account(int userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}
