package hive.hive.com.hive.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import hive.hive.com.hive.Dialog.CreateEventDialog;
import hive.hive.com.hive.Fragments.AllPostsFragment;
import hive.hive.com.hive.Fragments.CreatePostFragment;
import hive.hive.com.hive.Fragments.EditProfileFragment;
import hive.hive.com.hive.Fragments.EventDetailFragment;
import hive.hive.com.hive.Fragments.EventsFragment;
import hive.hive.com.hive.Fragments.LoginFragment;
import hive.hive.com.hive.Fragments.RegistrationFragment;
import hive.hive.com.hive.Fragments.UserProfileFragment;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.Enums;
import hive.hive.com.hive.Utils.FacebookUtils;
import hive.hive.com.hive.Utils.UserSessionUtils;

import static hive.hive.com.hive.Utils.Enums.ALLPOSTSFRAGMENT;
import static hive.hive.com.hive.Utils.Enums.CREATEEVENTDIALOG;
import static hive.hive.com.hive.Utils.Enums.CREATEPOSTFRAGMENT;
import static hive.hive.com.hive.Utils.Enums.EDITPROFILEFRAGMENT;
import static hive.hive.com.hive.Utils.Enums.EVENTDETAILFRAGMENT;
import static hive.hive.com.hive.Utils.Enums.EVENTSFRAGMENTS;
import static hive.hive.com.hive.Utils.Enums.FACEBOOK_LOGIN;
import static hive.hive.com.hive.Utils.Enums.HIVE_LOGIN;
import static hive.hive.com.hive.Utils.Enums.USERPROFILEFRAGMENT;
import static hive.hive.com.hive.Utils.FacebookUtils.setLoggedInStatus;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserProfileFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener,
        CreatePostFragment.OnFragmentInteractionListener, AllPostsFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
        CreateEventDialog.CreateEventDialogListener, EventsFragment.OnFragmentInteractionListener, EventDetailFragment.OnFragmentInteractionListener,
        RegistrationFragment.OnFragmentInteractionListener {

    static UserSessionUtils userSession;

    static MainActivity instance;

    public static ProgressDialog progDailog;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Facebook Login Check
        FacebookSdk.sdkInitialize(getApplicationContext());

        instance = this;
        //Setting Main Screen
        setTitle("Hive");
        setContentView(R.layout.activity_main);

        initializeViews();

        userSession = new UserSessionUtils(getApplicationContext());
        UserSessionUtils.UserSessionDetails userSessionDetails = userSession.getUserDetails();
        ConnectionUtils.setUserSessionDetails(userSessionDetails);

        if (userSession.checkLogin()) {
            if (userSessionDetails.getKEY_LOGIN_TYPE() == FACEBOOK_LOGIN.getVal()) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    waitForFacebookSdk();
                } else {
                    init();
                }
                setLoggedInStatus(AccessToken.getCurrentAccessToken());

                if (FacebookUtils.isFacebookLoggedIn()) {
                    Profile profile = Profile.getCurrentProfile();
                    FacebookUtils.setProfileId(profile.getId());
                    FacebookUtils.setUserName(profile.getName());
                    UserProfileFragment userProfileFragment = new UserProfileFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, userProfileFragment, String.valueOf(Enums.USERPROFILEFRAGMENT));
                    transaction.addToBackStack(String.valueOf(Enums.USERPROFILEFRAGMENT));
                    transaction.commit();
                } else {
                    setLoginScreen();
                }
            } else if (userSessionDetails.getKEY_LOGIN_TYPE() == HIVE_LOGIN.getVal()) {
                UserProfileFragment userProfileFragment = new UserProfileFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, userProfileFragment, String.valueOf(Enums.USERPROFILEFRAGMENT));
                transaction.addToBackStack(String.valueOf(Enums.USERPROFILEFRAGMENT));
                transaction.commit();
            }
        } else {
            setLoginScreen();
        }
    }

    /**
     * Used for initializing facebooksdk
     */

    private void waitForFacebookSdk() {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                int tries = 0;
                while (tries < 3) {
                    if (AccessToken.getCurrentAccessToken() == null) {
                        tries++;
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        return null;
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object result) {
                init();
            }
        };
        asyncTask.execute();
    }

    /**
     * Used for initializing facebooksdk
     */
    private void init() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired()) {
            // do work
            Log.d("ACCESSTOKENNULL", "TRUE");
        } else {
            // do some other work
            Log.d("ACCESSTOKENNULL", "FALSE");
        }
    }

    /**
     * Used to set login screen because facebook login has not been done.
     */
    public static void setLoginScreen() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getInstance().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loginFragment, String.valueOf(Enums.LOGINFRAGMENT));
        transaction.addToBackStack(String.valueOf(Enums.LOGINFRAGMENT));
        transaction.commit();
    }

    /**
     * Used to initialize all the views on the general screen.
     */
    private void initializeViews() {

        //Initialization of Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Initialization of DrawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Initialization of NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progDailog = new ProgressDialog(this);
        progDailog.setCancelable(true);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setIndeterminate(true);
        progDailog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.splashscreen));

        //Initialization of FloatingActionButton
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                String currentScreen = getActiveFragment();
                if (currentScreen.contentEquals(EVENTSFRAGMENTS.name())) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    CreateEventDialog createEventDialog = (CreateEventDialog) fragmentManager.findFragmentByTag(CREATEEVENTDIALOG.name());
                    Log.d(EVENTSFRAGMENTS.name(), "Current screen");
                    FragmentManager fm = getSupportFragmentManager();
                    if (createEventDialog == null)
                        createEventDialog = new CreateEventDialog();
                    createEventDialog.show(fm, CREATEEVENTDIALOG.name());
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            String currentScreen = getActiveFragment();
            if (currentScreen.contentEquals(EVENTDETAILFRAGMENT.name())) {
                getFragmentManager().popBackStack();
            } else if (currentScreen.contentEquals(USERPROFILEFRAGMENT.name())) {
                this.finish();
            } else if (userSession.isUserLoggedIn()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                UserProfileFragment userProfileFragment = (UserProfileFragment) fragmentManager.findFragmentByTag(USERPROFILEFRAGMENT.name());
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, userProfileFragment, USERPROFILEFRAGMENT.name());
                transaction.addToBackStack(USERPROFILEFRAGMENT.name());
                transaction.commit();
            } else {
                this.finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (!userSession.checkLogin()) {
            setLoginScreen();
        } else {

            if (id == R.id.nav_camera) {
                // Handle the camera action
            } else if (id == R.id.nav_edit_profile) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                EditProfileFragment editProfileFragment = (EditProfileFragment) fragmentManager.findFragmentByTag(EDITPROFILEFRAGMENT.name());
                if (editProfileFragment == null) {
                    Log.d(EDITPROFILEFRAGMENT.name(), "Null");
                    editProfileFragment = new EditProfileFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, editProfileFragment, EDITPROFILEFRAGMENT.name());
                    transaction.addToBackStack(EDITPROFILEFRAGMENT.name());
                    transaction.commit();
                } else {
                    Log.d(EDITPROFILEFRAGMENT.name(), "Not Null");
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, editProfileFragment, EDITPROFILEFRAGMENT.name());
                    transaction.commit();
                }

                /*EditProfileFragment editProfileFragment = new EditProfileFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, editProfileFragment);
                transaction.commit();*/


            } else if (id == R.id.user_profile) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                UserProfileFragment userProfileFragment = (UserProfileFragment) fragmentManager.findFragmentByTag(USERPROFILEFRAGMENT.name());
                if (userProfileFragment == null) {
                    Log.d(USERPROFILEFRAGMENT.name(), "Null");
                    userProfileFragment = new UserProfileFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, userProfileFragment, Enums.USERPROFILEFRAGMENT.name());
                    transaction.addToBackStack(USERPROFILEFRAGMENT.name());
                    transaction.commit();
                } else {
                    Log.d(USERPROFILEFRAGMENT.name(), "Not Null");
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, userProfileFragment, USERPROFILEFRAGMENT.name());
                    transaction.commit();
                }


            } else if (id == R.id.hive_events) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                EventsFragment eventsFragment = (EventsFragment) fragmentManager.findFragmentByTag(EVENTSFRAGMENTS.name());
                if (eventsFragment == null) {
                    Log.d(EVENTSFRAGMENTS.name(), "Null");
                    eventsFragment = new EventsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, eventsFragment, EVENTSFRAGMENTS.name());
                    transaction.addToBackStack(EVENTSFRAGMENTS.name());
                    transaction.commit();
                } else {
                    Log.d(EVENTSFRAGMENTS.name(), "Not Null");
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, eventsFragment, EVENTSFRAGMENTS.name());
                    transaction.commit();
                }

            } else if (id == R.id.create_post) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                CreatePostFragment createPostFragment = (CreatePostFragment) fragmentManager.findFragmentByTag(CREATEPOSTFRAGMENT.name());
                if (createPostFragment == null) {
                    Log.d(CREATEPOSTFRAGMENT.name(), "Null");
                    createPostFragment = new CreatePostFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, createPostFragment, CREATEPOSTFRAGMENT.name());
                    transaction.addToBackStack(CREATEPOSTFRAGMENT.name());
                    transaction.commit();
                } else {
                    Log.d(CREATEPOSTFRAGMENT.name(), "Not Null");
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, createPostFragment, CREATEPOSTFRAGMENT.name());
                    transaction.commit();
                }

            } else if (id == R.id.all_posts) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                AllPostsFragment allPostsFragment = (AllPostsFragment) fragmentManager.findFragmentByTag(ALLPOSTSFRAGMENT.name());
                if (allPostsFragment == null) {
                    Log.d(ALLPOSTSFRAGMENT.name(), "Null");
                    allPostsFragment = new AllPostsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, allPostsFragment, ALLPOSTSFRAGMENT.name());
                    transaction.addToBackStack(Enums.ALLPOSTSFRAGMENT.name());
                    transaction.commit();
                } else {
                    Log.d(ALLPOSTSFRAGMENT.name(), "Not Null");
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, allPostsFragment, ALLPOSTSFRAGMENT.name());
                    transaction.commit();
                }

            } else if (id == R.id.events) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                EventsFragment eventsFragment = (EventsFragment) fragmentManager.findFragmentByTag(EVENTSFRAGMENTS.name());
                if (eventsFragment == null) {
                    Log.d(EVENTSFRAGMENTS.name(), "Null");
                    eventsFragment = new EventsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, eventsFragment, EVENTSFRAGMENTS.name());
                    transaction.addToBackStack(EVENTSFRAGMENTS.name());
                    transaction.commit();
                } else {
                    Log.d(EVENTSFRAGMENTS.name(), "Not Null");
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, eventsFragment, EVENTSFRAGMENTS.name());
                    transaction.commit();
                }

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static UserSessionUtils getUserSession() {
        return userSession;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void showLoader() {
        progDailog.show();
    }

    public static void hideLoader() {
        progDailog.hide();
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public String getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        return getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
    }

    @Override
    public void OnFinishCreateEventDialog() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CREATEEVENTDIALOG.name());
        if (fragment != null) {
            DialogFragment dialogFragment = (DialogFragment) fragment;
            dialogFragment.dismiss();
        }
        Snackbar.make(getCurrentFocus(), "Event was created !", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    @Override
    public void OnErrorCreateEventErrorDialog() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CREATEEVENTDIALOG.name());
        if (fragment != null) {
            DialogFragment dialogFragment = (DialogFragment) fragment;
            dialogFragment.dismiss();
        }
        Snackbar.make(getCurrentFocus(), "Some Error has occured with the server !... Please try again later !", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }


    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}
