package hive.hive.com.hive.Connections;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

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

import hive.hive.com.hive.Activities.MainActivity;

/**
 * Created by abhishekgupta on 12/03/16.
 */
public class RegisterUserGeoTagConnection extends AsyncTask<Void, Void, Boolean> {

    ContentValues param;

    public RegisterUserGeoTagConnection(ContentValues param) {
        this.param = param;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            URL url = new URL("http://abhishek.activexenon.com/hive/hive_update_user_settings.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(param));
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            int resCode;
            resCode = conn.getResponseCode();
            InputStream in;

            Log.d("REGISTERUSERGEOTAG","GOING TO START LA");
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
                if (result.toString().contentEquals("true")) {
                    Log.d("YOU GOT IT RIGHT", "RIGHT WORK");
                    return true;
                } else if (result.toString().contentEquals("saltError")) {
                    Log.d("Salt Error", "Same Salt");
                    return false;
                } else if (result.toString().contentEquals("existingUser")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }

        return false;
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
            result.append(URLEncoder.encode(String.valueOf(params.get(key).toString()), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        MainActivity.hideLoader();
    }
}
