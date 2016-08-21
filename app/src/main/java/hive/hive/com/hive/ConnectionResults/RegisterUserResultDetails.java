package hive.hive.com.hive.ConnectionResults;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishekgupta on 11/07/16.
 */
public class RegisterUserResultDetails {

    @SerializedName("userId")
    public String userId;

    @SerializedName("salt")
    public String salt;

    @SerializedName("result")
    public String result;

    @SerializedName("errCd")
    public String errCd;

    @SerializedName("errMsg")
    public String errMsg;

    public RegisterUserResultDetails(String userId, String salt, String result, String errCd, String errMsg) {
        this.userId = userId;
        this.salt = salt;
        this.result = result;
        this.errCd = errCd;
        this.errMsg = errMsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrCd() {
        return errCd;
    }

    public void setErrCd(String errCd) {
        this.errCd = errCd;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
