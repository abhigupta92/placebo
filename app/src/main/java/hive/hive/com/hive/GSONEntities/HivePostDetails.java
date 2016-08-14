package hive.hive.com.hive.GSONEntities;

/**
 * Created by abhishekgupta on 27/02/16.
 */
public class HivePostDetails {

    public String title;
    public String profilePicId;
    public int NumOfVotes;
    public int postID;
    public String postName;
    public String imageURL;
    public int voteStatus;

    public HivePostDetails(String title, String profilePicId, int NumOfVotes, int postID, String postName, String imageURL, int voteStatus) {
        this.postName = postName;
        this.postID = postID;
        this.title = title;
        this.profilePicId = profilePicId;
        this.NumOfVotes = NumOfVotes;
        this.imageURL = imageURL;
        this.voteStatus = voteStatus;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfilePicId() {
        return profilePicId;
    }

    public void setProfilePicId(String profilePicId) {
        this.profilePicId = profilePicId;
    }

    public int getNumOfVotes() {
        return NumOfVotes;
    }

    public void setNumOfVotes(int numOfVotes) {
        NumOfVotes = numOfVotes;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public int getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(int voteStatus) {
        this.voteStatus = voteStatus;
    }
}
