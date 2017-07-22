package com.hillelevo.cityelf;

public class Constants {
  public static final String TAG = "CityElfLog";

  public static  final String GET = "GET";
  public static  final String POST = "POST";
  public static final String AUTH = "Authorization";
  public static final int CONNECTION_TIMEOUT = 1000 * 15;

  public class WebUrls {
    public static final String SEND_MASSAGE_URL = "http://cityelf.com:8088/services/users/all";
    public static final String AUTHORIZATION_URL = "http://192.168.0.120:8088/services/registration/login";
    public static final String REGISTRATION_URL = "http://192.168.0.120:8088/services/registration/register";
    public static final String TEST_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=улица+Дерибасовская+Одесса+Одесская+область+Украина&key=AIzaSyCvCVjPsoJyCifJNO9EtlJuBW53eQHPHpY&language=ru";
    public static final String API_KEY_URL = "&key=AIzaSyCvCVjPsoJyCifJNO9EtlJuBW53eQHPHpY&language=ru";
    public static final String ADDRESS_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
  }

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
    public static final String CHECK_NOTIFICATION_ADDRESS_1 = "ChechAddressNotif1";
    public static final String CHECK_NOTIFICATION_ADDRESS_2 = "ChechAddressNotif2";
    public static final String TEXT_NOTIFICATION_ADDRESS_1 = "TextAddressNotif1";
    public static final String TEXT_NOTIFICATION_ADDRESS_2 = "TextAddressNotif2";

    public static final int MAX_ADDRESS_QUANTITY = 3;

  }
}
