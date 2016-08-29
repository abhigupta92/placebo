package hive.hive.com.hive.Connections;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import hive.hive.com.hive.Activities.MainActivity;

import static hive.hive.com.hive.Utils.ConnectionUtils.getQuery;

/**
 * Created by abhishekgupta on 23/06/16.
 */
public class GetEventDetailsConnection extends AsyncTask<Void, Void, JSONObject> {

    ContentValues reqParams;

    public GetEventDetailsConnection(ContentValues reqParams) {
        this.reqParams = reqParams;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {

        String parameters = getQuery(reqParams);;

        if (parameters == null) {
            return null;
        }

        try {
            URL url = new URL("http://abhishek.activexenon.com/hive/hive_get_event_details.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(String.valueOf(parameters));
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            int resCode;
            resCode = conn.getResponseCode();
            InputStream in;

            String data;
            StringBuilder result = new StringBuilder();
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((data = reader.readLine()) != null) {
                    result.append(data);
                }
                conn.disconnect();
                Log.d("RESULT : ", result.toString());

                JSONObject details = new JSONObject(result.toString());
                return details;

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        MainActivity.hideLoader();
    }
}
