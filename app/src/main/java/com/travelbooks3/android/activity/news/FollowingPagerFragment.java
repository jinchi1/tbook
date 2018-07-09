package com.travelbooks3.android.activity.news;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.news.adapter.NewsFollowingPagerAapter;
import com.travelbooks3.android.activity.news.data.FollowingPagerData;
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

/**
 * Created by system777 on 2017-06-23.
 */

public class FollowingPagerFragment extends Fragment{
    private View view;
    private LinearLayout noDataView;
    private RecyclerView list;
    private ArrayList<FollowingPagerData> _FollowingPagerDataArr;
    private ArrayList<FollowingPagerData.Post> _FollowingPagerPostArr;
    private Context context;
    private SwipeRefreshLayout _refresh_lay;



    private NewsFollowingPagerAapter adapter;


    private int _page                       = 1; //페이지


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*    android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();*/

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null){
            view = inflater.inflate(R.layout.fragment_news_pager_following, container, false);
        list = (RecyclerView) view.findViewById(R.id.news_pager_following_listview);
        _FollowingPagerDataArr = new ArrayList<FollowingPagerData>();
        _FollowingPagerPostArr = new ArrayList<FollowingPagerData.Post>();
        noDataView = (LinearLayout) view.findViewById(R.id.baseNoData);
        adapter = new NewsFollowingPagerAapter(getContext(), _FollowingPagerDataArr, _FollowingPagerPostArr);
        list.setAdapter(adapter);


        list.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView view, int newState) {
                if (newState == 0 && view.getChildCount() > 0 && ((LinearLayoutManager) view.getLayoutManager()).findLastVisibleItemPosition() >= view.getChildCount() - 1) {
                    loadNextPage();
                }
            }

        });
        _refresh_lay = (SwipeRefreshLayout)view.findViewById(R.id.news_pager_following_refresh);
        _refresh_lay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                _page = 1;
                _FollowingPagerDataArr = new ArrayList<FollowingPagerData>();
                _FollowingPagerPostArr = new ArrayList<FollowingPagerData.Post>();
                callApi();

                _refresh_lay.setRefreshing(false);
            }
        });


    }
        _FollowingPagerDataArr.clear();
        _FollowingPagerPostArr.clear();
        callApi();

        adapter.notifyDataSetChanged();
        return view;

    }

    private void callApi(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_USER_FOLLOWER_NOTI);

        client.addParameter("page", _page);
        client.addParameter("viewCount", Constants.VIEW_COUNT);

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("팔로우returnCode : " + returnCode);
                LogUtil.d("팔로우returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS){
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "notis");


                    JSONObject obj;

                    FollowingPagerData data = null;
                    FollowingPagerData.Post post = null;
                    try {

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = FollowingPagerData.FollowingPagerdataJSON(null, obj);

                            _FollowingPagerDataArr.add(data);
                            _FollowingPagerPostArr.add(data.post);


                        }
                        adapter.addItems(_FollowingPagerDataArr,_FollowingPagerPostArr);
                        adapter.notifyDataSetChanged();



                    } catch (Exception e) {
                        LogUtil.d("Follwoing list err = "+e.toString());

                    }
                }else if(returnCode == GReturnCode.SEARCH_FOLLOWER_NOTI_NO_DATA){
                    //setNoDataVisibility(true);
                    if(_page==1){
                        _FollowingPagerDataArr.clear();
                        _FollowingPagerPostArr.clear();
                        adapter.addItems(_FollowingPagerDataArr,_FollowingPagerPostArr);
                    }
                    adapter.notifyDataSetChanged();
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    public void setNoDataVisibility(boolean state){
        if(state){
            if(noDataView != null) noDataView.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }else{
            if(noDataView != null) noDataView.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }

    private void loadNextPage() {

        _page++;
        callApi();

    }

}
