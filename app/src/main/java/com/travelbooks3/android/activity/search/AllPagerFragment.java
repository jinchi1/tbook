package com.travelbooks3.android.activity.search;

/**
 * Created by system777 on 2017-09-18.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.search.adapter.AllDefaultAdapter;
import com.travelbooks3.android.activity.search.data.AllDefaultData;
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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class AllPagerFragment extends Fragment {
    private View view;
    private AllDefaultAdapter defaultAdpter;
    private static LinearLayout noDataView;
    public static ListView list_all;
    private int _page                       = 1; //페이지
    private Context context;
    private SwipeRefreshLayout _refresh_lay;
    public static ArrayList<AllDefaultData> AllDefaultDataArr;
    private String word;


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
            list_all = (ListView) view.findViewById(R.id.search_list);
           /* View header = view.inflate(this.getContext(), R.layout.row_place_pager_header, null);
            list.addHeaderView(header);
*/
            AllDefaultDataArr = new ArrayList<AllDefaultData>();
            noDataView = (LinearLayout) view.findViewById(R.id.baseNoData);
            defaultAdpter = new AllDefaultAdapter(getContext(), AllDefaultDataArr);
            list_all.setAdapter(defaultAdpter);

            list_all.setOnScrollListener(new AbsListView.OnScrollListener() {

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
        callApi_search_default();
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
            list_all.setVisibility(View.GONE);
        }else{
            if(noDataView != null) noDataView.setVisibility(View.GONE);
            list_all.setVisibility(View.VISIBLE);
        }
    }



    private void loadNextPage() {

        _page++;


    }


    private void callApi_search_default(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_USER_SEARCH_DEFAULT);

        client.addParameter("search_type", "0");

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
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "searchHistorys");


                    JSONObject obj;

                    AllDefaultData data = null;

                    try {

                        AllDefaultDataArr.clear();

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = AllDefaultData.AllDefaultJson(null, obj);

                            AllDefaultDataArr.add(data);

                        }
                        defaultAdpter.addItems(AllDefaultDataArr);
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
/*
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
    }*/

}
