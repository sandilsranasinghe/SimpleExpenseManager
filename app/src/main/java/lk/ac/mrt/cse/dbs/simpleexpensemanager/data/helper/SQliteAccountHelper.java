package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQliteAccountHelper extends SQLiteOpenHelper {
    private static final String SQL_DB_NAME = "ExpenseManager";
    private static final int SQL_DB_VERSION = 1;
    private static final String SQL_TABLE_NAME = "account";

    public static String getSQLTableName() {
        return SQL_TABLE_NAME;
    }

    public SQliteAccountHelper(Context context) {
        super(context, SQL_DB_NAME, null, SQL_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSQL = "CREATE TABLE " + SQL_TABLE_NAME + " ("
                + "account_no VARCHAR(255) PRIMARY KEY NOT NULL,"
                + "bank_name VARCHAR(255) NOT NULL,"
                + "holder_name TEXT,"
                + "balance DOUBLE DEFAULT 0 )";
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQL_TABLE_NAME);
    }
}
