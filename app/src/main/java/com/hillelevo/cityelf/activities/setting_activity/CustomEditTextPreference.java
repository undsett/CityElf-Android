package com.hillelevo.cityelf.activities.setting_activity;

import com.hillelevo.cityelf.activities.MainActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class CustomEditTextPreference extends EditTextPreference {


  public CustomEditTextPreference(Context context) {
    super(context);
  }

  public CustomEditTextPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    if (which == DialogInterface.BUTTON_POSITIVE) {
      String s = MainActivity.loadStringFromSharedPRefs("email");
      String s2 = this.getText();
      int a = 2;
      a = +2;


    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
      int a = 2;
    }
    super.onClick(dialog, which);
  }

}