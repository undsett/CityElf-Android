package com.hillelevo.cityelf.activities;

import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.map_activity.MapActivity;
import com.hillelevo.cityelf.data.UserLocalStore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setTheme(R.style.SplashTheme);

    if (!UserLocalStore
        .loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.NOT_FIRST_START)) {
      Intent firstStart = new Intent(this, MapActivity.class);
      firstStart.putExtra("firstStart", true);
      startActivity(firstStart);
    }
    else {
      Intent notFirstStart = new Intent(this, MainActivity.class);
      notFirstStart.putExtra("firstStart", false);
      startActivity(notFirstStart);
    }
    finish();
  }
}
