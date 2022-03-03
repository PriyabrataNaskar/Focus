package com.priyo.focus.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Priyabrata Naskar on 03-03-2022.
 */
public class PrefUtils {
    private static String prefKey = "SHARED_STORAGE_KEY";
    private static String remainingTimeKey = "TIME_REMAINING";
    private static String totalTimeKey = "TOTAL_TIME";

    /**
     * Save remaining time in Shared Prefs
     * @param context
     * @param remainingTime
     */
    public static void saveRemainingTime(Context context, int remainingTime){
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;
        sharedPref = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);

        editor = sharedPref.edit();

        //Saving data in shared prefs
        editor.putInt(remainingTimeKey, remainingTime);

        editor.apply();
    }

    /**
     * Save total time in Shared Prefs
     * @param context
     */
    public static void saveTotalTime(Context context, int totalTime){
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;
        sharedPref = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);

        editor = sharedPref.edit();

        //Saving data in shared prefs
        editor.putInt(totalTimeKey, totalTime);

        editor.apply();
    }

    public static int fetchRemainingTime(Context context){
        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);

        //Saving data in shared prefs
        return sharedPref.getInt(remainingTimeKey, 0);
    }

    public static void clearSharedPrefs(Context context){
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;
        sharedPref = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);

        editor = sharedPref.edit();

        editor.clear();
        editor.apply();
    }
}
