package hive.hive.com.hive.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hive.hive.com.hive.GSONEntities.ClosestHiveDetail;
import hive.hive.com.hive.R;

/**
 * Created by abhishekgupta on 25/04/16.
 */
public class HiveSelectionCustomListAdapter extends ArrayAdapter {

    private Context mContext;
    private int id;
    private List<ClosestHiveDetail> items;

    public HiveSelectionCustomListAdapter(Context context, int customlist, List<ClosestHiveDetail> list) {
        super(context, customlist, list);

        mContext = context;
        id = customlist;
        items = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;

        ViewHolder holder = new ViewHolder();

        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }
        holder.hiveName = (TextView) mView.findViewById(R.id.hiveName);
        holder.hiveRegion = (TextView) mView.findViewById(R.id.hiveRegion);
        if (items.get(position) != null) {

            holder.hiveName.setText(String.valueOf(items.get(position).getHiveID()));
            //holder.hiveName.setTextSize(50);
            holder.hiveRegion.setText(items.get(position).getHiveRegion());

        }

        return mView;
    }

    private class ViewHolder {
        TextView hiveName;
        TextView hiveRegion;
    }
}
