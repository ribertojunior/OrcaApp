package com.casasw.orcaapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Junior on 11/04/2017.
 * Orca Provider Tests
 */
@RunWith(AndroidJUnit4.class)
public class ProviderTest {
    private OrcaProvider mProvider;    

    @Before
    public void setUp() throws Exception {
        mProvider = new OrcaProvider();
        mProvider.testOnCreate(InstrumentationRegistry.getTargetContext());
        
    }

    @Test
    public void testUriMatcher() {
        Uri.Builder builder = new Uri.Builder();

        Uri uriPerson = builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_PERSON).build();
        assertTrue("Error: UriMatcher on Person failed: "+uriPerson.toString(), OrcaProvider.getsUriMatcher().match(uriPerson) == 100);
        
        Uri uriPersonID = OrcaContract.PersonEntry.buildPersonUri(123);
        assertTrue("Error: UriMatcher on Person ID failed: "+uriPersonID.toString(), OrcaProvider.getsUriMatcher().match(uriPersonID) == 840);

        builder = new Uri.Builder();
        Uri uriBudget =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_BUDGET).build();
        assertTrue("Error: UriMatcher on Budget failed: "+uriBudget.toString(), OrcaProvider.getsUriMatcher().match(uriBudget) == 635);

        Uri uriBudgetID = OrcaContract.BudgetEntry.buildBudgetUri(123);
        assertTrue("Error: UriMatcher on Budget ID failed ", OrcaProvider.getsUriMatcher().match(uriBudgetID) == 963);

        builder = new Uri.Builder();
        Uri uriProduct =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_PRODUCT).build();
        assertTrue("Error: UriMatcher on Product failed ", OrcaProvider.getsUriMatcher().match(uriProduct) == 329);

        Uri uriProductID = OrcaContract.ProductEntry.buildProductUri(123);
        assertTrue("Error: UriMatcher on Product ID failed ", OrcaProvider.getsUriMatcher().match(uriProductID) == 927);

        builder = new Uri.Builder();
        Uri uriClass =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_CLASS).build();
        assertTrue("Error: UriMatcher on Class failed ", OrcaProvider.getsUriMatcher().match(uriClass) == 158);

        Uri uriClassID = OrcaContract.ClassEntry.buildClassUri(123);
        assertTrue("Error: UriMatcher on Class ID failed ", OrcaProvider.getsUriMatcher().match(uriClassID) == 619);

        builder = new Uri.Builder();
        Uri uriInput =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_INPUT).build();
        assertTrue("Error: UriMatcher on Input failed ", OrcaProvider.getsUriMatcher().match(uriInput) == 550);

        Uri uriInputID = OrcaContract.InputEntry.buildInputUri(123);
        assertTrue("Error: UriMatcher on Input ID failed ", OrcaProvider.getsUriMatcher().match(uriInputID) == 407);

        builder = new Uri.Builder();
        Uri uriRoom =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_ROOM).build();
        assertTrue("Error: UriMatcher on Room failed ", OrcaProvider.getsUriMatcher().match(uriRoom) == 817);

        Uri uriRoomID = OrcaContract.RoomEntry.buildRoomUri(123);
        assertTrue("Error: UriMatcher on Room ID failed ", OrcaProvider.getsUriMatcher().match(uriRoomID) == 435);

        builder = new Uri.Builder();
        Uri uriBudgetPerson =
                builder.authority(OrcaContract.CONTENT_AUTHORITY)
                        .appendPath(OrcaContract.PATH_BUDGET).
                        appendPath(OrcaContract.PATH_PERSON).appendPath("123").build();
        assertTrue("Error: UriMatcher on Budget-Person failed ", OrcaProvider.getsUriMatcher().match(uriBudgetPerson) == 583);

        builder = new Uri.Builder();
        Uri uriBudgetRoomInput =
                builder.authority(OrcaContract.CONTENT_AUTHORITY)
                        .appendPath(OrcaContract.PATH_BUDGET)
                        .appendPath(OrcaContract.PATH_ROOM)
                        .appendPath(OrcaContract.PATH_INPUT).appendPath("123").build();
        assertTrue("Error: UriMatcher on Budget-Room-Input failed ", OrcaProvider.getsUriMatcher().match(uriBudgetRoomInput) == 950);

        builder = new Uri.Builder();
        Uri uriBudgetInputProduct =
                builder.authority(OrcaContract.CONTENT_AUTHORITY)
                        .appendPath(OrcaContract.PATH_BUDGET)                        
                        .appendPath(OrcaContract.PATH_INPUT)
                        .appendPath(OrcaContract.PATH_PRODUCT).appendPath("123").build();
        assertTrue("Error: UriMatcher on Budget-Input-Product failed ", OrcaProvider.getsUriMatcher().match(uriBudgetInputProduct) == 150);

        builder = new Uri.Builder();
        Uri uriProductClass =
                builder.authority(OrcaContract.CONTENT_AUTHORITY)
                        .appendPath(OrcaContract.PATH_PRODUCT)
                        .appendPath(OrcaContract.PATH_CLASS).appendPath("123").build();
        assertTrue("Error: UriMatcher on Budget-Input-Product failed ", OrcaProvider.getsUriMatcher().match(uriProductClass) == 654);
        
    }

    @Test
    public void testGetType(){
        
        Uri.Builder builder = new Uri.Builder();

        Uri uriPerson = builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_PERSON).build();
        assertEquals(OrcaContract.PersonEntry.CONTENT_TYPE, mProvider.getType(uriPerson));

        Uri uriPersonID = OrcaContract.PersonEntry.buildPersonUri(123);
        assertEquals(OrcaContract.PersonEntry.CONTENT_ITEM_TYPE, mProvider.getType(uriPersonID));

        builder = new Uri.Builder();
        Uri uriBudget =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_BUDGET).build();
        assertEquals(OrcaContract.BudgetEntry.CONTENT_TYPE, mProvider.getType(uriBudget));

        Uri uriBudgetID = OrcaContract.BudgetEntry.buildBudgetUri(123);
        assertEquals(OrcaContract.BudgetEntry.CONTENT_ITEM_TYPE, mProvider.getType(uriBudgetID));

        builder = new Uri.Builder();
        Uri uriProduct =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_PRODUCT).build();
        assertEquals(OrcaContract.ProductEntry.CONTENT_TYPE, mProvider.getType(uriProduct));

        Uri uriProductID = OrcaContract.ProductEntry.buildProductUri(123);
        assertEquals(OrcaContract.ProductEntry.CONTENT_ITEM_TYPE, mProvider.getType(uriProductID));

        builder = new Uri.Builder();
        Uri uriClass =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_CLASS).build();
        assertEquals(OrcaContract.ClassEntry.CONTENT_TYPE, mProvider.getType(uriClass));

        Uri uriClassID = OrcaContract.ClassEntry.buildClassUri(123);
        assertEquals(OrcaContract.ClassEntry.CONTENT_ITEM_TYPE, mProvider.getType(uriClassID));

        builder = new Uri.Builder();
        Uri uriInput =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_INPUT).build();
        assertEquals(OrcaContract.InputEntry.CONTENT_TYPE, mProvider.getType(uriInput));

        Uri uriInputID = OrcaContract.InputEntry.buildInputUri(123);
        assertEquals(OrcaContract.InputEntry.CONTENT_ITEM_TYPE, mProvider.getType(uriInputID));

        builder = new Uri.Builder();
        Uri uriRoom =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_ROOM).build();
        assertEquals(OrcaContract.RoomEntry.CONTENT_TYPE, mProvider.getType(uriRoom));

        Uri uriRoomID = OrcaContract.RoomEntry.buildRoomUri(123);
        assertEquals(OrcaContract.RoomEntry.CONTENT_ITEM_TYPE, mProvider.getType(uriRoomID));

        builder = new Uri.Builder();
        Uri uriBudgetPerson =
                builder.authority(OrcaContract.CONTENT_AUTHORITY)
                        .appendPath(OrcaContract.PATH_BUDGET).
                        appendPath(OrcaContract.PATH_PERSON).appendPath("123").build();
        assertEquals(OrcaContract.BudgetEntry.CONTENT_ITEM_TYPE, mProvider.getType(uriBudgetPerson));

        builder = new Uri.Builder();
        Uri uriBudgetRoomInput =
                builder.authority(OrcaContract.CONTENT_AUTHORITY)
                        .appendPath(OrcaContract.PATH_BUDGET)
                        .appendPath(OrcaContract.PATH_ROOM)
                        .appendPath(OrcaContract.PATH_INPUT).appendPath("123").build();
        assertEquals(OrcaContract.InputEntry.CONTENT_ITEM_TYPE, mProvider.getType(uriBudgetRoomInput));

        builder = new Uri.Builder();
        Uri uriBudgetInputProduct =
                builder.authority(OrcaContract.CONTENT_AUTHORITY)
                        .appendPath(OrcaContract.PATH_BUDGET)
                        .appendPath(OrcaContract.PATH_INPUT)
                        .appendPath(OrcaContract.PATH_PRODUCT).appendPath("123").build();
        assertEquals(OrcaContract.InputEntry.CONTENT_ITEM_TYPE, mProvider.getType(uriBudgetInputProduct));

        builder = new Uri.Builder();
        Uri uriProductClass =
                builder.authority(OrcaContract.CONTENT_AUTHORITY)
                        .appendPath(OrcaContract.PATH_PRODUCT)
                        .appendPath(OrcaContract.PATH_CLASS).appendPath("123").build();
        assertEquals(OrcaContract.ProductEntry.CONTENT_ITEM_TYPE, mProvider.getType(uriProductClass));
                
    }

    @Test
    public void testInsert(){
        ContentValues values = TestUtilities.createPersonValues();
        Uri.Builder builder = new Uri.Builder();
        Uri uriPerson = builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_PERSON).build();
        Uri uriPersonID = mProvider.insert(uriPerson, values);
        assertTrue(uriPersonID!=null);

        builder = new Uri.Builder();
        Uri uriBudget =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_BUDGET).build();
        values = TestUtilities.createBudgetValues(Long.parseLong(OrcaContract.PersonEntry.getIdFromUri(uriPersonID)));
        Uri uriBudgetId = mProvider.insert(uriBudget, values);
        assertTrue(uriBudgetId!=null);

        builder = new Uri.Builder();
        Uri uriClass =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_CLASS).build();
        values = TestUtilities.createClassValues();
        Uri uriClassID = mProvider.insert(uriClass, values);
        assertTrue(uriClassID!=null);

        builder = new Uri.Builder();
        Uri uriProduct =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_PRODUCT).build();
        values = TestUtilities.createProductValues(Long.parseLong(OrcaContract.ClassEntry.getIdFromUri(uriClassID)));
        Uri uriProductID = mProvider.insert(uriProduct, values);
        assertTrue(uriProductID!=null);

        builder = new Uri.Builder();
        Uri uriRoom =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_ROOM).build();
        values = TestUtilities.createRoomValues();
        Uri uriRoomID = mProvider.insert(uriRoom, values);
        assertTrue(uriRoomID!=null);

        builder = new Uri.Builder();
        Uri uriInput =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_INPUT).build();
        values = TestUtilities.createInputValues(
                Long.parseLong(OrcaContract.BudgetEntry.getIdFromUri(uriBudgetId)),
                Long.parseLong(OrcaContract.ProductEntry.getIdFromUri(uriProductID)),
                Long.parseLong(OrcaContract.RoomEntry.getIdFromUri(uriRoomID)));
        Uri uriInputID = mProvider.insert(uriInput, values);
        assertTrue(uriInputID!=null);


    }

    @Test
    public void testQuery(){
        Uri.Builder builder = new Uri.Builder();
        Uri uriPerson = builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_PERSON).build();
        Cursor c = mProvider.query(uriPerson, null, null, null, null);
        assertTrue(c != null);
        c = mProvider.query(OrcaContract.PersonEntry.buildPersonUri(123), null, null, null, null);
        assertTrue(c != null);

        builder = new Uri.Builder();
        Uri uriClass =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_CLASS).build();
        c = mProvider.query(uriClass, null, null, null, null);
        assertTrue(c != null);
        c = mProvider.query(OrcaContract.ClassEntry.buildClassUri(123), null, null, null, null);
        assertTrue(c != null);

        builder = new Uri.Builder();
        Uri uriProduct =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_PRODUCT).build();
        c = mProvider.query(uriProduct, null, null, null, null);
        assertTrue(c != null);
        c = mProvider.query(OrcaContract.ProductEntry.buildProductUri(123), null, null, null, null);
        assertTrue(c != null);

        builder = new Uri.Builder();
        Uri uriRoom =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_ROOM).build();
        c = mProvider.query(uriRoom, null, null, null, null);
        assertTrue(c != null);
        c = mProvider.query(OrcaContract.RoomEntry.buildRoomUri(123), null, null, null, null);
        assertTrue(c != null);

        builder = new Uri.Builder();
        Uri uriInput =
                builder.authority(OrcaContract.CONTENT_AUTHORITY).appendPath(OrcaContract.PATH_INPUT).build();
        c = mProvider.query(uriInput, null, null, null, null);
        assertTrue(c != null);
        c = mProvider.query(OrcaContract.InputEntry.buildInputUri(123), null, null, null, null);
        assertTrue(c != null);

        c.close();
    }
}
