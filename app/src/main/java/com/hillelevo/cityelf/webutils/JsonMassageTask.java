package com.hillelevo.cityelf.webutils;

import android.os.AsyncTask;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;

import com.hillelevo.cityelf.activities.MainActivity;
import com.hillelevo.cityelf.activities.map_activity.MapActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonMassageTask extends AsyncTask<String, Void, String> {
  String returnClass;

  @RequiresApi(api = VERSION_CODES.LOLLIPOP)
  @Override
  protected String doInBackground(String... params) {
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    StringBuffer buffer = new StringBuffer();
    returnClass = params[1] ;

    try {
      URL url = new URL(params[0]);
      connection = (HttpURLConnection) url.openConnection();
      connection.connect();

      InputStream stream = connection.getInputStream();

      reader = new BufferedReader(new InputStreamReader(stream));

      String line = "";

      while ((line = reader.readLine()) != null) {
        buffer.append(line);
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return buffer.toString();

  }


  @RequiresApi(api = VERSION_CODES.LOLLIPOP)
  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);

    switch (returnClass){
      case "MainActivity":
        MainActivity.receiveResult(result);
        break;
      case "MapActivity":
        MapActivity.receiveResult(result);
        break;

    }
  }

}