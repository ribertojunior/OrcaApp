package com.casasw.orcaapp.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Junior on 06/04/2017.
 * Orca Db content provider
 */

public class OrcaProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //test methods and variables
    private boolean isTest =  false;
    public static UriMatcher getsUriMatcher() {
        return sUriMatcher;
    }

    public void testOnCreate(Context c){
        mOpenHelper = new OrcaDbHelper(c);
        isTest = true;
    }

    //edn of test methods

    private OrcaDbHelper mOpenHelper;

    private static final int PERSON = 100;
    private static final int PERSON_ID = 840;
    private static final int BUDGET = 635;
    private static final int BUDGET_ID = 963;
    private static final int PRODUCT = 329;
    private static final int PRODUCT_ID = 927;
    private static final int CLASS = 158;
    private static final int CLASS_ID = 619;
    private static final int INPUT = 550;
    private static final int INPUT_ID = 407;
    private static final int ROOM = 817;
    private static final int ROOM_ID = 435;
    private static final int BUDGET_PERSON = 583; //budget inner person inner input inner product inner class
    private static final int BUDGET_ROOM_INPUT = 950;
    private static final int BUDGET_INPUT_PRODUCT = 150;
    private static final int PRODUCT_CLASS = 654;

    static final String[] BUDGET_PERSON_COLUMNS = {
            OrcaContract.BudgetEntry.TABLE_NAME + "." + OrcaContract.BudgetEntry._ID,
            OrcaContract.BudgetEntry.COLUMN_CLIENT_ID,
            OrcaContract.BudgetEntry.TABLE_NAME + "." + OrcaContract.BudgetEntry.COLUMN_PHONE,
            OrcaContract.BudgetEntry.TABLE_NAME + "." + OrcaContract.BudgetEntry.COLUMN_ADDRESS,
            OrcaContract.BudgetEntry.TABLE_NAME + "." + OrcaContract.BudgetEntry.COLUMN_CITY,
            OrcaContract.PersonEntry.COLUMN_NAME
    };

    static final String[] BUDGET_ROOM_INPUT_COLUMNS = {
            OrcaContract.BudgetEntry.TABLE_NAME + "." + OrcaContract.BudgetEntry._ID,
            OrcaContract.InputEntry.COLUMN_QUANTITY,
            OrcaContract.RoomEntry.COLUMN_NAME,
            OrcaContract.RoomEntry.COLUMN_DESC
    };

    static final String[] BUDGET_INPUT_PRODUCT_COLUMNS = {
            OrcaContract.BudgetEntry.TABLE_NAME + "." + OrcaContract.BudgetEntry._ID,
            OrcaContract.InputEntry.COLUMN_QUANTITY,
            OrcaContract.ProductEntry.COLUMN_NAME,
            OrcaContract.ProductEntry.COLUMN_DESC,
            OrcaContract.ProductEntry.COLUMN_PRICE
    };

    static final String[] PRODUCT_CLASS_COLUMNS = {
            OrcaContract.ProductEntry.TABLE_NAME + "." + OrcaContract.ProductEntry._ID,
            OrcaContract.ProductEntry.TABLE_NAME + "." +OrcaContract.ProductEntry.COLUMN_NAME,
            OrcaContract.ProductEntry.COLUMN_DESC,
            OrcaContract.ProductEntry.COLUMN_PRICE,
            OrcaContract.ProductEntry.COLUMN_STOCK,
            OrcaContract.ClassEntry.TABLE_NAME + "." +OrcaContract.ClassEntry.COLUMN_NAME,
    };

    static final SQLiteQueryBuilder sProductQueryBuild;
    static {
        sProductQueryBuild = new SQLiteQueryBuilder();
        sProductQueryBuild.setTables(
                OrcaContract.ProductEntry.TABLE_NAME + " INNER JOIN " +
                        OrcaContract.ClassEntry.TABLE_NAME +
                        " ON " + OrcaContract.ProductEntry.TABLE_NAME +
                        "." + OrcaContract.ProductEntry.COLUMN_CLASS_ID +
                        " = " + OrcaContract.ClassEntry.TABLE_NAME +
                        "." + OrcaContract.ClassEntry._ID
        );
    }

    static final SQLiteQueryBuilder sBudgetPersonQueryBuild;
    static {
        sBudgetPersonQueryBuild = new SQLiteQueryBuilder();
        sBudgetPersonQueryBuild.setTables(
                OrcaContract.BudgetEntry.TABLE_NAME + " INNER JOIN " +
                        OrcaContract.PersonEntry.TABLE_NAME +
                        " ON " + OrcaContract.BudgetEntry.TABLE_NAME +
                        "." + OrcaContract.BudgetEntry.COLUMN_CLIENT_ID +
                        " = " + OrcaContract.PersonEntry.TABLE_NAME +
                        "." + OrcaContract.PersonEntry._ID
        );
    }

    static final SQLiteQueryBuilder sBudgetInputRoom;
    static {
        sBudgetInputRoom = new SQLiteQueryBuilder();
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
    }
    static final SQLiteQueryBuilder sBudgetInputProduct;
    static {
        sBudgetInputProduct = new SQLiteQueryBuilder();
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
    }

    private static final String sBudgetSelection = OrcaContract.BudgetEntry.TABLE_NAME +
            "." + OrcaContract.BudgetEntry._ID + " = ? ";

    private static final String sPersonSelection = OrcaContract.PersonEntry.TABLE_NAME +
            "." + OrcaContract.PersonEntry._ID + " = ? ";

    private static final String sProductSelection = OrcaContract.ProductEntry.TABLE_NAME +
            "." + OrcaContract.ProductEntry._ID + " = ? ";

    private static final String sClassSelection = OrcaContract.ClassEntry.TABLE_NAME +
            "." + OrcaContract.ClassEntry._ID + " = ? ";

    private static final String sInputSelection = OrcaContract.InputEntry.TABLE_NAME +
            "." + OrcaContract.InputEntry._ID + " = ? ";

    private static final String sRoomSelection = OrcaContract.RoomEntry.TABLE_NAME +
            "." + OrcaContract.RoomEntry._ID + " = ? ";
    
    private static final String sProductClassSelection = OrcaContract.ProductEntry.TABLE_NAME +
            "." + OrcaContract.ProductEntry.COLUMN_CLASS_ID + " = ? ";

    


    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_PERSON, PERSON);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_PERSON+"/#", PERSON_ID);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_BUDGET, BUDGET);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_BUDGET+"/#", BUDGET_ID);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_PRODUCT, PRODUCT);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_PRODUCT+"/#", PRODUCT_ID);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_CLASS, CLASS);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_CLASS+"/#", CLASS_ID);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_INPUT, INPUT);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_INPUT+"/#", INPUT_ID);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_ROOM, ROOM);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY, OrcaContract.PATH_ROOM+"/#", ROOM_ID);
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY,
                OrcaContract.PATH_BUDGET+"/"+OrcaContract.PATH_PERSON+"/#", BUDGET_PERSON); //budget_id
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY,
                OrcaContract.PATH_BUDGET+"/"+OrcaContract.PATH_ROOM+"/"+OrcaContract.PATH_INPUT+"/#", BUDGET_ROOM_INPUT); //budget_id
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY,
                OrcaContract.PATH_BUDGET+"/"+OrcaContract.PATH_INPUT+"/"+OrcaContract.PATH_PRODUCT+"/#", BUDGET_INPUT_PRODUCT); // budget_id
        uriMatcher.addURI(OrcaContract.CONTENT_AUTHORITY,
                OrcaContract.PATH_PRODUCT+"/"+OrcaContract.PATH_CLASS+"/#", PRODUCT_CLASS); //product_id


        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new OrcaDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        
        switch (sUriMatcher.match(uri)) {
            case PERSON:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.PersonEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PERSON_ID:
                selectionArgs = new String[]{OrcaContract.PersonEntry.getIdFromUri(uri)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.PersonEntry.TABLE_NAME,
                        projection,
                        sPersonSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BUDGET:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.BudgetEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BUDGET_ID:
                selectionArgs = new String[]{OrcaContract.BudgetEntry.getIdFromUri(uri)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.BudgetEntry.TABLE_NAME,
                        projection,
                        sBudgetSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODUCT:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODUCT_ID:
                selectionArgs = new String[]{OrcaContract.ProductEntry.getIdFromUri(uri)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.ProductEntry.TABLE_NAME,
                        projection,
                        sProductSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CLASS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.ClassEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CLASS_ID:
                selectionArgs = new String[]{OrcaContract.ClassEntry.getIdFromUri(uri)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.ClassEntry.TABLE_NAME,
                        projection,
                        sClassSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case INPUT:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.InputEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case INPUT_ID:
                selectionArgs = new String[]{OrcaContract.InputEntry.getIdFromUri(uri)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.InputEntry.TABLE_NAME,
                        projection,
                        sInputSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ROOM:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.RoomEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ROOM_ID:
                selectionArgs = new String[]{OrcaContract.RoomEntry.getIdFromUri(uri)};
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OrcaContract.RoomEntry.TABLE_NAME,
                        projection,
                        sRoomSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BUDGET_PERSON:
                selectionArgs = new String[]{OrcaContract.BudgetEntry.getIdFromUri(uri)};
                retCursor = sBudgetPersonQueryBuild.query(
                        mOpenHelper.getReadableDatabase(),
                        BUDGET_PERSON_COLUMNS,
                        sBudgetSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BUDGET_ROOM_INPUT:
                selectionArgs = new String[]{OrcaContract.BudgetEntry.getIdFromUri(uri)};
                retCursor = sBudgetInputRoom.query(
                        mOpenHelper.getReadableDatabase(),
                        BUDGET_ROOM_INPUT_COLUMNS,
                        sBudgetSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case BUDGET_INPUT_PRODUCT:
                selectionArgs = new String[]{OrcaContract.BudgetEntry.getIdFromUri(uri)};
                retCursor = sBudgetInputProduct.query(
                        mOpenHelper.getReadableDatabase(),
                        BUDGET_INPUT_PRODUCT_COLUMNS,
                        sBudgetSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PRODUCT_CLASS:
                selectionArgs = new String[]{OrcaContract.BudgetEntry.getIdFromUri(uri)};
                retCursor = sProductQueryBuild.query(
                        mOpenHelper.getReadableDatabase(),
                        PRODUCT_CLASS_COLUMNS,
                        sProductClassSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }
        
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {        
        switch (sUriMatcher.match(uri)) {
            case PERSON:
                return OrcaContract.PersonEntry.CONTENT_TYPE;
            case PERSON_ID:
                return OrcaContract.PersonEntry.CONTENT_ITEM_TYPE;
            case BUDGET:
                return OrcaContract.BudgetEntry.CONTENT_TYPE;
            case BUDGET_ID:
                return OrcaContract.BudgetEntry.CONTENT_ITEM_TYPE;
            case PRODUCT:
                return OrcaContract.ProductEntry.CONTENT_TYPE;
            case PRODUCT_ID:
                return OrcaContract.ProductEntry.CONTENT_ITEM_TYPE;
            case CLASS:
                return OrcaContract.ClassEntry.CONTENT_TYPE;
            case CLASS_ID:
                return OrcaContract.ClassEntry.CONTENT_ITEM_TYPE;
            case INPUT:
                return OrcaContract.InputEntry.CONTENT_TYPE;
            case INPUT_ID:
                return OrcaContract.InputEntry.CONTENT_ITEM_TYPE;
            case ROOM:
                return OrcaContract.RoomEntry.CONTENT_TYPE;
            case ROOM_ID:
                return OrcaContract.RoomEntry.CONTENT_ITEM_TYPE;
            case  BUDGET_PERSON:
                return OrcaContract.BudgetEntry.CONTENT_ITEM_TYPE;
            case BUDGET_ROOM_INPUT:
                return OrcaContract.InputEntry.CONTENT_ITEM_TYPE; 
            case BUDGET_INPUT_PRODUCT:
                return OrcaContract.InputEntry.CONTENT_ITEM_TYPE;
            case PRODUCT_CLASS:
                return OrcaContract.ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }        
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db  = mOpenHelper.getWritableDatabase();        
        Uri returnUri;
        long _id;
        switch (sUriMatcher.match(uri)) {
            case PERSON:                
                _id = db.insert( OrcaContract.PersonEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = OrcaContract.PersonEntry.buildPersonUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case BUDGET:
                _id = db.insert( OrcaContract.BudgetEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = OrcaContract.BudgetEntry.buildBudgetUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case PRODUCT:
                _id = db.insert( OrcaContract.ProductEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = OrcaContract.ProductEntry.buildProductUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case CLASS:
                _id = db.insert( OrcaContract.ClassEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = OrcaContract.ClassEntry.buildClassUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case INPUT:
                _id = db.insert( OrcaContract.InputEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = OrcaContract.InputEntry.buildInputUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case ROOM:
                _id = db.insert( OrcaContract.RoomEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = OrcaContract.RoomEntry.buildRoomUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);                
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }
        if (!isTest)
            getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String tableName;
        int del;
        if (s == null) {s="1";} //deleting every registry
        switch (sUriMatcher.match(uri)) {
            case PERSON:
                tableName = OrcaContract.PersonEntry.TABLE_NAME;
                break;
            case BUDGET:
                tableName = OrcaContract.BudgetEntry.TABLE_NAME;
                break;
            case PRODUCT:
                tableName = OrcaContract.ProductEntry.TABLE_NAME;
                break;
            case CLASS:
                tableName = OrcaContract.ClassEntry.TABLE_NAME;
                break;
            case INPUT:
                tableName = OrcaContract.InputEntry.TABLE_NAME;
                break;
            case ROOM:
                tableName = OrcaContract.RoomEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }
        del = db.delete(tableName,s,strings);
        if (del != 0){
            if (!isTest)
                getContext().getContentResolver().notifyChange(uri, null);
        }
        return del;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String tableName;
        int updates;

        switch (sUriMatcher.match(uri)) {
            case PERSON:
                tableName = OrcaContract.PersonEntry.TABLE_NAME;
                break;
            case BUDGET:
                tableName = OrcaContract.BudgetEntry.TABLE_NAME;
                break;
            case PRODUCT:
                tableName = OrcaContract.ProductEntry.TABLE_NAME;
                break;
            case CLASS:
                tableName = OrcaContract.ClassEntry.TABLE_NAME;
                break;
            case INPUT:
                tableName = OrcaContract.InputEntry.TABLE_NAME;
                break;
            case ROOM:
                tableName = OrcaContract.RoomEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }
        updates = db.update(tableName, contentValues, s, strings);
        if (updates != 0){
            if (!isTest)
                getContext().getContentResolver().notifyChange(uri, null);
        }
        return updates;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case PERSON:
                db.beginTransaction();
                try {
                    for (ContentValues value :
                            values) {
                        long _id = db.insert(OrcaContract.PersonEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                return count;
            case BUDGET:
                db.beginTransaction();
                try {
                    for (ContentValues value :
                            values) {
                        long _id = db.insert(OrcaContract.BudgetEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                return count;
            case PRODUCT:
                db.beginTransaction();
                try {
                    for (ContentValues value :
                            values) {
                        long _id = db.insert(OrcaContract.ProductEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                return count;
            case CLASS:
                db.beginTransaction();
                try {
                    for (ContentValues value :
                            values) {
                        long _id = db.insert(OrcaContract.ClassEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                return count;
            case INPUT:
                db.beginTransaction();
                try {
                    for (ContentValues value :
                            values) {
                        long _id = db.insert(OrcaContract.InputEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                return count;
            case ROOM:
                db.beginTransaction();
                try {
                    for (ContentValues value :
                            values) {
                        long _id = db.insert(OrcaContract.RoomEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                return count;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
