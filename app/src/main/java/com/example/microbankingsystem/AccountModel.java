package com.example.microbankingsystem;

public class AccountModel {
    private String accountNo;
    private double balance;
    private boolean joint;

    public AccountModel(String accountNo, double balance, boolean joint){
        this.accountNo = accountNo;
        this.balance = balance;
        this.joint = joint;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isJoint() {
        return joint;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setJoint(boolean joint) {
        this.joint = joint;
    }
}
