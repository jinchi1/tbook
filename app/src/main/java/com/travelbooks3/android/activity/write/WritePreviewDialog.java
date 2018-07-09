package com.travelbooks3.android.activity.write;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.travelbooks3.android.R;
import com.travelbooks3.android.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by system777 on 2017-12-01.
 */

public class WritePreviewDialog extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap _map;
    ListView feed_info_list;
    ArrayList<String> markerArr;
    ArrayList<String> transArr;
    ArrayList<String> latitudeArr;
    ArrayList<String> longitudeArr;
    ArrayList<String> addressArr;
    ArrayList<String> hourArr;
    ArrayList<String> minArr;
    ArrayList<String> subcommentArr;
    WritePreviewAdapter adapter;
    LatLng sydney;
    double mostlatitude = -1000.0;
    double mostlongitude = -1000.0;
    ;
    double leastlatitude = 1000.0;
    double leastlongitude = 1000.0;
    double templatitude;
    double templongitude;
    double focuslatitude;
    double focuslongitude;
    PolylineOptions options;
    int zoomlevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new_preview);
        feed_info_list = (ListView) findViewById(R.id.info_list_preview);
        feed_info_list.setDivider(null);
        Intent intent = getIntent();
        markerArr = intent.getStringArrayListExtra("marker");
        transArr = intent.getStringArrayListExtra("trans");
        latitudeArr = intent.getStringArrayListExtra("latitude");
        longitudeArr = intent.getStringArrayListExtra("longitude");
        addressArr = intent.getStringArrayListExtra("address");
        hourArr = intent.getStringArrayListExtra("hour");
        minArr = intent.getStringArrayListExtra("min");
        subcommentArr = intent.getStringArrayListExtra("subcomment");


        for (int i = 0; i < markerArr.size(); i++) {
            if (markerArr.get(i).equals("N")) {
                markerArr.remove(i);
                transArr.remove(i);
                latitudeArr.remove(i);
                longitudeArr.remove(i);
                addressArr.remove(i);
                hourArr.remove(i);
                minArr.remove(i);
                subcommentArr.remove(i);
            }
        }
        adapter = new WritePreviewAdapter(this, markerArr, transArr, latitudeArr, longitudeArr, addressArr, hourArr, minArr,subcommentArr);
        feed_info_list.setAdapter(adapter);


        options = new PolylineOptions();
        options.color(Color.BLACK);
        options.width(5);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_preview);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        _map = googleMap;
        if (markerArr != null) {

            for (int i = 0; i < markerArr.size(); i++) {

                if (!markerArr.get(i).equals("N")) {

                    if (markerArr.get(i).equals("R")) {

                        sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)), Double.parseDouble(longitudeArr.get(i)));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));
                        options.add(sydney);

                    } else if (markerArr.get(i).equals("Y")) {
                        sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)), Double.parseDouble(longitudeArr.get(i)));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                        options.add(sydney);
                    } else if (markerArr.get(i).equals("B")) {
                        sydney = new LatLng(Double.parseDouble(latitudeArr.get(i)), Double.parseDouble(longitudeArr.get(i)));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));
                        options.add(sydney);
                    }
                }
            }

        }


        int count =0;

        if (markerArr != null){
            for (int i = 0; i < markerArr.size(); i++) {
                if(!latitudeArr.get(i).equals("N")) {

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
        }

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
                /*PatternItem Dash = new Dash(20);
                PatternItem Gap = new Gap(20);
                List<PatternItem> Pattern = Arrays.asList(Gap,Dash);
                CustomCap arrow = new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.arrow),20);
                */
                //ButtCap arrow = new ButtCap();
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

}
