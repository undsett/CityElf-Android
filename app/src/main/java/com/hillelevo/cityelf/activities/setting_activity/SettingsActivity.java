package com.hillelevo.cityelf.activities.setting_activity;


import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements
    OnPreferenceChangeListener {

  SwitchPreference notificationSwitch;
  SwitchPreference notificationSMS;
  ListPreference listPreference;
  EditTextPreference addressPref;
  EditTextPreference emailPref;

  SharedPreferences sharedPreferences;
  android.app.FragmentManager manager;

  private AppCompatDelegate delegate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupActionBar();
    addPreferencesFromResource(R.xml.preferences);

    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    notificationSwitch = (SwitchPreference) findPreference("notificationPush");
    notificationSwitch.setOnPreferenceChangeListener(this);

    notificationSMS = (SwitchPreference) findPreference("notificationSms");
    notificationSMS.setOnPreferenceChangeListener(this);

    listPreference = (ListPreference) findPreference("languagePref");
    listPreference.setOnPreferenceChangeListener(this);

    addressPref = (EditTextPreference) findPreference("streetPref");
    addressPref.setOnPreferenceChangeListener(this);

    emailPref = (EditTextPreference) findPreference("emailPref");
    emailPref.setOnPreferenceChangeListener(this);

    manager = getFragmentManager();

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
      case "streetPref":
        addressPref.setSummary(addressPref.getText());
        break;
      case "emailPref":
        emailPref.setSummary(getShortAddress(emailPref.getText()));
        break;
      case "manyAddressPref":
        Intent intentMap = new Intent(SettingsActivity.this, ManyAddress
            .class);
        startActivity(intentMap);
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


  private static String getShortAddress(String address) {
    StringBuilder shortAddress = new StringBuilder();
    String[] twoWords = address.split("@");

    shortAddress.append(firstWord(twoWords[0]));
    shortAddress.append('@').append(twoWords[1]);

    return shortAddress.toString();
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
}
