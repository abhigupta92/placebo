package hive.hive.com.hive.GSONEntities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishekgupta on 26/10/16.
 */

public class HiveOnMapDtl {

    @SerializedName("CLUSTERID")
    public int clusterId;

    @SerializedName("SUBPARTID")
    public int subpartId;

    @SerializedName("MIDLAT")
    public double midLat;

    @SerializedName("MIDLNG")
    public double midLng;

    public HiveOnMapDtl(int clusterId, int subpartId, double midLat, double midLng) {
        this.clusterId = clusterId;
        this.subpartId = subpartId;
        this.midLat = midLat;
        this.midLng = midLng;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public int getSubpartId() {
        return subpartId;
    }

    public void setSubpartId(int subpartId) {
        this.subpartId = subpartId;
    }

    public double getMidLat() {
        return midLat;
    }

    public void setMidLat(int midLat) {
        this.midLat = midLat;
    }

    public double getMidLng() {
        return midLng;
    }

    public void setMidLng(int midLng) {
        this.midLng = midLng;
    }
}


