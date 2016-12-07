package hive.hive.com.hive.Connections;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import hive.hive.com.hive.Activities.MainActivity;

/**
 * Created by abhishekgupta on 09/03/16.
 */
public class GetGeoLocationDetailsConnection extends AsyncTask<Void, Void, ContentValues> {

    String placeID;
    InputStream iStream = null;
    StringBuilder jsonStringBuilder;
    private StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyAQUVUOgbUxx3SRYkSwR16nCzy3tg7tgeg&sensor=true&placeid=");


    public GetGeoLocationDetailsConnection(String placeID) {
        this.placeID = placeID;
        url.append(placeID);
    }

    @Override
    protected ContentValues doInBackground(Void... params) {

        try {
            URL url = new URL(this.url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.connect();

            int resCode;

            resCode = conn.getResponseCode();
            InputStream in;

            String data;
            jsonStringBuilder = new StringBuilder();
            JSONObject details = null;
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((data = reader.readLine()) != null) {
                    jsonStringBuilder.append(data);
                }
                conn.disconnect();
            }
        } catch (Exception e) {

        }

        String postalCode, city, state;
        ContentValues contentValues = new ContentValues();
        contentValues.put("placeID", this.placeID);
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                JSONObject jsonObject = new JSONObject(jsonStringBuilder.toString());
                Log.d("VALUE OF DATA IS : ", jsonObject.toString());
                JSONArray clusterDetails = jsonObject.getJSONObject("result").getJSONArray("address_components");
                contentValues.put("Geotag", clusterDetails.getJSONObject(0).getString("short_name"));
                Log.d("GeoTagSelected : ", contentValues.getAsString("Geotag"));
                boolean stateFound = false, pinCodeFound = false, cityFound = false, countryFound = false;
                for (int i = 0; i < clusterDetails.length(); i++) {
                    JSONArray typesArray = clusterDetails.getJSONObject(i).getJSONArray("types");
                    for (int j = 0; j < typesArray.length(); j++) {
                        if (cityFound && stateFound && pinCodeFound) {
                            break;
                        }
                        if (typesArray.get(j).toString().equalsIgnoreCase("postal_code")) {
                            postalCode = clusterDetails.getJSONObject(i).getString("long_name");
                            contentValues.put("postalCode", postalCode);
                            Log.d("PINCODE : ", postalCode);
                            pinCodeFound = true;
                        }
                        if (typesArray.get(j).toString().equalsIgnoreCase("locality")) {
                            city = clusterDetails.getJSONObject(i).getString("long_name");
                            contentValues.put("city", city);
                            Log.d("CITY : ", city);
                            cityFound = true;
                        }
                        if (typesArray.get(j).toString().equalsIgnoreCase("administrative_area_level_1")) {
                            state = clusterDetails.getJSONObject(i).getString("long_name");
                            contentValues.put("state", state);
                            Log.d("STATE : ", state);
                            stateFound = true;
                        }
                        if (typesArray.get(j).toString().equalsIgnoreCase("country")) {
                            try {
                                String country = clusterDetails.getJSONObject(i).getString("country");
                                contentValues.put("country", country);
                                countryFound = true;
                            }catch(Exception e){
                                Log.d("Country :", "Country not found");
                            }
                        }
                    }
                }
                if (!stateFound && cityFound) {
                    contentValues.put("state", String.valueOf(contentValues.get("city")));
                }
                if (!cityFound && stateFound) {
                    contentValues.put("city", contentValues.getAsString("state"));
                }
                if (!stateFound && !cityFound) {
                    contentValues.put("city", contentValues.getAsString("country"));
                    contentValues.put("state", contentValues.getAsString("country"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (contentValues.size() >= 1) {
            return contentValues;
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(ContentValues contentValues) {
        MainActivity.hideLoader();
    }
}
