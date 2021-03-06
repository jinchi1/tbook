package com.travelbooks3.android.activity.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.search.adapter.PlaceDefaultAdpter;
import com.travelbooks3.android.activity.search.data.PlaceDefaultData;
import com.travelbooks3.android.activity.search.data.PlacePagerData;
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

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;

import static com.travelbooks3.android.activity.search.SearchPagerFragment.WORD_PARAM;

/**
 * Created by system777 on 2017-06-21.
 */

public class PlacePagerFragment extends Fragment{
    private View view;
   // private PlacePagerAdapter adapter;
    private PlaceDefaultAdpter defaultAdpter;
    private static LinearLayout noDataView;
    public static ListView list_place;
    private LinearLayout header_layout;
    private int _page                       = 1; //페이지
    private Context context;
    private SwipeRefreshLayout _refresh_lay;
    public static ArrayList<PlacePagerData> PlacePagerDataArr;
    public static ArrayList<PlaceDefaultData> PlaceDefaultDataArr;
    private String word;
    FragmentManager fragmentManager;
    Double latitude,longitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();




    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LogUtil.d("장소페이지");
        if(view == null){
            view = inflater.inflate(R.layout.fragment_search, container, false);
            list_place = (ListView) view.findViewById(R.id.search_list);
            View header = view.inflate(this.getContext(), R.layout.row_place_pager_header, null);

            list_place.addHeaderView(header);
            header_layout = (LinearLayout)header.findViewById(R.id.header_layout);


            PlaceDefaultDataArr = new ArrayList<PlaceDefaultData>();
            noDataView = (LinearLayout) view.findViewById(R.id.baseNoData);
            defaultAdpter = new PlaceDefaultAdpter(getContext(), PlaceDefaultDataArr);
            list_place.setAdapter(defaultAdpter);


            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.d("fddsafdsafdsafdsafdsafdsafdsafdsafdsafdsa");
                    Bundle bundle = new Bundle();
                    Fragment f= new MyPlacePostFragment();
                    f.setArguments(bundle);
                   fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_base_lay, f);
                    fragmentTransaction.addToBackStack(null);
                    //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.commit();

                }
            });




            _refresh_lay = (SwipeRefreshLayout) view.findViewById(R.id.place_pager_refresh);
            _refresh_lay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    _page = 1;
                    callApi_search_default();


                    _refresh_lay.setRefreshing(false);
                }
            });


        }
        return view;
    }

/*
    private void callApi(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_USER_SEARCH_PLACE);


        word="제주";


        client.addParameter("page", _page);
        client.addParameter("viewCount", Constants.VIEW_COUNT);
        client.addParameter("word", word);

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                //callApi();
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

                    PlacePagerData data = null;

                    try {

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = PlacePagerData.placePagerJson(null, obj);

                            PlacePagerDataArr.add(data);

                        }
                        adapter.addItems(PlacePagerDataArr);
                        adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        LogUtil.d("Follwoing list err = "+e.toString());

                    }
                }else if(returnCode == GReturnCode.SEARCH_PLACE_NO_DATA){
                    setNoDataVisibility(true);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }*/


    public static void setNoDataVisibility(boolean state){
        if(state){
            if(noDataView != null) noDataView.setVisibility(View.VISIBLE);
            list_place.setVisibility(View.GONE);
        }else{
            if(noDataView != null) noDataView.setVisibility(View.GONE);
            list_place.setVisibility(View.VISIBLE);
        }
    }



    private void loadNextPage() {

        _page++;


    }


    private void callApi_search_default(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_USER_SEARCH_DEFAULT);

        client.addParameter("search_type", "3");

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
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "tripPhoto");


                    JSONObject obj;

                    PlaceDefaultData data = null;

                    try {

                        PlaceDefaultDataArr.clear();

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = PlaceDefaultData.PlaceDefaultJson(null, obj);

                            PlaceDefaultDataArr.add(data);

                        }
                        defaultAdpter.addItems(PlaceDefaultDataArr);
                        defaultAdpter.notifyDataSetChanged();

                        //adapter.addItems(PlaceDefaultDataArr);
                        //adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        LogUtil.d("list err = "+e.toString());

                    }
                }else if(returnCode == GReturnCode.SEARCH_PLACE_NO_DATA){
                    setNoDataVisibility(true);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            callApi_search_default();
        }
        else
        {

        }
    }



}
