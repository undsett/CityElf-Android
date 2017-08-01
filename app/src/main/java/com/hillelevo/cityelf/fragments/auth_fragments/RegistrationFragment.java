package com.hillelevo.cityelf.fragments.auth_fragments;

import static com.hillelevo.cityelf.Constants.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.hillelevo.cityelf.Constants;
import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.Constants.WebUrls;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.MainActivity;
import com.hillelevo.cityelf.activities.map_activity.PlaceArrayAdapter;
import com.hillelevo.cityelf.data.UserLocalStore;
import com.hillelevo.cityelf.webutils.JsonMessageTask;
import com.hillelevo.cityelf.webutils.JsonMessageTask.JsonMessageResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class RegistrationFragment extends Fragment implements JsonMessageResponse, OnClickListener,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks {

  EditText etEmail, etPhone, etAddress, etPassword;
  Button btnRegister;

  private String email = null;
  private int phone = 0;
  private String address = "";
  private String password = null;

//  UserLocalStore userLocalStore;
//  User registeredUser = new User(null, null, 0, null, null);

  private static final int GOOGLE_API_CLIENT_ID = 0;
  private View view;
  AutoCompleteTextView mAutocompleteTextView;
  private PlaceArrayAdapter mPlaceArrayAdapter;
  private static final LatLngBounds BOUNDS_VIEW = new LatLngBounds(
      new LatLng(46.325628, 30.677791), new LatLng(46.598067, 30.797954));
  private GoogleApiClient mGoogleApiClient;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_fragment_registration, container, false);

    etEmail = (EditText) view.findViewById(R.id.etEmail);
    etPhone = (EditText) view.findViewById(R.id.etPhone);
    etAddress = (EditText) view.findViewById(R.id.etAddress);
    inflater.inflate(R.layout.layout_fragment_registration, container, false);
    etPassword = (EditText) view.findViewById(R.id.etPassword);
    btnRegister = (Button) view.findViewById(R.id.btnRegister);

    btnRegister.setOnClickListener(this);

//    userLocalStore = new UserLocalStore(getContext());
    return view;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnRegister:

        email = etEmail.getText().toString();
        if (etPhone.getText().length() != 0) {
          phone = Integer.parseInt(etPhone.getText().toString());
        } else {
          phone = 0;
        }
        if (etAddress.getText().length() != 0) {
          address = etAddress.getText().toString();
        } else {
          address = UserLocalStore.loadStringFromSharedPrefs(getActivity().getApplicationContext(),
              Prefs.ADDRESS_1);
        }
        password = etPassword.getText().toString();

        if (email.equals("")) {
          Toast.makeText(getContext(), "Введите email", Toast.LENGTH_SHORT).show();
          break;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
          Toast.makeText(getContext(), "Вы ввели неправильный email", Toast.LENGTH_SHORT).show();
          break;
        } else if (phone != 0) {
          String checkPhone = Integer.toString(phone);
          if (!Patterns.PHONE.matcher(checkPhone).matches()){
            Toast.makeText(getContext(), "Вы ввели неправильный номер телефона", Toast.LENGTH_SHORT).show();
            break;
          }
        } else if (address.equals("")) {
          Toast.makeText(getContext(), "Введите адрес", Toast.LENGTH_SHORT).show();
          break;
        } else if (password.equals("")) {
          Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
          break;
        } else if (password.length() < 4) {
          Toast.makeText(getContext(), "Пароль должен содержать больше четырех символов",
              Toast.LENGTH_SHORT).show();
          break;
        } else {
          Toast.makeText(getContext(), "FIREBASE ID IS " + UserLocalStore.loadStringFromSharedPrefs(
              getActivity().getApplicationContext(), Prefs.FIREBASE_ID),
              Toast.LENGTH_SHORT).show();

//          "firebaseid=" + UserLocalStore.loadStringFromSharedPRefs(getActivity()
//              .getApplicationContext(), Prefs.FIREBASE_ID)

          String bodyParams =
              "firebaseid=" + "Web" + "&email=" + email +
                  "&password=" + password;
          new JsonMessageTask(RegistrationFragment.this)
              .execute(WebUrls.REGISTRATION_URL, Constants.POST, bodyParams);
          break;
        }
    }
  }

  @Override
  public void messageResponse(String output) {
    if (output == null || output.isEmpty()) {
      showMessage("Registration failed");
//      registeredUser = null;
    } else {
      try {
        JSONObject jsonObject = new JSONObject(output);
        if (jsonObject != null) {
          int code = jsonObject.getInt("code");
          String message = jsonObject.getString("message");

          showMessage(message + code);

          if (code == 11 && message.equals("User registration OK")) {

//            registeredUser = new User(userLocalStore.getStoredToken(), email, phone, address,
//                password);

            authenticate(email, address, password);
            showMessage(message);

          } else {
            showMessage(message);
//            registeredUser = null;
          }
        } else {
          showMessage("Registration failed");
        }


      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  private void authenticate(String email, String address, String password) {

    Log.d(TAG, email + " Registered");

    UserLocalStore.saveStringToSharedPrefs(getActivity().getApplicationContext(), Prefs.EMAIL,
        email);
    UserLocalStore.saveStringToSharedPrefs(getActivity().getApplicationContext(), Prefs.ADDRESS_1,
        address);
    UserLocalStore.saveStringToSharedPrefs(getActivity().getApplicationContext(), Prefs.PASSWORD,
        password);
    UserLocalStore.saveBooleanToSharedPrefs(getActivity().getApplicationContext(), Prefs.REGISTERED,
        true);

    showMessage("На Ваш email выслана ссылка для подтверждения регистрации.");

    Intent intent = new Intent(getContext(), MainActivity.class);
    RegistrationFragment.this.startActivity(intent);
  }

  private void showMessage(String massage) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
    dialogBuilder.setMessage(massage);
    dialogBuilder.setPositiveButton("Ok", null);
    dialogBuilder.show();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    view = getView();
    mAutocompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.etAddress);

    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
        .addApi(Places.GEO_DATA_API)
        .addApi(Places.PLACE_DETECTION_API)
        .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
        .addConnectionCallbacks(this)
        .build();
    mGoogleApiClient.connect();

    autocompleteInputStreet(mAutocompleteTextView);

    return;
  }


  public void autocompleteInputStreet(AutoCompleteTextView mAutocompleteTextView) {
    mAutocompleteTextView.setThreshold(3);
    mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
    AutocompleteFilter filter = new AutocompleteFilter.Builder()
        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
        .setCountry("UA")
        .build();//country filter
    mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
        BOUNDS_VIEW, filter);
    mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
  }

  private AdapterView.OnItemClickListener mAutocompleteClickListener
      = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
      final String placeId = String.valueOf(item.placeId);

      String nameOfStreet = String.valueOf(item.description);

      if (getVerificationCity(nameOfStreet)) {
        mAutocompleteTextView.setText(shortAddress(nameOfStreet) + " ");
        mAutocompleteTextView.setSelection(mAutocompleteTextView.getText().length());
      } else {
        Toast toast = Toast.makeText(RegistrationFragment.super.getContext(),
            "Возможно этот адрес не находится в Одессе", Toast.LENGTH_LONG);
        toast.show();
        mAutocompleteTextView.setText("");
      }

      PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
          .getPlaceById(mGoogleApiClient, placeId);
      placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
    }
  };

  private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
      = new ResultCallback<PlaceBuffer>() {
    @Override
    public void onResult(PlaceBuffer places) {
      if (!places.getStatus().isSuccess()) {
        places.getStatus().toString();
        return;
      }
      // Selecting the first object buffer.
      final Place place = places.get(0);
      CharSequence attributions = places.getAttributions();
    }
  };

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }


  public boolean getVerificationCity(String street) {
    return street.contains("Одеса") || street.contains("Одесса");
  }

  private CharSequence shortAddress(String userAddress) {
    if (getVerificationCity(userAddress)) {
      return userAddress.substring(0, userAddress.indexOf(", Одес"));
    } else {
      Toast toast = Toast.makeText(this.getContext(),
          "Возможно этот адрес не находится в Одессе", Toast.LENGTH_LONG);
      toast.show();
      return userAddress;
    }
  }
}

