package com.travelbooks3.android.activity.write;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.travelbooks3.android.R;
import com.travelbooks3.android.TravelbookApp;
import com.travelbooks3.android.activity.BaseActivity;
import com.travelbooks3.android.activity.main.MainFragment;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.Alert;
import com.travelbooks3.android.util.FormatUtil;
import com.travelbooks3.android.util.ImageUtil;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.travelbooks3.android.common.Constants.SERVER_URL;
import static java.security.AccessController.getContext;

/**
 * Created by system777 on 2017-09-06.
 */

public class WriteNextActivity extends AppCompatActivity implements OnMapReadyCallback {

    ArrayList <String> photoArr;
    ArrayList <String> markerArr;
    ArrayList <String> transArr;
    ArrayList <String> latitudeArr;
    ArrayList <String> longitudeArr;
    ArrayList <String> photoIdx;
    ArrayList <String> addressArr;
    ArrayList <String> subCommentArr;
    ArrayList <String> hourArr;
    ArrayList <String> minArr;

    ProgressDialog progressDialog;

    ViewPager viewPager_write;
    EditText comment;
    TextView writeTrip;
    LinearLayout comment_lay;

    private GoogleMap _map;
    private PolylineOptions options;
    FragmentManager fragmentManager;
    LatLng sydney;

    WriteNextPhotoPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_next);
        comment=(EditText)findViewById(R.id.comment);
        viewPager_write=(ViewPager)findViewById(R.id.write_viewPager_next);
        writeTrip=(TextView)findViewById(R.id.write_trip);
        comment_lay=(LinearLayout)findViewById(R.id.write_lay);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_next);
        mapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("업로드중");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        options = new PolylineOptions();
        options.color(Color.BLACK);
        options.width(5);



        Intent next = getIntent();
        photoArr = next.getStringArrayListExtra("path");
        markerArr = next.getStringArrayListExtra("marker");
        transArr = next.getStringArrayListExtra("trans");
        latitudeArr = next.getStringArrayListExtra("latitude");
        longitudeArr = next.getStringArrayListExtra("longitude");
        addressArr = next.getStringArrayListExtra("address");
        subCommentArr = next.getStringArrayListExtra("subComment");
        hourArr = next.getStringArrayListExtra("hour");
        minArr = next.getStringArrayListExtra("minute");


        photoIdx = new ArrayList<String>();
        /*for(int i=0; i<photoArr.size(); i++)
        {
            LogUtil.d("photo: "+photoArr.get(i));
            LogUtil.d("marker: "+markerArr.get(i));
            LogUtil.d("trans: "+transArr.get(i));
            LogUtil.d("위도: "+latitudeArr.get(i));
            LogUtil.d("경도: "+longitudeArr.get(i));

            if(!markerArr.get(i).equals("N"))
            {

                if(markerArr.get(i).equals("R")) {

                    sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)),Double.parseDouble(longitudeArr.get(i)));
                    _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_red)));
                    options.add(sydney);

                }
                else if(markerArr.get(i).equals("Y"))
                {
                    sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)),Double.parseDouble(longitudeArr.get(i)));
                    _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_yellow)));
                    options.add(sydney);
                }
                else if(markerArr.get(i).equals("B"))
                {
                    sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)),Double.parseDouble(longitudeArr.get(i)));
                    _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_navy)));
                    options.add(sydney);
                }
            }



        }*/



        viewPager_write.setClipToPadding(false);
        //viewPager.setPadding(40,0,40,0);
        viewPager_write.setOffscreenPageLimit(2);
        //viewPager_write.setPageMargin((getResources().getDisplayMetrics().widthPixels /-9));

        if(photoArr!=null) {
            viewPager_write.removeAllViews();
            adapter = new WriteNextPhotoPagerAdapter(this, photoArr);

            viewPager_write.setAdapter(adapter);

            viewPager_write.setCurrentItem(0);
            viewPager_write.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {

                                    }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

        }


        writeTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callApi();

            }
        });

        comment_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(comment.getWindowToken(), 0);
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        _map = googleMap;

      /*  LatLng sydney = new LatLng(latitude, longitude);
        _map.addMarker(new MarkerOptions().position(sydney));
        _map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        _map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 19));

*/

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
                    options.add(sydney);

                }
                else if(markerArr.get(i).equals("Y"))
                {
                    sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)),Double.parseDouble(longitudeArr.get(i)));
                    _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                    options.add(sydney);
                }
                else if(markerArr.get(i).equals("B"))
                {
                    sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)),Double.parseDouble(longitudeArr.get(i)));
                    _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));
                    options.add(sydney);
                }

                /*calDistance(latitudeArr.get(i),longitudeArr.get(i),)*/

            }




        }




        /*if(sydney!=null){
            _map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
            _map.addPolyline(options);
        }*/
        int count =0;
        int zoomlevel;
        double templatitude,templongitude,focuslatitude,focuslongitude;
        double mostlatitude = -1000.0;
        double mostlongitude = -1000.0;
        double leastlatitude = 1000.0;
        double leastlongitude = 1000.0;

            for (int i = 0; i < markerArr.size(); i++) {
                if(!markerArr.get(i).equals("N")){
                    count++;
                    templatitude = Double.parseDouble(latitudeArr.get(i));
                    if (mostlatitude < templatitude) mostlatitude = templatitude;
                    if (leastlatitude > templatitude) leastlatitude = templatitude;
                    templongitude = Double.parseDouble(longitudeArr.get(i));
                    if (mostlongitude < templongitude) mostlongitude = templongitude;
                    if (leastlongitude > templongitude) leastlongitude = templongitude;

                }
            }
            focuslatitude = (mostlatitude + leastlatitude) / 2;
            focuslongitude = (mostlongitude + leastlongitude) / 2;




        LatLng focuslatlng = new LatLng(focuslatitude,focuslongitude);
        LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+focuslatlng.latitude+"aaa"+focuslatlng.longitude);
        if(focuslatlng!=null&&focuslatlng.latitude!=90.0)        {

            Double distance = calDistance(mostlatitude,mostlongitude,leastlatitude,leastlongitude);

            if(distance>8044000.28303850) zoomlevel=0;
            else if(distance>5022000.14151925) zoomlevel=1;
            else if(distance>2511000.07075963) zoomlevel=2;
            else if(distance>1255500.03537981) zoomlevel=3;
            else if(distance>627750.01768991) zoomlevel =4;
            else if(distance>313875.00884495) zoomlevel =5;
            else if(distance>156937.50442248) zoomlevel =6;
            else if(distance>78468.75221124) zoomlevel =7;
            else if(distance>39234.37610562) zoomlevel =8;
            else if(distance>19617.18805281) zoomlevel =9;
            else if(distance>9808.59402640) zoomlevel =10;
            else if(distance>4909.29701320 ) zoomlevel =11;
            else if(distance>2452.14850660 ) zoomlevel =12;
            else if(distance>1226.07425330) zoomlevel =13;
            else if(distance>613.03712665) zoomlevel =14;
            else if(distance>306.51856332) zoomlevel =15;
            else if(distance>153.25928166) zoomlevel =16;
            else if(distance>76.62964083) zoomlevel =17;
            else if(distance<76.62964083) zoomlevel = 18;
            else zoomlevel =18;
            LogUtil.d("mostlat"+mostlatitude+"mostlon"+mostlongitude+"leastlat"+leastlatitude+"leastlon"+leastlongitude);
            LogUtil.d("distance ="+distance+ " zoomlevel" + zoomlevel);
            if(count>1) {
                if (zoomlevel == 0) {
                    _map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel));
                } else {

                    _map.animateCamera(CameraUpdateFactory.newLatLngZoom(focuslatlng, zoomlevel));
                }
                _map.addPolyline(options);
            }
            else if(count==0)
            {
                LatLng a = new LatLng(37.352693,127.045898);
                _map.moveCamera(CameraUpdateFactory.newLatLngZoom(a,0));
            }
            else {
                _map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
            }
        }else {
            LatLng a = new LatLng(37.352693,127.045898);
            _map.moveCamera(CameraUpdateFactory.newLatLngZoom(a,0));
        }




    }


    private void callApi(){
        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_TRIP_CREATE_PHOTO);
        progressDialog.show();


        for(int i=0;i<photoArr.size();i++)
        {
            if(i<9) client.addParameter("file0"+String.valueOf(i+1),ImageUtil.toFile(photoArr.get(i)));
            else client.addParameter("file"+String.valueOf(i+1),ImageUtil.toFile(photoArr.get(i)));

        }

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                LogUtil.d("사진 전송실패");
                progressDialog.cancel();
                Toast.makeText(getApplicationContext(),"업로드 실패",Toast.LENGTH_SHORT).show();
            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS_PHOTO_REGISTER){
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);

                    String trip_uid = JSONUtil.getString(json, "trip_uid");
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "tripPhoto");
                    JSONObject aa;
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        try {
                            aa = jsonArray.getJSONObject(i);
                            photoIdx.add(aa.getString("idx"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        LogUtil.d("photoIdx = "+i+photoIdx.get(i));
                    }

                    callApi_TripCreate(trip_uid);

                }else {
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    private void callApi_TripCreate(String trip_uid)
    {

        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_TRIP_CREATE);
            // trip_uid
        client.addParameter("trip_uid", trip_uid);

        for(int i=0;i<photoIdx.size();i++)
        {
            client.addParameter("photos["+i+"].idx",photoIdx.get(i));
            client.addParameter("photos["+i+"].latitude",latitudeArr.get(i));
            client.addParameter("photos["+i+"].longitude",longitudeArr.get(i));
            client.addParameter("photos["+i+"].addr",addressArr.get(i));
            client.addParameter("photos["+i+"].m_color",markerArr.get(i));
            client.addParameter("photos["+i+"].trans_idx",Integer.parseInt(transArr.get(i)));
            client.addParameter("photos["+i+"].route_comment",subCommentArr.get(i));
            client.addParameter("photos["+i+"].hour",hourArr.get(i));
            client.addParameter("photos["+i+"].min",minArr.get(i));

            //client.addParameter("route["+i+"].trans_idx",transArr.get(i));
            LogUtil.d("sssssssssssssssssss"+photoIdx.get(i));
            LogUtil.d("sssssssssssssssssss"+latitudeArr.get(i));
            LogUtil.d("sssssssssssssssssss"+longitudeArr.get(i));
            LogUtil.d("sssssssssssssssssss"+addressArr.get(i));
            LogUtil.d("sssssssssssssssssss"+markerArr.get(i));
            LogUtil.d("sssssssssssssssssss"+transArr.get(i));
            LogUtil.d("sssssssssssssssssss"+subCommentArr.get(i));
            LogUtil.d("sssssssssssssssssss"+hourArr.get(i));
            LogUtil.d("sssssssssssssssssss"+minArr.get(i));

        }

        client.addParameter("comment", comment.getText());

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler()
        {
            @Override
            public void failedExecute(GBean bean)
            {
                progressDialog.cancel();
            }
        });
        client.addHandler(new GResultHandler()
        {

            @Override
            public GBean result(final GBean bean, Object result, final int returnCode, String returnMessage, final JSONObject responseData)
            {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);

                Alert alert = new Alert();

                if (returnCode == GReturnCode.SUCCESS_POSTING_REGISTER)
                {
                       /* alert.setOnCloseListener(new Alert.OnCloseListener()
                        {
                            @Override
                            public void onClose(DialogInterface dialog, int which, Object data)
                            {
                                finish();
                            }
                        });
                        alert.showAlert(getApplicationContext(), returnMessage);*/
                        LogUtil.d("returnCode : " + returnCode);
                        LogUtil.d("returnMessage : " + returnMessage);



                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(comment.getWindowToken(), 0);

                    progressDialog.cancel();
                    ((TravelbookApp)getApplicationContext()).setResumeValue(1);
                        finish();



                }
                else
                {

                   /* alert.showAlert(getApplicationContext(), returnMessage);*/

                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);

                }

                return null;

            }

        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    public double calDistance(double lat1, double lon1, double lat2, double lon2){

        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private double deg2rad(double deg){
        return (double)(deg * Math.PI / (double)180d);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad){
        return (double)(rad * (double)180d / Math.PI);
    }

    @Override
    public void onBackPressed() {


        Intent back = new Intent(WriteNextActivity.this,WriteActivity.class);
        back.putExtra("path",photoArr);
        startActivity(back);
        finish();

        //super.onBackPressed();
    }
}
