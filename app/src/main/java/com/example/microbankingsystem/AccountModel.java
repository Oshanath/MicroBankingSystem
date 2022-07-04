package com.example.microbankingsystem;

public class AccountModel {
    private String accountNo;
    private double balance;
    private int joint;

    public AccountModel(String accountNo, double balance, int joint){
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

    public int isJoint() {
        return joint;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setJoint(int joint) {
        this.joint = joint;
    }
}
