package technology.thinkbench.budgetbuddy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import technology.thinkbench.budgetbuddy.data.DataContract.ExpenditureEntry;

/**
 * Created by willis on 3/16/18.
 */

public class DataDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DataDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "budget.db";
    private static final int DATABASE_VERSION = 1;

    public DataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //called the first time the db is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_EXPENDITURE_TABLE =  "CREATE TABLE " + ExpenditureEntry.TABLE_NAME + " ("
                + ExpenditureEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExpenditureEntry.COLUMN_EXPENDITURE_LABEL + " TEXT NOT NULL, "
                + ExpenditureEntry.COLUMN_EXPENDITURE_AMOUNT + " DECIMAL, "
                + ExpenditureEntry.COLUMN_EXPENDITURE_TAG + " TEXT NOT NULL, "
                + ExpenditureEntry.COLUMN_EXPENDITURE_DATE + " TIMESTAMP);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_EXPENDITURE_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

}
