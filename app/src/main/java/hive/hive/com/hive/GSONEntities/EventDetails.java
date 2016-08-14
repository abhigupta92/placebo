package hive.hive.com.hive.GSONEntities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishekgupta on 23/06/16.
 */
public class EventDetails {

    @SerializedName("ID")
    public int eventId;

    @SerializedName("EVENT_NAME")
    public String eventName;

    @SerializedName("EVENT_USER_ID")
    public String eventUserId;

    @SerializedName("EVENT_LOCATION")
    public String eventLocation;

    @SerializedName("EVENT_TIME")
    public String eventTime;

    @SerializedName("EVENT_TYPE")
    public String eventType;

    @SerializedName("EVENT_DATE")
    public String eventDate;

    @SerializedName("EVENT_NUM_OF_PEOPLE")
    public String eventNumOfPpl;

    @SerializedName("EVENT_DESC")
    public String eventDesc;

    @SerializedName("EVENT_STATUS")
    public String eventStatus;

    @SerializedName("EVENT_INTEREST")
    public int eventInterest;

    public EventDetails(int eventId, String eventName, String eventUserId,
                        String eventLocation, String eventTime, String eventType,
                        String eventDate, String eventNumOfPpl, String eventDesc,
                        String eventStatus, int eventInterest) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventUserId = eventUserId;
        this.eventLocation = eventLocation;
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.eventNumOfPpl = eventNumOfPpl;
        this.eventDesc = eventDesc;
        this.eventStatus = eventStatus;
        this.eventInterest = eventInterest;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventUserId() {
        return eventUserId;
    }

    public void setEventUserId(String eventUserId) {
        this.eventUserId = eventUserId;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventNumOfPpl() {
        return eventNumOfPpl;
    }

    public void setEventNumOfPpl(String eventNumOfPpl) {
        this.eventNumOfPpl = eventNumOfPpl;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public int getEventInterest() {
        return eventInterest;
    }

    public void setEventInterest(int eventInterest) {
        this.eventInterest = eventInterest;
    }
}
