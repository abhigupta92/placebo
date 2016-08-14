package hive.hive.com.hive.Connections;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import hive.hive.com.hive.GSONEntities.UserSettingsDetails;

/**
 * Created by abhishekgupta on 20/03/16.
 */
public class GetUserSettingsConnection extends AsyncTask<Void, Void, UserSettingsDetails> {

    String userID;

    public GetUserSettingsConnection(String profileId) {
        userID = profileId;
    }

    @Override
    protected UserSettingsDetails doInBackground(Void... params) {

        StringBuilder parameters = new StringBuilder();

        try {
            parameters.append(URLEncoder.encode("profileID", "UTF-8"));
            parameters.append("=");
            parameters.append(URLEncoder.encode(String.valueOf(userID), "UTF-8"));
        } catch (Exception e) {
            return null;
        }

        try {
            URL url = new URL("http://abhishek.activexenon.com/hive/newsfeed_get_user_settings.php");
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

                Gson gson = new Gson();
                UserSettingsDetails details = gson.fromJson(result.toString(), UserSettingsDetails.class);
                return details;

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }
        return null;
    }
}
