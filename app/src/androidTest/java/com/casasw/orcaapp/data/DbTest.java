package com.casasw.orcaapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Junior on 06/04/2017.
 * Database tests
 */
@RunWith(AndroidJUnit4.class)
public class DbTest {

    public static final String TAG = DbTest.class.getSimpleName();

    void deleteDatabase() {
        InstrumentationRegistry.getContext().deleteDatabase(OrcaDbHelper.DATABASE_NAME);
    }

    @Before
    public void setUp() throws Exception {
        deleteDatabase();
    }

    @Test
    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(OrcaContract.PersonEntry.TABLE_NAME);
        tableNameHashSet.add(OrcaContract.BudgetEntry.TABLE_NAME);
        tableNameHashSet.add(OrcaContract.ProductEntry.TABLE_NAME);
        tableNameHashSet.add(OrcaContract.ClassEntry.TABLE_NAME);
        tableNameHashSet.add(OrcaContract.InputEntry.TABLE_NAME);        

        deleteDatabase();

        SQLiteDatabase db =
                new OrcaDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        }while (c.moveToNext());

        assertTrue("Error: Your database was created without tables",
                tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + OrcaContract.PersonEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> columnHashSet = new HashSet<String>();
        columnHashSet.add(OrcaContract.PersonEntry._ID);
        columnHashSet.add(OrcaContract.PersonEntry.COLUMN_NAME);
        columnHashSet.add(OrcaContract.PersonEntry.COLUMN_PHONE);
        columnHashSet.add(OrcaContract.PersonEntry.COLUMN_EMAIL);
        columnHashSet.add(OrcaContract.PersonEntry.COLUMN_ADDRESS);        
        columnHashSet.add(OrcaContract.PersonEntry.COLUMN_CITY);
        columnHashSet.add(OrcaContract.PersonEntry.COLUMN_PRO);
        

        int columnNameIndex = c.getColumnIndex("name");
        String columnName;
        do {
            columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        }while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required movie entry columns.",
                columnHashSet.isEmpty());

        c.close();
        db.close();

    }

    @Test
    public void testPersonTable(){
        SQLiteDatabase db = new OrcaDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();
        //insertion
        ContentValues values = TestUtilities.createPersonValues();
        long in = db.insert(OrcaContract.PersonEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in player table has fail.", in != -1 );
        Cursor cursor = db.query(OrcaContract.PersonEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Person table select has fail.",
                cursor.moveToFirst());
        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(OrcaContract.PersonEntry.COLUMN_PRO, "Mechanician");
        String sPersonSelection =
                OrcaContract.PersonEntry.TABLE_NAME +
                        "." + OrcaContract.PersonEntry._ID + " = ?";
        int updates = db.update(OrcaContract.PersonEntry.TABLE_NAME, values, sPersonSelection, new String[]{""+in});
        assertTrue("Error: Person table update has fail.", updates>0);

        //delete
        int del = db.delete(OrcaContract.PersonEntry.TABLE_NAME, sPersonSelection, new String[]{""+in});
        assertTrue("Error: Person table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testBudgetTable(){
        SQLiteDatabase db = new OrcaDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPersonValues();
        values.put(OrcaContract.PersonEntry.COLUMN_PRO, "client");
        db.insert(OrcaContract.PersonEntry.TABLE_NAME, null, values);
        Cursor cursor = db.query(OrcaContract.PersonEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();

        values = TestUtilities.createBudgetValues(cursor.getLong(cursor.getColumnIndex(OrcaContract.PersonEntry._ID)));
        long in = db.insert(OrcaContract.BudgetEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in team table has fail.", in != -1 );
        cursor = db.query(OrcaContract.BudgetEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Budget table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(OrcaContract.BudgetEntry.COLUMN_CITY, "8tao de 13");
        String sSelection =
                OrcaContract.BudgetEntry.TABLE_NAME +
                        "." + OrcaContract.BudgetEntry._ID + " = ?";
        int updates = db.update(OrcaContract.BudgetEntry.TABLE_NAME, values, sSelection, new String[]{""+in});
        assertTrue("Error: Budget table update has fail.", updates>0);

        //delete
        int del = db.delete(OrcaContract.BudgetEntry.TABLE_NAME, sSelection, new String[]{""+in});
        assertTrue("Error: Budget table delete row has fail.", del==1);


        cursor.close();
        db.close();
    }

    @Test
    public void testClassTable(){
        SQLiteDatabase db = new OrcaDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();
        
        ContentValues values = TestUtilities.createClassValues();
        long inClass = db.insert(OrcaContract.ClassEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in venue table has fail.", inClass != -1 );

        Cursor cursor = db.query(OrcaContract.ClassEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Class table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(OrcaContract.ClassEntry.COLUMN_NAME, "New name");
        String sSelection =
                OrcaContract.ClassEntry.TABLE_NAME +
                        "." + OrcaContract.ClassEntry._ID + " = ?";
        int updates = db.update(OrcaContract.ClassEntry.TABLE_NAME, values, sSelection, new String[]{""+inClass});
        assertTrue("Error: Class table update has fail.", updates>0);

        //delete
        int del = db.delete(OrcaContract.ClassEntry.TABLE_NAME, sSelection, new String[]{""+inClass});
        assertTrue("Error: Class table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testProductTable(){
        SQLiteDatabase db = new OrcaDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createClassValues();
        
        db.insert(OrcaContract.ClassEntry.TABLE_NAME, null, values);
        Cursor cursor = db.query(OrcaContract.ClassEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();

        values = TestUtilities.createProductValues(cursor.getLong(cursor.getColumnIndex(OrcaContract.ClassEntry._ID)));
        
        long inProduct = db.insert(OrcaContract.ProductEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in venue table has fail.", inProduct != -1 );

        cursor = db.query(OrcaContract.ProductEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Product table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(OrcaContract.ProductEntry.COLUMN_PRICE, "69.69");
        String sSelection =
                OrcaContract.ProductEntry.TABLE_NAME +
                        "." + OrcaContract.ProductEntry._ID + " = ?";
        int updates = db.update(OrcaContract.ProductEntry.TABLE_NAME, values, sSelection, new String[]{""+inProduct});
        assertTrue("Error: Product table update has fail.", updates>0);

        //delete
        int del = db.delete(OrcaContract.ProductEntry.TABLE_NAME, sSelection, new String[]{""+inProduct});
        assertTrue("Error: Product table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testInputTable(){
        SQLiteDatabase db = new OrcaDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPersonValues();
        values.put(OrcaContract.PersonEntry.COLUMN_PRO, "client");
        db.insert(OrcaContract.PersonEntry.TABLE_NAME, null, values);
        Cursor cursor = db.query(OrcaContract.PersonEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();

        values = TestUtilities.createBudgetValues(cursor.getLong(cursor.getColumnIndex(OrcaContract.PersonEntry._ID)));
        long inBudget = db.insert(OrcaContract.BudgetEntry.TABLE_NAME, null, values);
        cursor = db.query(OrcaContract.BudgetEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        inBudget = cursor.getLong(cursor.getColumnIndex(OrcaContract.BudgetEntry._ID));

        values = TestUtilities.createClassValues();

        db.insert(OrcaContract.ClassEntry.TABLE_NAME, null, values);
        cursor = db.query(OrcaContract.ClassEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();

        values = TestUtilities.createProductValues(cursor.getLong(cursor.getColumnIndex(OrcaContract.ClassEntry._ID)));

        long inProduct = db.insert(OrcaContract.ProductEntry.TABLE_NAME, null, values);
        cursor = db.query(OrcaContract.ProductEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        inProduct = cursor.getColumnIndex(OrcaContract.ProductEntry._ID);

        values = TestUtilities.createInputValues(inBudget, inProduct);
        long inInput = db.insert(OrcaContract.InputEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in match table has fail.", inInput != -1 );

        cursor = db.query(OrcaContract.InputEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Input table select has fail.",
                cursor.moveToFirst());

        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(OrcaContract.InputEntry.COLUMN_QUANTITY, "69");
        String sSelection =
                OrcaContract.InputEntry.TABLE_NAME +
                        "." + OrcaContract.InputEntry._ID + " = ?";
        int updates = db.update(OrcaContract.InputEntry.TABLE_NAME, values, sSelection, new String[]{""+inInput});
        assertTrue("Error: Input table update has fail.", updates>0);

        //delete
        int del = db.delete(OrcaContract.InputEntry.TABLE_NAME, sSelection, new String[]{""+inInput});
        assertTrue("Error: Input table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

}
