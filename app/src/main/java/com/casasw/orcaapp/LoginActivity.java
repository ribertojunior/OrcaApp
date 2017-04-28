package com.casasw.orcaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * A login screen that offers login via google/facebook.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = LoginActivity.class.getSimpleName();
    static final String EXTRA_NAME = "NAME";
    static final String EXTRA_PHOTO = "PHOTO";
    static final String EXTRA_EMAIL = "EMAIL";

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private static final int RC_SIGN_IN = 9001;
    private boolean OUT;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        OUT = false;
        /*LinearLayout container = (LinearLayout) findViewById(R.id.login_container);
        Drawable back;
        int[] pool = {
                R.drawable.back_00,
                R.drawable.back_01,
                R.drawable.back_02,
                R.drawable.back_03,
                R.drawable.back_04,
                R.drawable.back_05,
                R.drawable.back_06};
        int index = (int) (Math.random()*7);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            back = getDrawable(pool[index]);
        } else {
            back = getResources().getDrawable(pool[index]);
        }
        container.setBackground(back);*/



        /*Facebook login*/
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "user_friends"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookLoginSuccess(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: "+exception.toString());
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    OUT = false;
                    findViewById(R.id.sign_out_button).setVisibility(View.GONE);
                    findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.login_button).setVisibility(View.VISIBLE);
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        };


        /*End of Facebook*/

        /*google*/
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        String action = getIntent().getAction();
        if (action != null && action.equals("com.casasw.LoginActivity")) {
            OUT = true;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String account = prefs.getString(getString(R.string.pref_account_key), getString(R.string.pref_account_default));
            if (account.equals(getString(R.string.pref_account_google))) {
                findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
                findViewById(R.id.login_button).setVisibility(View.GONE);
            }
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        }

        if (isLoggedIn() && !OUT) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            Intent intent = new Intent(this, ProfileActivity.class);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_NAME, prefs.getString(getString(R.string.pref_user_name_key), getString(R.string.error_name)));
            bundle.putString(EXTRA_EMAIL, prefs.getString(getString(R.string.pref_user_email_key), getString(R.string.error_email)));
            Profile profile = Profile.getCurrentProfile();
            bundle.putString(EXTRA_PHOTO,profile.getProfilePictureUri(100, 100).toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (!OUT) {
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (!OUT) {
            if (requestCode == RC_SIGN_IN) { //google
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            } else { //facebook
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.login_button).setVisibility(View.GONE);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putString(getString(R.string.pref_account_key),
                    getString(R.string.pref_account_google)).apply();
            Intent intent = new Intent(this, ProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_NAME, acct.getDisplayName());
            bundle.putString(EXTRA_EMAIL, acct.getEmail());
            bundle.putString(EXTRA_PHOTO,acct.getPhotoUrl().toString());
            intent.putExtras(bundle);
            startActivity(intent);
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.login_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    public void helpOnClick(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void signOut(View view) {
        showProgressDialog();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        OUT = false;
                        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
                        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                        findViewById(R.id.login_button).setVisibility(View.VISIBLE);
                        // [END_EXCLUDE]
                    }
                });

        //LoginManager.getInstance().logOut();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
    private boolean isLoggedIn() {
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        return !(accesstoken == null || accesstoken.getPermissions().isEmpty());
    }

    private void facebookLoginSuccess(LoginResult loginResult) {
        showProgressDialog();
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Profile profile = Profile.getCurrentProfile();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    Bundle bundle = new Bundle();
                    findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                    findViewById(R.id.sign_out_button).setVisibility(View.GONE);
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    String aux = object.getString("name");
                    prefs.edit().putString(getString(R.string.pref_user_name_key), aux).apply();
                    bundle.putString(EXTRA_NAME, aux);
                    aux = object.getString("email");
                    prefs.edit().putString(getString(R.string.pref_user_name_key), aux).apply();
                    bundle.putString(EXTRA_EMAIL,aux);
                    bundle.putString(EXTRA_PHOTO,profile.getProfilePictureUri(100, 100).toString());
                    intent.putExtras(bundle);
                    hideProgressDialog();
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,name, email");
        request.setParameters(bundle);
        request.executeAsync();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        prefs.edit().putString(getString(R.string.pref_account_key),
                getString(R.string.pref_account_facebook)).apply();
    }
}

