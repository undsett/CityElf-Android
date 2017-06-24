package com.hillelevo.cityelf.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.Actions;
import com.hillelevo.cityelf.Constants.Params;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.map_activity.MapActivity;
import com.hillelevo.cityelf.webutils.JsonMassageTask;

import static com.hillelevo.cityelf.Constants.TAG;

public class MainActivity extends AppCompatActivity {

  private static String result;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

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
        new JsonMassageTask().execute(Constants.SEND_MASSAGE_URL);
        Toast toast = Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
        return true;

      case R.id.map_test:
        Intent intentMap = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intentMap);
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
    }
  };

  public static void reciveResult(String output){
    result = output;
  }
}