package com.hillelevo.cityelf.activities.setting_activity;

import static com.hillelevo.cityelf.Constants.TAG;

import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class ManyAddress extends AppCompatActivity {

  private SharedPreferences setting;
  private SharedPreferences.Editor prefEditor;
  private CheckBox checkBox1;
  private CheckBox checkBox2;
  private EditText address1;
  private EditText address2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_many_address);

    checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
    checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
    address1 = (EditText) findViewById(R.id.address1);
    address2 = (EditText) findViewById(R.id.address2);

    setting = PreferenceManager.getDefaultSharedPreferences(this);
    prefEditor = setting.edit();

    saveToSharedBool(checkBox1, Prefs.CHECK_NOTIFICATION_ADDRESS_1);
    saveToSharedBool(checkBox2, Prefs.CHECK_NOTIFICATION_ADDRESS_2);

    saveToSharedString(address1, Prefs.TEXT_NOTIFICATION_ADDRESS_1);
    saveToSharedString(address2, Prefs.TEXT_NOTIFICATION_ADDRESS_2);

    setupActionBar();


  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setHomeButtonEnabled(true);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }


  private void saveToSharedString(TextView object, final String keyObj) {
    Log.d(TAG, "MainActivity savedToSharedPrefs: " + object + ", " + keyObj);
    String s2 = setting.getString(keyObj, "");

    object.setText(setting.getString(keyObj, ""));

    object.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before,
          int count) {
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
          int after) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        prefEditor.putString(keyObj, s.toString()).commit();
      }
    });
  }


  private void saveToSharedBool(final CompoundButton object, final String keyObj) {
    Log.d(TAG, "MainActivity savedToSharedPrefs: " + object + ", " + keyObj);
    if (setting.contains(keyObj) && setting.getBoolean(keyObj, false)) {
      object.setChecked(true);
    } else {
      object.setChecked(false);

    }
    object.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (object.isChecked()) {
          prefEditor.putBoolean(keyObj, true);
          prefEditor.apply();
        } else {
          prefEditor.putBoolean(keyObj, false);
          prefEditor.apply();
        }
      }
    });

  }

}
