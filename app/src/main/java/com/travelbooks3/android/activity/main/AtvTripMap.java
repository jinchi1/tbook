package com.travelbooks3.android.activity.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.travelbooks3.android.R;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.LogUtil;

public class AtvTripMap extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap _map;
    private MapView mapview;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_trip_map);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
        }
        String data = bundle.getString(Constants.EXTRAS_JSON_STRING);
        String data2 = bundle.getString(Constants.EXTRAS_JSON_STRING2);
        String data3 = bundle.getString(Constants.EXTRAS_JSON_STRING3);
        LogUtil.d("bundle is = " + data.toString()+data2.toString());

        latitude = Double.parseDouble(data);
        longitude = Double.parseDouble(data2);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);

        mapFragment.getMapAsync(this);

    }
/*
    @Override
    protected void onResume() {
        super.onResume();
    }*/
 /* @Override
    public void onMapReady(GoogleMap googleMap) {

        _map = googleMap;
        LatLng sydney = new LatLng(38.1256, 125.1215);
        _map.addMarker(new MarkerOptions().position(sydney));
        _map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        _map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 19));

    }*/
    @Override
    public void onMapReady(GoogleMap googleMap) {

        _map = googleMap;
        LatLng sydney = new LatLng(latitude, longitude);
        _map.addMarker(new MarkerOptions().position(sydney));
        _map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

    }
/*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0, 0);
    }*/


}


