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
import com.hillelevo.cityelf.fragments.auth_fragments.LoginFragment;
import com.hillelevo.cityelf.fragments.auth_fragments.LoginFragment.OnRegistrationClickListener;
import com.hillelevo.cityelf.data.UserLocalStore;
import com.hillelevo.cityelf.fragments.auth_fragments.RegistrationFragment;


public class AuthorizationActivity extends FragmentActivity implements
    OnRegistrationClickListener {

  TextView textView;

  FragmentManager fragmentManager;
  FragmentTransaction fragmentTransaction;

  LoginFragment loginFragment;
  RegistrationFragment registrationFragment;
  UserLocalStore userLocalStore;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authorization);

    textView = (TextView) findViewById(R.id.tvRegisteraitUser);

    fragmentManager = getSupportFragmentManager();
    loginFragment = new LoginFragment();
    fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations(0, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
    fragmentTransaction.replace(R.id.fragment_container, loginFragment, "LoginFragment");
//    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();

//    userLocalStore = new UserLocalStore(this);
  }

  @Override
  public void onRegistrationClick() {
    registrationFragment = new RegistrationFragment();
    fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations(0, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
    fragmentTransaction.replace(R.id.fragment_container, registrationFragment, "RegistrationFragment");
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }

//  @Override
//  protected void onStart(){
//    super.onStart();
//
//    if(authenticate() == true){
//      displayUserDetails();
//    }else{
//      startActivity(new Intent(AuthorizationActivity.this, LoginFragment.class));
//    }
//  }

  private void displayUserDetails() {
    String message =
        UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.EMAIL)
            + UserLocalStore.loadStringFromSharedPrefs(getApplicationContext(), Prefs.ADDRESS_1);
    Toast toast = Toast.makeText(AuthorizationActivity.this, message, Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    toast.show();
  }

  @Override
  public void onBackPressed() {
    if(fragmentManager.getBackStackEntryCount() > 0) {
      fragmentManager.popBackStack();
    }
    else {
      super.onBackPressed();
    }
  }

  //  private boolean authenticate() {
//    return userLocalStore.getUserLoggedIn();
//  }
}