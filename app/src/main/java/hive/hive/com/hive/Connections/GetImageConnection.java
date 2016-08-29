package hive.hive.com.hive.Connections;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;

import hive.hive.com.hive.Activities.MainActivity;

/**
 * Created by abhishekgupta on 24/02/16.
 */
public class GetImageConnection extends AsyncTask<Void, Void, Bitmap> {

    Bitmap bitmap;
    InputStream iStream = null;

    @Override
    protected Bitmap doInBackground(Void... params) {

        try {
            URL url = new URL("http://abhishek.activexenon.com/captured_images/captured_image");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            /** Connecting to url */
            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);

            return bitmap;


        }catch (Exception e){
            Log.d("GETIMAGE","SOMETHING WENT WRONG GOD");
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
            result.append(URLEncoder.encode((String) params.get(key), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        MainActivity.hideLoader();
    }
}
