package hive.hive.com.hive.Utils;

/**
 * Created by abhishekgupta on 16/07/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import hive.hive.com.hive.Activities.MainActivity;

public class UserSessionUtils {
    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared preferences mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREFER_NAME = "USER_SESSION";

    // All Shared Preferences Keys
    public static final String IS_USER_LOGIN = "IS_USER_LOGGED_IN";

    // User name and password(make variable public to access from outside)
    public static final String KEY_USERID = "USERID";
    public static final String KEY_PASSWORD = "PASSWORD";
    public static final String KEY_SALT = "SALT";

    public static final String KEY_LOGIN_TYPE = "LOGIN_TYPE";

    private String userName;


    // Constructor
    public UserSessionUtils(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String userId, String password, String salt, long loginType) {
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in preferences
        editor.putString(KEY_USERID, userId);

        // Storing email in preferences
        editor.putString(KEY_PASSWORD, password);

        editor.putString(KEY_SALT, salt);

        editor.putLong(KEY_LOGIN_TYPE, loginType);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (this.isUserLoggedIn()) {
            return true;
        }
        return false;
    }


    /**
     * Get stored session data
     */
    public UserSessionDetails getUserDetails() {

        UserSessionDetails userDetails = new UserSessionDetails();
        userDetails.setKEY_USERID(pref.getString(KEY_USERID, null));
        userDetails.setKEY_PASSWORD(pref.getString(KEY_PASSWORD, null));
        userDetails.setKEY_SALT(pref.getString(KEY_SALT, null));
        userDetails.setKEY_LOGIN_TYPE(String.valueOf(pref.getLong(KEY_LOGIN_TYPE, 0)));
        userDetails.setKEY_USERNAME(String.valueOf(this.userName));

        return userDetails;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to MainActivity
        Intent i = new Intent(_context, MainActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public String loginType() {
        return String.valueOf(pref.getLong(KEY_LOGIN_TYPE, 0));
    }

    public String getUserId() {
        return pref.getString(KEY_USERID, null);
    }

    public class UserSessionDetails {
        String KEY_USERID;
        String KEY_USERNAME;
        String KEY_PASSWORD;
        String KEY_SALT;
        String KEY_LOGIN_TYPE;

        public UserSessionDetails() {
        }

        public UserSessionDetails(String KEY_USERID, String KEY_USERNAME, String KEY_PASSWORD, String KEY_SALT, String KEY_LOGIN_TYPE) {
            this.KEY_USERID = KEY_USERID;
            this.KEY_USERNAME = KEY_USERNAME;
            this.KEY_PASSWORD = KEY_PASSWORD;
            this.KEY_SALT = KEY_SALT;
            this.KEY_LOGIN_TYPE = KEY_LOGIN_TYPE;
        }

        public String getKEY_USERID() {
            return KEY_USERID;
        }

        public void setKEY_USERID(String KEY_USERID) {
            this.KEY_USERID = KEY_USERID;
        }

        public String getKEY_PASSWORD() {
            return KEY_PASSWORD;
        }

        public void setKEY_PASSWORD(String KEY_PASSWORD) {
            this.KEY_PASSWORD = KEY_PASSWORD;
        }

        public String getKEY_SALT() {
            return KEY_SALT;
        }

        public void setKEY_SALT(String KEY_SALT) {
            this.KEY_SALT = KEY_SALT;
        }

        public String getKEY_LOGIN_TYPE() {
            return KEY_LOGIN_TYPE;
        }

        public void setKEY_LOGIN_TYPE(String KEY_LOGIN_TYPE) {
            this.KEY_LOGIN_TYPE = KEY_LOGIN_TYPE;
        }

        public String getKEY_USERNAME() {
            return KEY_USERNAME;
        }

        public void setKEY_USERNAME(String KEY_USERNAME) {
            this.KEY_USERNAME = KEY_USERNAME;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
