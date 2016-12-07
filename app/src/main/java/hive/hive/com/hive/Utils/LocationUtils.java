package hive.hive.com.hive.Utils;

import android.location.Location;
import android.support.v4.app.FragmentActivity;

/**
 * Created by abhishekgupta on 09/10/16.
 */

public class LocationUtils {

    public static double latitude;
    public static double longitude;

    public static void getCurrentLocation(FragmentActivity activity, LocationValue locationValue) {
        CustomLocationManager.getCustomLocationManager().getCurrentLocation(activity, locationValue);
    }

    public static LocationValue locationValue = new LocationValue() {
        @Override
        public void getCurrentLocation(Location location) {
            // You will get location here if the GPS is enabled
            if (location != null) {
                setLatitude(location.getLatitude());
                setLongitude(location.getLongitude());
            }
        }
    };

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        LocationUtils.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        LocationUtils.longitude = longitude;
    }

    public LocationValue getLocationValue() {
        return locationValue;
    }

    public void setLocationValue(LocationValue locationValue) {
        this.locationValue = locationValue;
    }
}
