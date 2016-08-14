package hive.hive.com.hive.GSONEntities;

/**
 * Created by abhishekgupta on 25/04/16.
 */
public class ClosestHiveDetail {

    long hiveID;
    long hiveCount;
    String hiveRegion;
    long clusterId;

    public ClosestHiveDetail(long hiveID, long hiveCount, String hiveRegion, long clusterId) {
        this.hiveID = hiveID;
        this.hiveCount = hiveCount;
        this.hiveRegion = hiveRegion;
        this.clusterId = clusterId;
    }

    public long getHiveID() {
        return hiveID;
    }

    public void setHiveID(long hiveID) {
        this.hiveID = hiveID;
    }

    public long getHiveCount() {
        return hiveCount;
    }

    public void setHiveCount(long hiveCount) {
        this.hiveCount = hiveCount;
    }

    public String getHiveRegion() {
        return hiveRegion;
    }

    public void setHiveRegion(String hiveRegion) {
        this.hiveRegion = hiveRegion;
    }

    public long getClusterId() {
        return clusterId;
    }

    public void setClusterId(long clusterId) {
        this.clusterId = clusterId;
    }
}
