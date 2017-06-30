package com.hillelevo.cityelf.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.LoginFragment.OnRegisterNewClickListener;


public class AuthorizationActivity extends FragmentActivity implements OnRegisterNewClickListener {

  TextView textView;

  FragmentManager fragmentManager;
  FragmentTransaction fragmentTransaction;

  LoginFragment loginFragment;
  RegistrationFragment registrationFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authorization);

    textView = (TextView) findViewById(R.id.registerait_user);

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
}