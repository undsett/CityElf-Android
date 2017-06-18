package com.hillelevo.cityelf;

public class Constants {
  public static final String TAG = "CityElfLog";

  public static final String SEND_MASSAGE_URL = "http://localhost:8088/services/users/all";
  public static final String TEST_URL = "https://learn.javascript.ru/json";

  public class Actions {
    public static final String BROADCAST_ACTION_FIREBASE_TOKEN = "FirebaseTokenAction";
    public static final String BROADCAST_ACTION_FIREBASE_MESSAGE = "FirebaseMessageAction";
  }

  public class Params {
    public static final String FIREBASE_TOKEN = "FirebaseToken";
    public static final String FIREBASE_MESSAGE = "FirebaseMessage";
  }

  public class Prefs {
    public static final String APP_PREFERENCES = "CityElfPrefs";
    public static final String REGISTERED = "Registered";
    public static final String ADDRESS_1 = "Address1";
    public static final String ADDRESS_2 = "Address2";
    public static final String ADDRESS_3 = "Address3";
    public static final String ADDRESS = "Address";
    public static final int MAX_ADDRESS_QUANTITY = 3;
  }
}
