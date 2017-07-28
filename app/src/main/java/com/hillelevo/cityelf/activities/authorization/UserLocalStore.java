package com.hillelevo.cityelf.activities.authorization;


import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

  public static final String SP_NAME = "userDetails";
  private SharedPreferences userLocalDatabase;

  public UserLocalStore(Context context) {
    userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
  }

  public void storeToken(String token){
    SharedPreferences.Editor spEditor = userLocalDatabase.edit();
    spEditor.putString("firebase_id", token);
    spEditor.commit();
  }

  public void storeAddress(String address){
    SharedPreferences.Editor spEditor = userLocalDatabase.edit();
    spEditor.putString("address", address);
    spEditor.commit();
  }

  public void storeUserData(User user) {
    SharedPreferences.Editor spEditor = userLocalDatabase.edit();
    spEditor.putString("firebase_id", user.getFirebaseId());
    spEditor.putString("email", user.getEmail());
    spEditor.putInt("phone", user.getPhone());
    spEditor.putString("address", user.getAddress());
    spEditor.putString("password", user.getPassword());
    spEditor.commit();
  }

  public User getLoggedInUser() {
    String firebaseId = userLocalDatabase.getString("firebase_id", "");
    String email = userLocalDatabase.getString("email", "");
    int phone = userLocalDatabase.getInt("phone", 0);
    String address = userLocalDatabase.getString("address", "");
    String password = userLocalDatabase.getString("password", "");

    User storedUser = new User(firebaseId, email, phone, address, password);

    return storedUser;
  }

  public String getStoredToken(){
    return userLocalDatabase.getString("firebase_id", "");
  }

  public String getStoredAddress(){
    return userLocalDatabase.getString("address", "");
  }

  public void setUserLoggedIn(Boolean loggedIn) {
    SharedPreferences.Editor spEditor = userLocalDatabase.edit();
    spEditor.putBoolean("loggedIn", loggedIn);
    spEditor.commit();
  }

  public boolean getUserLoggedIn() {
    if (userLocalDatabase.getBoolean("loggedIn", false) == true) {
      return true;
    } else {
      return false;
    }
  }

  public void clearUserData() {
    SharedPreferences.Editor spEditor = userLocalDatabase.edit();
    spEditor.clear();
    spEditor.commit();
  }
}
