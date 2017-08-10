package com.hillelevo.cityelf.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.data.UserLocalStore;
import com.hillelevo.cityelf.fragments.auth_fragments.ForgotPasswordFragment;
import com.hillelevo.cityelf.fragments.auth_fragments.LoginFragment;
import com.hillelevo.cityelf.fragments.auth_fragments.LoginFragment.OnRegistrationClickListener;
import com.hillelevo.cityelf.fragments.auth_fragments.LoginFragment.OnRestorePasswordClickListener;
import com.hillelevo.cityelf.fragments.auth_fragments.RegistrationFragment;


public class AuthorizationActivity extends FragmentActivity implements
    OnRegistrationClickListener, OnRestorePasswordClickListener {


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
    loginFragment = new LoginFragment();
    fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations(0, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
    fragmentTransaction.replace(R.id.fragment_container, loginFragment, "LoginFragment");
//    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();

  }

  @Override
  public void onRegistrationClick() {
    registrationFragment = new RegistrationFragment();
    fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations(0, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
    fragmentTransaction
        .replace(R.id.fragment_container, registrationFragment, "RegistrationFragment");
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }

  @Override
  public void onRestorePasswordClick() {
    forgotPasswordFragment = new ForgotPasswordFragment();
    fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations(0, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
    fragmentTransaction
        .replace(R.id.fragment_container, forgotPasswordFragment, "ForgotPasswordFragment");
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }

  @Override
  public void onBackPressed() {
    if (fragmentManager.getBackStackEntryCount() > 0) {
      fragmentManager.popBackStack();
    } else {
      super.onBackPressed();
    }
  }
}