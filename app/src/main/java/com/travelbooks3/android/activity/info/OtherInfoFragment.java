package com.travelbooks3.android.activity.info;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.info.adapter.MyBookmarkAdapter;
import com.travelbooks3.android.activity.info.adapter.MyGridAdapter;
import com.travelbooks3.android.activity.info.adapter.MyListAdapter;
import com.travelbooks3.android.activity.info.model.MyBookmarkData;
import com.travelbooks3.android.activity.info.model.MyInfoData;
import com.travelbooks3.android.activity.info.setting.AtvSetting;
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

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import ra.genius.net.GBean;
import ra.genius.net.GExecutor;
import ra.genius.net.GHttpMethod;

import static android.R.color.white;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.travelbooks3.android.R.color.color_d92051;

/**
 * Created by system777 on 2017-09-25.
 */

public class OtherInfoFragment extends Fragment implements View.OnClickListener{

  public static final String ARG_PARAM = "index";
    boolean lastitemVisibleFlag = false;

        private String _param;
        private String search_user_idx;
        private String search_type;

        private int _page = 1;

        private View view;

        MyBookmarkAdapter bookmarkAdapter;
        MyGridAdapter gridAdapter;
        MyListAdapter listAdapter;

        FragmentManager fragmentManager;

        private ArrayList<MyBookmarkData> _MyBookmarkDataArr;
        private ArrayList<MyInfoData> _MyInfoDataArr;

        private Test test;
        public TextView nameView;
        public TextView infoIntroduce;
        public TextView infoFeedNum;
        public TextView infoFollowerNum;
        public TextView infoFollowingNum;
        public ImageView profilePhotoView;
        public ImageView gridButton;
        public ImageView listButton;
        public ImageView bookmarkButton;
        public ImageView mapButton;
        public TextView followTxt;
        public LinearLayout info_following_lay;
        public LinearLayout info_follower_lay;
        public LinearLayout followBtn;

        public String user_idx;
        public String name;
        public String isFollow;
        public String follower;
        public String following;
        public String posting;
        public String profile_photo;
        public String photo_bg;
        public String website;
        public String country;
        public String email;
        public String phone;
        public String sex;
        public String follow_idx;
        public String introduce;


    private CSwipeRefreshLayout _refresh_lay;

        GridViewWithHeaderAndFooter gridView;

/*

        public static com.travelbooks3.android.activity.info.InfoFragment newInstance(String idx) {
            Bundle args = new Bundle();
            com.travelbooks3.android.activity.info.InfoFragment fragment = new com.travelbooks3.android.activity.info.InfoFragment();
            fragment.setArguments(args);
            return fragment;
        }*/

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

      /*      if(getArguments() != null){
                _param = getArguments().getString(ARG_PARAM);
            }

            android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();

*/



        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            search_user_idx=getArguments().getString("search_user_idx");
            search_type=getArguments().getString("search_type");



            if(view==null) {
                view = inflater.inflate(R.layout.fragment_info, container, false);
                gridView =(GridViewWithHeaderAndFooter)view.findViewById(R.id.info_grid);
                View header = view.inflate(this.getContext(), R.layout.fragment_other_info_header, null);
                gridView.addHeaderView(header);


                followBtn = (LinearLayout) header.findViewById(R.id.follow_lay);
                info_follower_lay = (LinearLayout)header.findViewById(R.id.info_follower_lay);
                info_following_lay = (LinearLayout)header.findViewById(R.id.info_following_lay);
                nameView = (TextView) header.findViewById(R.id.info_myName_txt);
                infoIntroduce = (TextView)header.findViewById(R.id.info_stateMeg_txt);
                infoFeedNum = (TextView) header.findViewById(R.id.info_feedNum_txt);
                infoFollowerNum = (TextView) header.findViewById(R.id.info_followerNum_txt);
                infoFollowingNum = (TextView) header.findViewById(R.id.info_followingNum_txt);
                profilePhotoView = (ImageView) header.findViewById(R.id.info_profile_img);
                gridButton = (ImageView)header.findViewById(R.id.gridButton);
                listButton = (ImageView)header.findViewById(R.id.listButton);
                bookmarkButton = (ImageView)header.findViewById(R.id.bookmarkButton);
                mapButton = (ImageView)header.findViewById(R.id.mapButton);
                followTxt = (TextView)header.findViewById(R.id.follow_txt);

                _MyBookmarkDataArr = new ArrayList<MyBookmarkData>();
                bookmarkAdapter = new MyBookmarkAdapter(this.getContext(), _MyBookmarkDataArr);

                _MyInfoDataArr = new ArrayList<MyInfoData>();
                gridAdapter = new MyGridAdapter(this.getContext(), _MyInfoDataArr);
                listAdapter = new MyListAdapter(this.getContext(), _MyInfoDataArr);

                gridView.setNumColumns(3);
                gridView.setAdapter(gridAdapter) ;

                gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {

                            loadNextPage();

                        }

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);


                    }

                });

                _refresh_lay = (CSwipeRefreshLayout) view.findViewById(R.id.main_refresh);
                _refresh_lay.setOnRefreshListener(new CSwipeRefreshLayout.OnRefreshListener() {

                    @Override
                    public void onRefresh() {

                        _page = 1;
                        callApi(search_user_idx,search_type);
                    }
                });


                followBtn.setOnClickListener(this);
                info_following_lay.setOnClickListener(this);
                info_follower_lay.setOnClickListener(this);
                gridButton.setOnClickListener(this);
                listButton.setOnClickListener(this);
                bookmarkButton.setOnClickListener(this);
                mapButton.setOnClickListener(this);


                _MyInfoDataArr.clear();
                _page=1;
                callApi(search_user_idx,search_type);

            }

            return view;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.info_following_lay:
                    Fragment info_followingF = new FollowingListFragment();
                    Bundle following_bundle = new Bundle();
                    following_bundle.putString("user_idx",user_idx);
                    info_followingF.setArguments(following_bundle);
                    fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                    fragmentTransaction2.replace(R.id.main_base_lay, info_followingF);
                    fragmentTransaction2.addToBackStack(null);
                    //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction2.commit();

                    break;
                case R.id.info_follower_lay:
                    Fragment info_followerF = new FollowerListFragment();
                    Bundle follower_bundle = new Bundle();
                    follower_bundle.putString("user_idx",user_idx);
                    info_followerF.setArguments(follower_bundle);
                    fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                    fragmentTransaction3.replace(R.id.main_base_lay, info_followerF);
                    fragmentTransaction3.addToBackStack(null);
                   // fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction3.commit();

                    break;

                case R.id.follow_lay:

                    if(isFollow!=null) {

                        if (isFollow.equals("N")) {
                            callApi_follow(search_user_idx);
                            followTxt.setText("팔로잉");
                            isFollow = "Y";
                            follower = String.valueOf(Integer.parseInt(follower) + 1);
                            infoFollowerNum.setText(follower);
                            followBtn.setBackgroundResource(R.drawable.follow2);
                            followTxt.setTextColor(getResources().getColor(color_d92051));


                        } else if (isFollow.equals("Y")) {
                            callApi_Unfollow(search_user_idx);
                            followTxt.setText("팔로우");
                            isFollow = "N";
                            follower = String.valueOf(Integer.parseInt(follower) - 1);
                            infoFollowerNum.setText(follower);
                            followBtn.setBackgroundResource(R.drawable.my_page_edit);
                            followTxt.setTextColor(getResources().getColor(white));


                        }
                    }
                    break;

  /*          case R.id.info_setting_lay:
                ((TravelbookApp) getActivity().getApplicationContext()).logout(getActivity(), null);
                break;*/
                case R.id.gridButton:
 /*               Intent intent2 = new Intent(getActivity(), MypageActivity.class);
                startActivity(intent2);*/
                    LogUtil.d("gridButton");
                    _page = 1;
                    gridButton.setImageResource(R.drawable.all_image_icon_red);
                    listButton.setImageResource(R.drawable.chart);
                    bookmarkButton.setImageResource(R.drawable.info_bookmark);
                    mapButton.setImageResource(R.drawable.spot_icon_line);
                    gridView.setNumColumns(3);
                    _MyInfoDataArr.clear();
                    search_type=null;
                    callApi(search_user_idx,search_type);
                    gridView.setAdapter(gridAdapter);
                    gridAdapter.notifyDataSetChanged();
                    gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

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
                    break;
                case R.id.listButton:
                    LogUtil.d("listButton");
                    gridButton.setImageResource(R.drawable.all_image_icon);
                    listButton.setImageResource(R.drawable.chart_red);
                    bookmarkButton.setImageResource(R.drawable.info_bookmark);
                    mapButton.setImageResource(R.drawable.spot_icon_line);
                    _page = 1;
                    gridView.setNumColumns(1);
                    _MyInfoDataArr.clear();
                    search_type=null;
                    callApi(search_user_idx,search_type);
                    gridView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                    gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            if (scrollState == 0 && view.getCount() > 0 && view.getLastVisiblePosition() >= view.getCount() - 1) {
                                LogUtil.d("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeend");
                                loadNextPage();
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }

                    });
                    break;
                case R.id.bookmarkButton:
                    LogUtil.d("bookmarkButton");
                    gridButton.setImageResource(R.drawable.all_image_icon);
                    listButton.setImageResource(R.drawable.chart);
                    bookmarkButton.setImageResource(R.drawable.bookmark_red);
                    mapButton.setImageResource(R.drawable.spot_icon_line);
                    gridView.setNumColumns(3);
                    //gridView.setAdapter(bookmarkAdapter);
                    _page = 1;
                    _MyBookmarkDataArr.clear();
                    bookcallApi(search_user_idx);
                    gridView.setAdapter(bookmarkAdapter);
                    bookmarkAdapter.notifyDataSetChanged();
                    gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            if (scrollState == 0 && view.getCount() > 0 && view.getLastVisiblePosition() >= view.getCount() - 1) {
                                bookloadNextPage();
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }

                    });

                    break;
                case R.id.mapButton:
                    LogUtil.d("mapButton");
                    Fragment f = new MapFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("user_idx",user_idx);
                    f.setArguments(bundle);
                    fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_base_lay, f);
                    fragmentTransaction.addToBackStack(null);
                   // fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.commit();




            }
        }



        private void callApi(String search_user_idx,String search_type){
            GPClient client = new GPClient(getContext());
            client.addProgress();

            client.setUri(Constants.SERVER_URL+"/user/"+search_user_idx+"/detail");

            if(search_type!=null)
            {
                client.addParameter("search_type",1);
            }
            if(_page==1)
            {
                _MyInfoDataArr.clear();
                gridAdapter.notifyDataSetChanged();
            }

            client.addParameter("page", _page);
            client.addParameter("viewCount", 30);

            client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
            client.addHandler(new GFailedHandler() {
                @Override
                public void failedExecute(GBean bean) {
                    //callApi();
                    _refresh_lay.setRefreshing(false);
                }
            });
            client.addHandler( new GResultHandler() {

                @Override
                public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    _refresh_lay.setRefreshing(false);

                    if(returnCode == GReturnCode.SUCCESS){
                        JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                        //JSONArray jsonArray = JSONUtil.getJSONArray(json, "post");
                        JSONObject json2 = JSONUtil.getJSONObject(json,"userInfo");
                        JSONObject obj;

                        try{
                            if (!json2.isNull("user_idx"))       user_idx=json2.getString("user_idx");
                            if (!json2.isNull("name"))       name=json2.getString("name");
                            if (!json2.isNull("isFollow"))       isFollow=json2.getString("isFollow");
                            if (!json2.isNull("follower"))       follower=json2.getString("follower");
                            if (!json2.isNull("following"))      following=json2.getString("following");
                            if (!json2.isNull("posting"))       posting=json2.getString("posting");
                            if (!json2.isNull("profile_photo")) profile_photo=json2.getString("profile_photo");
                            if (!json2.isNull("photo_bg"))       photo_bg=json2.getString("photo_bg");
                            if (!json2.isNull("website"))       website=json2.getString("website");
                            if (!json2.isNull("country"))       country=json2.getString("country");
                            if (!json2.isNull("email"))       email=json2.getString("email");
                            if (!json2.isNull("phone"))       phone=json2.getString("phone");
                            if (!json2.isNull("sex"))       sex=json2.getString("sex");
                            if (!json2.isNull("follow_idx"))       follow_idx=json2.getString("follow_idx");
                            if (!json2.isNull("introduce"))       introduce=json2.getString("introduce");
                            if(!json2.isNull("posts"))
                            {
                                JSONArray jsonArray = JSONUtil.getJSONArray(json2, "posts");

                                MyInfoData data = null;


                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    LogUtil.i("cccc",String.valueOf(jsonArray.length()));
                                    obj = jsonArray.getJSONObject(i);
                                    data = MyInfoData.MyInfoListJson(null, obj);


                                    _MyInfoDataArr.add(data);


                                }

                                gridAdapter.addItems(_MyInfoDataArr);



                            }



                            nameView.setText(name);
                            if(introduce!=null&&!introduce.equals("")) infoIntroduce.setText(introduce);
                            infoFeedNum.setText(posting);
                            infoFollowerNum.setText(follower);
                            infoFollowingNum.setText(following);

                            if(isFollow.equals("Y"))
                            {
                                followTxt.setText("팔로잉");
                                followBtn.setBackgroundResource(R.drawable.follow2);
                                followTxt.setTextColor(getResources().getColor(color_d92051));
                            }else if(isFollow.equals("N"))
                            {
                                followTxt.setText("팔로우");
                            }

                            if (!TextUtils.isEmpty(profile_photo)) {
                                Glide.with(getApplicationContext())
                                        .load(Constants.SERVER_IMG_URL + profile_photo)
                                        .placeholder(R.drawable.written_face)
                                        .centerCrop()
                                        .thumbnail(0.1f)
                                        //.skipMemoryCache(true)
                                        //.diskCacheStrategy(DiskCacheStrategy.RESULT)
                                        .into(profilePhotoView);
//            }
                            } else {
                                Glide.with(getApplicationContext())
                                        .load(Constants.SERVER_IMG_URL + profile_photo)
                                        .placeholder(R.drawable.written_face)
                                        .error(R.drawable.written_face)
                                        .centerCrop()
                                        .thumbnail(0.1f)
                                        //.skipMemoryCache(true)
                                        //.diskCacheStrategy(DiskCacheStrategy.RESULT)
                                        .into(profilePhotoView);
                            }

                            gridAdapter.notifyDataSetChanged();
                        }

                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }else if(returnCode == GReturnCode.SEARCH_INFO_NO_DATA){
                        LogUtil.d(returnMessage);
                    }
                    return null;
                }


            });
            GExecutor.getInstance().cancelAndExecute(client);
        }


        private void bookcallApi(final String search_user_idx){
            GPClient client = new GPClient(getContext());
            client.addProgress();

            client.setUri(Constants.SERVER_URL+"/trip/bookmark/"+search_user_idx+"/list");

            client.addParameter("page", _page);
            if(_page==1)
            {
                _MyBookmarkDataArr.clear();
                bookmarkAdapter.notifyDataSetChanged();
            }
            client.addParameter("viewCount", 30);

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

                        MyBookmarkData data = null;
                        try {

                            for (int i =0; i< jsonArray.length(); i++) {
                                obj = jsonArray.getJSONObject(i);
                                data = MyBookmarkData.MyBookmarkListJson(null, obj);

                                _MyBookmarkDataArr.add(data);

                            }
                            bookmarkAdapter.addItems(_MyBookmarkDataArr);
                            bookmarkAdapter.notifyDataSetChanged();



                        } catch (Exception e) {
                            LogUtil.d("bookmark list err = "+e.toString());

                        }
                    }else if(returnCode == GReturnCode.SEARCH_FOLLOWER_NOTI_NO_DATA){
                        //setNoDataVisibility(true);
                    }
                    return null;
                }


            });
            GExecutor.getInstance().cancelAndExecute(client);
        }





    private void callApi_follow(String user_idx)
    {
        GPClient client = new GPClient(getContext());
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_USER_FOLLOW);
        client.addParameter("follower_idx",user_idx);
        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_ARRAY));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler(new GResultHandler() {
            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {
                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);

                if (returnCode == GReturnCode.SUCCESS_FOLLOW_REGISTER) {
                    LogUtil.d("팔로우 성공");
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONObject json2 = JSONUtil.getJSONObject(json,"follow");
                    try {
                        if (!json2.isNull("idx")) follow_idx = json2.getString("idx");
                        LogUtil.d("follow idx"+follow_idx);

                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }



                    gridAdapter.notifyDataSetChanged();
                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }

    private void bookloadNextPage() {

        _page++;
        bookcallApi(search_user_idx);

    }


    private void loadNextPage() {

        _page++;
        search_type =null;
        callApi(search_user_idx,search_type);

    }

    private void callApi_Unfollow(String follow_idx)
    {
        GPClient client = new GPClient(getContext());
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_USER_FOLLOW+follow_idx);
        client.setHttpMethod(GHttpMethod.Delete);
        //client.addParameter("idx",follow_idx);
        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_ARRAY));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler(new GResultHandler() {
            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {
                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);

                if (returnCode == GReturnCode.SUCCESS_FOLLOW_DELETE) {
                    LogUtil.d("팔로우 취소성공");

                    gridAdapter.notifyDataSetChanged();
                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }

    }
