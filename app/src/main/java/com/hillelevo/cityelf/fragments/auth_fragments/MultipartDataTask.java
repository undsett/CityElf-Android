package com.hillelevo.cityelf.fragments.auth_fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.data.UserLocalStore;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MultipartDataTask extends AsyncTask<String, Void, Void> {

  //  private final ProgressDialog dialog = new ProgressDialog(getAplthis);
  // can use UI thread here
//  protected void onPreExecute() {
//    this.dialog.setMessage("Loading...");
//    this.dialog.setCancelable(false);
//    this.dialog.show();
//  }
  @Override
  protected Void doInBackground(String... params) {
    // TODO Auto-generated method stub
    HttpURLConnection connection = null;
    DataOutputStream outputStream = null;
    DataInputStream inputStream = null;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1 * 1024 * 1024;

    try {
      //------------------ CLIENT REQUEST
      FileInputStream fileInputStream = new FileInputStream(new File(params[2]));
      // open a URL connection to the Servlet
      URL url = new URL(params[0]);
      // Open a HTTP connection to the URL
      connection = (HttpURLConnection) url.openConnection();

      connection.setRequestProperty(Prefs.AUTH, params[3]);
      // Allow Inputs
      connection.setDoInput(true);
      // Allow Outputs
      connection.setDoOutput(true);
      // Don't use a cached copy.
      connection.setUseCaches(false);
      // Use a post method.
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Connection", "Keep-Alive");
      connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
      outputStream = new DataOutputStream(connection.getOutputStream());

      outputStream.writeBytes(params[4]);


      bytesAvailable = fileInputStream.available();
      bufferSize = Math.min(bytesAvailable, maxBufferSize);
      buffer = new byte[bufferSize];
      bytesRead = fileInputStream.read(buffer, 0, bufferSize);
      while (bytesRead > 0) {
        outputStream.write(buffer, 0, bufferSize);
        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
      }

      int responseCode = connection.getResponseCode();

//      outputStream.writeBytes(lineEnd);
      outputStream.writeBytes(twoHyphens + boundary + twoHyphens);

      responseCode = connection.getResponseCode();
      fileInputStream.close();
      outputStream.flush();
      outputStream.close();
      responseCode = connection.getResponseCode();
    } catch (MalformedURLException ex) {
      Log.e("Debug", "error: " + ex.getMessage(), ex);
    } catch (IOException ioe) {
      Log.e("Debug", "error: " + ioe.getMessage(), ioe);
    }
    //------------------ read the SERVER RESPONSE
    try {
      inputStream = new DataInputStream(connection.getInputStream());
      String str;
      while ((str = inputStream.readLine()) != null) {
        Log.e("Debug", "Server Response " + str);
//        reponse_data = str;
      }
      inputStream.close();
    } catch (IOException ioex) {
      Log.e("Debug", "error: " + ioex.getMessage(), ioex);
    }
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {

//    if (this.dialog.isShowing()) {
//      this.dialog.dismiss();
  }
}
