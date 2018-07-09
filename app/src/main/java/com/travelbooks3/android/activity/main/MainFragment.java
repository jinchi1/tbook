package com.travelbooks3.android.activity.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.travelbooks3.android.R;
import com.travelbooks3.android.TravelbookApp;
import com.travelbooks3.android.activity.info.InfoFragment;
import com.travelbooks3.android.activity.main.adapter.MainFragAdapter;
import com.travelbooks3.android.activity.main.adapter.MainFriendAdapter;
import com.travelbooks3.android.activity.main.model.MainFriendsData;
import com.travelbooks3.android.activity.main.model.MainListData;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.CSwipeRefreshLayout;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;

import static android.view.View.VISIBLE;

public class MainFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private View view;
    private Context context;
    private int _page = 1;
    private int _allPage = 0;

    private ListView _list;
    private LinearLayout noDataBase;
    private LinearLayout friendLay;

    private ArrayList<MainFriendsData> _friendArr;
    private MainFriendAdapter _friendAdapter;

    private ArrayList<MainListData> postsArr;
    private MainFragAdapter mainFragAdapter;

    private CSwipeRefreshLayout _refresh_lay;


    LinkedHashMap<Integer,Integer> count;

    public static MainFragment newInstance(String idx, String name) {
        Bundle args = new Bundle();
        args.putString(InfoFragment.ARG_PARAM, idx);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

/*
    public static MainFragment newInstance(String idx) {
        Bundle args = new Bundle();
        args.putString(InfoFragment.ARG_PARAM, idx);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }
*/


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.context = getContext();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _page = 1;
        count= new LinkedHashMap<Integer, Integer>();

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_main, container, false);
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

            toolbar.inflateMenu(R.menu.menu_main);                                                      /////////////////////플러스친구
            toolbar.setOnMenuItemClickListener(this);
            toolbar.showOverflowMenu();

            _list = (ListView) view.findViewById(R.id.main_list);
            _list.setDivider(null);

            //footer
            View footerNoData = getActivity().getLayoutInflater().inflate(R.layout.view_no_data_base, null);
            noDataBase = (LinearLayout) footerNoData.findViewById(R.id.no_data_base);
            _list.addFooterView(noDataBase);

            // header
            View header = getActivity().getLayoutInflater().inflate(R.layout.header_main, null);
            friendLay = (LinearLayout) header.findViewById(R.id.friend_lay);
            _list.addHeaderView(header);

            postsArr = new ArrayList<MainListData>();
            LinkedHashMap<Integer,Integer> count = new LinkedHashMap<Integer,Integer>();
            mainFragAdapter = new MainFragAdapter(getContext(), postsArr, count,_list);

            _list.setAdapter(mainFragAdapter);
            _list.setOnScrollListener(new AbsListView.OnScrollListener() {

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



            //친구추천
            RecyclerView friendList = (RecyclerView) header.findViewById(R.id.main_friend_recommand_list);
            friendList.setHasFixedSize(true);

            _friendArr = new ArrayList<MainFriendsData>();
            _friendAdapter = new MainFriendAdapter(context, getActivity(), _friendArr);
            friendList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            friendList.setAdapter(_friendAdapter);

           // refresh
            _refresh_lay = (CSwipeRefreshLayout) view.findViewById(R.id.main_refresh);
            _refresh_lay.setOnRefreshListener(new CSwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    postsArr = null;
                    postsArr = new ArrayList<MainListData>();
                    mainFragAdapter.setAddItem(postsArr);
                    _friendArr = null;
                    _friendArr = new ArrayList<MainFriendsData>();

                    _page = 1;
                    callApi_MainIndex();
                }
            });

            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            callApi_MainIndex();

        }
        return view;
    }



    /*private void loadNextPage() {
        if (_allPage > _page) {
            _page++;
            callApi_MainIndex();
        }
    }*/
    private void loadNextPage() {

             _page++;
            callApi_MainIndex();

    }
    private void callApi_MainIndex() {
        LogUtil.d("callApi_MainIndex = " + Constants.SERVER_URL_API_MAIN_INDEX + "?pager=" + _page + "&viewCount=" + Constants.VIEW_COUNT);

        GPClient client = new GPClient(getContext());
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_MAIN_INDEX);

        client.addParameter("page", _page);
        client.addParameter("viewCount", Constants.VIEW_COUNT);
        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_ARRAY));

        client.addHandler(new GFailedHandler() {

            @Override
            public void failedExecute(GBean bean) {
                _refresh_lay.setRefreshing(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(getString(R.string.txt_221));
                builder.setPositiveButton(getString(R.string.txt_1006), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callApi_MainIndex();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(getString(R.string.txt_1007), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TravelbookApp) getActivity().getApplicationContext()).logout(getActivity(), null);
                        //getActivity().finish();
                    }
                });
                builder.show();
            }
        });

        client.addHandler(new GResultHandler() {
            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {
                LogUtil.d("returnCode: " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);

                _refresh_lay.setRefreshing(false);

                if (returnCode == GReturnCode.SUCCESS) {
                    JSONObject body = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);

                    //FRIENDS
                    if (!body.isNull("users")) {
                        try {
                            JSONArray userJSONArr = JSONUtil.getJSONArray(body, "users");
                            LogUtil.d("friends list count : "+userJSONArr.length());

                            for (int i = 0; i < userJSONArr.length(); i++) {
                                _friendArr.add(MainFriendsData.MainFriendsJSON(null, userJSONArr.getJSONObject(i)));
                            }
                            _friendAdapter.addItems(_friendArr);
                        } catch (Exception e) {
                            LogUtil.d("friends list err = "+e.toString());
                        }
                    }

                    //LIST

                    JSONArray items = JSONUtil.getJSONArray(body, "posts");
                    LogUtil.d("posts.length() : " + items.length());
                    JSONObject obj;
                    MainListData data = null;

                    try{
                        for (int i = 0; i < items.length(); i++) {
                            obj = items.getJSONObject(i);
                            data = MainListData.mainListJson(null, obj);
                            postsArr.add(data);
                            LogUtil.d(obj.toString());
                        }
                        mainFragAdapter.setAddItem(postsArr);
                    }catch (Exception e){
                        LogUtil.d("list err = "+e.toString());
                    }
                } else if (returnCode == GReturnCode.FAIL_SESSION || returnCode == GReturnCode.FAIL_SESSION_DUPLICATE) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    Toast.makeText(getContext(), returnMessage, Toast.LENGTH_SHORT).show();

                    LogUtil.d(returnMessage);
                    ((TravelbookApp) getActivity().getApplicationContext()).logout(getActivity(), null);

                } /*else { // 299 데이터가 존재하지 않습니다.
                    LogUtil.d("list count = " + _list.getCount());
                    if (_list.getCount() <= 1) {
                        noDataBase.setVisibility(VISIBLE);
                    }
                }*/
                else if(returnCode == GReturnCode.NO_DATA) { // 299 데이터가 존재하지 않습니다.
                    LogUtil.d("list count = " + _list.getCount());
                        noDataBase.setVisibility(VISIBLE);
                    JSONObject body = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);

                    //FRIENDS
                    if (!body.isNull("users")) {
                        try {
                            JSONArray userJSONArr = JSONUtil.getJSONArray(body, "users");
                            LogUtil.d("friends list count : "+userJSONArr.length());

                            for (int i = 0; i < userJSONArr.length(); i++) {
                                _friendArr.add(MainFriendsData.MainFriendsJSON(null, userJSONArr.getJSONObject(i)));
                            }
                            _friendAdapter.addItems(_friendArr);
                        } catch (Exception e) {
                            LogUtil.d("friends list err = "+e.toString());
                        }
                    }

                    }

                return null;
            }
        });

        GExecutor.getInstance().cancelAndExecute(client);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        startActivity(new Intent(getActivity(), PlusFriendActivity.class));
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }





}


