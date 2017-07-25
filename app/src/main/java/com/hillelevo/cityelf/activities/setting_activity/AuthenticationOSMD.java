package com.hillelevo.cityelf.activities.setting_activity;

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
import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.activities.map_activity.PlaceArrayAdapter;
import com.hillelevo.cityelf.webutils.JsonMessageTask.JsonMessageResponse;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

public class AuthenticationOSMD extends AppCompatActivity implements JsonMessageResponse,
    View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks {

  private Button btnAddDocument;
  private Button btnSendRequest;
  ImageView imageView;

  private TextView imageName;
  private EditText userName;
  private AutoCompleteTextView mAutocompleteTextView;
  private PlaceArrayAdapter mPlaceArrayAdapter;
  private static final LatLngBounds BOUNDS_VIEW = new LatLngBounds(
      new LatLng(46.325628, 30.677791), new LatLng(46.598067, 30.797954));
  private static final int GOOGLE_API_CLIENT_ID = 0;
  private GoogleApiClient mGoogleApiClient;
  private static final String LOG_TAG = "OSMD_Activity";

  private String filePath;
  private String mailFrom;
  private String address;
  private String name;
  private String imageFile1;
  private String imageFile2;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authentication_osmd);
    setupActionBar();

    btnAddDocument = (Button) findViewById(R.id.btn_add_document);
    btnAddDocument.setOnClickListener(this);
    btnSendRequest = (Button) findViewById(R.id.btn_send_request);
    btnSendRequest.setOnClickListener(this);
    imageName = (TextView) findViewById(R.id.image_name);
    userName = (EditText) findViewById(R.id.fistNameLastName);

    //delete this
    imageView = (ImageView) findViewById(R.id.imageView);

    registerConnectToGoogle();
    mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.addressAdministation);
    autocompleteInputStreet(mAutocompleteTextView);


  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void setupActionBar() {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setHomeButtonEnabled(true);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  public void registerConnectToGoogle() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Places.GEO_DATA_API)
        .addApi(Places.PLACE_DETECTION_API)
        .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
        .addConnectionCallbacks(this)
        .build();
    mGoogleApiClient.connect();
  }


  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_add_document:
        addDocument();
        break;
      case R.id.btn_send_request:
        address = String.valueOf(mAutocompleteTextView.getText());
        name = String.valueOf(userName.getText());

        if (!address.isEmpty() && !name.isEmpty() && filePath != null && filePath != "") {
          /* todo SEND REQUEST ON THIS FRAGMENT
          * use variable: address, name, mailFrom (get from sharedPreference), file
          */

          Toast toast = Toast.makeText(getApplicationContext(),
              Constants.RESPONSE_TO_A_REQUEST, Toast.LENGTH_LONG);
          toast.show();
          mAutocompleteTextView.setText("");
          userName.setText("");
          filePath = "";
          imageName.setText("");
        }
        break;
    }

  }

  private void addDocument() {
    //open gallery
    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
    photoPickerIntent.setType("image/*");
    startActivityForResult(photoPickerIntent, 1);

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1) {
      if (resultCode == Activity.RESULT_OK) {
        Uri selectedImage = data.getData();

        imageFile1 = String.valueOf(selectedImage);

        filePath = getPath(selectedImage);
        String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
        String nameFile = filePath.substring(filePath.lastIndexOf("/"), filePath.length());
        imageName.setText(nameFile);

        if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg")
            || file_extn.equals("png")) {

          try {
            MultipartEntity multipartEntity = new MultipartEntity(
                HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("file", new FileBody(new File(imageFile2)));
            multipartEntity.addPart("name", new StringBody("Oleg Mikhailov"));
            multipartEntity.addPart("mailFrom", new StringBody("mosmdf@gmail.com"));
            multipartEntity.addPart("address", new StringBody("Tiraspolskaya"));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }


  public String getPath(Uri uri) {
    String[] projection = {MediaColumns.DATA};
    Cursor cursor = managedQuery(uri, projection, null, null, null);
    int column_index = cursor
        .getColumnIndexOrThrow(MediaColumns.DATA);
    cursor.moveToFirst();
    imageFile2 = cursor.getString(column_index);
    Bitmap file = null;

    try {
      file = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
    } catch (IOException e) {
      e.printStackTrace();
    }
    imageView.setImageBitmap(file);

    return cursor.getString(column_index);
  }

  //autocomplete part
  public void autocompleteInputStreet(AutoCompleteTextView mAutocompleteTextView) {
    mAutocompleteTextView.setThreshold(3);
    mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
    AutocompleteFilter filter = new AutocompleteFilter.Builder()
        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
        .setCountry("UA")
        .build();//country filter
    mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
        BOUNDS_VIEW, filter);
    mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
  }

  private AdapterView.OnItemClickListener mAutocompleteClickListener
      = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
      final String placeId = String.valueOf(item.placeId);

      mAutocompleteTextView.setSelection(0);

      PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
          .getPlaceById(mGoogleApiClient, placeId);
      placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
      Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
    }
  };

  private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
      = new ResultCallback<PlaceBuffer>() {
    @Override
    public void onResult(PlaceBuffer places) {
      if (!places.getStatus().isSuccess()) {
        Log.e(LOG_TAG, "Place query did not complete. Error: " +
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
    Log.i(LOG_TAG, "Google Places API connected.");
  }

  @Override
  public void onConnectionSuspended(int i) {
    mPlaceArrayAdapter.setGoogleApiClient(null);
    Log.e(LOG_TAG, "Google Places API connection suspended.");
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.e(LOG_TAG, "Google Places API connection failed with error code: "
        + connectionResult.getErrorCode());

    Toast.makeText(this,
        "Google Places API connection failed with error code:" +
            connectionResult.getErrorCode(),
        Toast.LENGTH_LONG).show();
  }

  @Override
  public void messageResponse(String output) {

  }
}
