package com.example.microbankingsystem;

import java.io.Serializable;

public class AccountModel implements Serializable {
    private String accountNo;
    private double balance;
    private String type;
    private int pin;

    public AccountModel(String accountNo, double balance, String type, int pin) {
        this.accountNo = accountNo;
        this.balance = balance;
        this.type = type;
        this.pin = pin;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "accountNo='" + accountNo + '\'' +
                ", balance=" + balance +
                ", type='" + type + '\'' +
                ", pin=" + pin +
                '}';
    }
}
