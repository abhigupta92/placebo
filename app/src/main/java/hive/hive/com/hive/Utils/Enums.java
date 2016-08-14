package hive.hive.com.hive.Utils;

/**
 * Created by abhishekgupta on 21/02/16.
 */
public enum Enums {

    FACEBOOK("FACEBOOK"),

    //Activities
    MAINACTIVITY("MAINACTIVITY"),

    //Adapters
    HIVELISTADAPTER("HIVELISTADAPTER"),
    CUSTOMLISTADAPTER("CUSTOMLISTADAPTER"),
    ALLEVENTCUSTOMLISTADAPTER("ALLEVENTCUSTOMLISTADAPTER"),

    //Connections
    CLOSESTHIVELISTCONNECTION("CLOSESTHIVELISTCONNECTION"),
    GETGEOLOCATIONDETAILSCONNECTION("GETGEOLOCATIONDETAILSCONNECTION"),
    GETIMAGECONNECTION("GETIMAGECONNECTION"),
    GETUSERSETTINGSCONNECTION("GETUSERSETTINGSCONNECTION"),
    HIVEPOSTSCONNECTION("HIVEPOSTSCONNECTION"),
    LIKEPOSTCONNECTION("LIKEPOSTCONNECTION"),
    POSTTOHIVECONNECTION("POSTTOHIVECONNECTION"),
    REGISTERUSERCONNECTION("REGISTERUSERCONNECTION"),
    REGISTERUSERGEOTAGCONNECTION("REGISTERUSERGEOTAGCONNECTION"),
    USERFBDETAILS("USERFBDETAILS"),
    EVENTLISTCONNECTION("EVENTLISTCONNECTION"),
    EVENTINTERESTCONNECTION("EVENTINTERESTCONNECTION"),
    CREATEEVENTCONNECTION("CREATEEVENTCONNECTION"),

    //Fragments
    ALLPOSTSFRAGMENT("ALLPOSTSFRAGMENT"),
    USERPROFILEFRAGMENT("USERPROFILEFRAGMENT"),
    LOGINFRAGMENT("LOGINFRAGMENT"),
    CREATEPOSTFRAGMENT("CREATEPOSTFRAGMENT"),
    EDITPROFILEFRAGMENT("EDITPROFILEFRAGMENT"),
    EVENTSFRAGMENTS("EVENTSFRAGMENTS"),
    EVENTDETAILFRAGMENT("EVENTDETAILFRAGMENT"),

    //GSONEntities
    CLOSESTHIVEDETAIL("CLOSESTHIVEDETAIL"),
    HIVEPOSTDETAILS("HIVEPOSTDETAILS"),
    USERDETAILS("USERDETAILS"),
    USERSETTINGSDETAILS("USERSETTINGSDETAILS"),
    EVENTDETAILS("EVENTDETAILS"),

    //Utils
    FACEBOOKUTILS("FACEBOOKUTILS"),
    FILECACHE("FILECACHE"),
    CONNECTIONUTILS("CONNECTIONUTILS"),
    IMAGELOADER("IMAGELOADER"),
    INFINITESCROLLLISTENER("INFINITESCROLLLISTENER"),
    MEDIAUTILS("MEDIAUTILS"),
    MEMORYCACHE("MEMORYCACHE"),
    STRINGUTILS("STRINGUTILS"),

    //LOGIN TYPE
    HIVE_LOGIN("1"),
    FACEBOOK_LOGIN("2"),

    //Dialogs
    CREATEEVENTDIALOG("CREATEEVENTDIALOG");

    private String enumVal;

    Enums(String enumVal) {
        this.enumVal = enumVal;
    }

    public String getVal() {
        return enumVal;
    }
}
