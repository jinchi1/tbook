package com.travelbooks3.android.activity.write;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.travelbooks3.android.R;
import com.travelbooks3.android.util.LogUtil;

import java.util.List;

/**
 * Created by system777 on 2017-11-23.
 */

public class WriteDialog extends AppCompatActivity implements OnMapReadyCallback {

    TextView next_txt;
    TextView cancel_txt;
    ImageView marker_red;
    ImageView marker_yellow;
    ImageView marker_navy;
    ImageView marker_grey;
    String startPhoto;
    String selectedPhoto;
    String address;
    double latitude;
    double longitude;
    String templat;
    String templon;
    String markerColor ="N";
    GoogleMap map;
    MapFragment mapFragment;
    PlaceAutocompleteFragment autocompleteFragment;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        LatLng sydney = new LatLng(37.566535,126.97796919999996);
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                Gps gps = new Gps(WriteDialog.this);
                if (gps.isGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
         /*   LogUtil.i("lllll",latitude.toString());
            LogUtil.i("lllll",longitude.toString());*/
                    Geocoder geocoder = new Geocoder(WriteDialog.this);
                    try {
                        List<Address> addr = geocoder.getFromLocation(latitude, longitude, 10);
                        String geocode = addr.get(0).getAddressLine(0);
                        address = geocode;
                        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText(geocode);
                        LogUtil.d(latitude+ longitude+"aa");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    gps.showSettingsAlert();
                }

                return false;
            }
        });


        if(!markerColor.equals("N"))
        {
            latitude = Double.valueOf(templat);
            longitude = Double.valueOf(templon);
            switch (markerColor)
            {
                case "R" :
                    marker_red.performClick();
                    break;
                case "Y" :
                    marker_yellow.performClick();
                    break;
                case "B" :
                    marker_navy.performClick();
                    break;
                default :
                    break;
            }
            ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText(address);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 메인 layout
        setContentView(R.layout.activity_write_new_select1);
        marker_red = (ImageView) findViewById(R.id.marker_red);
        marker_yellow = (ImageView) findViewById(R.id.marker_yellow);
        marker_navy = (ImageView) findViewById(R.id.marker_navy);
        marker_grey = (ImageView) findViewById(R.id.marker_grey);

        Intent intent = getIntent();
        startPhoto = intent.getStringExtra("startPhoto");
        selectedPhoto = intent.getStringExtra("selectedPhoto");
        markerColor = intent.getStringExtra("marker");
        templat = intent.getStringExtra("latitude");
        templon = intent.getStringExtra("longitude");
        address = intent.getStringExtra("address");





        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.address);
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(13.0f);
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setPadding(2, 0, 0, 0);
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("사진의 위치를 선택해 주세요");
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setBackgroundResource(R.drawable.place_border2);
        //((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setImageResource(R.drawable.pin_grey);
        ((ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setBackgroundResource(R.drawable.place_border);
        ((ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        ((ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setPadding(14, 10, 2, 10);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                address = place.getAddress().toString();
                latitude =  place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                marker_grey.performClick();

            }

            @Override
            public void onError(Status status) {

            }
        });




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(WriteDialog.this);



        LogUtil.d(selectedPhoto);

        marker_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude != 0.0) {

                    marker_red.setBackgroundResource(R.drawable.write_box_border2_red);
                    marker_yellow.setBackgroundResource(R.drawable.write_box_border2);
                    marker_navy.setBackgroundResource(R.drawable.write_box_border2);
                    markerColor = "R";
                    LatLng a = new LatLng(latitude,longitude);
                    map.clear();
                    map.addMarker(new MarkerOptions().position(a).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(a, 13));
                } else {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WriteDialog.this);
                    alert_confirm.setMessage("주소를 입력해 주세요")
                            .setPositiveButton("입력", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).performClick();

                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alert_confirm.create();
                    alertDialog.show();
                }
            }
        });


        marker_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude != 0.0) {

                    marker_yellow.setBackgroundResource(R.drawable.write_box_border2_red);
                    marker_red.setBackgroundResource(R.drawable.write_box_border2);
                    marker_navy.setBackgroundResource(R.drawable.write_box_border2);
                    markerColor ="Y";
                    LatLng a = new LatLng(latitude,longitude);
                    map.clear();
                    map.addMarker(new MarkerOptions().position(a).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(a, 13));
                } else {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WriteDialog.this);
                    alert_confirm.setMessage("주소를 입력해 주세요")
                            .setPositiveButton("입력", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).performClick();

                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alert_confirm.create();
                    alertDialog.show();
                }
            }
        });

        marker_navy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude != 0.0) {

                    marker_navy.setBackgroundResource(R.drawable.write_box_border2_red);
                    marker_yellow.setBackgroundResource(R.drawable.write_box_border2);
                    marker_red.setBackgroundResource(R.drawable.write_box_border2);
                    markerColor = "B";
                    LatLng a = new LatLng(latitude,longitude);
                    map.clear();
                    map.addMarker(new MarkerOptions().position(a).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(a, 13));
                } else {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WriteDialog.this);
                    alert_confirm.setMessage("주소를 입력해 주세요")
                            .setPositiveButton("입력", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).performClick();

                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alert_confirm.create();
                    alertDialog.show();
                }
            }
        });

        marker_grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                markerColor = "N";
                marker_red.setBackgroundResource(R.drawable.write_box_border2);
                marker_yellow.setBackgroundResource(R.drawable.write_box_border2);
                marker_navy.setBackgroundResource(R.drawable.write_box_border2);

            }
        });


        next_txt = (TextView)findViewById(R.id.next_txt);
        cancel_txt = (TextView)findViewById(R.id.cancel_txt);
        next_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(markerColor.equals("N"))
                {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WriteDialog.this);
                    alert_confirm.setMessage("마커를 선택해 주세요");
                    AlertDialog alertDialog = alert_confirm.create();
                    alertDialog.show();

                }
                else {

                    //overridePendingTransition(0, R.anim.slide_right);
                    Intent intent = new Intent(WriteDialog.this, WriteDialog2.class);
                    intent.putExtra("startPhoto", startPhoto);
                    intent.putExtra("selectedPhoto", selectedPhoto);
                    intent.putExtra("marker", markerColor);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("address", address);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left, 0);
                    finish();
                }
            }
        });

        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(2);
        finish();
    }



}
