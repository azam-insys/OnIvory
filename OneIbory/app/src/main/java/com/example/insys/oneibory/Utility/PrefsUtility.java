package com.example.insys.oneibory.Utility;

/**
 * Created by insys on 11/9/2015.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsUtility {


    public static final String PREFS_LOGIN_Name = "__UserName__";
    public static final String PREFS_LOGIN_SessionId = "SessionId";
    public static final String PREFS_LOGIN_Mobile = "__Mobile__";
    public static final String PREFS_LOGIN_USERNAME_KEY = "__USERNAME__";
    public static final String PREFS_LOGIN_PASSWORD = "PASSWORD";


    public static final String PREFS_LOGIN_TIME = "__TIME__";
    public static final String PREFS_LOGIN_GENDER_KEY = "__GENDER__";
    public static final String PREFS_LOGIN_Country_KEY = "__Country__";
    public static final String PREFS_LOGIN_Latitude_KEY = "__Latitude__";
    public static final String PREFS_LOGIN_Longitude_KEY = "__Longitude__";


    public static final String PREFS_LOGIN_PICProfile_KEY = "__PICProfile__";

    public static final String PREFS_LOGIN_PIC_LOGO_KEY = "__PICLOGO_";

    public static final String PREFS_LOGIN_PIC_PRODUCT_KEY = "__PRODUCT_";
    ;
    public static final String PREFS_LOGIN_PicCoverProfile_KEY = "__PICCoverProfile__";
    public static final String PREFS_LOGIN_TeamName_KEY = "TEAMNAME";
    public static final String PREFS_LOGIN_Status_KEY = "STATUS";

    public static final String PREFS_LOGIN_AboutJourney_KEY = "ABOUTORJOURNEY";

    public static final String PREFS_LOGIN_Social_KEY = "FacebookLogin";

    public static final String PREFS_LOGIN_NEWNOTIFICATION = "New Notification";


    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getFromPrefs(Context context, String key,
                                      String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    public static void DeleteSharedPrefernce(Context context) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.clear().commit();

    }
}
