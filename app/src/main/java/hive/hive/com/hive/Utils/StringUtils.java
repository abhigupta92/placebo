package hive.hive.com.hive.Utils;

import android.util.Log;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by abhishekgupta on 22/02/16.
 */
public class StringUtils {

    public static String EncryptMD5(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(string.getBytes());
        byte messageDigest[] = digest.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String h = Integer.toHexString(0xFF & messageDigest[i]);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();
    }

    public static String getSalt() {

        String randomString = RandomStringUtils.randomAlphanumeric(8);

        return randomString;
    }

    public static String generateUserId() {

        String userId = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(30) + 20);
        Log.d("STRINGUTILS", userId);
        return userId;
    }

}
