package com.travelbooks3.android.activity.news;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.news.adapter.NewPagerAdapter;
import com.travelbooks3.android.activity.news.data.NewPagerData;
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
import java.util.logging.Handler;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;


public class NewPagerFragment extends Fragment{
    private View view2;
    private LinearLayout noDataView2;
    private RecyclerView _list;
    private ArrayList<NewPagerData> _MyFollowingPagerDataArr;
    private ArrayList<NewPagerData.MyPost> _MyFollowingPagerPostArr;

    private SwipeRefreshLayout _refresh_lay;


    private NewPagerAdapter _Myadapter;
    private NewPagerAdapter _Refresh_Myadapter;


    private int _page                       = 1; //페이지

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater2, @Nullable ViewGroup container2, @Nullable Bundle savedInstanceState) {

            if(view2==null) {
                view2 = inflater2.inflate(R.layout.fragment_news_pager_following2, container2, false);
                //view2.invalidate();
                _list = (RecyclerView) view2.findViewById(R.id.news_pager_following_listview2);
                _MyFollowingPagerDataArr = new ArrayList<NewPagerData>();
                _MyFollowingPagerPostArr = new ArrayList<NewPagerData.MyPost>();
                noDataView2 = (LinearLayout) view2.findViewById(R.id.baseNoData2);
                _Myadapter = new NewPagerAdapter(getContext(), _MyFollowingPagerDataArr, _MyFollowingPagerPostArr);
                _list.setAdapter(_Myadapter);

                _list.setOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(RecyclerView view, int newState) {
                        if (newState == 0 && view.getChildCount() > 0 && ((LinearLayoutManager) view.getLayoutManager()).findLastVisibleItemPosition() >= view.getChildCount() - 1) {
                            loadNextPage();
                        }
                    }

                });

                _refresh_lay = (SwipeRefreshLayout) view2.findViewById(R.id.news_pager_following_refresh2);
                _refresh_lay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        _page = 1;
                        _MyFollowingPagerDataArr = new ArrayList<NewPagerData>();
                        _MyFollowingPagerPostArr = new ArrayList<NewPagerData.MyPost>();
                        callApi2();

                        _refresh_lay.setRefreshing(false);

                    }
                });

            }
        _Myadapter.notifyDataSetChanged();
        return view2;

    }

    private void callApi2(){
        GPClient client2 = new GPClient(getContext());
        client2.addProgress();

        client2.setUri(Constants.SERVER_URL_API_USER_MY_NOTI);

        client2.addParameter("page", _page);
        client2.addParameter("viewCount", Constants.VIEW_COUNT);

        client2.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client2.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                callApi2();
            }
        });
        client2.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("내소식returnCode : " + returnCode);
                LogUtil.d("내소식returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS){
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "notis");


                    JSONObject obj;

                    NewPagerData data = null;
                    NewPagerData.MyPost post = null;
                    try {

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = NewPagerData.NewPagerdataJSON(null, obj);

                            _MyFollowingPagerDataArr.add(data);
                            _MyFollowingPagerPostArr.add(data.post);

                        }
                        _Myadapter.addItems(_MyFollowingPagerDataArr,_MyFollowingPagerPostArr);
                        _Myadapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        LogUtil.d("Follwoing list err = "+e.toString());

                    }
                }else if(returnCode == GReturnCode.SEARCH_MY_NOTI_NO_DATA){
                    //setNoDataVisibility(true);
                    if(_page==1) {
                        _MyFollowingPagerDataArr.clear();
                        _MyFollowingPagerPostArr.clear();
                        _Myadapter.addItems(_MyFollowingPagerDataArr,_MyFollowingPagerPostArr);

                    }
                    _Myadapter.notifyDataSetChanged();
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client2);
    }

    public void setNoDataVisibility(boolean state){
        if(state){
            if(noDataView2 != null) noDataView2.setVisibility(View.VISIBLE);
            _list.setVisibility(View.GONE);
        }else{
            if(noDataView2 != null) noDataView2.setVisibility(View.GONE);
            _list.setVisibility(View.VISIBLE);
        }
    }

    private void loadNextPage() {

        _page++;
        callApi2();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            _MyFollowingPagerDataArr = new ArrayList<NewPagerData>();
            _MyFollowingPagerPostArr = new ArrayList<NewPagerData.MyPost>();
            callApi2();
        }
        else
        {

        }
    }

}
