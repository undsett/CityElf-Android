package com.hillelevo.cityelf.webutils;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonMassageTask extends AsyncTask<String, Void, String> {
  String returnClass;

  private JsonMassageResponse response = null;

  public JsonMassageTask(JsonMassageResponse listener) {
    response = listener;
  }


  @Override
  protected String doInBackground(String... params) {
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    StringBuffer buffer = new StringBuffer();
    returnClass = params[1];


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
    if (buffer == null) {

      return null;
    } else {
      return buffer.toString();
    }
  }

  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);

    switch (returnClass){
        case "MainActivity":
          if (response != null)
          response.massageResponse(result);
          break;
        case "MapActivity":
          if (response != null)
            response.massageResponse(result);
          break;

    }
  }
  public interface JsonMassageResponse {
    void massageResponse(String output);
  }
}


