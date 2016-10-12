package hive.hive.com.hive.GSONEntities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishekgupta on 21/02/16.
 */
public class UserDetails {

    @SerializedName("USERID")
    private String userId;

    @SerializedName("SALT")
    private String salt;

    @SerializedName("NAME")
    private String userName;

    @SerializedName("EMAIL")
    private String userEmail;

    @SerializedName("DOB")
    private String userBirthday;

    @SerializedName("HOMETOWN")
    private String userHomeTown;

    @SerializedName("NUMOFPOSTS")
    private String userNumOfPosts;

    @SerializedName("LASTPOSTED")
    private String userLastPosted;

    @SerializedName("HIVEID")
    private String hiveId;

    @SerializedName("CLUSTERID")
    private String clusterId;

    @SerializedName("HIVE_LAT")
    private String hiveLatPos;

    @SerializedName("HIVE_LNG")
    private String hiveLngPos;

    public UserDetails(String userId, String salt, String userName, String userEmail, String userBirthday, String userHomeTown, String userNumOfPosts, String userLastPosted, String hiveId, String clusterId, String hiveLatPos, String hiveLngPos) {
        this.userId = userId;
        this.salt = salt;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userBirthday = userBirthday;
        this.userHomeTown = userHomeTown;
        this.userNumOfPosts = userNumOfPosts;
        this.userLastPosted = userLastPosted;
        this.hiveId = hiveId;
        this.clusterId = clusterId;
        this.hiveLatPos = hiveLatPos;
        this.hiveLngPos = hiveLngPos;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserHomeTown() {
        return userHomeTown;
    }

    public void setUserHomeTown(String userHomeTown) {
        this.userHomeTown = userHomeTown;
    }

    public String getUserNumOfPosts() {
        return userNumOfPosts;
    }

    public void setUserNumOfPosts(String userNumOfPosts) {
        this.userNumOfPosts = userNumOfPosts;
    }

    public String getUserLastPosted() {
        return userLastPosted;
    }

    public void setUserLastPosted(String userLastPosted) {
        this.userLastPosted = userLastPosted;
    }

    public String getHiveId() {
        return hiveId;
    }

    public void setHiveId(String hiveId) {
        this.hiveId = hiveId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getHiveLatPos() {
        return hiveLatPos;
    }

    public void setHiveLatPos(String hiveLatPos) {
        this.hiveLatPos = hiveLatPos;
    }

    public String getHiveLngPos() {
        return hiveLngPos;
    }

    public void setHiveLngPos(String hiveLngPos) {
        this.hiveLngPos = hiveLngPos;
    }
}
