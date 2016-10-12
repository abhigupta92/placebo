package hive.hive.com.hive.Connections;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import hive.hive.com.hive.Activities.MainActivity;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.StringUtils;

/**
 * Created by abhishekgupta on 30/08/16.
 */

public class UploadProfilePicConnection extends AsyncTask<Void, Void, Boolean> {

    Bitmap bmp;
    String userId;

    public UploadProfilePicConnection(Bitmap bmp) {
        this.bmp = bmp;
        this.userId = MainActivity.getUserSession().getUserId();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        int serverResponseCode = 0;

        try {
            Bitmap profilePic = null;

            String profilePicName = "profile_pic".concat(String.valueOf(MainActivity.getUserSession().getUserId()));

            Log.d("profilePicName : ", profilePicName);
            String salt = ConnectionUtils.getSalt();

            String saltedImageName = salt.concat(StringUtils.EncryptMD5(profilePicName));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();


            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(imageBytes);
            URL url = new URL("http://abhishek.activexenon.com/hive/hive_upload_profile_pic.php");

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs

            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Encoding", "");
            //conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", "profile_picture");
            conn.setRequestProperty("userId", this.userId);

            dos = new DataOutputStream(conn.getOutputStream());

            //first parameter - email
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"userId\"" + lineEnd + lineEnd
                    + this.userId + lineEnd);


            String filePath = "http://abhishek.activexenon.com/hive/profile_pic_images/" + saltedImageName;
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"filepath\"" + lineEnd + lineEnd
                    + filePath + lineEnd);

            //forth parameter - filename PLEASE CHANGE FILENAME="CAPTURED_IMAGE (N) FOR STORING SUCCESSFULLY LA
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"profile_pic\";filename=\""
                    + saltedImageName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();

            InputStream in;
            String data;
            in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder result = new StringBuilder();
            while ((data = reader.readLine()) != null) {
                result.append(data);
            }


            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
            conn.disconnect();

            Log.d("UploadProfilePic",result.toString());
            if(result.toString().contentEquals("success")){
                ConnectionUtils.setNumOfNextPost(ConnectionUtils.getNumOfNextPost() + 1);
                return true;
            }else{
                Log.d("PostToHive",result.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}
