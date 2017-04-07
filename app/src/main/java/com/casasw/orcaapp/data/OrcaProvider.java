package com.casasw.orcaapp.data;

import android.content.UriMatcher;

/**
 * Created by Junior on 06/04/2017.
 * Orca Db content provider
 */

public class OrcaProvider {
    public static final String TAG = OrcaProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();


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
    private static final int BUDGET_PERSON = 583; //budget inner person inner input inner product inner class
    private static final int BUDGET_ROOM_INPUT = 950;

    private static UriMatcher buildUriMatcher() {
        return null;
    }

}
