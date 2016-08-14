package hive.hive.com.hive.GSONEntities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishekgupta on 15/05/16.
 */
public class EventListDetails {

    @SerializedName("ID")
    public int eventId;

    @SerializedName("EVENT_NAME")
    public String eventTitle;

    @SerializedName("EVENT_USER_ID")
    public String profilePicId;

    @SerializedName("EVENT_LOCATION")
    public String eventLocation;

    public EventListDetails(int eventId, String eventTitle, String profilePicId, String eventLocation) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.profilePicId = profilePicId;
        this.eventLocation = eventLocation;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getProfilePicId() {
        return profilePicId;
    }

    public void setProfilePicId(String profilePicId) {
        this.profilePicId = profilePicId;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
}


