package hive.hive.com.hive.Connections;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import hive.hive.com.hive.Activities.MainActivity;

/**
 * Created by abhishekgupta on 19/08/15.
 */
public class UserFBDetails extends AsyncTask<Void, Void, String> {

    String fbID;

    public UserFBDetails(String fbID) {
        this.fbID = fbID;
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            URL url = new URL("http://abhishek.activexenon.com/hive/hive_get_profile_details.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            StringBuilder result = new StringBuilder();

            result.append(URLEncoder.encode("USERID", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(fbID, "UTF-8"));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(result.toString());
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            int resCode;

            resCode = conn.getResponseCode();
            InputStream in;

            String data;
            JSONObject details = null;
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((data = reader.readLine()) != null) {
                    System.out.println("VALUE OF DATA IS : " + data);
                    details = new JSONObject(data);
                }
            }

            if (details != null) {
                return details.toString();
            } else {
                return null;
            }
        } catch (Exception e) {

        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        MainActivity.hideLoader();
    }
}
