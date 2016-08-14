package hive.hive.com.hive.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hive.hive.com.hive.GSONEntities.HivePostDetails;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.Enums;
import hive.hive.com.hive.Utils.ImageLoader;

import static hive.hive.com.hive.Utils.Enums.CUSTOMLISTADAPTER;

/**
 * Created by abhishekgupta on 27/02/16.
 */
public class AllPostCustomListAdapter extends ArrayAdapter {
    private Context mContext;
    private int id;
    private List<HivePostDetails> items;
    public ImageLoader imageLoader;

    public AllPostCustomListAdapter(Context context, int customlist, List<HivePostDetails> list) {
        super(context, customlist, list);
        mContext = context;
        id = customlist;
        items = list;
        imageLoader = new ImageLoader(context);
        Log.d(CUSTOMLISTADAPTER.name(), "CLEaRING CACHE");
        imageLoader.clearCache();
        Log.d(CUSTOMLISTADAPTER.name(), "CREATING NEW LIST");
    }



    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class ViewHolder {

        private int postID;
        public TextView title, tvVotes;
        public ImageView image;
        public CheckBox checkBoxLike;
        ProfilePictureView ppv;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        final ViewHolder holder = new ViewHolder();

        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        holder.title = (TextView) mView.findViewById(R.id.listViewPostTitle);
        //holder.tvVotes = (TextView) mView.findViewById(R.id.textViewPostVotes);
        holder.ppv = (ProfilePictureView) mView.findViewById(R.id.hivePostPicID);
        holder.image = (ImageView) mView.findViewById(R.id.postImage);
        holder.image.setImageBitmap(null);
        holder.checkBoxLike = (CheckBox) mView.findViewById(R.id.checkboxLike);


        if (items.get(position) != null) {

            holder.postID = items.get(position).postID;
            holder.title.setTextSize(20);
            holder.title.setGravity(Gravity.CENTER);
            holder.title.setText(items.get(position).title);
            holder.ppv.setProfileId(items.get(position).profilePicId);
            holder.checkBoxLike.setText(items.get(position).NumOfVotes + "Votes");
            imageLoader.DisplayImage(items.get(position).imageURL, holder.image);
            if (items.get(position).voteStatus == 1) {
                holder.checkBoxLike.setChecked(true);
            } else {
                holder.checkBoxLike.setChecked(false);
            }

            holder.checkBoxLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int likeStatus = 0;
                    if(((CheckBox)v).isChecked()){
                        likeStatus = 1;
                    }
                    int postID = items.get(position).postID;
                    String userID = items.get(position).getProfilePicId();
                    ContentValues cv = new ContentValues();
                    cv.put("POSTID", postID);
                    cv.put("USERID", userID);
                    cv.put("VOTE_STATUS", likeStatus);
                    JSONObject postedOpinion = ConnectionUtils.setUserPostOpinionStatus(cv);

                    try {
                        Log.d("STATUS POSTEDOPINION", postedOpinion.get("STATUS").toString());
                        Log.d("VOTES POSTEDOPINION", postedOpinion.get("NUMOFVOTES").toString());
                        if (postedOpinion.get("STATUS").toString().contentEquals("success")) {
                            Toast.makeText(getContext(), "Posted Your Opinion !", Toast.LENGTH_LONG).show();
                            int numOfVotes = postedOpinion.getInt("NUMOFVOTES");

                            ((CheckBox)v).setText(numOfVotes + "Votes");

                        } else {
                            Toast.makeText(getContext(), "Could Not Post Your Opinion !", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            /*holder.checkBoxLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int postID = items.get(position).postID;
                    String userID = items.get(position).getProfilePicId();
                    boolean status = isChecked;
                    int likeStatus = status ? 1 : 0;
                    ContentValues cv = new ContentValues();
                    cv.put("POSTID", postID);
                    cv.put("USERID", userID);
                    cv.put("VOTE_STATUS", likeStatus);
                    JSONObject postedOpinion = ConnectionUtils.setUserPostOpinionStatus(cv);

                    try {
                        Log.d("STATUS POSTEDOPINION", postedOpinion.get("STATUS").toString());
                        Log.d("VOTES POSTEDOPINION", postedOpinion.get("NUMOFVOTES").toString());
                        if (postedOpinion.get("STATUS").toString().contentEquals("success")) {
                            Toast.makeText(getContext(), "Posted Your Opinion !", Toast.LENGTH_LONG).show();
                            int numOfVotes = postedOpinion.getInt("NUMOFVOTES");

                            buttonView.setText(numOfVotes + "Votes");

                        } else {
                            Toast.makeText(getContext(), "Could Not Post Your Opinion !", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });*/
        }

        return mView;
    }


}
