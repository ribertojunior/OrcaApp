package com.casasw.orcaapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
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

    private static final String TAG = DbTest.class.getSimpleName();

    private void deleteDatabase() {
        InstrumentationRegistry.getTargetContext().deleteDatabase(OrcaDbHelper.DATABASE_NAME);
    }

    @Before
    public void setUp() throws Exception {
        deleteDatabase();
    }

    @Test
    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<>();
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

        final HashSet<String> columnHashSet = new HashSet<>();
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
        assertTrue("Error: Insertion in person table has fail.", in != -1 );
        String selection = OrcaContract.PersonEntry.TABLE_NAME + "." + OrcaContract.PersonEntry._ID + " = ?";
        Cursor cursor = db.query(OrcaContract.PersonEntry.TABLE_NAME, null,selection, new String[]{in+""}, null, null, null);
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
        long inPerson = db.insert(OrcaContract.PersonEntry.TABLE_NAME, null, values);
        Cursor cursor = db.query(OrcaContract.PersonEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();

        values = TestUtilities.createBudgetValues(inPerson);
        long in = db.insert(OrcaContract.BudgetEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in budget table has fail.", in != -1 );
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
        assertTrue("Error: Budget table update has fail.", updates==1);

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
        assertTrue("Error: Insertion in class table has fail.", inClass != -1 );

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
        assertTrue("Error: Class table update has fail.", updates==1);

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
        
        long inClass = db.insert(OrcaContract.ClassEntry.TABLE_NAME, null, values);
        Cursor cursor = db.query(OrcaContract.ClassEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();

        values = TestUtilities.createProductValues(inClass);
        
        long inProduct = db.insert(OrcaContract.ProductEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in product table has fail.", inProduct != -1 );

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
        assertTrue("Error: Product table update has fail.", updates==1);

        //delete
        int del = db.delete(OrcaContract.ProductEntry.TABLE_NAME, sSelection, new String[]{""+inProduct});
        assertTrue("Error: Product table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testRoomTable(){
        SQLiteDatabase db = new OrcaDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createRoomValues();
        long in = db.insert(OrcaContract.RoomEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in room table has fail.", in != -1 );

        Cursor cursor = db.query(OrcaContract.RoomEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: Room table select has fail.",
                cursor.moveToFirst());
        TestUtilities.validateCurrentRecord(
                "Error: The returning cursor is not equals to ContentValues inserted.", cursor, values);

        //update
        values.put(OrcaContract.RoomEntry.COLUMN_NAME, "New Name");
        String selection =
                OrcaContract.RoomEntry.TABLE_NAME +
                        "." + OrcaContract.RoomEntry._ID + " = ?";
        int updates = db.update(OrcaContract.RoomEntry.TABLE_NAME, values, selection, new String[]{in+""});
        assertTrue("Error: Room table update has fail.", updates==1);

        //delete
        int del = db.delete(OrcaContract.RoomEntry.TABLE_NAME, selection, new String[]{in+""});
        assertTrue("Error: Room table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testInputTable(){
        SQLiteDatabase db = new OrcaDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPersonValues();
        values.put(OrcaContract.PersonEntry.COLUMN_PRO, "client");
        long inPerson = db.insert(OrcaContract.PersonEntry.TABLE_NAME, null, values);

        values = TestUtilities.createBudgetValues(inPerson);
        long inBudget = db.insert(OrcaContract.BudgetEntry.TABLE_NAME, null, values);

        values = TestUtilities.createClassValues();

        long inClass = db.insert(OrcaContract.ClassEntry.TABLE_NAME, null, values);

        values = TestUtilities.createProductValues(inClass);

        long inProduct = db.insert(OrcaContract.ProductEntry.TABLE_NAME, null, values);

        values = TestUtilities.createRoomValues();

        long inRoom = db.insert(OrcaContract.RoomEntry.TABLE_NAME, null, values);


        values = TestUtilities.createInputValues(inBudget, inProduct, inRoom);
        long inInput = db.insert(OrcaContract.InputEntry.TABLE_NAME, null, values);
        assertTrue("Error: Insertion in input table has fail.", inInput != -1 );

        Cursor cursor = db.query(OrcaContract.InputEntry.TABLE_NAME, null, null, null, null, null, null);
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
        assertTrue("Error: Input table update has fail.", updates==1);

        //delete
        int del = db.delete(OrcaContract.InputEntry.TABLE_NAME, sSelection, new String[]{""+inInput});
        assertTrue("Error: Input table delete row has fail.", del==1);

        cursor.close();
        db.close();
    }

    @Test
    public void testInnerJoin() {
        SQLiteDatabase db = new OrcaDbHelper(InstrumentationRegistry.getTargetContext()).getWritableDatabase();

        ContentValues values = TestUtilities.createPersonValues();
        long inPerson = db.insert(OrcaContract.PersonEntry.TABLE_NAME, null, values);

        values = TestUtilities.createBudgetValues(inPerson);
        long inBudget = db.insert(OrcaContract.BudgetEntry.TABLE_NAME, null, values);

        values = TestUtilities.createClassValues();
        long inClass = db.insert(OrcaContract.ClassEntry.TABLE_NAME, null, values);

        values = TestUtilities.createProductValues(inClass);
        long inProduct = db.insert(OrcaContract.ProductEntry.TABLE_NAME, null, values);

        values = TestUtilities.createRoomValues();
        long inRoom = db.insert(OrcaContract.RoomEntry.TABLE_NAME, null, values);

        values = TestUtilities.createInputValues(inBudget, inProduct, inRoom);
        db.insert(OrcaContract.InputEntry.TABLE_NAME, null, values);

        SQLiteQueryBuilder sProductQueryBuilder = new SQLiteQueryBuilder();
        sProductQueryBuilder.setTables(
                OrcaContract.ProductEntry.TABLE_NAME + " INNER JOIN " +
                        OrcaContract.ClassEntry.TABLE_NAME +
                        " ON " + OrcaContract.ProductEntry.TABLE_NAME +
                        "." + OrcaContract.ProductEntry.COLUMN_CLASS_ID +
                        " = " + OrcaContract.ClassEntry.TABLE_NAME +
                        "." + OrcaContract.ClassEntry._ID
        );
        String selection = OrcaContract.ProductEntry.TABLE_NAME +
                "." + OrcaContract.ProductEntry.COLUMN_CLASS_ID + " = ? ";
        String[] selectionArgs = new String[]{""+inClass};
        Cursor cursor = sProductQueryBuilder.query(
                        db, TestUtilities.PRODUCT_CLASS_COLUMNS, selection, selectionArgs,
                        null, null, null );
        assertTrue("Error: Product-Class inner join is returning no data.", cursor.moveToFirst());
        TestUtilities.logCursor(cursor, TAG);

        SQLiteQueryBuilder sBudgetPersonQueryBuilder = new SQLiteQueryBuilder();
        sBudgetPersonQueryBuilder.setTables(
                OrcaContract.BudgetEntry.TABLE_NAME + " INNER JOIN " +
                        OrcaContract.PersonEntry.TABLE_NAME +
                        " ON " + OrcaContract.BudgetEntry.TABLE_NAME +
                        "." + OrcaContract.BudgetEntry.COLUMN_CLIENT_ID +
                        " = " + OrcaContract.PersonEntry.TABLE_NAME +
                        "." + OrcaContract.PersonEntry._ID
        );
        selection = OrcaContract.BudgetEntry.TABLE_NAME +
                "." + OrcaContract.BudgetEntry._ID + " = ? ";
        selectionArgs = new String[]{""+inBudget};
        cursor = sBudgetPersonQueryBuilder.query(
                        db, TestUtilities.BUDGET_PERSON_COLUMNS, selection, selectionArgs,
                        null, null, null);
        assertTrue("Error: Budget-Person inner join is returning no data.", cursor.moveToFirst());
        TestUtilities.logCursor(cursor, TAG);

        SQLiteQueryBuilder sBudgetInputRoom = new SQLiteQueryBuilder();
        sBudgetInputRoom.setTables(
                OrcaContract.BudgetEntry.TABLE_NAME + " INNER JOIN " +
                        OrcaContract.InputEntry.TABLE_NAME +
                        " ON " + OrcaContract.BudgetEntry.TABLE_NAME +
                        "." + OrcaContract.BudgetEntry._ID +
                        " = " + OrcaContract.InputEntry.TABLE_NAME +
                        "." + OrcaContract.InputEntry.COLUMN_BUDGET_ID +
                        " INNER JOIN " +
                        OrcaContract.RoomEntry.TABLE_NAME +
                        " ON " + OrcaContract.RoomEntry.TABLE_NAME +
                        "." + OrcaContract.RoomEntry._ID +
                        " = " + OrcaContract.InputEntry.COLUMN_ROOM_ID
        );
        cursor = sBudgetInputRoom.query(
                db, TestUtilities.BUDGET_INPUT_ROOM, selection, selectionArgs,
                null, null, null
        );
        assertTrue("Error: Budget-Input-Room inner join is returning no data.", cursor.moveToFirst());
        TestUtilities.logCursor(cursor, TAG);

        SQLiteQueryBuilder sBudgetInputProduct = new SQLiteQueryBuilder();
        sBudgetInputProduct.setTables(
                OrcaContract.BudgetEntry.TABLE_NAME + " INNER JOIN " +
                        OrcaContract.InputEntry.TABLE_NAME +
                        " ON " + OrcaContract.BudgetEntry.TABLE_NAME +
                        "." + OrcaContract.BudgetEntry._ID +
                        " = " + OrcaContract.InputEntry.TABLE_NAME +
                        "." + OrcaContract.InputEntry.COLUMN_BUDGET_ID +
                        " INNER JOIN " +
                        OrcaContract.ProductEntry.TABLE_NAME +
                        " ON " + OrcaContract.ProductEntry.TABLE_NAME +
                        "." + OrcaContract.ProductEntry._ID +
                        " = " + OrcaContract.InputEntry.COLUMN_PRODUCT_ID
        );
        cursor = sBudgetInputProduct.query(
                db, TestUtilities.BUDGET_INPUT_PRODUCT, selection, selectionArgs,
                null, null, null
        );
        assertTrue("Error: Budget-Input-Product inner join is returning no data.", cursor.moveToFirst());
        TestUtilities.logCursor(cursor, TAG);

        cursor.close();
        db.close();
    }

}
