package com.hillelevo.cityelf.activities;

import android.content.Context;
import android.content.SharedPreferences;

public class FirstStartApp {

  SharedPreferences pref;
  SharedPreferences.Editor editor;
  Context _context;

  int PRIVATE_MODE = 0;

  private static final String PREF_NAME = "ifFirstLaunch";

  private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


  public FirstStartApp(Context context) {
    this._context = context;
    pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    editor = pref.edit();
  }

  public void setFirstLaunch(boolean status) {
    editor.putBoolean(IS_FIRST_TIME_LAUNCH, status);
    editor.commit();
  }

  public boolean isFirstLaunch() {
    return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
  }

}
