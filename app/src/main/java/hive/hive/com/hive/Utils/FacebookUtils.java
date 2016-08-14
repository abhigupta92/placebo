package hive.hive.com.hive.Utils;

import android.util.Log;

import com.facebook.AccessToken;

import static hive.hive.com.hive.Utils.Enums.FACEBOOKUTILS;

/**
 * Created by abhishekgupta on 21/02/16.
 */
public class FacebookUtils {

    private static boolean facebookLoggedIn = false;
    private static String profileId;
    private static String FBName;

    public static void setLoggedInStatus(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            Log.d(String.valueOf(Enums.FACEBOOK), "Logged In");
            facebookLoggedIn = true;
        } else {
            facebookLoggedIn = false;
            Log.d(String.valueOf(Enums.FACEBOOK), "Not Logged In");
        }
    }

    public static boolean isFacebookLoggedIn() {
        return facebookLoggedIn;
    }

    public static void setProfileId(String id) {
        profileId = id;
    }

    public static String getProfileId() {
        Log.d(FACEBOOKUTILS.name(),profileId);
        return profileId;
    }

    public static void setUserName(String name) {
        FBName = name;
    }

    public static String getUserName() {
        return FBName;
    }
}
