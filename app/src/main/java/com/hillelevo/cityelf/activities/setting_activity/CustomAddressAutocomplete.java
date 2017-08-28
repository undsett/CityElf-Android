package com.hillelevo.cityelf.activities.setting_activity;

import static com.hillelevo.cityelf.Constants.TAG;

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
import com.hillelevo.cityelf.Constants.Actions;
import com.hillelevo.cityelf.Constants.Prefs;
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.map_activity.PlaceArrayAdapter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomAddressAutocomplete extends DialogPreference implements OnClickListener,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks, OnItemClickListener {

  private AutoCompleteTextView mAutocompleteTextView;
  private Button positiveButton;
  private ImageButton btnClear;
  private GoogleApiClient mGoogleApiClient;
  private static final int GOOGLE_API_CLIENT_ID = 0;
  private static final String LOG_TAG = "Google Places Autocomplete";
  private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
  private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
  private static final String OUT_JSON = "/json";

  private static final String API_KEY = "AIzaSyCvCVjPsoJyCifJNO9EtlJuBW53eQHPHpY";
  public static final LatLngBounds BOUNDS_VIEW = new LatLngBounds(
      new LatLng(46.325628, 30.677791), new LatLng(46.598067, 30.797954));
  private PlaceArrayAdapter mPlaceArrayAdapter;

  private String nameOfStreet;

  public CustomAddressAutocomplete(Context context, AttributeSet attrs) {
    super(context, attrs);
    setDialogLayoutResource(R.layout.layout_autocomplete_address);

  }

  @Override
  protected void showDialog(Bundle state) {
    super.showDialog(state);

    positiveButton = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
    positiveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (nameOfStreet.contains(", Одес")) {
          if (nameOfStreet != null) {
            sendLocalBroadcast(Actions.BROADCAST_ACTION_SETTING_ADDRESS, nameOfStreet);
          }
          getDialog().dismiss();
        } else {
          getToast(Constants.ERROR_INPUT_ADDRESS, Toast.LENGTH_SHORT);
        }
      }

    });
  }
  private void sendLocalBroadcast(String action, String date) {
    Intent localIntent = new Intent(action);
    localIntent.putExtra(Prefs.ADDRESS_1, date);

    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(localIntent);
  }
  @Override
  protected void onBindDialogView(View view) {
    mAutocompleteTextView = (AutoCompleteTextView) view
        .findViewById(R.id.autoCompleteTextViewSettings);
    mAutocompleteTextView
        .setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_item));
    mAutocompleteTextView.setOnItemClickListener(this);

    btnClear = (ImageButton) view.findViewById(R.id.btn_clear);
    btnClear.setVisibility(View.INVISIBLE);
    btnClear.setOnClickListener(this);

    registerConnectToGoogle();
    showHideImageBtnClearInputText(mAutocompleteTextView, btnClear);
    autocompleteInputStreet(mAutocompleteTextView);

    super.onBindDialogView(view);
  }

  public void registerConnectToGoogle() {
    mGoogleApiClient = new GoogleApiClient.Builder(getContext())
        .addApi(Places.GEO_DATA_API)
        .addApi(Places.PLACE_DETECTION_API)
        .addConnectionCallbacks(this)
        .build();

    mGoogleApiClient.connect();
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_clear:
        mAutocompleteTextView.setText("");
        btnClear.setVisibility(View.INVISIBLE);
        break;
    }
  }

  private void showHideImageBtnClearInputText(EditText inputText, final ImageButton button) {
    inputText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence inputText, int i, int i1, int i2) {
        if (inputText.toString().trim().length() == 0) {
          button.setVisibility(View.INVISIBLE);
        } else {
          button.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
  }


  public void autocompleteInputStreet(AutoCompleteTextView mAutocompleteTextView) {
    mAutocompleteTextView.setThreshold(3);
    mAutocompleteTextView
        .setOnItemClickListener(mAutocompleteClickListener);
    AutocompleteFilter filter = new AutocompleteFilter.Builder()
        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
        .setCountry("UA")
        .build();//country filter
    mPlaceArrayAdapter = new PlaceArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
        BOUNDS_VIEW, filter);
    mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
  }

  private AdapterView.OnItemClickListener mAutocompleteClickListener
      = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
      final String placeId = String.valueOf(item.placeId);
      Log.d(TAG, "Selected: " + item.description);

      nameOfStreet = String.valueOf(item.description);
      mAutocompleteTextView.setText(shortAddress(nameOfStreet) + " ");
      mAutocompleteTextView.setSelection(mAutocompleteTextView.getText().length());

      PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
          .getPlaceById(mGoogleApiClient, placeId);
      placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
      Log.d(TAG, "Fetching details for ID: " + item.placeId);
    }
  };

  private static ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
      = new ResultCallback<PlaceBuffer>() {
    @Override
    public void onResult(PlaceBuffer places) {
      if (!places.getStatus().isSuccess()) {
        Log.d(TAG, "Place query did not complete. Error: " +
            places.getStatus().toString());
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
    Log.d(TAG, "Google Places API connected in Settings.");
  }

  @Override
  public void onConnectionSuspended(int i) {
    mPlaceArrayAdapter.setGoogleApiClient(null);
    Log.d(TAG, "Google Places API connection suspended.");
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.d(TAG, "Google Places API connection failed with error code: "
        + connectionResult.getErrorCode());
  }

  public static ArrayList autocomplete(String input) {
    ArrayList resultList = null;

    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    try {
      StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
      sb.append("?key=" + API_KEY);
      sb.append("&components=country:gr");
      sb.append("&input=" + URLEncoder.encode(input, "utf8"));

      URL url = new URL(sb.toString());
      conn = (HttpURLConnection) url.openConnection();
      InputStreamReader in = new InputStreamReader(conn.getInputStream());

      // Load the results into a StringBuilder
      int read;
      char[] buff = new char[1024];
      while ((read = in.read(buff)) != -1) {
        jsonResults.append(buff, 0, read);
      }
    } catch (MalformedURLException e) {
      return resultList;
    } catch (IOException e) {
      return resultList;
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }

    try {
      // Create a JSON object hierarchy from the results
      JSONObject jsonObj = new JSONObject(jsonResults.toString());
      JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

      // Extract the Place descriptions from the results
      resultList = new ArrayList(predsJsonArray.length());
      for (int i = 0; i < predsJsonArray.length(); i++) {
        resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
      }
    } catch (JSONException e) {
      Log.e(LOG_TAG, "Cannot process JSON results", e);
    }

    return resultList;
  }

  @Override
  public void onItemClick(AdapterView adapterView, View view, int position, long id) {
    String str = (String) adapterView.getItemAtPosition(position);
    Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
  }


  class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {

    private ArrayList resultList;

    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
      super(context, textViewResourceId);
    }

    public int getCount() {
      return resultList.size();
    }

    public String getItem(int index) {
      return (String) resultList.get(index);
    }

    public Filter getFilter() {
      Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
          FilterResults filterResults = new FilterResults();
          if (constraint != null) {
            // Retrieve the autocomplete results.
            resultList = autocomplete(constraint.toString());

            // Assign the data to the FilterResults
            filterResults.values = resultList;
            filterResults.count = resultList.size();
          }
          return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
          if (results != null && results.count > 0) {
            //notifyDataSetChanged();
          } else {
            //notifyDataSetInvalidated();
          }
        }
      };
      return filter;
    }
  }

  public  CharSequence shortAddress(String userAddress) {
    if (getVerificationCity(userAddress)) {
      return userAddress.substring(0, userAddress.indexOf(", Одес"));
    } else {
      getToast(Constants.ERROR_INPUT_ADDRESS, Toast.LENGTH_SHORT);
      return userAddress;
    }
  }

  public static boolean getVerificationCity(String street) {
    return street.contains("Одеса") || street.contains("Одесса");
  }

  private void getToast(Object obj, int length) {
    Toast toast = Toast.makeText(getContext(),
        String.valueOf(obj), length);
    toast.show();
  }
}
