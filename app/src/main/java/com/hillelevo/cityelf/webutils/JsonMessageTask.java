package com.hillelevo.cityelf.webutils;

import static com.hillelevo.cityelf.Constants.TAG;

import android.os.AsyncTask;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.WebUrls;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class JsonMessageTask extends AsyncTask<String, Void, String> {

  private JsonMessageResponse response = null;

  public JsonMessageTask(JsonMessageResponse listener) {
    response = listener;
  }


  @RequiresApi(api = VERSION_CODES.LOLLIPOP)
  @Override
  protected String doInBackground(String... params) {
    URL url = null;

    HttpURLConnection connection = null;
    OutputStream outputStream = null;
    BufferedReader reader = null;
    StringBuffer buffer = new StringBuffer();

    try {
      if (params[1] != null && params[1].equals("POST")) {

        try {
          url = new URL(params[0]);
          connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod("POST");
          connection.setDoInput(true);
          connection.setDoOutput(true);

//          connection.setConnectTimeout(1000 * 15);
//          connection.setReadTimeout(50000);

//          String bodyParams = "email=" + params[2] + "&password=" + params[3];
          String bodyParams = params[2];


//          connection.setRequestProperty(Constants.AUTH, params[2]);

          outputStream = connection.getOutputStream();
          outputStream.write(bodyParams.getBytes());
          outputStream.close();

          int responseCode = connection.getResponseCode();

          Log.d(TAG, "Response Code is: " + responseCode);

          if (responseCode != 200) {

          }
        } catch (MalformedURLException e) {
          e.printStackTrace();
        } catch (ProtocolException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else if (params[1] != null && params[1].equals("GET")) {
        url = new URL(params[0]);
        connection = (HttpURLConnection) url.openConnection();

//        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpsURLConnection.HTTP_OK) {
          Log.d(TAG, "Response Code is: " + responseCode);
        }

      }

      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      buffer = new StringBuffer();

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
    response.messageResponse(result);
  }

  public interface JsonMessageResponse {

    void messageResponse(String output);
  }

}
