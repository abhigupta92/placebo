package hive.hive.com.hive.Connections;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static hive.hive.com.hive.Utils.ConnectionUtils.getQuery;

/**
 * Created by abhishekgupta on 24/10/16.
 */
public class HivesOnMapConnection extends AsyncTask<Void, Void, JSONArray> {

    ContentValues reqParams;

    public HivesOnMapConnection(ContentValues contentValues) {
        reqParams = contentValues;
    }

    @Override
    protected JSONArray doInBackground(Void... params) {

        try {

            URL url = new URL("http://abhishek.activexenon.com/hive/get_hives_on_map.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            writer.write(getQuery(reqParams));

            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            int resCode;

            resCode = conn.getResponseCode();
            InputStream in;

            String data;
            JSONArray hiveCoords = null;

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((data = reader.readLine()) != null) {
                    Log.d("Map Coords : ", data.toString());
                    try {
                        hiveCoords = new JSONArray(data);
                    } catch (JSONException e) {
                        JSONObject hiveCoordsTemp = new JSONObject(data);
                        hiveCoords = new JSONArray();
                        hiveCoords.put(hiveCoordsTemp);
                    }
                }
            }

            return hiveCoords;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
