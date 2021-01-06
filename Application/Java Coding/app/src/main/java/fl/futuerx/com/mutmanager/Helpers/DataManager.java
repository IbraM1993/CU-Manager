package fl.futuerx.com.mutmanager.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fl.futuerx.com.mutmanager.Models.Instructor;

public class DataManager {
    private static final DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
    }

    public String INSTRUCTOR_KEY_ID     = "id";
    public String INSTRUCTOR_KEY_NAME   = "name";
    public String INSTRUCTOR_KEY_PHONE  = "phone";
    public String INSTRUCTOR_KEY_EMAIL  = "email";

    public Instructor getCurrent(Context context){

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        int    id     = pref.getInt   (INSTRUCTOR_KEY_ID   , -1);
        String name   = pref.getString(INSTRUCTOR_KEY_NAME , "");
        String phone  = pref.getString(INSTRUCTOR_KEY_PHONE, "");
        String email  = pref.getString(INSTRUCTOR_KEY_EMAIL, "");

        return id == -1 ? null : new Instructor(id, name, phone, email);
    }

    public void setInstructor(Context context, Instructor ins) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit() .putInt(INSTRUCTOR_KEY_ID       ,ins == null ? -1 : ins.id)
                    .putString(INSTRUCTOR_KEY_NAME  ,ins == null ? "" : ins.name)
                    .putString(INSTRUCTOR_KEY_PHONE ,ins == null ? "" : ins.email)
                    .putString(INSTRUCTOR_KEY_EMAIL ,ins == null ? "" : ins.phone).apply();

    }

    public String getMD5EncryptedString(String encTarget){
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }
        return md5;
    }
}
