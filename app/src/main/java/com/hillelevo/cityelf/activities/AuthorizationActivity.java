package com.hillelevo.cityelf.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.fragments.auth_fragments.ForgotPasswordFragment;
import com.hillelevo.cityelf.fragments.auth_fragments.LoginFragment;
import com.hillelevo.cityelf.fragments.auth_fragments.LoginFragment.OnRegisteraitNewClickListener;
import com.hillelevo.cityelf.data.UserLocalStore;
import com.hillelevo.cityelf.fragments.auth_fragments.LoginFragment.OnRestorePasswordNewClickListener;
import com.hillelevo.cityelf.fragments.auth_fragments.RegistrationFragment;


public class AuthorizationActivity extends FragmentActivity implements
    OnRegisteraitNewClickListener, OnRestorePasswordNewClickListener {

  TextView textView;

  FragmentManager fragmentManager;
  FragmentTransaction fragmentTransaction;

  LoginFragment loginFragment;
  RegistrationFragment registrationFragment;
  ForgotPasswordFragment forgotPasswordFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authorization);

    textView = (TextView) findViewById(R.id.tvRegisteraitUser);
    textView = (TextView) findViewById(R.id.tvRestorePassword);

    fragmentManager = getSupportFragmentManager();
    fragmentTransaction = fragmentManager.beginTransaction();
    loginFragment = new LoginFragment();
    fragmentTransaction.add(R.id.fragment_container, loginFragment);
    fragmentTransaction.commit();

  }

  @Override
  public void onRegistraitClick() {
    fragmentManager = getSupportFragmentManager();
    fragmentTransaction = fragmentManager.beginTransaction();
    registrationFragment = new RegistrationFragment();
    fragmentTransaction.replace(R.id.fragment_container, registrationFragment);
    fragmentTransaction.commit();
  }

  @Override
  public void onRestorePasswordClick() {
    fragmentManager = getSupportFragmentManager();
    fragmentTransaction = fragmentManager.beginTransaction();
    forgotPasswordFragment = new ForgotPasswordFragment();
    fragmentTransaction.replace(R.id.fragment_container, forgotPasswordFragment);
    fragmentTransaction.commit();
  }

//  private void displayUserDetails() {
//    String message =
//        UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.EMAIL)
//            + UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.ADDRESS_1);
//    Toast toast = Toast.makeText(AuthorizationActivity.this, message, Toast.LENGTH_LONG);
//    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//    toast.show();
//  }

}