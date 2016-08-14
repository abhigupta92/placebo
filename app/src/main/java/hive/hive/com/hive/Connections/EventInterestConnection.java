package hive.hive.com.hive.Connections;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static hive.hive.com.hive.Utils.ConnectionUtils.getQuery;

/**
 * Created by abhishekgupta on 30/06/16.
 */
public class EventInterestConnection extends AsyncTask<Void, Void, Boolean> {

    ContentValues reqParams;

    String EVENTINTERESTCONNECTION = "EVENTINTERESTCONNECTION";

    public EventInterestConnection(ContentValues reqParams) {
        this.reqParams = reqParams;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            URL url = new URL("http://abhishek.activexenon.com/hive/hive_event_interest.php");
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
                    Log.d(EVENTINTERESTCONNECTION, data);
                }
                JSONObject jsonResult = new JSONObject(result.toString());
                if (jsonResult.getString("result").contentEquals("success")) {
                    return true;
                }
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
