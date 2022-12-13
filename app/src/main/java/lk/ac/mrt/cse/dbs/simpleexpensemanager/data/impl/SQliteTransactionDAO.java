package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.helper.SQliteAccountHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.helper.SQliteTransactionHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQliteTransactionDAO implements TransactionDAO {
    SQliteTransactionHelper dbHelper;

    public SQliteTransactionDAO(Context context) {
        this.dbHelper = new SQliteTransactionHelper(context);
    }

    @Override
    public void logTransaction(
            Date date, String accountNo, ExpenseType expenseType, double amount
    )
    {
        ContentValues contentValues = new ContentValues();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        contentValues.put("date", dateFormat.format(date));
        contentValues.put("account_no", accountNo);
        contentValues.put("expense_type", String.valueOf(expenseType));
        contentValues.put("amount", amount);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(dbHelper.getSQLTableName(), null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<Transaction>();

        String sqlQuery =
                "SELECT date, account_no, expense_type, amount FROM " + dbHelper.getSQLTableName();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        while (cursor.moveToNext()) {
            Date date;
            try {
                date = new SimpleDateFormat("dd.MM.yyyy").parse(cursor.getString(0));
            } catch (ParseException parseException) {
                continue;
            }
            Transaction transaction = new Transaction(
                    date,
                    cursor.getString(1),
                    ExpenseType.valueOf(cursor.getString(2)),
                    cursor.getDouble(3)
            );
            transactionList.add(transaction);
        }
        cursor.close();

        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = new ArrayList<Transaction>();

        String sqlQuery =
                "SELECT date, account_no, expense_type, amount FROM " + dbHelper.getSQLTableName();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        int count = 0;
        while (cursor.moveToNext() && count<limit) {
            Date date;
            try {
                date = new SimpleDateFormat("dd.MM.yyyy").parse(cursor.getString(0));
            } catch (ParseException parseException) {
                continue;
            }
            Transaction transaction = new Transaction(
                    date,
                    cursor.getString(1),
                    ExpenseType.valueOf(cursor.getString(2)),
                    cursor.getDouble(3)
            );
            transactionList.add(transaction);
            count += 1;
        }
        cursor.close();

        return transactionList;
    }
}
