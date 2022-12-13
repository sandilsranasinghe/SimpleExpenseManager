package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQliteTransactionHelper extends SQLiteOpenHelper {
    private static final String SQL_DB_NAME = "ExpenseManager";
    private static final int SQL_DB_VERSION = 1;
    private static final String SQL_TABLE_NAME = "acc_transactions";

    public String getSQLTableName() {
        return SQL_TABLE_NAME;
    }

    public SQliteTransactionHelper(Context context) {
        super(context, SQL_DB_NAME, null, SQL_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSQL = "CREATE TABLE " + SQL_TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "account_no VARCHAR(255) NOT NULL,"
                + "expense_type VARCHAR(255) NOT NULL,"
                + "amount DOUBLE,"
                + "date DATE,"
                + "FOREIGN KEY (account_no) REFERENCES "
                + SQliteAccountHelper.getSQLTableName() + " (account_no) )";
        System.out.println(createSQL);
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQL_TABLE_NAME);
    }
}
