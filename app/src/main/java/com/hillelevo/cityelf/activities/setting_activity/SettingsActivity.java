package com.hillelevo.cityelf.activities.setting_activity;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.Constants.WebUrls;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.AuthorizationActivity;
import com.hillelevo.cityelf.activities.MainActivity;
import com.hillelevo.cityelf.activities.map_activity.MapActivity;
import com.hillelevo.cityelf.data.UserLocalStore;
import com.hillelevo.cityelf.webutils.JsonMessageTask;
import com.hillelevo.cityelf.webutils.JsonMessageTask.JsonMessageResponse;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends PreferenceActivity implements
    OnPreferenceChangeListener, OnSharedPreferenceChangeListener, JsonMessageResponse,
    OnPreferenceClickListener {

  private SwitchPreference notificationSwitch;
  private SwitchPreference notificationSMS;
  private ListPreference languagePref;
  private Preference addressPref;
  private EditTextPreference emailPref;
  private Preference exit;
  private RingtonePreference ringtonePref;
  private Preference pref;
  private Geocoder geocoder;
  private String userAddress;
  private Preference login;

  private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


  private String key;
  private String res = null;
  private boolean registered;

  private SharedPreferences sharedPreferences;

  private AppCompatDelegate delegate;
  private PreferenceCategory category;

  Preference pref2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupActionBar();
    PreferenceManager prefMgr = getPreferenceManager();
    prefMgr.setSharedPreferencesName(Prefs.APP_PREFERENCES);
    prefMgr.setSharedPreferencesMode(Context.MODE_PRIVATE);
    geocoder = new Geocoder(this, new Locale("ru", "RU"));

    //HARDCODE
    //UserLocalStore.saveBooleanToSharedPrefs(getApplicationContext(), Prefs.REGISTERED, true);

    addPreferencesFromResource(R.xml.preferences);
    registered = UserLocalStore
        .loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.REGISTERED);
    sharedPreferences = prefMgr.getSharedPreferences();
    category = (PreferenceCategory) findPreference("registered_user");

    if (!registered) {
      Preference logout1 = findPreference("email");
      category.removePreference(logout1);
      Preference logout2 = findPreference("password");
      category.removePreference(logout2);
      Preference logout3 = findPreference("address");
      category.removePreference(logout3);
      Preference logout4 = findPreference("manyAddressPref");
      category.removePreference(logout4);
      PreferenceCategory category2 = (PreferenceCategory) findPreference("aboutPref");
      Preference logout5 = findPreference("osmdReg");
      category2.removePreference(logout5);
      PreferenceScreen screen = getPreferenceScreen();
      Preference pref = getPreferenceManager().findPreference("exitCategory");
      screen.removePreference(pref);

      login = (Preference) findPreference("login");
      login.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          if (UserLocalStore.loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.ANOMYMOUS)) {
            Intent intent = new Intent(SettingsActivity.this, AuthorizationActivity.class);
            startActivity(intent);
          } else {
            Intent firstStart = new Intent(SettingsActivity.this, MapActivity.class);
            startActivity(firstStart);
            Toast.makeText(SettingsActivity.this, "Please enter your address first",
                Toast.LENGTH_SHORT).show();
          }
          return false;
        }
      });


    } else {
      Preference logout = findPreference("login");
      category.removePreference(logout);

      emailPref = (EditTextPreference) findPreference("email");
      emailPref.setSummary(getShortAddress(
          UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.EMAIL)));
      emailPref.setText(getShortAddress(
          (UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.EMAIL))));
      emailPref.setOnPreferenceChangeListener(this);

      addressPref = (Preference) findPreference("address");
      addressPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          try {
            AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setCountry("UA")
                .build();
            Intent intent =
                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setBoundsBias(MapActivity.BOUNDS_VIEW)
                    .setFilter(filter)
                    .build(SettingsActivity.this);

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
          } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
          }
          return false;
        }
      });
      addressPref.setSummary(getFormatedStreetName(
          UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.ADDRESS_1)));

      exit = (Preference) findPreference("exit");
      exit.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          UserLocalStore.clearUserData(getApplicationContext());
          return false;
        }
      });

    }

    notificationSwitch = (SwitchPreference) findPreference("notificationPush");
    notificationSwitch.setOnPreferenceChangeListener(this);

    notificationSMS = (SwitchPreference) findPreference("notificationSms");
    notificationSMS.setOnPreferenceChangeListener(this);

    languagePref = (ListPreference) findPreference("languagePref");
    languagePref.setOnPreferenceChangeListener(this);

    ringtonePref = (RingtonePreference) findPreference("ringtonePref");
    ringtonePref.setOnPreferenceChangeListener(this);

  }


  private void getToast(Object obj, int length) {
    Toast toast = Toast.makeText(getApplicationContext(),
        String.valueOf(obj), length);
    toast.show();
  }

  private void setupActionBar() {
    ActionBar actionBar = getDelegate().getSupportActionBar();
    if (actionBar != null) {
      actionBar.setHomeButtonEnabled(true);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        Place place = PlaceAutocomplete.getPlace(this, data);

        userAddress = sendGeo(place.getLatLng());
        if (userAddress.contains(", Одес")) {
          addressPref.setSummary(getFormatedStreetName(userAddress));
          // send userUpdate address
          updateUserAddress(userAddress);
          UserLocalStore
              .saveStringToSharedPrefs(getApplicationContext(), Prefs.ADDRESS_1, userAddress);
          Log.d(Constants.TAG, "Place: " + place.getName());
        } else {
          getToast(Constants.ERROR_INPUT_ADDRESS, Toast.LENGTH_LONG);
        }
      } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
        Status status = PlaceAutocomplete.getStatus(this, data);
        //  Handle the error.
        Log.d(Constants.TAG, status.getStatusMessage());

      } else if (resultCode == RESULT_CANCELED) {
        // The user canceled the operation.
      }
    }

  }

  private void updateUserAddress(String address) {
    JSONObject updatePreferenceObject = new JSONObject();
    try {
      updatePreferenceObject.put("id", 13);//hardcode
      updatePreferenceObject.put("phone", "09364646464");

      JSONObject newAddress = new JSONObject();
      newAddress.put("address", address);
      newAddress.put("addressUa", address);
      JSONArray array = new JSONArray();
      array.put(newAddress);

      updatePreferenceObject.put("addresses", array);
      updatePreferenceObject.put("phone", "09364646464");

    } catch (JSONException e) {
      e.printStackTrace();
    }
    String jsonData = updatePreferenceObject.toString();

    new JsonMessageTask(SettingsActivity.this).execute(WebUrls.UPDATE_USER_URL, "PUT", jsonData,
        UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.EMAIL),
        UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.PASSWORD));
  }

  private String sendGeo(LatLng coordinate) {
    List<Address> addresses = new ArrayList<>();
    try {
      addresses = geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 4);
    } catch (IOException e) {
      e.printStackTrace();
    }

    android.location.Address address = addresses.get(0);

    String str = address.getAddressLine(0);
    return address.getAddressLine(0);
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
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(80L);
        break;
      case "languagePref":
        Integer language = Integer.valueOf(String.valueOf(newValue));
        if (language == 1) {
          getToast("Русский", Toast.LENGTH_SHORT);
        } else {
          getToast("Украинский", Toast.LENGTH_SHORT);
        }
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

  public static String getFormatedStreetName(String userAddress) {
    if (userAddress != null && !userAddress.equals("")) {
      if (userAddress.contains(", Одес")) {
        return userAddress.substring(0, userAddress.indexOf(", Одес"));
      } else {
        return userAddress;
      }
    } else {
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
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String _key) {
    pref = findPreference(_key);
    key = _key;

    if (pref instanceof EditTextPreference && !key.equals("password")) {
      EditTextPreference editTextPref = (EditTextPreference) pref;
      //// TODO: 27.07.17 send to server
      JSONObject updatePreferenceObject = new JSONObject();
/*
      try {
        // HARDCODED!
        updatePreferenceObject.put("id", "13");
//        updatePreferenceObject.put("phone", "0975555555");

//        updatePreferenceObject.put("id", UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(),
//        Prefs.USER_ID));
        updatePreferenceObject.put(key, editTextPref.getText());

      } catch (JSONException e) {
        e.printStackTrace();
      }
      String jsonData = updatePreferenceObject.toString();

<<<<<<< HEAD
      new JsonMessageTask(SettingsActivity.this).execute(WebUrls.UPDATE_USER_URL, Constants.PUT, jsonData);

      if (res.isEmpty()) {
        String s = ((EditTextPreference) pref).getText();
        if (key.equals("email")) {
          pref.setSummary(getShortAddress(s));
        } else if (key.equals("address")) {
          pref.setSummary(s);
        }
=======
      new JsonMessageTask(SettingsActivity.this).execute(WebUrls.UPDATE_USER_URL, "PUT", jsonData);
*/
      String s = ((EditTextPreference) pref).getText();
      if (key.equals("email")) {
        pref.setSummary(getShortAddress(s));
      } else if (key.equals("address")) {
        pref.setSummary(s);

      }

    }
  }

  @Override

  public void messageResponse(String output) {
    res = output;

    if (output.isEmpty()) {
//   TODO
    }
  }

  @Override
  public boolean onPreferenceClick(Preference preference) {

    return false;
  }
}