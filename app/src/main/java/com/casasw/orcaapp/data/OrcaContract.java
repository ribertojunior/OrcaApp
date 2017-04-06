package com.casasw.orcaapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Junior on 06/04/2017.
 * Defines Db
 */

public class OrcaContract {
    static final String CONTENT_AUTHORITY = "com.casasw.sportclub";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_PERSON = "person";
    //static final String PATH_USER = "user";
    static final String PATH_BUDGET = "budget";
    static final String PATH_PRODUCT = "product";
    static final String PATH_CLASS = "class";
    static final String PATH_INPUT = "input";

    /**
    * Inner class for person table
     **/
    static final class PersonEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PERSON).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_PERSON;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSON;

        static Uri buildPersonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        static final String TABLE_NAME = "person";

        static final String COLUMN_NAME = "person_name";

        static final String COLUMN_PHONE = "phone";

        static final String COLUMN_EMAIL = "email";

        static final String COLUMN_ADDRESS = "address";

        static final String COLUMN_CITY = "city";

        static final String COLUMN_PRO = "profession";
    }

    /**
     * Inner class for Budget table
     **/
    static final class BudgetEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BUDGET).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_BUDGET;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BUDGET;

        static Uri buildPersonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        static final String TABLE_NAME = "budget";

        static final String COLUMN_CLIENT_ID = "client_id";

        static final String COLUMN_PHONE = "phone";

        static final String COLUMN_ADDRESS = "address";

        static final String COLUMN_CITY = "city";

    }

    /**
     * Inner class for Product table
     **/
    static final class ProductEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRODUCT).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_PRODUCT;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;

        static Uri buildPersonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        static final String TABLE_NAME = "product";

        static final String COLUMN_NAME = "product_name";

        static final String COLUMN_STOCK = "stock";

        static final String COLUMN_PRICE = "price";

        static final String COLUMN_DESC = "description";
        
        static final String COLUMN_CLASS_ID = "class_id";

    }

    /**
     * Inner class for Class table
     **/
    static final class ClassEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLASS).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_CLASS;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLASS;

        static Uri buildPersonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        static final String TABLE_NAME = "class";

        static final String COLUMN_NAME = "product_name";

    }

    /**
     * Inner class for Input table
     **/
    static final class InputEntry implements BaseColumns {
        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INPUT).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PATH_INPUT;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INPUT;

        static Uri buildPersonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(uri.getPathSegments().size()-1);
        }

        static final String TABLE_NAME = "input";

        static final String COLUMN_BUDGET_ID = "budget_id";

        static final String COLUMN_PRODUCT_ID = "product_id";

        static final String COLUMN_QUANTITY = "quantity";

    }

}
