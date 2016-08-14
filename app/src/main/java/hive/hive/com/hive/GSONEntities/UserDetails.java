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


    public UserDetails(String userId, String salt, String userName, String userEmail, String userBirthday, String userHomeTown, String userNumOfPosts, String userLastPosted) {
        this.userId = userId;
        this.salt = salt;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userBirthday = userBirthday;
        this.userHomeTown = userHomeTown;
        this.userNumOfPosts = userNumOfPosts;
        this.userLastPosted = userLastPosted;
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

}
