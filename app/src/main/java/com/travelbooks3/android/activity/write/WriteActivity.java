package com.travelbooks3.android.activity.write;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.places.Places;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.travelbooks3.android.R;
import com.travelbooks3.android.util.FormatUtil;
import com.travelbooks3.android.util.LogUtil;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import photran.me.timer.listener.OnTimeSetListener;

public class WriteActivity extends AppCompatActivity implements OnMapReadyCallback/*,GoogleMap.OnMapClickListener*/,OnTimeSetListener {


    double latitude;
    double longitude;
    LatLng preSydney;
    ViewPager viewPager;
    Spinner marker_spinner;
    Spinner trans_spinner;
    ImageView time_spinner;
    LinearLayout write_lay;



    private GoogleMap _map;
    private PolylineOptions options;
    private Gps gps;
    TextView next;
    TextView my_location_txt;
    LinearLayout my_location_lay;
    EditText Edit_subComment;
    WritePhotoPagerAdapter adapter;
    ImageView my_location;


   /* MarkerAdapter markerAdapter;
    TransAdapter transAdapter;
    TimeAdapter timeAdapter;*/

    ArrayList<String> photoArr; //사진픽해서 가져온// 데이터(주소값)
    ArrayList<String> markerArr;
    ArrayList<String> transArr;
    ArrayList<String> latitudeArr;
    ArrayList<String> longitudeArr;
    ArrayList<String> addressArr;
    ArrayList<LatLng> polyArr;
    ArrayList<String> subCommentArr;
    ArrayList<String> hourArr;
    ArrayList<String> minArr;

    //UploadData uploadDatas;
    int selectedPhotoPosition;
    //UploadData.photos photoData;
    //UploadData.route routeData;

    int count;
    int backcount;
    int TIMECOUNT=0;
    int mHour, mMinute;


    String geoPoint;
    LatLng sydney;

    com.google.android.gms.maps.model.Marker rmarker;
    com.google.android.gms.maps.model.Marker ymarker;
    com.google.android.gms.maps.model.Marker bmarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);



        registerReceiver(mReceiver,
                new IntentFilter(photran.me.timer.TimePickerDialog.TIME_PICKER_DIALOG_RESULT_ACTION));

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(13.0f);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setPadding(2,0,0,0);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("사진의 위치를 선택해 주세요");
        ((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setImageResource(R.drawable.map_pin_grey);
        ((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setScaleType(ImageView.ScaleType.FIT_CENTER);
        ((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setPadding(0,5,0,5);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                LogUtil.d("위치"+place.getAddress());
                LogUtil.d("LatLng"+place.getLatLng().latitude+place.getLatLng().longitude);
                getPointFromGeoCoder(place.getAddress().toString(), place.getLatLng().latitude, place.getLatLng().longitude);
                addressArr.set(selectedPhotoPosition,place.getAddress().toString());
                LogUtil.d("Address"+place.getAddress().toString());
                my_location_txt.setTextColor(Color.parseColor("#CCCCCC"));
                my_location.setImageResource(R.drawable.now_location);
            }

            @Override
            public void onError(Status status) {
                LogUtil.d("error : "+status);
            }
        });


        write_lay = (LinearLayout)findViewById(R.id.write_lay);

        write_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(Edit_subComment.getWindowToken(), 0);
            }
        });

        my_location_txt = (TextView)findViewById(R.id.my_location_txt);
        my_location_lay = (LinearLayout)findViewById(R.id.my_location_lay);
        next = (TextView)findViewById(R.id.txtView_write);
        viewPager =(ViewPager)findViewById(R.id.write_viewPager);
        Edit_subComment = (EditText)findViewById(R.id.Edit_subComment);
        Edit_subComment.setVisibility(View.INVISIBLE);
        my_location = (ImageView)findViewById(R.id.my_location);
        my_location_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gps gps = new Gps(WriteActivity.this);
                if (gps.isGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
         /*   LogUtil.i("lllll",latitude.toString());
            LogUtil.i("lllll",longitude.toString());*/
                    Geocoder geocoder = new Geocoder(WriteActivity.this);
                    try {
                        List<Address> addr = geocoder.getFromLocation(latitude, longitude, 10);
                        String geocode = addr.get(0).getAddressLine(0);
                        getPointFromGeoCoder(geocode, latitude, longitude);
                        addressArr.set(selectedPhotoPosition, geocode);
                        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText(geocode);
                        my_location_txt.setTextColor(Color.parseColor("#d92051"));
                        my_location.setImageResource(R.drawable.now_location_red);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    gps.showSettingsAlert();
                }

            }
            });
       /* my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gps gps = new Gps(WriteActivity.this);
                if(gps.isGetLocation()){
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
         *//*   LogUtil.i("lllll",latitude.toString());
            LogUtil.i("lllll",longitude.toString());*//*
                    Geocoder geocoder = new Geocoder(WriteActivity.this);
                    try {
                        List<Address> addr = geocoder.getFromLocation(latitude,longitude,10);
                        String geocode = addr.get(0).getAddressLine(0);
                        getPointFromGeoCoder(geocode, latitude, longitude);
                        addressArr.set(selectedPhotoPosition,geocode);
                        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText(geocode);
                        my_location_txt.setTextColor(Color.parseColor("#d92051"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    gps.showSettingsAlert();
                }




            }
        });*/



        viewPager.setClipToPadding(false);
        //viewPager.setPadding(40,0,40,0);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin((getResources().getDisplayMetrics().widthPixels /-9));
        final Intent i = getIntent();
        photoArr= i.getStringArrayListExtra("path");
        markerArr = new ArrayList<String>();
        transArr = new ArrayList<String>();
        latitudeArr = new ArrayList<String>();
        longitudeArr = new ArrayList<String>();
        addressArr = new ArrayList<String>();
        subCommentArr = new ArrayList<String>();
        hourArr = new ArrayList<String>();
        minArr = new ArrayList<String>();


        polyArr = new ArrayList<LatLng>();


        options = new PolylineOptions();
        options.color(Color.RED);
        options.width(3);

        selectedPhotoPosition=0;

        LatLng N = new LatLng(0,0);

        for(int l=0;l<photoArr.size();l++)
        {

            markerArr.add("N");
            transArr.add("0");
            latitudeArr.add("N");
            longitudeArr.add("N");
            addressArr.add("N");
            polyArr.add(N);
            subCommentArr.add("ND-0702-1206-1230");
            hourArr.add("N");
            minArr.add("N");

        }




        /*uploadDatas = new UploadData();
        uploadDatas.photosArrayList = new ArrayList<UploadData.photos>(photoArr.size());
        uploadDatas.routeArrayList = new ArrayList<UploadData.route>(photoArr.size());*/


        marker_spinner = (Spinner)findViewById(R.id.marker_spinner);
        trans_spinner = (Spinner)findViewById(R.id.trans_spinner);
        time_spinner = (ImageView) findViewById(R.id.time_image);


        marker_spinner.setEnabled(false);
        trans_spinner.setEnabled(false);
        time_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (TIMECOUNT)
                {
                    case 0:

                        LogUtil.d("timecount0입니당");
                        break;
                    case 1:



                        /*TimePickerDialog.OnTimeSetListener mTimeSetListener =


                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override

                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                        // TODO Auto-generated method stub

                                        //사용자가 입력한 값을 가져온뒤

                                        mHour = hourOfDay;
                                        mMinute = minute;
                                        hourArr.set(selectedPhotoPosition,String.valueOf(mHour));
                                        minArr.set(selectedPhotoPosition,String.valueOf(mMinute));
                                        time_spinner.setImageResource(R.drawable.time_red);
                                        LogUtil.d("time"+mHour+mMinute);

                                    }
                                };

                        new TimePickerDialog(WriteActivity.this, mTimeSetListener, mHour,

                                mMinute, true).show();

                        break;*/
                        new photran.me.timer.TimePickerDialog.Builder()
                                .setTimer(12, 0, true)
                                .setThemeDark(false)
                                .createDialog().show(getFragmentManager(), photran.me.timer.TimePickerDialog.class.getName());


                }
            }
        });

        ArrayList<Marker> markers = new ArrayList<Marker>();
        markers.add(new Marker("N",R.drawable.pin_grey_line));
        markers.add(new Marker("R",R.drawable.map_pin_red));
        markers.add(new Marker("Y",R.drawable.map_pin_yellow));
        markers.add(new Marker("B",R.drawable.map_pin_navy));

        ArrayList<Marker> trans = new ArrayList<Marker>();
        trans.add(new Marker("0",R.drawable.bus_line));
        trans.add(new Marker("1",R.drawable.subway));
        trans.add(new Marker("2",R.drawable.bus));
        trans.add(new Marker("3",R.drawable.train));
        trans.add(new Marker("4",R.drawable.car));
        trans.add(new Marker("5",R.drawable.airplane));
        trans.add(new Marker("6",R.drawable.tram));
        trans.add(new Marker("7",R.drawable.bicycle));
        trans.add(new Marker("8",R.drawable.walk));
//        trans.add(new Marker("9",R.drawable.cable));


        MarkerAdapter markerAdapter = new MarkerAdapter(getApplicationContext(),markers);
        marker_spinner.setAdapter(markerAdapter);
        TransAdapter transAdapter = new TransAdapter(getApplicationContext(),trans);
        trans_spinner.setAdapter(transAdapter);

        marker_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

             switch(position){
                    case 0:
                        Log.i("position0",String.valueOf(position+id));
                        //"N";
                        try{
                            markerArr.set(selectedPhotoPosition,"N");
                            transArr.set(selectedPhotoPosition,"0");
                            hourArr.set(selectedPhotoPosition,"N");
                            minArr.set(selectedPhotoPosition,"N");
                            trans_spinner.setEnabled(false);
                            TIMECOUNT =0;
                            trans_spinner.setBackgroundResource(R.drawable.choice_box);
                            time_spinner.setBackgroundResource(R.drawable.choice_box);
                            trans_spinner.setSelection(0);
                            time_spinner.setImageResource(R.drawable.time_line);
                            Edit_subComment.setVisibility(View.INVISIBLE);
                            Edit_subComment.setText(null);
                            ((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setImageResource(R.drawable.map_pin_grey);
                            _map.clear();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            //finish();
                        }

                            break;
                    case 1:
                        Log.i("position1",String.valueOf(position+id));
                        //"R";

                        markerArr.set(selectedPhotoPosition,"R");
                        sydney = new LatLng(Double.parseDouble(latitudeArr.get(selectedPhotoPosition)),Double.parseDouble(longitudeArr.get(selectedPhotoPosition)));
                        //_map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));

                        rmarker = _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));


                        _map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
                        //options.add(sydney);
                        //_map.addPolyline(options);
                        polyArr.set(selectedPhotoPosition,sydney);

                        count =0;
                        for(int i=0; i<selectedPhotoPosition;i++)
                        {
                            if(!markerArr.get(i).toString().equals("N")){
                                count++;
                            }
                        }
                        if(count!=0)
                        {
                            trans_spinner.setEnabled(true);
                            trans_spinner.setBackgroundResource(R.drawable.choice_box_red);
                            Edit_subComment.setVisibility(View.VISIBLE);
                            //Edit_subComment.setText(null);
                            TIMECOUNT=1;
                            time_spinner.setBackgroundResource(R.drawable.choice_box_red);
                            LogUtil.d("VVVVVVVV257");
                        }
                        ((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setImageResource(R.drawable.map_pin_red);

                        break;
                    case 2:
                        Log.i("position2",String.valueOf(position+id));
                       // "Y";

                        markerArr.set(selectedPhotoPosition,"Y");
                        sydney = new LatLng(Double.parseDouble(latitudeArr.get(selectedPhotoPosition)),Double.parseDouble(longitudeArr.get(selectedPhotoPosition)));
                        //_map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                        ymarker = _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));

                        _map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
                        //options.add(sydney);
                        //_map.addPolyline(options);
                        polyArr.set(selectedPhotoPosition,sydney);

                        count =0;
                        for(int i=0; i<selectedPhotoPosition;i++)
                        {
                            if(!markerArr.get(i).toString().equals("N")){
                                count++;
                            }
                        }
                        if(count!=0)
                        {
                            trans_spinner.setEnabled(true);
                            trans_spinner.setBackgroundResource(R.drawable.choice_box_red);
                            Edit_subComment.setVisibility(View.VISIBLE);
                            //Edit_subComment.setText(null);
                            TIMECOUNT=1;
                            time_spinner.setBackgroundResource(R.drawable.choice_box_red);
                            LogUtil.d("VVVVVVVV286");
                        }
                        ((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setImageResource(R.drawable.map_pin_yellow);
                        break;
                    case 3:
                        Log.i("position3",String.valueOf(position));
                        //"B";

                        markerArr.set(selectedPhotoPosition,"B");
                        LatLng sydney = new LatLng(Double.parseDouble(latitudeArr.get(selectedPhotoPosition)),Double.parseDouble(longitudeArr.get(selectedPhotoPosition)));
                        //_map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));

                        bmarker =  _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));


                        _map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
                        //options.add(sydney);
                        //_map.addPolyline(options);
                        polyArr.set(selectedPhotoPosition,sydney);

                        count =0;
                        for(int i=0; i<selectedPhotoPosition;i++)
                        {
                            if(!markerArr.get(i).toString().equals("N")){
                                count++;
                            }
                        }
                        if(count!=0)
                        {
                            trans_spinner.setEnabled(true);
                            trans_spinner.setBackgroundResource(R.drawable.choice_box_red);
                            Edit_subComment.setVisibility(View.VISIBLE);
                            //Edit_subComment.setText(null);
                            TIMECOUNT=1;
                            time_spinner.setBackgroundResource(R.drawable.choice_box_red);
                            LogUtil.d("VVVVVVVV314");
                        }
                        ((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setImageResource(R.drawable.map_pin_navy);
                        break;

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        trans_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0:
                        Log.i("position0",String.valueOf(position));

                        try {
                            transArr.set(selectedPhotoPosition, "0");
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            finish();
                        }
                            break;
                    case 1:
                        Log.i("position1",String.valueOf(position));
                        transArr.set(selectedPhotoPosition,"1");
                        break;
                    case 2:
                        Log.i("position2",String.valueOf(position));
                        transArr.set(selectedPhotoPosition,"2");
                        break;
                    case 3:
                        Log.i("position3",String.valueOf(position));
                        transArr.set(selectedPhotoPosition,"3");
                        break;
                    case 4:
                        Log.i("position4",String.valueOf(position));
                        transArr.set(selectedPhotoPosition,"4");
                        break;
                    case 5:
                        Log.i("position5",String.valueOf(position));
                        transArr.set(selectedPhotoPosition,"5");
                        break;
                    case 6:
                        Log.i("position6",String.valueOf(position));
                        transArr.set(selectedPhotoPosition,"6");
                        break;
                    case 7:
                        Log.i("position6",String.valueOf(position));
                        transArr.set(selectedPhotoPosition,"7");
                        break;
                    case 8:
                        Log.i("position6",String.valueOf(position));
                        transArr.set(selectedPhotoPosition,"8");
                        break;


                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





/////////////////////////////////////////////////////////////다음페이지 클릭리스너
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(Edit_subComment.getWindowToken(), 0);

                for(int k=0;k<photoArr.size();k++) {
                    LogUtil.i("ssss",markerArr.get(k));
                    LogUtil.i("ssss",photoArr.get(k));
                    LogUtil.i("ssss",transArr.get(k));
                    LogUtil.i("ssss",latitudeArr.get(k));
                    LogUtil.i("ssss",longitudeArr.get(k));
                    LogUtil.i("ssss",String.valueOf(polyArr.get(k).latitude));
                    LogUtil.i("ssss",String.valueOf(polyArr.get(k).longitude));
                    LogUtil.i("ssss",addressArr.get(k));
                    LogUtil.i("ssss",subCommentArr.get(k));
                    LogUtil.i("ssss",hourArr.get(k));
                    LogUtil.i("ssss",minArr.get(k));

                }


                if(Edit_subComment.getText().toString()!=null)
                {
                    subCommentArr.set(selectedPhotoPosition,Edit_subComment.getText().toString());
                }
                Intent next = new Intent(WriteActivity.this,WriteNextActivity.class);
                next.putStringArrayListExtra("path",photoArr);
                next.putStringArrayListExtra("marker",markerArr);
                next.putStringArrayListExtra("trans",transArr);
                next.putStringArrayListExtra("latitude",latitudeArr);
                next.putStringArrayListExtra("longitude",longitudeArr);
                next.putStringArrayListExtra("address",addressArr);
                next.putStringArrayListExtra("subComment",subCommentArr);
                next.putStringArrayListExtra("hour",hourArr);
                next.putStringArrayListExtra("minute",minArr);
                next.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(next);
                finish();

            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////페이지 옆으로넘길때
        if(photoArr!=null) {
            viewPager.removeAllViews();
            adapter = new WritePhotoPagerAdapter(this, photoArr);

            viewPager.setAdapter(adapter);

            viewPager.setCurrentItem(0);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {



                    selectedPhotoPosition = num;


                    if(!longitudeArr.get(selectedPhotoPosition).equals("N")) {
                        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText(addressArr.get(selectedPhotoPosition));
                        marker_spinner.setEnabled(true);
                        if(markerArr.get(selectedPhotoPosition).equals("R")) marker_spinner.setSelection(1);
                        else if(markerArr.get(selectedPhotoPosition).equals("Y")) marker_spinner.setSelection(2);
                        else if(markerArr.get(selectedPhotoPosition).equals("B")) marker_spinner.setSelection(3);
                        else if(markerArr.get(selectedPhotoPosition).equals("N")) marker_spinner.setSelection(0);

                        marker_spinner.setBackgroundResource(R.drawable.choice_box_red);
                        trans_spinner.setEnabled(false);
                        trans_spinner.setSelection(0);
                        TIMECOUNT=0;
                        time_spinner.setImageResource(R.drawable.time_line);
                        trans_spinner.setBackgroundResource(R.drawable.choice_box);
                        time_spinner.setBackgroundResource(R.drawable.choice_box);
                        Edit_subComment.setVisibility(View.INVISIBLE);


                        backcount =0;
                        for(int i=0; i<selectedPhotoPosition;i++)
                        {
                            if(!markerArr.get(i).toString().equals("N")){
                                backcount++;
                            }
                        }
                        if(backcount!=0)
                        {
                            trans_spinner.setEnabled(true);
                            trans_spinner.setBackgroundResource(R.drawable.choice_box_red);

                            if(transArr.get(selectedPhotoPosition).equals("0")) trans_spinner.setSelection(0);
                            else if(transArr.get(selectedPhotoPosition).equals("1")) trans_spinner.setSelection(1);
                            else if(transArr.get(selectedPhotoPosition).equals("2")) trans_spinner.setSelection(2);
                            else if(transArr.get(selectedPhotoPosition).equals("3")) trans_spinner.setSelection(3);
                            else if(transArr.get(selectedPhotoPosition).equals("4")) trans_spinner.setSelection(4);
                            else if(transArr.get(selectedPhotoPosition).equals("5")) trans_spinner.setSelection(5);
                            else if(transArr.get(selectedPhotoPosition).equals("6")) trans_spinner.setSelection(6);
                            else if(transArr.get(selectedPhotoPosition).equals("7")) trans_spinner.setSelection(7);
                            else if(transArr.get(selectedPhotoPosition).equals("8")) trans_spinner.setSelection(8);

                            if(!hourArr.get(selectedPhotoPosition).equals("N")) time_spinner.setImageResource(R.drawable.time_red);

                            Edit_subComment.setVisibility(View.VISIBLE);
                            if(!subCommentArr.get(selectedPhotoPosition).equals("ND-0702-1206-1230")) Edit_subComment.setText(subCommentArr.get(selectedPhotoPosition));
                            time_spinner.setBackgroundResource(R.drawable.choice_box_red);
                            TIMECOUNT=1;
                            LogUtil.d("VVVVVVVV490");
                        }

                    } else{
                        marker_spinner.setSelection(0);
                        trans_spinner.setSelection(0);
                        time_spinner.setImageResource(R.drawable.time_line);
                        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText(null);


                        Edit_subComment.setVisibility(View.INVISIBLE);
                        Edit_subComment.setText(null);
                        my_location.setImageResource(R.drawable.now_location);
                        my_location_txt.setTextColor(Color.parseColor("#CCCCCC"));

                   /* if(subCommentArr.get(selectedPhotoPosition).equals("ND-0702-1206-1230")||subCommentArr.get(selectedPhotoPosition)==null)
                    {
                        Edit_subComment.setText(null);
                        Edit_subComment.setVisibility(View.INVISIBLE);
                        LogUtil.d("VVVVVVVVVV453");
                    }
                    else
                    {
                        Edit_subComment.setVisibility(View.VISIBLE);
                        LogUtil.d("VVVVVVVVVVVV457");
                        Edit_subComment.setText(subCommentArr.get(selectedPhotoPosition));
                    }*/

                        marker_spinner.setEnabled(false);
                        trans_spinner.setEnabled(false);
                        TIMECOUNT=0;

                        marker_spinner.setBackgroundResource(R.drawable.choice_box);
                        trans_spinner.setBackgroundResource(R.drawable.choice_box);
                        time_spinner.setBackgroundResource(R.drawable.choice_box);
                    }
                    LogUtil.d("baaa :"+selectedPhotoPosition);


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    if(state == 2) {
                        LogUtil.d("edittext position: " + selectedPhotoPosition);
                        if(Edit_subComment.getText().toString()!=null)subCommentArr.set(selectedPhotoPosition,Edit_subComment.getText().toString());
                    }
                }
            });

        }
///////////////////////////////////////////////////////////////////////////////////////////////////서치키보드 리스너


///////////////////////////////////////////////////////////////////////////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        _map = googleMap;
        LatLng sydney = new LatLng(37.4562557, 126.7052062);
/*
        _map.addMarker(new MarkerOptions().position(sydney));
        _map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 19));
*/

        _map.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


/*    public void onMapClick(LatLng point) {

        // 현재 위도와 경도에서 화면 포인트를 알려준다
        Point screenPt = _map.getProjection().
                toScreenLocation(point);

        // 현재 화면에 찍힌 포인트로 부터 위도와 경도를 알려준다.
        LatLng latLng = _map.getProjection().
                fromScreenLocation(screenPt);

        Toast.makeText(getApplicationContext(), String.valueOf(point.latitude)+String.valueOf(point.longitude), Toast.LENGTH_SHORT).show();

    }*/


    private com.travelbooks3.android.activity.write.Point getPointFromGeoCoder(String addr, double latitude, double longitude) {
        com.travelbooks3.android.activity.write.Point point = new com.travelbooks3.android.activity.write.Point();
        point.addr = addr;


        point.havePoint = true;
        point.y = latitude;
        point.x = longitude;
        //Toast.makeText(getApplicationContext(),"입력주소 : "+addr+"\nlatitude : "+point.y+" longitude : "+point.x,Toast.LENGTH_SHORT).setGravity(Gravity.TOP,0,0);
        Toast.makeText(this,"adsfasdf",Toast.LENGTH_SHORT);
        LogUtil.d("longitude :" + point.x);
        LogUtil.d("latitude :" + point.y);


        if(!markerArr.get(selectedPhotoPosition).equals("N"))
        {
            /*if(markerArr.get(selectedPhotoPosition).equals("R"))rmarker.remove();
            else if (markerArr.get(selectedPhotoPosition).equals("Y"))ymarker.remove();
            else if (markerArr.get(selectedPhotoPosition).equals("B"))bmarker.remove();*/
            _map.clear();
            longitudeArr.set(selectedPhotoPosition,String.valueOf(point.x));
            latitudeArr.set(selectedPhotoPosition,String.valueOf(point.y));


            for(int i=0; i<photoArr.size(); i++)
            {
                LogUtil.d("photo: "+photoArr.get(i));
                LogUtil.d("marker: "+markerArr.get(i));
                LogUtil.d("trans: "+transArr.get(i));
                LogUtil.d("위도: "+latitudeArr.get(i));
                LogUtil.d("경도: "+longitudeArr.get(i));
                LogUtil.d("주소: "+addressArr.get(i));
                LogUtil.d("서브커멘트: "+subCommentArr.get(i));
                LogUtil.d("시간: "+hourArr.get(i));
                LogUtil.d("분: "+minArr.get(i));


                if(!markerArr.get(i).equals("N"))
                {

                    if(markerArr.get(i).equals("R")) {

                        sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)),Double.parseDouble(longitudeArr.get(i)));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));
                        //options.add(sydney);

                    }
                    else if(markerArr.get(i).equals("Y"))
                    {
                        sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)),Double.parseDouble(longitudeArr.get(i)));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                        //options.add(sydney);
                    }
                    else if(markerArr.get(i).equals("B"))
                    {
                        sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)),Double.parseDouble(longitudeArr.get(i)));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));
                        //options.add(sydney);
                    }

                /*calDistance(latitudeArr.get(i),longitudeArr.get(i),)*/

                }




            }

        }
        longitudeArr.set(selectedPhotoPosition,String.valueOf(point.x));
        latitudeArr.set(selectedPhotoPosition,String.valueOf(point.y));
        //addressArr.set(selectedPhotoPosition,point.addr);
        marker_spinner.setEnabled(true);
        marker_spinner.setBackgroundResource(R.drawable.choice_box_red);
//        LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaa"+preSydney.latitude+"aaaaaaaa"+latitudeArr.get(selectedPhotoPosition));

        /*
        trans_spinner.setEnabled(true);
        trans_spinner.setBackgroundResource(R.drawable.choice_box_red);

        if(selectedPhotoPosition==photoArr.size()-1)
        {
            trans_spinner.setEnabled(false);
        }*/



        return point;
    }
/////////////////////////////////////////////////////////////////위도경도 거리계산

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final photran.me.timer.TimePickerDialog.Builder builder = new photran.me.timer.TimePickerDialog.Builder(intent);
             }
    };

    @Override
    public void onTimeSet(photran.me.timer.TimePickerDialog dialog, int hourOfDay, int minute) {

        mHour = hourOfDay;
        mMinute = minute;
        hourArr.set(selectedPhotoPosition,String.valueOf(mHour));
        minArr.set(selectedPhotoPosition,String.valueOf(mMinute));
        time_spinner.setImageResource(R.drawable.time_red);
        LogUtil.d("time"+mHour+mMinute);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////시간 리스너
}
