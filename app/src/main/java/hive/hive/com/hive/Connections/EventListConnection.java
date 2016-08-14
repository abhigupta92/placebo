package hive.hive.com.hive.Connections;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;

import hive.hive.com.hive.Fragments.EventsFragment;

import static hive.hive.com.hive.Utils.Enums.EVENTLISTCONNECTION;

/**
 * Created by abhishekgupta on 15/05/16.
 */
public class EventListConnection extends AsyncTask<Void, Void, JSONArray> {

    public ProgressDialog dialog;
    ContentValues reqParams;
    EventsFragment context;

    public EventListConnection(EventsFragment applicationContext, ContentValues reqParams) {
        this.reqParams = reqParams;
        this.context = applicationContext;
    }

    @Override
    protected JSONArray doInBackground(Void... params) {

        try {
            URL url = new URL("http://abhishek.activexenon.com/hive/hive_get_event_list.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(this.reqParams));
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            int resCode;
            resCode = conn.getResponseCode();
            InputStream in;

            String data;
            StringBuilder result = new StringBuilder();
            JSONArray details;
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((data = reader.readLine()) != null) {
                    result.append(data);
                    //Log.d(EVENTLISTCONNECTION.name(), data);
                }
                details = new JSONArray(result.toString());
                return details;
            }

            conn.disconnect();
        }catch (Exception e) {
            e.printStackTrace();
            Log.d(EVENTLISTCONNECTION.name(), e.toString());
        }

        return null;
    }

    @Override
    protected void onPreExecute() {

        dialog = new ProgressDialog(context.getActivity());
        dialog.setCancelable(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setMessage("Fetching list...");
        //dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.splashscreen));

        dialog.show();

        //Toast.makeText(context.getActivity(), "TESTING CONTEXT STUFF !", Toast.LENGTH_LONG).show();
    }

    private String getQuery(ContentValues params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Set<String> keys = params.keySet();

        for (String key : keys) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        dialog.dismiss();
    }
}
