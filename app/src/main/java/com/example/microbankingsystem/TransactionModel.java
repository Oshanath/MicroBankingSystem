package com.example.microbankingsystem;

import java.util.Date;

public class TransactionModel{

    private int ID;
    private double amount;
    private String accNo, type;
    private String date;

    public TransactionModel(int ID, String accNo, double amount, String type, String date) {
        this.ID = ID;
        this.accNo = accNo;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    @Override
    public String toString() {
        return "TransactionModel{" +
                "ID=" + ID +
                ", amount=" + amount +
                ", accNo='" + accNo + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
