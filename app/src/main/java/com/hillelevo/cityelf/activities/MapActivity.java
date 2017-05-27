package com.hillelevo.cityelf.activities;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hillelevo.cityelf.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@RequiresApi(api = VERSION_CODES.LOLLIPOP)
public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
    View.OnClickListener {

  private GoogleMap mMap;
  private LatLng defaultMarker;
  private LatLngBounds LIMIT_OF_SITY = new LatLngBounds(new LatLng(46.400, 30.530),
      new LatLng(46.550, 30.850));
  private LatLng coordinate;
  private UiSettings uiSettings;

  private Button btnSendAddress;
  private Button btnAccount;
  private Button btnPreferences;
  private EditText inputAddress;

  private Geocoder geocoder;
  private Locale ruLocale = new Locale.Builder().setLanguage("ru").setScript("Cyrl").build();

  private String userAddress = "Канатна, 22";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_map);

    inputAddress = (EditText) findViewById(R.id.inputAddress);
    btnSendAddress = (Button) findViewById(R.id.btnSendAddress);
    btnSendAddress.setOnClickListener(this);
    btnAccount = (Button) findViewById(R.id.btnAccount);
    btnAccount.setOnClickListener(this);
    btnPreferences = (Button) findViewById(R.id.btnPreferences);
    btnPreferences.setOnClickListener(this);

    geocoder = new Geocoder(this, ruLocale);

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);





    }


  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    uiSettings = mMap.getUiSettings();
    uiSettings.setMapToolbarEnabled(false); //hide google icons
    // Default marker from center Odessa
    defaultMarker = new LatLng(46.4796777, 30.7457675);
    mMap.addMarker(new MarkerOptions()
        .position(defaultMarker)
        .title("Odessa marker")
        .icon(BitmapDescriptorFactory.defaultMarker(
            BitmapDescriptorFactory.HUE_AZURE))
        .draggable(true)); //move marker

    mMap.setOnMarkerDragListener(new OnMarkerDragListener() {
      @Override
      public void onMarkerDragStart(Marker marker) {

      }

      @Override
      public void onMarkerDrag(Marker marker) {

      }

      @Override
      public void onMarkerDragEnd(Marker marker) {
        coordinate = marker.getPosition();
        userAddress = sendGeo(coordinate, marker);
        System.out.println(userAddress);
      }
    });

    //Animate orientation camera
    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(defaultMarker)
        .zoom(13)
        .bearing(0)
        .tilt(0)
        .build();

    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    // Camera move limit
    mMap.setLatLngBoundsForCameraTarget(LIMIT_OF_SITY);


  }

  private String sendGeo(LatLng point, Marker marker) {

    List<Address> addresses = new ArrayList<>();
    try {
      addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
    } catch (IOException e) {
      e.printStackTrace();
    }

    android.location.Address address = addresses.get(0);
    StringBuilder sb = null;
    if (address != null) {
      sb = new StringBuilder();
      for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
        if (i == 1) {
          continue;
        }
        sb.append(address.getAddressLine(i) + "\n");
      }
      Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();//message on screen
    }

    assert sb != null;
    return address.getAddressLine(0);
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnSendAddress:
        System.out.println(userAddress);//marker address
        System.out.println(inputAddress.getText()); //typing address
        //todo send address from server
        break;
      case R.id.btnAccount:
        //todo user account
        break;
      case R.id.btnPreferences:
        //todo preferences btn
        break;
    }


  }

}
