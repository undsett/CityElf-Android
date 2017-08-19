package com.hillelevo.cityelf;

import android.support.annotation.ColorInt;

public class Constants {
  public static final String TAG = "CityElfLog";

  public static  final String GET = "GET";
  public static  final String POST = "POST";
  public static  final String PUT = "PUT";
  public static final int CONNECTION_TIMEOUT = 1000 * 15;
  public static final String RESPONSE_TO_A_REQUEST = "Ваш запрос отправлен. Мы рассмотрим его в течение 3 рабочих дней, о результате сообщим по электронной почте";
  public static final String ERROR_INPUT_ADDRESS = "Возможно этот адрес не находится в Одессе";

  public class WebUrls {

    public static final String GET_ALL_FORECASTS = "test:8080/services/allforecasts/get?address=";
    public static final String GET_ALL_ADVERTS = "test:8080/services/advertisements/getall?addressid=";
    public static final String GET_ALL_POOLS = "test:8080/services/polls/getall?addressid=";
    public static final String ADD_NEW_USER = "test:8080/services/registration/adduser";
    public static final String AUTHORIZATION_URL = "test:8080/services/registration/login";
    public static final String REGISTRATION_URL = "test:8080/services/registration/register";
    public static final String FORGOT_PASSWORD_URL = "test:8080/services/forgot/reset?email=";
    public static final String UPDATE_USER_URL = "test:8080/services/users/updateuser";
    public static final String USER_REPORT_SHUTDOWN = "test:8080/services/peoplereport/add";
    public static final String API_KEY_URL = "&key=AIzaSyCvCVjPsoJyCifJNO9EtlJuBW53eQHPHpY&language=ru";
    public static final String ADDRESS_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    public static final String COORDINATE_URL_START = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
    public static final String COORDINATE_URL_END = "&sensor=true\\";
    public static final String USER_UPLOAD_URL = "test:8080/services/users/upload";
    public static final String GET_USERDATA_URL = "test:8080/services/users/";
    public static final String ADD_NEW_ADVERTS_URL = "test:8080/services/advertisements/admin/addadvertisement";
    public static final String ADD_NEW_POLLS_URL = "test:8080/services/polls/admin/addpoll";
  }

  public class Actions {
    public static final String BROADCAST_ACTION_FIREBASE_TOKEN = "FirebaseTokenAction";
    public static final String BROADCAST_ACTION_FIREBASE_MESSAGE = "FirebaseMessageAction";
  }

  public class Colors{
    public static final String BLUE = "#2d9cdb";
    @ColorInt public static final String BLUE2 = "#2d9cdb";

  }

  public class Prefs {
    public static final String APP_PREFERENCES = "CityElfPrefs";
    public static final String NOT_FIRST_START = "NotFirstStart";
    public static final String ANOMYMOUS = "Anonymous";
    public static final String REGISTERED = "Registered";
    public static final String OSMD_ADMIN = "OsmdAdmin";
    public static final String AUTH = "Authorization";
    public static final String AUTH_CERTIFICATE = "authCertificate";
    public static final String FIREBASE_ID = "FirebaseId";
    public static final String USER_ID = "userId";
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    public static final String ADDRESS_1_ID = "Address1Id";
    public static final String ADDRESS_1 = "Address1";
    public static final String ADDRESS_2 = "Address2";
    public static final String ADDRESS_3 = "Address3";
    public static final String ADDRESS_4 = "Address4";
    public static final String ADDRESS_FOR_CHECK = "AddressForCheck";
    public static final String ADDRESS = "Address";
    public static final String CHECK_NOTIFICATION_ADDRESS_1 = "CheckAddressNotif1";
    public static final String CHECK_NOTIFICATION_ADDRESS_2 = "CheckAddressNotif2";
    public static final String TEXT_NOTIFICATION_ADDRESS_1 = "TextAddressNotif1";
    public static final String TEXT_NOTIFICATION_ADDRESS_2 = "TextAddressNotif2";
    public static final String RINGTONE = "ringtonePref";
    public static final String WATER = "Water";
    public static final String GAS = "Gas";
    public static final String ELECTRICITY = "Electricity";

    public static final int MAX_ADDRESS_QUANTITY = 4;
  }
}
