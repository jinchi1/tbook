package com.travelbooks3.android.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.info.model.Mapdata;
import com.travelbooks3.android.activity.write.Gps;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;
import ra.genius.net.GHttpMethod;

/**
 * Created by system777 on 2017-08-28.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private MapView mapView;
    private GoogleMap mMap;
    private ArrayList<Mapdata> mapdataArrayList;
    double latitude;
    double longitude;
    String user_idx;

    Mapdata data = null;

    public MapFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        Gps gps;
        gps = new Gps(getContext());
        if(gps.isGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
         /*   LogUtil.i("lllll",latitude.toString());
            LogUtil.i("lllll",longitude.toString());*/
        }
        else {
            latitude = 37.56;
            longitude= 126.97;
        }
            user_idx = getArguments().getString("user_idx");
            view = inflater.inflate(R.layout.mapview, container, false);

        return view;

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView)view.findViewById(R.id.map);

        if(mapView != null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        mapcallApi(user_idx);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)).title("현재위치"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(16).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
    }

    private void mapcallApi(String user_idx){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setHttpMethod(GHttpMethod.Get);
        client.setUri(Constants.SERVER_URL+"/mypage/posting/"+user_idx+"/list");


        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS){
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONObject json2 = JSONUtil.getJSONObject(json, "mypage");
                    JSONArray jsonArray = JSONUtil.getJSONArray(json2, "photos");


                    JSONObject obj;
                    LatLng latlng;
                    try {

                        for (int i =0; i< jsonArray.length(); i++) {

                            obj = jsonArray.getJSONObject(i);
                            latlng = new LatLng(JSONUtil.getDouble(obj, "latitude"),JSONUtil.getDouble(obj, "longitude"));

               /*             mapdataArrayList.add(data);
                            if (i == 0)
                            {
                                // currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));
                                googleMap.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);
                            }
*/
                            mMap.addMarker(new MarkerOptions().position(latlng).snippet("Lat:" + latlng.latitude + "Lng:" + latlng.longitude).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));


                        }

                    } catch (Exception e) {
                        LogUtil.d("list err = "+e.toString());

                    }
                }else if(returnCode == GReturnCode.SEARCH_FOLLOWER_NOTI_NO_DATA){
                    //setNoDataVisibility(true);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

}
