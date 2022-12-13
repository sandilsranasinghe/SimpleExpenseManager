package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.helper.SQliteAccountHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class SQliteAccountDAO implements AccountDAO {
    SQliteAccountHelper dbHelper;

    public SQliteAccountDAO(Context context) {
        this.dbHelper = new SQliteAccountHelper(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<String>();

        String sqlQuery = "SELECT account_no FROM " + dbHelper.getSQLTableName();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        while (cursor.moveToNext()) {
            String account_no = cursor.getString(0);
            accountNumbersList.add(account_no);
        }
        cursor.close();

        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<Account>();

        String sqlQuery =
                "SELECT account_no, bank_name, holder_name, balance FROM " + dbHelper.getSQLTableName();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        while (cursor.moveToNext()) {
            Account account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3)
            );
            accountList.add(account);
        }
        cursor.close();

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String sqlQuery =
                "SELECT account_no, bank_name, holder_name, balance FROM " + dbHelper.getSQLTableName()
                + " WHERE account_no = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, new String[]{accountNo});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Account account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3)
            );
            return account;
        } else {
            throw new InvalidAccountException("Account " + accountNo + " not found");
        }
    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_no", account.getAccountNo());
        contentValues.put("bank_name", account.getBankName());
        contentValues.put("holder_name", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(dbHelper.getSQLTableName(), null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sqlWhereString = "accountNo LIKE ?";
        String[] sqlWhereArgs = {accountNo};

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete(dbHelper.getSQLTableName(), sqlWhereString, sqlWhereArgs);
        if (!(deletedRows > 0)) {
            throw new InvalidAccountException("Could not delete account " + accountNo);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws
            InvalidAccountException
    {
        Account account = getAccount(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("balance", account.getBalance());
        String sqlWhereString = "account_no LIKE ?";
        String[] sqlWhereArgs = {accountNo};

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(dbHelper.getSQLTableName(), contentValues, sqlWhereString, sqlWhereArgs);
    }


}