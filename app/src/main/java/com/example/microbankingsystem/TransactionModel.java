package com.example.microbankingsystem;

import java.util.Date;

public class TransactionModel{

    int ID, amount;
    String accNo, type;
    Date date;

    public TransactionModel(int ID, String accNo, int amount, String type, Date date) {
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
