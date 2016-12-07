package hive.hive.com.hive.Connections;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

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
import hive.hive.com.hive.Fragments.AllPostsFragment;

/**
 * Created by abhishekgupta on 05/08/15.
 */
public class HivePostsConnection extends AsyncTask<Void, Void, JSONArray> {

    public static long start, end;
    String userId;

    public HivePostsConnection(AllPostsFragment context, String userId, long start, long end) {
        this.start = start;
        this.end = end;
        this.userId = userId;
    }

    @Override
    protected JSONArray doInBackground(Void... params) {

        try {

            URL url = new URL("http://abhishek.activexenon.com/hive/newsfeed_trendingPosts.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            StringBuilder posts = new StringBuilder();
            posts.append(URLEncoder.encode("start", "UTF-8"));
            posts.append("=");
            posts.append(URLEncoder.encode(String.valueOf(start), "UTF-8"));
            posts.append("&");
            posts.append(URLEncoder.encode("end", "UTF-8"));
            posts.append("=");
            posts.append(URLEncoder.encode(String.valueOf(end), "UTF-8"));
            posts.append("&");
            posts.append(URLEncoder.encode("userId", "UTF-8"));
            posts.append("=");
            posts.append(URLEncoder.encode(String.valueOf(userId), "UTF-8"));
            writer.write(posts.toString());

            Log.d("URLQUERY", posts.toString());

            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            int resCode;

            resCode = conn.getResponseCode();
            InputStream in;

            String data;
            JSONArray details = null;
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((data = reader.readLine()) != null) {
                    Log.d("Data received : ", " THE DAATA IS " + data.toString());
                    details = new JSONArray(data);
                }
            }

            if (details != null) {
                return details;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        MainActivity.hideLoader();
    }

    public static long getStart() {
        return start;
    }

    public static void setStart(long start) {
        HivePostsConnection.start = start;
    }

    public static long getEnd() {
        return end;
    }

    public static void setEnd(long end) {
        HivePostsConnection.end = end;
    }
}

