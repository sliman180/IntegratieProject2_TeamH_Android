package be.kdg.kandoe.kandoeandroid.helpers;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SharedPreferencesMethods {

    public static void saveInSharedPreferences(AppCompatActivity activity, String key, String value) {
        android.content.SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getFromSharedPreferences(Activity activity, String key){
        android.content.SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return sharedPref.getString(key, "");
    }
}
