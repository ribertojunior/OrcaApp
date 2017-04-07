package com.casasw.orcaapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.runner.RunWith;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Junior on 30/03/2017.
 * Utilities for tests.
 */
@RunWith(AndroidJUnit4.class)
public class TestUtilities {

    static final String[] PRODUCT_CLASS_COLUMNS ={
            OrcaContract.ProductEntry.TABLE_NAME +
                    "." + OrcaContract.ProductEntry._ID,
            OrcaContract.ProductEntry.TABLE_NAME +
                    "." + OrcaContract.ProductEntry.COLUMN_NAME,
            OrcaContract.ProductEntry.COLUMN_STOCK,
            OrcaContract.ProductEntry.COLUMN_PRICE,
            OrcaContract.ProductEntry.COLUMN_DESC,
            OrcaContract.ClassEntry.TABLE_NAME +
                    "." + OrcaContract.ClassEntry.COLUMN_NAME

    };

    static final  String[] BUDGET_PERSON_COLUMNS = {
            OrcaContract.BudgetEntry.TABLE_NAME +
                    "." + OrcaContract.BudgetEntry._ID,
            OrcaContract.PersonEntry.COLUMN_NAME,
            OrcaContract.BudgetEntry.TABLE_NAME +
                    "." + OrcaContract.BudgetEntry.COLUMN_ADDRESS,
            OrcaContract.BudgetEntry.TABLE_NAME +
                    "." + OrcaContract.BudgetEntry.COLUMN_PHONE

    };

    static final String[] BUDGET_INPUT_ROOM = {
            OrcaContract.BudgetEntry.TABLE_NAME +
                    "." + OrcaContract.BudgetEntry._ID,
            OrcaContract.InputEntry.COLUMN_ROOM_ID,
            OrcaContract.RoomEntry.COLUMN_NAME,
            OrcaContract.RoomEntry.COLUMN_DESC

    };

    static final String[] BUDGET_INPUT_PRODUCT = {
            OrcaContract.BudgetEntry.TABLE_NAME +
                    "." + OrcaContract.BudgetEntry._ID,
            OrcaContract.InputEntry.COLUMN_QUANTITY,
            OrcaContract.ProductEntry.TABLE_NAME +
                    "." + OrcaContract.ProductEntry.COLUMN_NAME,
            OrcaContract.ProductEntry.COLUMN_PRICE,
            OrcaContract.ProductEntry.COLUMN_DESC

    };

    static void logCursor(Cursor c, final String LOG_TAG) {
        String col = "";
        for (int i =0;i<c.getColumnCount();i++) {
            col =  col + "["+c.getColumnName(i)+"] - ";
        }
        String values = "";
        do {
            for (int i =0;i<c.getColumnCount();i++) {
                values =  values + c.getString(i) +" - ";
            }
            values = values + "\n";

        }while (c.moveToNext());
        Log.v(LOG_TAG, col);
        Log.v(LOG_TAG, values);
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            String value = valueCursor.getString(idx);
            if (expectedValue.length() > value.length()) {
                expectedValue = expectedValue.substring(0, value.length()-1);
                value = value.substring(0, value.length()-1);
            }

            assertTrue("Column '"+columnName+"' Value '" + value +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue.equals(value));


        }
    }

    static ContentValues createPersonValues() {
        ContentValues values = new ContentValues();
        int random = (int) Math.floor(Math.random()*100);
        values.put(OrcaContract.PersonEntry.COLUMN_NAME, "Josivaldo "+random);
        values.put(OrcaContract.PersonEntry.COLUMN_PHONE, "+55 12 9999-9999");
        values.put(OrcaContract.PersonEntry.COLUMN_EMAIL, "josivaldo@josivaldo.com");
        values.put(OrcaContract.PersonEntry.COLUMN_ADDRESS, "42 bbb street, Neighbourhood");
        values.put(OrcaContract.PersonEntry.COLUMN_CITY, "Josivaldo City");
        values.put(OrcaContract.PersonEntry.COLUMN_PRO, "electrician");

        return values;
    }

    static ContentValues createBudgetValues(long _id) {
        ContentValues values = new ContentValues();        
        values.put(OrcaContract.BudgetEntry.COLUMN_CLIENT_ID, ""+_id);
        values.put(OrcaContract.BudgetEntry.COLUMN_PHONE,  "+55 12 9999-9999");
        values.put(OrcaContract.PersonEntry.COLUMN_ADDRESS, "42 bbb street, Neighbourhood");
        values.put(OrcaContract.BudgetEntry.COLUMN_CITY, "Josivaldo City");        
        return values;
    }

    static ContentValues createProductValues(long _id) {
        ContentValues values = new ContentValues();
        int random = (int) Math.floor(Math.random()*100);
        values.put(OrcaContract.ProductEntry.COLUMN_NAME, "Josivaldo Product "+random);
        values.put(OrcaContract.ProductEntry.COLUMN_STOCK, "7");
        values.put(OrcaContract.ProductEntry.COLUMN_PRICE, "23.18"); //-23.189813, -45.870086
        values.put(OrcaContract.ProductEntry.COLUMN_DESC, "Cool desc abour Josivaldo Product "+random);
        values.put(OrcaContract.ProductEntry.COLUMN_CLASS_ID, ""+_id);        

        return values;
    }

    static ContentValues createClassValues() {
        ContentValues values = new ContentValues();
        int random = (int) Math.floor(Math.random()*100);
        values.put(OrcaContract.ClassEntry.COLUMN_NAME, "Josivaldo Class "+random);
        
        return values;
    }

    static ContentValues createInputValues(long budget_id, long product_id, long room_id) {
        ContentValues values = new ContentValues();
        values.put(OrcaContract.InputEntry.COLUMN_BUDGET_ID, ""+budget_id);
        values.put(OrcaContract.InputEntry.COLUMN_PRODUCT_ID, ""+product_id);
        values.put(OrcaContract.InputEntry.COLUMN_ROOM_ID, ""+room_id);
        values.put(OrcaContract.InputEntry.COLUMN_QUANTITY, "5");

        return values;
    }

    static ContentValues createRoomValues() {
        ContentValues values = new ContentValues();
        int random = (int) Math.floor(Math.random()*100);
        values.put(OrcaContract.RoomEntry.COLUMN_NAME, "Josivaldo Room");
        values.put(OrcaContract.RoomEntry.COLUMN_DESC, "Josivaldo Room "+random);

        return values;
    }
}
