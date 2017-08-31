package com.hillelevo.cityelf.activities.map_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

public class InternetConnection {

  public static boolean isConnect(Context context) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }

  public static void showWarningDialog(Context context) {
    AlertDialog.Builder errorDialog = new AlertDialog.Builder(context);
    errorDialog.setTitle("Внимание")
        .setMessage("Проверьте интернет подключение.")
        .setPositiveButton("Ok", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
          }
        });
    errorDialog.show();
  }

}
