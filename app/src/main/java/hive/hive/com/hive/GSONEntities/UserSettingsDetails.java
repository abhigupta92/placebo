package hive.hive.com.hive.GSONEntities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishekgupta on 20/03/16.
 */
public class UserSettingsDetails {

    @SerializedName("ID")
    private String id;

    @SerializedName("USERID")
    private String userID;

    @SerializedName("NAME")
    private String userName;

    @SerializedName("ABOUT")
    private String userAbout;

    @SerializedName("GENDER")
    private String userGender;

    @SerializedName("CLUSTERID")
    private String userClusterID;

    @SerializedName("HIVEID")
    private String userHiveID;

    @SerializedName("placeID")
    private String userPlaceID;

    public UserSettingsDetails(String id, String userID, String userName, String userAbout, String userGender, String userClusterID, String userHiveID, String userPlaceID) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.userAbout = userAbout;
        this.userGender = userGender;
        this.userClusterID = userClusterID;
        this.userHiveID = userHiveID;
        this.userPlaceID = userPlaceID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAbout() {
        return userAbout;
    }

    public void setUserAbout(String userAbout) {
        this.userAbout = userAbout;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserClusterID() {
        return userClusterID;
    }

    public void setUserClusterID(String userClusterID) {
        this.userClusterID = userClusterID;
    }

    public String getUserHiveID() {
        return userHiveID;
    }

    public void setUserHiveID(String userHiveID) {
        this.userHiveID = userHiveID;
    }

    public String getUserPlaceID() {
        return userPlaceID;
    }

    public void setUserPlaceID(String userPlaceID) {
        this.userPlaceID = userPlaceID;
    }
}
