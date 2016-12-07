package hive.hive.com.hive.Connections;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

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
 * Created by abhishekgupta on 06/04/16.
 */
public class LikePostConnection extends AsyncTask<Void, Void, JSONObject> {

    ContentValues param;

    public LikePostConnection(ContentValues param) {
        this.param = param;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected JSONObject doInBackground(Void... params) {

        try {
            URL url = new URL("http://abhishek.activexenon.com/hive/hive_post_opinion.php");
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

            String data;
            StringBuilder result = new StringBuilder();
            JSONObject details;
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((data = reader.readLine()) != null) {
                    result.append(data);
                    Log.d("LIKEPOSTCONNECTION",result.toString());
                }
                details = new JSONObject(result.toString());
                return details;
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }


        return null;
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
    protected void onPostExecute(JSONObject jsonObject) {
        MainActivity.hideLoader();
    }
}
