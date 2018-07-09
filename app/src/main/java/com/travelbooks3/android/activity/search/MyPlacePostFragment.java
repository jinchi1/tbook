package com.travelbooks3.android.activity.search;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.search.adapter.SearchTagPostAdapter;
import com.travelbooks3.android.activity.search.data.SearchTagPostData;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import ra.genius.net.GBean;
import ra.genius.net.GExecutor;

/**
 * Created by system777 on 2017-09-22.
 */


public class MyPlacePostFragment extends Fragment implements OnMapReadyCallback,LocationListener {

    private View view;
    private GridViewWithHeaderAndFooter list;
    private SwipeRefreshLayout _refresh_lay;
    private SearchTagPostAdapter adapter;
    private int _page=1;
    private ArrayList<SearchTagPostData> searchTagPostDataArrayList;
    private String word;
    private List<Address> addr;
    private MapView mapView;
    Double latitude,longitude;
    GoogleMap map;
    String geocode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
//        LogUtil.i("word",word);


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
            gps.showSettingsAlert();
        }

        Geocoder geocoder = new Geocoder(getContext());
        try {
            addr = geocoder.getFromLocation(latitude,longitude,10);
            geocode = addr.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }



        if(view == null){

            view = inflater.inflate(R.layout.fragment_my_search_place_post, container, false);
            mapView = null;

            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            list = (GridViewWithHeaderAndFooter) view.findViewById(R.id.search_list);
            list.setNumColumns(3);
            TextView addr = (TextView)view.findViewById(R.id.addr);

            addr.setText(geocode);

            searchTagPostDataArrayList = new ArrayList<SearchTagPostData>();
            adapter = new SearchTagPostAdapter(getContext(),searchTagPostDataArrayList);
            list.setAdapter(adapter);

            list.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == 0 && view.getCount() > 0 && view.getLastVisiblePosition() >= view.getCount() - 1) {
                        loadNextPage();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }

            });

            /*
            TagDefaultDataArr = new ArrayList<TagDefaultData>();
            defaultAdpter = new TagDefaultAdapter(getContext(), TagDefaultDataArr);
            list_tag.setAdapter(defaultAdpter);

*/            _refresh_lay = (SwipeRefreshLayout) view.findViewById(R.id.pager_refresh);
            _refresh_lay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    _page = 1;

                    searchTagPostDataArrayList.clear();
                    callApi_search_place();
                    adapter.notifyDataSetChanged();


                    _refresh_lay.setRefreshing(false);
                }
            });


        }

        searchTagPostDataArrayList.clear();
        callApi_search_place();
        adapter.notifyDataSetChanged();
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
    }

    private void loadNextPage() {

        _page++;

        callApi_search_place();


    }

    private void callApi_search_place(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_USER_SEARCH_NEAR_CURRENT);

        client.addParameter("latitude",latitude);
        client.addParameter("longitude",longitude);
        client.addParameter("viewCount", 15);
        client.addParameter("page", _page);

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
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "posts");



                    JSONObject obj;

                    SearchTagPostData data = null;

                    try {


                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = SearchTagPostData.SearchTagPostJson(null, obj);

                            searchTagPostDataArrayList.add(data);

                        }
                        adapter.addItems(searchTagPostDataArrayList);
                        adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        LogUtil.d("list err = "+e.toString());

                    }
                }else if(returnCode == GReturnCode.SEARCH_PLACE_NO_DATA){

                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        map = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("hellow"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(16).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
