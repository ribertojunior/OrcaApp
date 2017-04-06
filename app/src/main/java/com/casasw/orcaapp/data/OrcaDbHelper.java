package com.casasw.orcaapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.casasw.orcaapp.BuildConfig;

/**
 * Created by Junior on 06/04/2017.
 * Manages the local db
 */


class OrcaDbHelper  extends SQLiteOpenHelper{
    private static final String TAG = OrcaDbHelper.class.getSimpleName();

    private static int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "orca.db";

    OrcaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PERSON_TABLE = "CREATE TABLE " + OrcaContract.PersonEntry.TABLE_NAME + " (" +
                OrcaContract.PersonEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OrcaContract.PersonEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                OrcaContract.PersonEntry.COLUMN_PHONE + " TEXT NOT NULL, " +
                OrcaContract.PersonEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                OrcaContract.PersonEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                OrcaContract.PersonEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                OrcaContract.PersonEntry.COLUMN_PRO + " TEXT NOT NULL);";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: person table: \n"+SQL_CREATE_PERSON_TABLE);
        }
        
        sqLiteDatabase.execSQL(SQL_CREATE_PERSON_TABLE);

        final String SQL_CREATE_BUDGET_TABLE = "CREATE TABLE " + OrcaContract.BudgetEntry.TABLE_NAME + " (" +
                OrcaContract.BudgetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OrcaContract.BudgetEntry.COLUMN_CLIENT_ID + " INTEGER NOT NULL, " +
                OrcaContract.BudgetEntry.COLUMN_PHONE + " TEXT NOT NULL, " +                
                OrcaContract.BudgetEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                OrcaContract.BudgetEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                "FOREIGN KEY ("+ OrcaContract.BudgetEntry.COLUMN_CLIENT_ID +") REFERENCES " +
                OrcaContract.PersonEntry.TABLE_NAME + " (" + OrcaContract.PersonEntry._ID + ") );";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: budget table: \n"+SQL_CREATE_BUDGET_TABLE);
        }

        sqLiteDatabase.execSQL(SQL_CREATE_BUDGET_TABLE);

        final String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + OrcaContract.ProductEntry.TABLE_NAME + " (" +
                OrcaContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OrcaContract.ProductEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                OrcaContract.ProductEntry.COLUMN_STOCK + " INTEGER NOT NULL, " +
                OrcaContract.ProductEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                OrcaContract.ProductEntry.COLUMN_DESC + " TEXT NOT NULL, " +
                OrcaContract.ProductEntry.COLUMN_CLASS_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY ("+ OrcaContract.ProductEntry.COLUMN_CLASS_ID +") REFERENCES " +
                OrcaContract.ClassEntry.TABLE_NAME + " (" + OrcaContract.ClassEntry._ID + ") );";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: product table: \n"+SQL_CREATE_PRODUCT_TABLE);
        }

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCT_TABLE);

        final String SQL_CREATE_CLASS_TABLE = "CREATE TABLE " + OrcaContract.ClassEntry.TABLE_NAME + " (" +
                OrcaContract.ClassEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OrcaContract.ClassEntry.COLUMN_NAME + " TEXT NOT NULL);";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: class table: \n"+SQL_CREATE_CLASS_TABLE);
        }

        sqLiteDatabase.execSQL(SQL_CREATE_CLASS_TABLE);

        final String SQL_CREATE_INPUT_TABLE = "CREATE TABLE " + OrcaContract.InputEntry.TABLE_NAME + " (" +
                OrcaContract.InputEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OrcaContract.InputEntry.COLUMN_BUDGET_ID + " INTEGER NOT NULL, " +
                OrcaContract.InputEntry.COLUMN_PRODUCT_ID + " INTEGER NOT NULL, " +
                OrcaContract.InputEntry.COLUMN_QUANTITY + " REAL NOT NULL, " +
                "FOREIGN KEY ("+ OrcaContract.InputEntry.COLUMN_BUDGET_ID +") REFERENCES " +
                OrcaContract.BudgetEntry.TABLE_NAME + " (" + OrcaContract.BudgetEntry._ID + "), " +
                "FOREIGN KEY ("+ OrcaContract.InputEntry.COLUMN_PRODUCT_ID +") REFERENCES " +
                OrcaContract.ProductEntry.TABLE_NAME + " (" + OrcaContract.ProductEntry._ID + ") );";
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate: input table: \n"+SQL_CREATE_INPUT_TABLE);
        }

        sqLiteDatabase.execSQL(SQL_CREATE_INPUT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrcaContract.PersonEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrcaContract.BudgetEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrcaContract.ProductEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrcaContract.ClassEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrcaContract.InputEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
