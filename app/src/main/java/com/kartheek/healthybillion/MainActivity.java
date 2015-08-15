package com.kartheek.healthybillion;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.kartheek.healthybillion.task2.SwipeToRefreshActivity;
import com.kartheek.healthybillion.task3.MapsActivity;
import com.kartheek.healthybillion.task4.GalleryActivity;
import com.kartheek.healthybillion.utils.Constants;
import com.kartheek.healthybillion.volley.RequestManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult> {

    private DrawerLayout drawerlayout;
    private ImageView ivSignin;
    private static final String TAG = MainActivity.class.getSimpleName();

    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 9001;
    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";

    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    private ProgressDialog progressDialog;

    /* Client for accessing Google APIs */
    private GoogleApiClient mGoogleApiClient;

    /* View to display current status (signed-in, signed-out, disconnected, etc) */
    private TextView mStatus;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;
    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    private static final int UPDATE_REQUEST = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        intiDialog();
        //Initializing the views
        initViews();
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
        // [START create_google_api_client]
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .addScope(new Scope(Scopes.PLUS_ME))
                .addScope(new Scope(Scopes.PROFILE))
                .build();
        mGoogleApiClient.connect();
        updateUI(mGoogleApiClient.isConnected());
    }

    private void updateUI(boolean connected) {
        invalidateOptionsMenu();
        if (!connected) {
            findViewById(R.id.info_layout).setVisibility(View.GONE);
            findViewById(R.id.layout_signin).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_signin).setVisibility(View.GONE);
            SharedPreferences preferences = getSharedPreferences(Constants.PREF_SHARED_PREFS, MODE_PRIVATE);
            ((TextView) findViewById(R.id.tvName)).setText(preferences.getString(Constants.PREF_NAME, ""));
            ((TextView) findViewById(R.id.tvEmail)).setText(preferences.getString(Constants.PREF_EMAIL, ""));
            ((TextView) findViewById(R.id.tvdob)).setText(preferences.getString(Constants.PREF_DOB, ""));
            ((TextView) findViewById(R.id.tvAboutMe)).setText(preferences.getString(Constants.PREF_ABOUTj_ME, ""));
            int gender = preferences.getInt(Constants.PREF_GENDER, 0);
            ((TextView) findViewById(R.id.tvGender)).setText((gender == 0) ? "Male" : "Female");
            String url = preferences.getString(Constants.PREF_PIC, "");
            if (!TextUtils.isEmpty(url)) {
                ImageLoader imageLoader = new ImageLoader(RequestManager.getRequestQueue(), new ImageLoader.ImageCache() {
                    @Override
                    public Bitmap getBitmap(String s) {
                        File cacheDir = getCacheDir();
                        File file = new File(cacheDir, s);
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                            return bitmap;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void putBitmap(String s, Bitmap bitmap) {
                        File cacheDir = getCacheDir();
                        File file = new File(cacheDir, s);
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 50, new FileOutputStream(file));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ((NetworkImageView) findViewById(R.id.ivProfilePic)).setImageUrl(url, imageLoader);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mGoogleApiClient.isConnected()) {
            getMenuInflater().inflate(R.menu.menu_task1, menu);
        }
        return true;
    }

    private void initViews() {
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        ivSignin = (ImageView) findViewById(R.id.ivSignin);

    }

    private void intiDialog() {
        // Initializing the progress dialog.
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setMessage("Signing in ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_white);
        setTitle("Healthy Billion");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerlayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_update:

                startActivityForResult(new Intent(this, UpdateActivity.class), UPDATE_REQUEST);
                return true;
            case R.id.action_signout:
                signoutUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signoutUser() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            invalidateOptionsMenu();
            updateUI(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerlayout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.menu_task1:

                return true;
            case R.id.menu_task2:
                startActivity(new Intent(this, SwipeToRefreshActivity.class));
                Toast.makeText(this, "Task 2", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_task3:
                startActivity(new Intent(this, MapsActivity.class));
                Toast.makeText(this, "Task 3", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_task4:
                startActivity(new Intent(this, GalleryActivity.class));
                Toast.makeText(this, "Task 4", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    // [START on_start_on_stop]
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    // [END on_start_on_stop]

    // [START on_save_instance_state]
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();

    }

    public void signUser(View view) {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();
        progressDialog.show();
    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);
        return builder.build();
    }


    @Override
    public void onConnected(Bundle bundle) {

        progressDialog.dismiss();
  /* This Line is the key */
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
        saveData();
        updateUI(true);


    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost. The GoogleApiClient will automatically
        // attempt to re-connect. Any UI elements that depend on connection to Google APIs should
        // be hidden or disabled until onConnected is called again.
        Log.w(TAG, "onConnectionSuspended:" + i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further errors.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }
            mIsResolving = false;
            mGoogleApiClient.connect();
        } else if (requestCode == this.UPDATE_REQUEST && resultCode == RESULT_OK) {
            updateUI(true);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        progressDialog.dismiss();
        Toast.makeText(this, "Request failed  " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI

        }
    }

    private void showErrorDialog(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();

        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            // Show the default Google Play services error dialog which may still start an intent
            // on our behalf if the user can resolve the issue.
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mShouldResolve = false;
                        }
                    }).show();
        } else {
            // No default Google Play Services error, display a message to the user.
            Toast.makeText(this, "Error in signing in google", Toast.LENGTH_SHORT).show();
            mShouldResolve = false;
        }
    }

    private void saveData() {

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String personGooglePlusProfile = currentPerson.getUrl();
            String birthday = currentPerson.getBirthday();
            int gender = currentPerson.getGender();
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            String aboutMe = currentPerson.getAboutMe();
            Person.Image image = currentPerson.getImage();
            Log.d(TAG, "name : " + personName + "  dob : " + birthday + " email : " + email + "  aboutme" + aboutMe);
            Toast.makeText(this, "name : " + personName + "  dob : " + birthday + " email : " + email + "  aboutme" + aboutMe, Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = getSharedPreferences(Constants.PREF_SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(Constants.PREF_NAME, personName);
            edit.putString(Constants.PREF_PROFILE, personGooglePlusProfile);
            edit.putString(Constants.PREF_DOB, birthday);
            edit.putString(Constants.PREF_EMAIL, email);
            edit.putString(Constants.PREF_PIC, image.getUrl());
            edit.putString(Constants.PREF_ABOUTj_ME, aboutMe);
            edit.putInt(Constants.PREF_GENDER, gender);
            edit.commit();
        } else {
            Toast.makeText(this, "Error in retrying the current person. ", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult) {

    }
}
