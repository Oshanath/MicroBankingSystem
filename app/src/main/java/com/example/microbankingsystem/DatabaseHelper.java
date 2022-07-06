package com.example.microbankingsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TRANSACTIONS = "TRANSACTIONS";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_ACC_NO = "ACC_NO";
    public static final String COLUMN_AMOUNT = "AMOUNT";
    public static final String COLUMN_TYPE = "TYPE";
    public static final String COLUMN_TRANS_DATE = "TRANS_DATE";
    public static final String ACCOUNTS = "ACCOUNTS";
    public static final String COLUMN_ACCOUNT_NO = "ACCOUNT_NO";
    public static final String COLUMN_BALANCE = "BALANCE";
    public static final String COLUMN_JOINT = "JOINT";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "transactions.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTransactionTable = "CREATE TABLE " + TRANSACTIONS + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + COLUMN_ACC_NO + " VARCHAR(10) NOT NULL, " + COLUMN_AMOUNT + " DOUBLE NOT NULL, " + COLUMN_TYPE + " VARCHAR(10) NOT NULL, " + COLUMN_TRANS_DATE + " VARCHAR(10) NOT NULL)";

        String createAccountTable = "CREATE TABLE " + ACCOUNTS + " ( " + COLUMN_ACCOUNT_NO + " VARCHAR(10) PRIMARY KEY NOT NULL, " + COLUMN_BALANCE + " DOUBLE NOT NULL, " + COLUMN_JOINT + " INTEGER NOT NULL)";
        sqLiteDatabase.execSQL(createAccountTable);
        sqLiteDatabase.execSQL(createTransactionTable);
        System.out.println("Works");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean record_transaction(TransactionModel transactionModel){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACC_NO, transactionModel.getAccNo());
        cv.put(COLUMN_AMOUNT, transactionModel.getAmount());
        cv.put(COLUMN_TYPE, transactionModel.getType());
        cv.put(COLUMN_TRANS_DATE, transactionModel.getDate().toString());

        long insert = db.insert(TRANSACTIONS, null, cv);
        if(insert==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public List<TransactionModel> getAllTransactions(){

        List<TransactionModel> allTransactions = new ArrayList<>();

        String getTransactionQuery = "SELECT * FROM " + TRANSACTIONS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(getTransactionQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int ID = cursor.getInt(0);
                String accNo = cursor.getString(1);
                double amount = cursor.getDouble(2);
                String type = cursor.getString(3);
                String date = cursor.getString(4);
                TransactionModel tmp_trans = new TransactionModel(ID, accNo, amount, type, date);
                allTransactions.add(tmp_trans);
            } while (cursor.moveToNext());
        } else {
        }

        cursor.close();
        //db.close();
        return allTransactions;
    }

    public boolean addAccount(AccountModel account){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACCOUNT_NO, account.getAccountNo());
        cv.put(COLUMN_BALANCE, account.getBalance());
        cv.put(COLUMN_JOINT, account.isJoint());

        long insert = sqLiteDatabase.insert(ACCOUNTS, null, cv);
        if(insert==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public List<String> getAllAccounts(){
        List<String> accounts = new ArrayList<String>();

        Cursor cursor = readAllFromTable(ACCOUNTS);

        if (cursor.moveToFirst()){
            do{
                String accNo = cursor.getString(0);
                accounts.add(accNo);
            }while(cursor.moveToNext());
        }else{
            return null;
        }

        return accounts;
    }

    public AccountModel getAccount(String acc_no){

        AccountModel accountModel = null;
        Cursor cursor = readAllFromTable(ACCOUNTS);

        if (cursor.moveToFirst()){
            do{
                if(cursor.getString(0).equals(acc_no)) {
                    Double balance = cursor.getDouble(1);
                    int Joint = cursor.getInt(2);
                    accountModel = new AccountModel(acc_no, balance, Joint);
                    break;
                }
            }while(cursor.moveToNext());
        }else{
            return null;
        }

        return accountModel;
    }

    private Cursor readAllFromTable(String dbName){
        String getTransactionQuery = "SELECT * FROM " + dbName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getTransactionQuery, null);
        //db.close();
        return cursor;
        // close the returning cursor when you use this function
    }

}
