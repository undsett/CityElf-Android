package com.hillelevo.cityelf.activities;

import static com.hillelevo.cityelf.Constants.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.astuetz.PagerSlidingTabStrip;
import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.Actions;
import com.hillelevo.cityelf.Constants.Params;
import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.map_activity.MapActivity;
import com.hillelevo.cityelf.activities.setting_activity.SettingsActivity;
import com.hillelevo.cityelf.fragments.BottomDialogFragment;
import com.hillelevo.cityelf.webutils.JsonMassageTask;
import com.hillelevo.cityelf.webutils.JsonMassageTask.JsonMassageResponse;

public class MainActivity extends AppCompatActivity implements JsonMassageResponse {

  private SharedPreferences settings;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button buttonReport = (Button) findViewById(R.id.buttonReport);

    // Show Report dialog - BottomDialogFragment
    buttonReport.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
            .beginTransaction();
        android.support.v4.app.Fragment prev = getSupportFragmentManager()
            .findFragmentByTag("dialog");
        if (prev != null) {
          ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        BottomDialogFragment newFragment = BottomDialogFragment
            .newInstance(loadRegisteredStatusFromSharedPrefs());
        newFragment.show(ft, "dialog");
      }

    });


    // Get the ViewPager and set it's PagerAdapter so that it can display items
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
    viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));

    // Give the PagerSlidingTabStrip the ViewPager
    PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
    // Attach the view pager to the tab strip
    tabsStrip.setViewPager(viewPager);

    // Create LocalBroadcastManager and register it to all actions;
    LocalBroadcastManager messageBroadcastManager = LocalBroadcastManager.getInstance(this);
    messageBroadcastManager.registerReceiver(MessageReceiver,
        new IntentFilter(Actions.BROADCAST_ACTION_FIREBASE_TOKEN));
    messageBroadcastManager.registerReceiver(MessageReceiver,
        new IntentFilter(Actions.BROADCAST_ACTION_FIREBASE_MESSAGE));

    settings = getSharedPreferences(Prefs.APP_PREFERENCES, Context.MODE_PRIVATE);

    // Add user registration status to Shared Prefs, HARDCODED!
    saveToSharedPrefs(Prefs.REGISTERED, true);
    //TODO Add real registration status

    // Add test address to Shared Prefs, HARDCODED!
    saveToSharedPrefs(Prefs.ADDRESS_1, "Test street, 1");
    saveToSharedPrefs(Prefs.ADDRESS_2, "Test street, 2");
    //TODO Add to Shared Prefs real address from Map Activity and Registration form
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.json_test:

        new JsonMassageTask(this).execute(Constants.TEST_URL);
        showMassage("Loading...");
        return true;

      case R.id.map_test:
        Intent intentMap = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intentMap);
        return true;

      case R.id.settings_test:
        Intent intentSetting = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intentSetting);
        return true;

      case R.id.action_enter:
        Intent intentLogin = new Intent(MainActivity.this, AuthorizationActivity.class);
        startActivity(intentLogin);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * BroadcastReceiver for local broadcasts
   */
  private BroadcastReceiver MessageReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {

      String action = intent.getAction();
      String token = intent.getStringExtra(Params.FIREBASE_TOKEN);
      Log.d(TAG, "MainActivity onReceive: " + action);
      Log.d(TAG, "MainActivity onReceive: " + token);
      showDebugAlertDialog(token);
    }
  };

  //massage from JsonMassageTask
  @Override
  public void massageResponse(String output) {
    showMassage(output);
  }

  public void showMassage(String massage) {
      Toast toast = Toast.makeText(MainActivity.this, massage, Toast.LENGTH_LONG);
      toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
      toast.show();
  }


  //Save and load data to Shared Prefs

  private void saveToSharedPrefs(String type, String data) {
    Log.d(TAG, "MainActivity savedToSharedPrefs: " + type + ", " + data);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(type, data);
    editor.apply();
  }

  private void saveToSharedPrefs(String type, boolean registered) {
    Log.d(TAG, "MainActivity savedToSharedPrefs: " + type + ", " + registered);
    SharedPreferences.Editor editor = settings.edit();
    editor.putBoolean(type, registered);
    editor.apply();
  }

  private boolean loadRegisteredStatusFromSharedPrefs() {
    //Check for data by id
    if (settings != null && settings.contains(Prefs.REGISTERED)) {
      Log.d(TAG, "MainActivity mSettings != null, loading registration status");
      return settings.getBoolean(Prefs.REGISTERED, false);
    } else {
      Log.d(TAG, "MainActivity mSettings != null, no registration status");
      return false;
    }
  }

  // AlertDialog for firebase testing

  private void showDebugAlertDialog(String token) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Firebase id");

    // Set up the input
    final EditText input = new EditText(this);
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    input.setText(token.toCharArray(), 0, token.length());
    builder.setView(input);

// Set up the button
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });

    builder.show();
  }
}