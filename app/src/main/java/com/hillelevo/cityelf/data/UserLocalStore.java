package com.hillelevo.cityelf.data;


import static com.hillelevo.cityelf.Constants.TAG;

import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.Prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserLocalStore {

  private static SharedPreferences userLocalDatabase;

  public static void clearUserData(Context context) {
    initUserLocalDatabase(context);
    SharedPreferences.Editor spEditor = userLocalDatabase.edit();
    spEditor.clear();
    spEditor.apply();
  }

  public static void saveStringToSharedPrefs(Context context, String type, String data) {
    initUserLocalDatabase(context);
    Log.d(TAG, "UserLocalStore savedToSharedPrefs: " + type + ", " + data);
    SharedPreferences.Editor editor = userLocalDatabase.edit();
    editor.putString(type, data);
    editor.apply();
  }

  public static void saveIntToSharedPrefs(Context context, String type,int data) {
    initUserLocalDatabase(context);
    Log.d(TAG, "UserLocalStore savedToSharedPrefs: " + type + ", " + data);
    SharedPreferences.Editor editor = userLocalDatabase.edit();
    editor.putInt(type, data);
    editor.apply();
  }

  public static void saveBooleanToSharedPrefs(Context context, String type, boolean data) {
    initUserLocalDatabase(context);
    Log.d(TAG, "UserLocalStore savedToSharedPrefs: " + type + ", " + data);
    SharedPreferences.Editor editor = userLocalDatabase.edit();
    editor.putBoolean(type, data);
    editor.apply();
  }

  public static String loadStringFromSharedPrefs(Context context, String prefKey) {
    initUserLocalDatabase(context);
    if (userLocalDatabase != null && userLocalDatabase.contains(prefKey)) {
      Log.d(TAG, "UserLocalStore mSettings != null, loading " + prefKey);
      return userLocalDatabase.getString(prefKey, "");
    } else {
      Log.d(TAG, "UserLocalStore mSettings != null, no " + prefKey);
      return "";
    }
  }

  public static int loadIntFromSharedPrefs(Context context, String prefKey) {
    initUserLocalDatabase(context);
    if (userLocalDatabase != null && userLocalDatabase.contains(prefKey)) {
      Log.d(TAG, "UserLocalStore mSettings != null, loading " + prefKey);
      return userLocalDatabase.getInt(prefKey, 0);
    } else {
      Log.d(TAG, "UserLocalStore mSettings != null, no " + prefKey);
      return 0;
    }
  }

  public static boolean loadBooleanFromSharedPrefs(Context context, String prefKey) {
    initUserLocalDatabase(context);
    if (userLocalDatabase != null && userLocalDatabase.contains(prefKey)) {
      Log.d(TAG, "UserLocalStore mSettings != null, loading " + prefKey);
      return userLocalDatabase.getBoolean(prefKey, false);
    } else {
      Log.d(TAG, "UserLocalStore mSettings != null, no " + prefKey);
      return false;
    }
  }

  private static void initUserLocalDatabase(Context context) {
    userLocalDatabase = context.getSharedPreferences(Prefs.APP_PREFERENCES, Context.MODE_PRIVATE);
  }
}
