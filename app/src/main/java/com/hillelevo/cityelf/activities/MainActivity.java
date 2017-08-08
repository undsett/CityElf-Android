package com.hillelevo.cityelf.activities;

import static com.hillelevo.cityelf.Constants.TAG;

import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.Actions;
import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.Constants.WebUrls;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.TimeUtils;
import com.hillelevo.cityelf.activities.map_activity.MapActivity;
import com.hillelevo.cityelf.activities.setting_activity.SettingsActivity;
import com.hillelevo.cityelf.data.Advert;
import com.hillelevo.cityelf.data.Notification;
import com.hillelevo.cityelf.data.Poll;
import com.hillelevo.cityelf.data.UserLocalStore;
import com.hillelevo.cityelf.fragments.AdvertFragment;
import com.hillelevo.cityelf.fragments.BottomDialogFragment;
import com.hillelevo.cityelf.fragments.NotificationFragment;
import com.hillelevo.cityelf.fragments.PollFragment;
import com.hillelevo.cityelf.webutils.JsonMessageTask;
import com.hillelevo.cityelf.webutils.JsonMessageTask.JsonMessageResponse;
import com.hillelevo.cityelf.fragments.BottomDialogFragment.OnDialogReportClickListener;
import com.hillelevo.cityelf.webutils.AdvertsTask;
import com.hillelevo.cityelf.webutils.AdvertsTask.AdvertsResponse;
import com.hillelevo.cityelf.webutils.PoolsTask;
import com.hillelevo.cityelf.webutils.PoolsTask.PoolsResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity implements JsonMessageResponse,
    OnDialogReportClickListener,
    AdvertsResponse, PoolsResponse {

  //  private boolean anonymous;
  private boolean registered;
  private boolean osmd_admin;
  private boolean active;
  private String address;
  private CustomPagerAdapter pagerAdapter;
  private ArrayList<Notification> notifications = new ArrayList<>();
  private ArrayList<Advert> adverts = new ArrayList<>();
  private ArrayList<Poll> polls = new ArrayList<>();

  private TabLayout tabLayout;
  private TextView emptyNotification;

  private JSONObject jsonObject = null;


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

    address = UserLocalStore
        .loadStringFromSharedPrefs(getApplicationContext(), Prefs.ADDRESS_1);

    emptyNotification = (TextView) findViewById(R.id.empty_notification);
    emptyNotification.setVisibility(View.GONE);
    // Check intent, send AddNewUser request to server
    Intent intent = getIntent();

    if (intent.hasExtra("AddUser") && !UserLocalStore
        .loadBooleanFromSharedPrefs(getApplicationContext(),
            Prefs.ANOMYMOUS)) {

      Log.d(TAG, "onCreate: AddUser request sent");
      UserLocalStore.saveBooleanToSharedPrefs(getApplicationContext(), Prefs.ANOMYMOUS, true);
      UserLocalStore.saveBooleanToSharedPrefs(getApplicationContext(), Prefs.NOT_FIRST_START, true);

      //TODO Send AddNewUser request to server

      startAddNewUserResponse();

      String firebseId = UserLocalStore
          .loadStringFromSharedPrefs(getApplicationContext(), Prefs.FIREBASE_ID);

      String bodyParams = "firebaseid=" + firebseId + "&address=" + address;

//      new JsonMessageTask(MainActivity.this)
//          .execute(WebUrls.ADD_NEW_USER, Constants.POST, bodyParams);
    }

    else if (intent.hasExtra("CheckAnotherAddress")) {
      address = UserLocalStore
          .loadStringFromSharedPrefs(getApplicationContext(), Prefs.ADDRESS_FOR_CHECK);
    }

    if (!UserLocalStore
        .loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.NOT_FIRST_START)) {
      Intent firstStart = new Intent(MainActivity.this, MapActivity.class);
      firstStart.putExtra("firstStart", true);
      startActivity(firstStart);
      finish();
    }

    showLoadingAlertDialog();

    // Load registered status from Shared Prefs

//    anonymous = UserLocalStore
//        .loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.ANOMYMOUS);

    registered = UserLocalStore
        .loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.REGISTERED);
    osmd_admin = UserLocalStore
        .loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.OSMD_ADMIN);

    Button buttonReport = (Button) findViewById(R.id.buttonReport);

    // Fill ViewPager with data
    startForecastsResponse();

    if (UserLocalStore.loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.REGISTERED)) {
      startGetAllAdverts();
      startGetAllPools();
    }
    ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
    pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
    pager.setAdapter(pagerAdapter);

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
  }

  /**
   * Click on ReportDialog Report button
   *
   * @param type type of event: 0 - Electricity, 1 - Gas, 2 - Water
   * @param addressNumber address of event
   */
  @Override
  public void onDialogReportClick(String type, int addressNumber) {
    sendReportToServer(type, addressNumber);
  }

  @Override
  public void onDialogLoginClick() {
    if (UserLocalStore.loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.ANOMYMOUS)) {
      Intent intent = new Intent(MainActivity.this, AuthorizationActivity.class);
      startActivity(intent);
    } else {
      Intent intentMap = new Intent(MainActivity.this, MapActivity.class);
      startActivity(intentMap);
      Toast.makeText(this, "Пожалуйста введите свой адрес чтобы продолжить", Toast.LENGTH_SHORT).show();
    }
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
        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
        startActivity(intent);
        return true;
      case R.id.settings:

        //// TODO: 17.07.17 This step depends from status-registred
        Intent intentLogin = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intentLogin);

        return true;
      case R.id.btnMap:
        Intent intentMap = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intentMap);
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
      String token = intent.getStringExtra(Prefs.FIREBASE_ID);
      Log.d(TAG, "MainActivity onReceive: " + action);
      Log.d(TAG, "MainActivity onReceive: " + token);
      if (active) {
        // Can show debug alert dialog
      }
    }
  };

  // ProgressDialog for loading data

  private ProgressDialog progressDialog;

  private void showLoadingAlertDialog() {
    progressDialog = ProgressDialog.show(MainActivity.this, "", "Загрузка данных...", true);
  }


  /**
   * Custom Adapter for ViewPager
   */
  private class CustomPagerAdapter extends FragmentPagerAdapter {

    public CustomPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public int getItemPosition(Object object) {
      return POSITION_NONE;
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

  private void startAddNewUserResponse() {
    String firebseId = UserLocalStore
        .loadStringFromSharedPrefs(getApplicationContext(), Prefs.FIREBASE_ID);
    String address = UserLocalStore
        .loadStringFromSharedPrefs(getApplicationContext(), Prefs.ADDRESS_1);
    String bodyParams = "firebaseid=" + firebseId + "&address=" + address;

    new JsonMessageTask(MainActivity.this)
        .execute(WebUrls.ADD_NEW_USER, Constants.POST, bodyParams);
  }

  private void startForecastsResponse() {
    if (!UserLocalStore
        .loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.NOT_FIRST_START)) {
//      UserLocalStore.saveStringToSharedPrefs(getApplicationContext(), Prefs.ADDRESS_1, null);
    } else {
//      String address = UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(),
//          Prefs.ADDRESS_1);
      showMessage(address);
      try {
        new JsonMessageTask(this)
            .execute(WebUrls.GET_ALL_FORECASTS + URLEncoder.encode(address, "UTF-8"),
                Constants.GET);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
  }

  private void startGetAllAdverts() {
    //for test 2341
    long addressId = UserLocalStore
        .loadIntFromSharedPrefs(getApplicationContext(), Prefs.ADDRESS_1_ID);
    new AdvertsTask(this)
        .execute(WebUrls.GET_ALL_ADVERTS + addressId,
            Constants.GET);
  }


  private void startGetAllPools() {
    String address = UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(),
        Prefs.ADDRESS_1);
    try {
      new PoolsTask(this)
          .execute(WebUrls.GET_ALL_ADVERTS + URLEncoder.encode(address, "UTF-8"),
              Constants.GET);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  //message from JsonMessageTask
  @Override
  public void messageResponse(String output) {

    if (output.contains("Error")) {
      showMessage(output);
    }
    if (!UserLocalStore
        .loadBooleanFromSharedPrefs(getApplicationContext(), Prefs.NOT_FIRST_START)) {
      try {
        jsonObject = new JSONObject(output);
        Log.d(TAG, "messageResponse: " + jsonObject.toString());
        int userId = jsonObject.getInt("id");
        UserLocalStore.saveIntToSharedPrefs(this.getApplicationContext(), Prefs.USER_ID,
            userId);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      String msg = Integer.toString(UserLocalStore.loadIntFromSharedPrefs(getApplicationContext(),
          Prefs.USER_ID));
      showMessage(msg);
//      UserLocalStore.saveBooleanToSharedPrefs(getApplicationContext(), Prefs.ANOMYMOUS, true);
//      UserLocalStore.saveBooleanToSharedPrefs(getApplicationContext(), Prefs.NOT_FIRST_START, true);
      startForecastsResponse();
    } else {
      showMessage(output);
      fillData(output);
    }
  }

  //message from PoolsTask
  @Override
  public void poolsResponse(String output) {

  }

  //message from AdvertsTask
  @Override
  public void advertsResponse(String output) {

   /* try {
      JSONObject jsn = new JSONObject(output);
    } catch (JSONException e) {
      e.printStackTrace();
    }*/
    showMessage(output);
    fillData(output);
  }


  public void showMessage(String message) {
    /*Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    toast.show();*/
    Log.d(TAG, "showMessage: " + message);
  }

  // Hardcoded method to fill up test Notifications, Adverts and Polls
  private void fillData(String message) {
    progressDialog.dismiss();
    JSONObject addressJsonObject = null;
    String title = null;
    String start = null;
    String estimatedStop = null;
    String address = null;
//    int count = 0;

    if (message == null || message.isEmpty() || message.equals("{}")) {
      showMessage("По Вашему адресу нет запланированных отключений");
      emptyNotification.setVisibility(View.VISIBLE);

    } else {
      try {
        jsonObject = new JSONObject(message);
        Log.d(TAG, "fillData: jsonObject length " + jsonObject.length());

        for (int count = 0; count < jsonObject.length(); count++) {

          if (jsonObject.has("Water")) {
            Log.d(TAG, "fillData: getJSONObject(\"Water\") != null");

            JSONObject waterJsonObject = jsonObject.getJSONObject("Water");
            title = "Отключение воды";
            start = waterJsonObject.getString("start");
            estimatedStop = waterJsonObject.getString("estimatedStop");

            addressJsonObject = waterJsonObject.getJSONObject("address");
            address = addressJsonObject.getString("address");
            notifications
                .add(new Notification(
                    title,
                    address,
                    TimeUtils
                        .getDuration(TimeUtils.getTime(start), TimeUtils.getTime(estimatedStop)),
                    TimeUtils.getDate(start),
                    "", 0));
//            count++;
          } else if (jsonObject.has("Gas")) {
            Log.d(TAG, "fillData: getJSONObject(\"Gas\") != null");
            JSONObject gasJsonObject = jsonObject.getJSONObject("Gas");
            title = "Отключение газа";
            start = gasJsonObject.getString("start");
            estimatedStop = gasJsonObject.getString("estimatedStop");

            addressJsonObject = gasJsonObject.getJSONObject("address");
            address = addressJsonObject.getString("address");
            notifications
                .add(new Notification(
                    title,
                    address,
                    TimeUtils
                        .getDuration(TimeUtils.getTime(start), TimeUtils.getTime(estimatedStop)),
                    TimeUtils.getDate(start),
                    "", 0));
//            count++;
          } else if (jsonObject.has("Electricity")) {
            Log.d(TAG, "fillData: getJSONObject(\"Electricity\") != null");

            JSONObject electricityJsonObject = jsonObject.getJSONObject("Electricity");

            title = "Отключение света";
            start = electricityJsonObject.getString("start");
            estimatedStop = electricityJsonObject.getString("estimatedStop");

            addressJsonObject = electricityJsonObject.getJSONObject("address");
            address = addressJsonObject.getString("address");
            notifications
                .add(new Notification(
                    title,
                    address,
                    TimeUtils
                        .getDuration(TimeUtils.getTime(start), TimeUtils.getTime(estimatedStop)),
                    TimeUtils.getDate(start),
                    "", 0));
//            count++;
          }
        }

        // Add new data to ViewPager
        pagerAdapter.notifyDataSetChanged();
        setupTabs();
//        count++;

      } catch (JSONException e1) {
        e1.printStackTrace();
      }

//      adverts.add(new Advert("Объявление 1", "Тестовая улица, 1", "сегодня",
//          "Тестовый опрос тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест "));
//
//      polls.add(new Poll("Опрос 1", "Тестовая улица, 1", "2 часа", "сегодня",
//          "Тестовый опрос тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест тест тест тест тест тест тест тест "
//              + "тест тест тест тест тест тест тест ", "Вариант 1", "Вариант 2",
//          "Вариант 3", "Вариант 4", 10));
//
    }


  }

  //TODO Change addressStreet to address ID!
  private void sendReportToServer(String type, int addressNumber) {
    Log.d(TAG, "sendReportToServer: address Number " + addressNumber);

    int addressId = 0;

    switch (addressNumber) {
      case 0:
        addressId = UserLocalStore
            .loadIntFromSharedPrefs(getApplicationContext(), Prefs.ADDRESS_1_ID);
        break;
      //Add more options
    }

    JSONObject request = new JSONObject();
    try {
      JSONObject shutdown = new JSONObject();

      shutdown.put("forecastType", type);
      shutdown.put("start", getSystemTime());
      JSONObject address = new JSONObject();
      address.put("id", addressId);
      shutdown.put("address", address);

      request.put("userId", UserLocalStore
          .loadIntFromSharedPrefs(getApplicationContext(), Prefs.USER_ID));

      request.put("shutdownReport", shutdown);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    String s = UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.EMAIL);
    String s2 = UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.PASSWORD);
    String report = request.toString();
    new JsonMessageTask(this).execute(WebUrls.USER_REPORT_SHUTDOWN, Constants.POST, report,
        UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.EMAIL),
        UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.PASSWORD));
  }

  public String getSystemTime() {
    Date cal = (Date) Calendar.getInstance().getTime();
    //2017-08-03T16:49:00
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String systemTime = formatter.format(cal).replace(" ", "T");

    return systemTime;
  }

  /**
   * Set up tabs for ViewPager
   */
  private void setupTabs() {
    TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.view_pager_tab, null);
    tabOne.setText(R.string.tab_notifications_title);
    tabLayout.getTabAt(0).setCustomView(tabOne);
    if (!registered) {
      tabLayout.setSelectedTabIndicatorColor(00000000);
    }

    if (registered) {
      TextView tabTwo = (TextView) LayoutInflater.from(this)
          .inflate(R.layout.view_pager_tab, null);
      tabTwo.setText(R.string.tab_adverts_title);
      tabLayout.getTabAt(1).setCustomView(tabTwo);

      TextView tabThree = (TextView) LayoutInflater.from(this)
          .inflate(R.layout.view_pager_tab, null);
      tabThree.setText(R.string.tab_polls_title);
      tabLayout.getTabAt(2).setCustomView(tabThree);
    }
  }
}