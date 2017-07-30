package com.hillelevo.cityelf.activities;

import static com.hillelevo.cityelf.Constants.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.Actions;
import com.hillelevo.cityelf.Constants.Params;
import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.Constants.WebUrls;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.authorization.AuthorizationActivity;
import com.hillelevo.cityelf.activities.authorization.UserLocalStore;
import com.hillelevo.cityelf.activities.map_activity.MapActivity;
import com.hillelevo.cityelf.activities.setting_activity.SettingsActivity;
import com.hillelevo.cityelf.data.Advert;
import com.hillelevo.cityelf.data.Notification;
import com.hillelevo.cityelf.data.Poll;
import com.hillelevo.cityelf.fragments.AdvertFragment;
import com.hillelevo.cityelf.fragments.BottomDialogFragment;
import com.hillelevo.cityelf.fragments.NotificationFragment;
import com.hillelevo.cityelf.fragments.PollFragment;
import com.hillelevo.cityelf.webutils.JsonMessageTask;
import com.hillelevo.cityelf.webutils.JsonMessageTask.JsonMessageResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements JsonMessageResponse {

  private static String result;
  private boolean registered;
  private boolean osmd_admin;
  private boolean active;
  UserLocalStore userLocalStore = null;
  private ArrayList<Notification> notifications = new ArrayList<>();
  private ArrayList<Advert> adverts = new ArrayList<>();
  private ArrayList<Poll> polls = new ArrayList<>();

  private TabLayout tabLayout;

  private static SharedPreferences settings;
  private FirstStartApp firstStartApp;

  @Override
  protected void onResume() {
    super.onResume();
    active = true;
  }

  @Override
  protected void onPause() {
    super.onPause();
    active = false;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    firstStartApp = new FirstStartApp(this);
    settings = getSharedPreferences(Prefs.APP_PREFERENCES, Context.MODE_PRIVATE);

    if (firstStartApp.isFirstLaunch()) {
      launchFirstTime();
      finish();

    }

    //settings = getSharedPreferences(Prefs.APP_PREFERENCES, Context.MODE_PRIVATE);
    // Add user registration status to Shared Prefs, HARDCODED!
    saveToSharedPrefs(Prefs.REGISTERED, false);

    saveToSharedPrefs(Prefs.OSMD_ADMIN, false);

    //TODO Add real registration status

    // Load registered status from Shared Prefs
    registered = loadBooleanStatusFromSharedPrefs(Prefs.REGISTERED);
    osmd_admin = loadBooleanStatusFromSharedPrefs(Prefs.OSMD_ADMIN);

    Button buttonReport = (Button) findViewById(R.id.buttonReport);

    // Fill ViewPager with data

//    startJsonResponse();
    // TODO !!! Replace test Notifications, Adverts and Polls with real ones from server
    // Generate test Notifications, Adverts and Polls
//    fillTestData();
    ViewPager pager = (ViewPager) findViewById(R.id.viewpager);

    pager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

    // Set custom tabs for ViewPager
    tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(pager);
    setupTabs();

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
            .newInstance(registered);
        newFragment.show(ft, "dialog");
      }

    });

    // Create LocalBroadcastManager and register it to all actions;
    LocalBroadcastManager messageBroadcastManager = LocalBroadcastManager.getInstance(this);
    messageBroadcastManager.registerReceiver(MessageReceiver,
        new IntentFilter(Actions.BROADCAST_ACTION_FIREBASE_TOKEN));
    messageBroadcastManager.registerReceiver(MessageReceiver,
        new IntentFilter(Actions.BROADCAST_ACTION_FIREBASE_MESSAGE));

    // Add test address to Shared Prefs, HARDCODED!
    saveToSharedPrefs(Prefs.ADDRESS_1, "Test street, 1");
    saveToSharedPrefs(Prefs.ADDRESS_2, "Test street, 2");
    //TODO Add to Shared Prefs real address from Map Activity and Registration form
  }

  private void launchFirstTime() {
    firstStartApp.setFirstLaunch(false);
    Intent firstStart = new Intent(MainActivity.this, MapActivity.class);
    startActivity(firstStart);
    finish();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (osmd_admin) {
      getMenuInflater().inflate(R.menu.menu2, menu);
    } else {
      getMenuInflater().inflate(R.menu.menu, menu);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.addPoll:

        return true;
      case R.id.action_enter:

        //// TODO: 17.07.17 This step depends from status-registred

        if (registered) {
          Intent intentLogin = new Intent(MainActivity.this, SettingsActivity.class);
          startActivity(intentLogin);
        } else {
          Intent intentLogin = new Intent(MainActivity.this, AuthorizationActivity.class);
          startActivity(intentLogin);
        }
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private String getB64Auth(String login, String pass) {
    String source = login + ":" + pass;
    String ret =
        "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
    return ret;
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
      if (active) {
        showDebugAlertDialog(token);
      }
    }
  };

  //Save and load data to Shared Prefs

  public static void saveToSharedPrefs(String type, String data) {
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

  public static String loadStringFromSharedPRefs(String prefKey) {
    if (settings != null && settings.contains(prefKey)) {
      Log.d(TAG, "MainActivity mSettings != null, loading registration status");
      return settings.getString(prefKey, "");
    } else {
      Log.d(TAG, "MainActivity mSettings != null, no registration status");
      return "";
    }
  }

  public static boolean loadBooleanStatusFromSharedPrefs(String prefKey) {
    //Check for data by id
    if (settings != null && settings.contains(prefKey)) {
      Log.d(TAG, "MainActivity mSettings != null, loading registration status");
      return settings.getBoolean(prefKey, true);
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

  /**
   * Custom Adapter for ViewPager
   */
  private class CustomPagerAdapter extends FragmentPagerAdapter {

    public CustomPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
      switch (pos) {
        case 0:
          return NotificationFragment.newInstance(notifications);
        case 1:
          return AdvertFragment.newInstance(adverts);
        case 2:
          return PollFragment.newInstance(polls);
        default:
          return NotificationFragment.newInstance(notifications);
      }
    }

    /**
     * Tabs in ViewPager
     *
     * @return tabs amount
     */
    @Override
    public int getCount() {
      // Registered user has 3 tabs
      if (registered) {
        return 3;
      }
      // Unregistered - one tab, Notifications
      else {
        return 1;
      }
    }
  }

  private void startJsonResponse() {
    if (firstStartApp.isFirstLaunch()) {
      userLocalStore.storeAddress(null);
    } else {
      String address = userLocalStore.getStoredAddress();
      try {
        new JsonMessageTask(this)
            .execute(WebUrls.GET_ALL_FORECASTS + URLEncoder.encode(address, "UTF-8"),
                Constants.GET);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
  }

  //message from JsonMessageTask
  @Override
  public void messageResponse(String output) {
    showMessage(output);
    fillTestData(output);
  }

  public void showMessage(String message) {
    Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    toast.show();
  }

  // Hardcoded method to fill up test Notifications, Adverts and Polls
  private void fillTestData(String message) {
    JSONObject jsonObject = null;
    JSONObject addressJsonObject = null;
    String title = null;
    String start = null;
    String estimatedStop = null;
    String address = null;
    int count = 0;

    if (message == null || message.isEmpty()) {
      showMessage("No Forecast");
    } else {
      try {
        jsonObject = new JSONObject(message);

        while (count < jsonObject.length()) {

          if (jsonObject.getJSONObject("Water") != null) {

            JSONObject waterJsonObject = jsonObject.getJSONObject("Water");
            title = "Отключение воды";
            start = waterJsonObject.getString("start");
            estimatedStop = waterJsonObject.getString("estimatedStop");

            addressJsonObject = waterJsonObject.getJSONObject("address");
            address = addressJsonObject.getString("address");
            continue;
          } else if (jsonObject.getJSONObject("Gas") != null) {
            JSONObject gasJsonObject = jsonObject.getJSONObject("Gas");
            title = "Отключение газа";
            start = gasJsonObject.getString("start");
            estimatedStop = gasJsonObject.getString("estimatedStop");

            addressJsonObject = gasJsonObject.getJSONObject("address");
            address = addressJsonObject.getString("address");
            continue;
          } else if (jsonObject.getJSONObject("Electricity") != null) {

            JSONObject electricityJsonObject = jsonObject.getJSONObject("Electricity");

            title = "Отключение света";
            start = electricityJsonObject.getString("start");
            estimatedStop = electricityJsonObject.getString("estimatedStop");

            addressJsonObject = electricityJsonObject.getJSONObject("address");
            address = addressJsonObject.getString("address");
            continue;
          }

          count++;

          notifications
              .add(new Notification(title, "Тестовая улица, 1", "2 часа", start,
                  "Тестовый опрос тест тест тест тест тест тест тест тест тест тест тест "
                      + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
                      + "тест тест тест тест тест тест тест ", 0));
//      notifications.add(new Notification("Уведомление 2", "Тестовая улица, 1", "2 часа", "сегодня",
//          "Тестовый опрос тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест ", 0));
//      notifications.add(new Notification("Уведомление 3", "Тестовая улица, 1", "2 часа", "сегодня",
//          "Тестовое уведомление тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест ", 0));
//      adverts.add(new Advert("Объявление 1", "Тестовая улица, 1", "сегодня",
//          "Тестовый опрос тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест "));
//      adverts.add(new Advert("Объявление 2", "Тестовая улица, 1", "сегодня",
//          "Тестовое уведомление тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест "));
//      adverts.add(new Advert("Объявление 3", "Тестовая улица, 1", "сегодня",
//          "Тестовый опрос тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест "));
//      polls.add(new Poll("Опрос 1", "Тестовая улица, 1", "2 часа", "сегодня",
//          "Тестовый опрос тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест ", "Вариант 1", "Вариант 2",
//          "Вариант 3", "Вариант 4", 10));
//      polls.add(new Poll("Опрос 2", "Тестовая улица, 1", "2 часа", "сегодня",
//          "Тестовый опрос тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест ", "Вариант 1", "Вариант 2",
//          "Вариант 3", "", 30));
//      polls.add(new Poll("Опрос 3", "Тестовая улица, 1", "2 часа", "сегодня",
//          "Тестовый опрос тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест ", "Вариант 1", "Вариант 2",
//          "", "", 20));
        }

      } catch (JSONException e) {
        e.printStackTrace();
      }

    }
  }

  /**
   * Set up tabs for ViewPager
   */
  private void setupTabs() {
    TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.view_pager_tab, null);
    tabOne.setText(R.string.tab_notifications_title);
    tabLayout.getTabAt(0).setCustomView(tabOne);

    if (registered) {
      TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.view_pager_tab, null);
      tabTwo.setText(R.string.tab_adverts_title);
      tabLayout.getTabAt(1).setCustomView(tabTwo);

      TextView tabThree = (TextView) LayoutInflater.from(this)
          .inflate(R.layout.view_pager_tab, null);
      tabThree.setText(R.string.tab_polls_title);
      tabLayout.getTabAt(2).setCustomView(tabThree);
    }
  }
}