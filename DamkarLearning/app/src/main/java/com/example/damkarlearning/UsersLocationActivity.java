package com.example.damkarlearning;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ASUS on 23/02/2017.
 */

public class UsersLocationActivity extends FragmentActivity implements OnMapReadyCallback {


    SharedPreferences sharedPreferences;
    int locationCount = 0;
    double mLatitude;
    double mLongitude;
    private String myAPI = "AIzaSyCdbysGo5NyB7yUIREr1_9IM1z5LGyp8N4";

    private int totUsers;
    private LatLng[] userPositions;
    private double[][] userLocations; // rows as user_id, column 0 as Latitude, column 1 as Longitude

    public void setUserPositions() {

    }

    public void setUserLocations() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userPositions = new LatLng[totUsers];
        userLocations = new double[totUsers][2];
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*
        for (int i = 0; i<userPositions.length; i++) {
            userPositions[i] = new LatLng(userLocations[i][0], userLocations[i][1]);
            googleMap.addMarker(new MarkerOptions().position(userPositions[i])
                    .title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userPositions[i]));
        }
        */

        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


}