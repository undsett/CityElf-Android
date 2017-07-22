package com.hillelevo.cityelf.activities.authorization;


import android.util.Base64;

public class User {

  private String firebaseId;
  private String email, address, password, authCertificate;
  private int phone;

  public User(String firebaseId, String email, int phone, String address, String password) {
    this.firebaseId = firebaseId;
    this.email = email;
    this.phone = phone;
    this.address = address;
    this.password = password;

  }

  public User(String firebaseId, String email, String password) {
    this.firebaseId = firebaseId;
    this.email = email;
    this.phone = 0;
    this.address = null;
    this.password = password;
    this.authCertificate = "Basic " + Base64
        .encodeToString((email + ":" + password).getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
  }

  public User(String firebaseId) {
    this.firebaseId = firebaseId;
    this.email = null;
    this.phone = 0;
    this.address = null;
    this.password = null;
    this.authCertificate = "Basic " + Base64
        .encodeToString((email + ":" + password).getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
  }

  public String getFirebaseId() {
    return firebaseId;
  }

  public String getEmail() {
    return email;
  }

  public String getAddress() {
    return address;
  }

  public String getPassword() {
    return password;
  }

  public String getAuthCertificate() {
    return authCertificate;
  }

  public int getPhone() {
    return phone;
  }
}
