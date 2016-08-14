package hive.hive.com.hive.Connections;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;

import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.StringUtils;

/**
 * Created by abhishekgupta on 16/06/15.
 */
public class PostToHiveConnection extends AsyncTask<Void, Void, Boolean> {


    ContentValues contentValues;

    public PostToHiveConnection(ContentValues contentValues) {
        this.contentValues = contentValues;
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


        //parameters
        Bitmap capturedImage = null;

        try {

            System.setProperty("http.keepAlive", "false");

            /////////////////////////////////////////////
            ////UPLOADING PICTURE AND DATA

            //download picture from the source
            Bitmap bitmap = ConnectionUtils.getBitmap();
            long numOfUserPost = ConnectionUtils.getNumOfNextPost();
            Log.d("NumOfPost : " , String.valueOf(numOfUserPost));
            String imageName = "image".concat(String.valueOf(numOfUserPost+1));
            Log.d("Image Name : " , imageName);
            String salt = ConnectionUtils.getSalt();

            String saltedImageName = salt.concat(StringUtils.EncryptMD5(imageName));

            //encoding image into a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();

            // open a URL connection to the Servlet
            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(imageBytes);
            URL url = new URL("http://abhishek.activexenon.com/hive/newsfeed_post.php");

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
            conn.setRequestProperty("profileID", contentValues.getAsString("profileID"));
            conn.setRequestProperty("name", String.valueOf(contentValues.get("name")));
            Log.d("Name to post with : " , String.valueOf(contentValues.get("name")));
            conn.setRequestProperty("title", contentValues.getAsString("title"));
            conn.setRequestProperty("content",contentValues.getAsString("content"));
            conn.setRequestProperty("category",contentValues.getAsString("category"));

            dos = new DataOutputStream(conn.getOutputStream());

            //first parameter - email
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"profileID\"" + lineEnd + lineEnd
                    + contentValues.getAsString("profileID") + lineEnd);

            //second parameter - userName
            String name = String.valueOf(contentValues.get("name"));
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"name\"" + lineEnd + lineEnd
                    + name + lineEnd);

            //third parameter - password
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"title\"" + lineEnd + lineEnd
                    + contentValues.getAsString("title") + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"content\"" + lineEnd + lineEnd
                    + contentValues.getAsString("content") + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"category\"" + lineEnd + lineEnd
                    + contentValues.getAsString("category") + lineEnd);

            String filePath = "http://abhishek.activexenon.com/captured_images/" + saltedImageName;
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"filepath\"" + lineEnd + lineEnd
                    + filePath + lineEnd);

            //forth parameter - filename PLEASE CHANGE FILENAME="CAPTURED_IMAGE (N) FOR STORING SUCCESSFULLY LA
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"captured_image\";filename=\""
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

            Log.d("PostToHive",result.toString());
            if(result.toString().contentEquals("success")){
                ConnectionUtils.setNumOfNextPost(ConnectionUtils.getNumOfNextPost() + 1);
                return true;
            }else{
                Log.d("PostToHive",result.toString());
            }

        }catch (Exception e){

        }
        return false;
    }

    private String getQuery(ContentValues contentValues) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Set<String> keys = contentValues.keySet();

        for (String key : keys) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) contentValues.get(key), "UTF-8"));
        }

        return result.toString();
    }

   /* *//**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     *//*
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    *//**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     *//*
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    *//**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     *//*
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }*/
}
