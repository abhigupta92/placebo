package hive.hive.com.hive.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

import hive.hive.com.hive.GSONEntities.EventListDetails;
import hive.hive.com.hive.R;

/**
 * Created by abhishekgupta on 15/05/16.
 */
public class AllEventCustomListAdapter extends ArrayAdapter {

    private Context mContext;
    private int id;
    private List<EventListDetails> items;

    public AllEventCustomListAdapter(Context context, int id, List<EventListDetails> list) {
        super(context, id, list);
        mContext = context;
        this.id = id;
        items = list;
    }

    public static class ViewHolder {

        public int eventId;
        public TextView title, location;
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

        holder.title = (TextView) mView.findViewById(R.id.tvEventName_event_item);
        holder.ppv = (ProfilePictureView) mView.findViewById(R.id.eventProfilePic_event_item);
        holder.location = (TextView) mView.findViewById(R.id.tvEventLocation_event_item);

        if (items.get(position) != null) {
            holder.eventId = items.get(position).eventId;
            holder.title.setText(items.get(position).eventTitle);
            holder.ppv.setProfileId(items.get(position).profilePicId);
            holder.location.setText(items.get(position).eventLocation);
        }

        return mView;
    }

}
