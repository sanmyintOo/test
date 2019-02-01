package com.example.sanmyintoo.testapplicaiton;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanmyintoo.testapplicaiton.model.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Add_Location extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener{

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("TAG: onMapReady: map is ready");
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
//
//                return;
//            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
    }

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );


    private AutoCompleteTextView mSearchText;

    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest locationRequest;
    private PlaceAutocompleteAdapter mplaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApliClient;
    private GeoDataClient mGeodataClient;
    private PlaceInfo mPlace;
    private Marker mMarker;
    private ImageView mInfo;
    private Button mBtn;
    String eventName, eventDes, url, date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__location);

        Bundle extras = getIntent().getExtras();
        eventName = extras.getString("EventName");
        eventDes = extras.getString("EventDes");
        url = extras.getString("ImageUrl");
        date = extras.getString("Date");
        time = extras.getString("Time");

        mSearchText = (AutoCompleteTextView) findViewById(R.id.searchText);
        mInfo = (ImageView) findViewById(R.id.place_info);
        mBtn = (Button) findViewById(R.id.choosebtn);



        if (isServiceOK()) {
            getLocationPermission();
        }
    }

    private  void init(){

        mGoogleApliClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();


        mSearchText.setOnItemClickListener(mAutoCompleteClickListener);
        mplaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApliClient, LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mplaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    geoLocate();
                }
                return false;
            }
        });

        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    }
                    else {
                        mMarker.showInfoWindow();
                    }
                }catch (NullPointerException e){
                    Toast.makeText(Add_Location.this, "Null pointer exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceInfo placeInfo = new PlaceInfo();

                Intent intent = new Intent(Add_Location.this, Add_members.class);
                String address = mPlace.getAddress();
                LatLng latlng = mPlace.getLatLng();
                intent.putExtra("Address", address);
                intent.putExtra("latlng", latlng);

                intent.putExtra("EventName", eventName);
                intent.putExtra("EventDes", eventDes);
                intent.putExtra("ImageUrl", url);
                intent.putExtra("Date", date);
                intent.putExtra("Time", time);

                startActivity(intent);
            }
        });
        hideKeyboard();
    }

    private void geoLocate(){
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(Add_Location.this);
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            System.out.println("geoLocate : IOException :" + e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
//            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
//            System.out.println("geoLocate : Found Address :" + address.toString());
        }
    }

    private void getDeviceLocation() {
        System.out.println("TAG: getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setFastestInterval(2000);
//        locationRequest.setInterval(4000);

        try {
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                  if(locationResult != null){
                      Location currentlocation = locationResult.getLastLocation();
                      moveCamera(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()),DEFAULT_ZOOM, "My location");
                  }
                  else {
                      Toast.makeText(Add_Location.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                  }

                }
            }, getMainLooper());
        } catch (SecurityException e) {
            System.out.println("TAG: getLocationPermission: Security Exception :" + e.getMessage());
        }

//        try{
//            if(mLocationPermissionGranted){
//                Task location = mFusedLocationProviderClient.getLastLocation();
//
//                if(location.getResult() != null){
//
//                    Location currentlocation =  (Location) location.getResult();
//                    moveCamera(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()),DEFAULT_ZOOM);
//
//                }
//                else{
//                    System.out.println("TAG: getLocationPermission: currentlocaiton is null location");
//                    Toast.makeText(Add_Location.this, "unable to get current location", Toast.LENGTH_SHORT).show();
//                }
//
////                location.addOnCompleteListener(new OnCompleteListener() {
////                    @Override
////                    public void onComplete(@NonNull Task task) {
////                        if (task.isSuccessful() && task.getResult() != null){
////                            System.out.println("TAG: getLocationPermission: location found");
////
////                            Location currentlocation =  (Location) task.getResult();
////                            moveCamera(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()),DEFAULT_ZOOM);
////                        }
////                        else{
////                            System.out.println("TAG: getLocationPermission: currentlocaiton is null location");
////                            Toast.makeText(Add_Location.this, "unable to get current location", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                });
//            }
//
//        }catch ( SecurityException e ) {
//            System.out.println("TAG: getLocationPermission: Security Exception :" + e.getMessage());
//        }

    }

    private void moveCamera(LatLng latlng, float zoom, PlaceInfo placeinfo) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
        mMap.clear();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Add_Location.this));

        if(placeinfo != null){
            try{
                String snippet = "Address: "+ placeinfo.getAddress() + "\n" +
                        "Phone Number: "+ placeinfo.getPhoneni() + "\n" +
                        "Rating: "+ placeinfo.getRating();


                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latlng)
                        .title(placeinfo.getName())
                        .snippet(snippet);
                mMarker = mMap.addMarker(markerOptions);

            }
            catch (NullPointerException e){
                Toast.makeText(this, "Null pointer exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            mMap.addMarker(new MarkerOptions().position(latlng));
        }

        hideKeyboard();
    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
        MarkerOptions options = new MarkerOptions()
                .position(latlng)
                .title(title);

        mMap.addMarker(options);
        hideKeyboard();
    }

    public boolean isServiceOK() {
        int avaliable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Add_Location.this);

        if (avaliable == ConnectionResult.SUCCESS) {
            //everything is fine and user can make map
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(avaliable)) {
            //an error occured but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Add_Location.this, avaliable, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initMap() {
        Log.d("TAG", "initMap: initalizing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(Add_Location.this);
    }

    private void getLocationPermission() {
        System.out.println("TAG: getLocationPermission: getting location permission");

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        System.out.println("TAG: onRequestPermissionResult: called");
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    System.out.println("TAG: onRequestPremisssionResult : permission granted");
                    mLocationPermissionGranted = true;
                    //initialize ur map
                    initMap();
                }
            }
        }

    }
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
    }

    /*
    --------------- google place api ----------------
     */

    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideKeyboard();
            final AutocompletePrediction item = mplaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApliClient, placeId);

            placeResult.setResultCallback(mUpdatePlaceDetailsCallBack);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallBack = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Toast.makeText(Add_Location.this, "Place query dit not complete succesfully :" + places.getStatus().toString(), Toast.LENGTH_SHORT).show();
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{


            mPlace = new PlaceInfo();
            mPlace.setName(place.getName().toString());
            mPlace.setAddress(place.getAddress().toString());
            mPlace.setId(place.getId());
            mPlace.setLatLng(place.getLatLng());
            mPlace.setPhoneni(place.getPhoneNumber().toString());
            mPlace.setRating(place.getRating());
            }catch (NullPointerException e){
                Toast.makeText(Add_Location.this, "Nullpointer exception : " + e.getMessage(), Toast.LENGTH_SHORT ).show();
            }
            moveCamera(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude),DEFAULT_ZOOM, mPlace);
            Toast.makeText(Add_Location.this, "Location found", Toast.LENGTH_LONG ).show();
            places.release();
        }
    };

    public void toSearchPeople(View view) {
        Intent intent = new Intent(this, Add_members.class);
        startActivity(intent);
    }



}
