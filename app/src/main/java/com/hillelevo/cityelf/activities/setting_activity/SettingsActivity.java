package com.hillelevo.cityelf.activities.setting_activity;


import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.MainActivity;
import com.hillelevo.cityelf.activities.authorization.UserLocalStore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements
    OnPreferenceChangeListener, OnSharedPreferenceChangeListener {

  private SwitchPreference notificationSwitch;
  private SwitchPreference notificationSMS;
  private ListPreference languagePref;
  private EditTextPreference addressPref;
  private EditTextPreference emailPref;
  private Preference exit;
  private RingtonePreference ringtonePref;

  private SharedPreferences sharedPreferences;

  private AppCompatDelegate delegate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupActionBar();
    PreferenceManager prefMgr = getPreferenceManager();
    prefMgr.setSharedPreferencesName(Prefs.APP_PREFERENCES);
    prefMgr.setSharedPreferencesMode(Context.MODE_PRIVATE);

    addPreferencesFromResource(R.xml.preferences);

    sharedPreferences = prefMgr.getSharedPreferences();

    notificationSwitch = (SwitchPreference) findPreference("notificationPush");
    notificationSwitch.setOnPreferenceChangeListener(this);

    notificationSMS = (SwitchPreference) findPreference("notificationSms");
    notificationSMS.setOnPreferenceChangeListener(this);

    languagePref = (ListPreference) findPreference("languagePref");
    languagePref.setOnPreferenceChangeListener(this);

    addressPref = (EditTextPreference) findPreference("address");
    addressPref.setSummary(sharedPreferences.getString("address", ""));
    addressPref.setOnPreferenceChangeListener(this);

    emailPref = (EditTextPreference) findPreference("email");
    emailPref.setSummary(getShortAddress(sharedPreferences.getString("email", "")));
    emailPref.setOnPreferenceChangeListener(this);

    ringtonePref = (RingtonePreference) findPreference("ringtonePref");
    ringtonePref.setOnPreferenceChangeListener(this);

    exit = (Preference) findPreference("exit");
    exit.setOnPreferenceClickListener(new OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        UserLocalStore userLocalStore = new UserLocalStore(SettingsActivity.super.getBaseContext());
        userLocalStore.clearUserData();
        return false;
      }
    });


  }


  private void getToast(Object obj) {
    Toast toast = Toast.makeText(getApplicationContext(),
        String.valueOf(obj), Toast.LENGTH_SHORT);
    toast.show();
  }

  private void setupActionBar() {
    ActionBar actionBar = getDelegate().getSupportActionBar();
    if (actionBar != null) {
      actionBar.setHomeButtonEnabled(true);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }


  //btnBack home
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        startActivity(new Intent(this, MainActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  @Override
  protected void onResume() {
    super.onResume();
    // Set up a listener whenever a key changes
    getPreferenceScreen().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    // Unregister the listener whenever a key changes
    getPreferenceScreen().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object newValue) {
    switch (preference.getKey()) {
      case "notificationSms":
        //// TODO: 17.06.17 send sms status
      case "notificationPush":
        boolean isVibrateOn = (Boolean) newValue;
        getToast(isVibrateOn);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(80L);
        break;
      case "languagePref":
        Integer language = Integer.valueOf(String.valueOf(newValue));
        if (language == 1) {
          getToast("Русский");
        } else {
          getToast("Украинский");
        }
        break;
      case "address":
        addressPref.setSummary(addressPref.getText());
        break;
      case "email":
        emailPref.setSummary(getShortAddress(emailPref.getText()));
        break;
      }
    return true;
  }


  private AppCompatDelegate getDelegate() {
    if (delegate == null) {
      delegate = AppCompatDelegate.create(this, null);
    }
    return delegate;
  }


  private String getShortAddress(String address) {
    if (address.contains("@")) {

      StringBuilder shortAddress = new StringBuilder();
      String[] twoWords = address.split("@");

      shortAddress.append(firstWord(twoWords[0]));
      shortAddress.append('@').append(twoWords[1]);

      return shortAddress.toString();
    } else {
      if (address.equals("")) {
        return "";
      }
      Toast toast = Toast.makeText(this,
          "Некорректный email", Toast.LENGTH_LONG);
      toast.show();
      emailPref.setText("");
      return "";
    }

  }

  private static String firstWord(String firstPart) {
    char[] word = firstPart.toCharArray();
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < firstPart.length(); i++) {
      if (i < 2) {
        str.append(word[i]);
      } else {
        str.append('*');
      }
    }
    return str.toString();
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Preference pref = findPreference(key);
    if (pref instanceof EditTextPreference && !key.equals("password")) {
      EditTextPreference editTextPref = (EditTextPreference) pref;
      //// TODO: 27.07.17 send to server
      String s = ((EditTextPreference) pref).getText();

      if (key.equals("email")) {
        pref.setSummary(getShortAddress(s));
      } else if (key.equals("address")) {
        pref.setSummary(s);
      }
    }
  }
}