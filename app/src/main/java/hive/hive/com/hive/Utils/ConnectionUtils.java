package hive.hive.com.hive.Utils;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import hive.hive.com.hive.ConnectionResults.LoginUserResultDetail;
import hive.hive.com.hive.ConnectionResults.RegisterUserResultDetails;
import hive.hive.com.hive.Connections.ClosestHiveListConnection;
import hive.hive.com.hive.Connections.CreateEventConnection;
import hive.hive.com.hive.Connections.EventInterestConnection;
import hive.hive.com.hive.Connections.EventListConnection;
import hive.hive.com.hive.Connections.GetEventDetailsConnection;
import hive.hive.com.hive.Connections.GetGeoLocationDetailsConnection;
import hive.hive.com.hive.Connections.GetUserSettingsConnection;
import hive.hive.com.hive.Connections.HivePostsConnection;
import hive.hive.com.hive.Connections.HivesOnMapConnection;
import hive.hive.com.hive.Connections.LikePostConnection;
import hive.hive.com.hive.Connections.LoginUserConnection;
import hive.hive.com.hive.Connections.PostToHiveConnection;
import hive.hive.com.hive.Connections.RegisterUserConnection;
import hive.hive.com.hive.Connections.RegisterUserGeoTagConnection;
import hive.hive.com.hive.Connections.UploadProfilePicConnection;
import hive.hive.com.hive.Connections.UserFBDetails;
import hive.hive.com.hive.Fragments.AllPostsFragment;
import hive.hive.com.hive.Fragments.EventsFragment;
import hive.hive.com.hive.GSONEntities.ClosestHiveDetail;
import hive.hive.com.hive.GSONEntities.EventDetails;
import hive.hive.com.hive.GSONEntities.EventListDetails;
import hive.hive.com.hive.GSONEntities.HiveOnMapDtl;
import hive.hive.com.hive.GSONEntities.HivePostDetails;
import hive.hive.com.hive.GSONEntities.UserDetails;
import hive.hive.com.hive.GSONEntities.UserSettingsDetails;

/**
 * Created by abhishekgupta on 22/02/16.
 */
public class ConnectionUtils {

    public static Bitmap bitmap;
    public static long numOfNextPost;
    public static String salt;
    public static ContentValues contentValues = new ContentValues();
    public static UserSessionUtils.UserSessionDetails userSessionDetails;

    public static String getSalt() {
        return salt;
    }

    public static void setSalt(String salt) {
        ConnectionUtils.salt = salt;
    }

    public static long getNumOfNextPost() {
        return numOfNextPost;
    }

    public static void setNumOfNextPost(long numOfNextPost) {
        ConnectionUtils.numOfNextPost = numOfNextPost;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        ConnectionUtils.bitmap = bitmap;
    }


    public static UserDetails getProfileDetails(String userId) {

        AsyncTask<Void, Void, String> userDetails;
        userDetails = (new UserFBDetails(userId)).execute();
        String profileDetails = new String();

        try {
            profileDetails = userDetails.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (profileDetails != null) {
            if (!profileDetails.contentEquals("")) {
                Gson gson = new Gson();
                UserDetails details = gson.fromJson(profileDetails, UserDetails.class);
                ConnectionUtils.setNumOfNextPost(Long.parseLong(details.getUserNumOfPosts()) + 1);
                ConnectionUtils.setSalt(details.getSalt());
                return details;
            }
        }

        return null;
    }

    /**
     * Used to write the post to hive
     *
     * @param contentValues
     * @return
     */
    public static boolean postToHive(ContentValues contentValues) {

        AsyncTask<Void, Void, Boolean> postToHiveStatus;
        postToHiveStatus = (new PostToHiveConnection(contentValues)).execute();

        try {
            return postToHiveStatus.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Used to retrive posts from Hive
     *
     * @param applicationContext
     * @param userId
     * @param start
     * @param end
     */
    public static List<HivePostDetails> getHivePosts(AllPostsFragment applicationContext, String userId, long start, long end) {

        AsyncTask<Void, Void, JSONArray> allHivePosts;
        allHivePosts = (new HivePostsConnection(applicationContext, userId, start, end).execute());
        List<HivePostDetails> hivePosts = new ArrayList<HivePostDetails>();

        JSONArray arrayOfPosts = null;
        try {
            arrayOfPosts = allHivePosts.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (arrayOfPosts != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<HivePostDetails>>() {
            }.getType();
            hivePosts = gson.fromJson(arrayOfPosts.toString(), listType);
        }

        return hivePosts;
    }

    /**
     * Used to retrive posts from Hive
     *
     * @param applicationContext
     * @param start
     * @param end
     */
    public static List<EventListDetails> getEvents(EventsFragment applicationContext, String searchString, long start, long end) {

        ContentValues reqParams = new ContentValues();
        reqParams.put("START", start);
        reqParams.put("END", end);
        reqParams.put("SEARCHKEY", searchString);
        reqParams.put("USER_ID", userSessionDetails.getKEY_USERID());

        AsyncTask<Void, Void, JSONArray> allEvents;
        allEvents = (new EventListConnection(applicationContext, reqParams).execute());
        List<EventListDetails> finalListEvents = new ArrayList<EventListDetails>();

        JSONArray arrayOfEvents = null;
        try {
            arrayOfEvents = allEvents.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (arrayOfEvents != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<EventListDetails>>() {
            }.getType();
            finalListEvents = gson.fromJson(arrayOfEvents.toString(), listType);
        }

        return finalListEvents;
    }

    /**
     * Getting the details of the gelocation selected by the user
     *
     * @param placeID
     * @param latLng
     * @param geoTag
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static boolean getSelectedGeolocationDetails(String placeID, LatLng latLng, String geoTag) throws ExecutionException, InterruptedException {
        AsyncTask<Void, Void, ContentValues> geoLocationDetails;
        geoLocationDetails = (new GetGeoLocationDetailsConnection(placeID).execute());
        contentValues = geoLocationDetails.get();
        if (contentValues.size() >= 1) {
            contentValues.put("userId", userSessionDetails.getKEY_USERID());
            contentValues.put("geoTag", geoTag);
            contentValues.put("Lat", String.valueOf(latLng.latitude));
            contentValues.put("Lng", String.valueOf(latLng.longitude));
            return true;

        } else {
            return false;
        }
    }

    /**
     * Registering the geolocation tag details of a user
     *
     * @param name
     * @param about
     * @param gender
     * @return
     */
    public static boolean registerSelectedGeolocationDetails(long selectedHiveID, long selectedClusterId, String name, String about, String gender) {

        contentValues.put("Name", name);
        contentValues.put("About", about);
        contentValues.put("selectedHiveId", selectedHiveID);
        contentValues.put("selectedClusterId", selectedClusterId);
        if (gender.contentEquals("Male")) {
            contentValues.put("Gender", 1);
        } else {
            contentValues.put("Gender", 2);
        }


        AsyncTask<Void, Void, Boolean> registerGeoLocationDetails;
        registerGeoLocationDetails = (new RegisterUserGeoTagConnection(contentValues).execute());
        try {
            boolean registered = registerGeoLocationDetails.get();
            if (registered) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return false;
    }

    public static UserSettingsDetails getUserSettings() {

        AsyncTask<Void, Void, UserSettingsDetails> userSettingsDetails;
        userSettingsDetails = (new GetUserSettingsConnection(userSessionDetails.getKEY_USERID()).execute());

        UserSettingsDetails userSettings = null;
        try {
            userSettings = userSettingsDetails.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return userSettings;
    }

    public static String userGeoLocation(String placeID) {
        AsyncTask<Void, Void, ContentValues> geoLocationDetails;
        geoLocationDetails = (new GetGeoLocationDetailsConnection(placeID).execute());
        try {
            ContentValues cv = geoLocationDetails.get();
            if (cv.size() > 0) {
                return cv.getAsString("Geotag");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return "notset";
    }

    public static JSONObject setUserPostOpinionStatus(ContentValues cv) {

        AsyncTask<Void, Void, JSONObject> userPostOpinion;
        userPostOpinion = (new LikePostConnection(cv).execute());

        try {
            JSONObject postStatusDetails = userPostOpinion.get();
            return postStatusDetails;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ClosestHiveDetail> getExistingClosestHives() {
        AsyncTask<Void, Void, JSONArray> existingClosestHives;
        existingClosestHives = (new ClosestHiveListConnection(contentValues).execute());
        JSONArray listOfHivesJSON = null;
        try {
            listOfHivesJSON = existingClosestHives.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<ClosestHiveDetail> listOfClosesHives = new ArrayList<>();
        if (listOfHivesJSON != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ClosestHiveDetail>>() {
            }.getType();
            listOfClosesHives = gson.fromJson(listOfHivesJSON.toString(), listType);
        }

        return listOfClosesHives;

    }

    public static JSONObject createEvent(ContentValues cvEvent) {

        AsyncTask<Void, Void, JSONObject> createEvent;
        createEvent = (new CreateEventConnection(cvEvent).execute());
        JSONObject response = null;
        try {
            response = createEvent.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static EventDetails getEventDetails(long eventId) {

        EventDetails eventDetails = null;

        ContentValues reqParams = new ContentValues();
        reqParams.put("EVENT_ID", eventId);
        reqParams.put("USER_ID", userSessionDetails.getKEY_USERID());

        AsyncTask<Void, Void, JSONObject> eventDetailObject;
        eventDetailObject = (new GetEventDetailsConnection(reqParams).execute());
        JSONObject response = null;

        try {
            response = eventDetailObject.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        eventDetails = gson.fromJson(response.toString(), EventDetails.class);
        return eventDetails;

    }

    public static boolean setEventInterest(long eventId, int status) {

        AsyncTask<Void, Void, Boolean> eventInterest;
        ContentValues reqParams = reqParams = new ContentValues();
        reqParams.put("USER_ID", userSessionDetails.getKEY_USERID());
        reqParams.put("EVENT_ID", eventId);
        reqParams.put("USER_EVENT_INTEREST", status);
        eventInterest = new EventInterestConnection(reqParams).execute();
        boolean result = false;

        try {
            result = eventInterest.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static ArrayList<HiveOnMapDtl> getHivesOnMap(ContentValues contentValues) {
        AsyncTask<Void, Void, JSONArray> hiveCoordsConn;
        contentValues.put("USER_ID", userSessionDetails.getKEY_USERID());
        hiveCoordsConn = new HivesOnMapConnection(contentValues).execute();
        JSONArray hiveCoordArray = null;

        ArrayList<HiveOnMapDtl> hivesOnMapCoords = null;

        try {
            hiveCoordArray = hiveCoordsConn.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (hiveCoordArray != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<HiveOnMapDtl>>() {
            }.getType();
            hivesOnMapCoords = gson.fromJson(hiveCoordArray.toString(), listType);
        }

        return hivesOnMapCoords;
    }

    public static boolean setUserProfilePic(Bitmap bmp) {

        boolean uploaded = false;

        AsyncTask<Void, Void, Boolean> uploadProfilePicTask;
        uploadProfilePicTask = new UploadProfilePicConnection(bmp).execute();

        try {
            uploaded = uploadProfilePicTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uploaded;

    }

    public static RegisterUserResultDetails registerUser(ContentValues cvRegDetails) {

        AsyncTask<Void, Void, JSONObject> registerUser;
        registerUser = new RegisterUserConnection(cvRegDetails).execute();
        JSONObject result = null;
        RegisterUserResultDetails registrationResult = null;

        try {
            result = registerUser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        registrationResult = gson.fromJson(result.toString(), RegisterUserResultDetails.class);
        return registrationResult;
    }

    public static LoginUserResultDetail loginUser(ContentValues cvLogin) {

        boolean loginResult = false;

        AsyncTask<Void, Void, JSONObject> loginUser;
        loginUser = new LoginUserConnection(cvLogin).execute();
        JSONObject result = null;
        LoginUserResultDetail loginResultDetails = null;

        try {
            result = loginUser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        loginResultDetails = gson.fromJson(result.toString(), LoginUserResultDetail.class);

        return loginResultDetails;
    }

    public static String getQuery(ContentValues params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Set<String> keys = params.keySet();

        for (String key : keys) {
            if (first)
                first = false;
            else
                result.append("&");

            try {
                result.append(URLEncoder.encode(key, "UTF-8"));

                result.append("=");
                result.append(URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    public static void setUserSessionDetails(UserSessionUtils.UserSessionDetails userSessionDetails) {
        ConnectionUtils.userSessionDetails = userSessionDetails;
    }

}
