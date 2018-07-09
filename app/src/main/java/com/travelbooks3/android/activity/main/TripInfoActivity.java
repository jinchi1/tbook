package com.travelbooks3.android.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.info.model.MyBookmarkData;
import com.travelbooks3.android.activity.info.model.MyInfoData;
import com.travelbooks3.android.activity.main.adapter.MainFeedInfoAdapter;
import com.travelbooks3.android.activity.main.adapter.ReplyCommentAdapter;
import com.travelbooks3.android.activity.main.model.MainListData;
import com.travelbooks3.android.activity.main.model.ReplyCommentData;
import com.travelbooks3.android.activity.news.clickPhoto.BookmarkFeedInfoAdapter;
import com.travelbooks3.android.activity.news.clickPhoto.FollowingFeedInfoAdapter;
import com.travelbooks3.android.activity.news.clickPhoto.MyInfoFeedInfoAdapter;
import com.travelbooks3.android.activity.news.clickPhoto.NewPagerFeedInfoAdapter;
import com.travelbooks3.android.activity.news.clickPhoto.SearchTagFeedInfoAdapter;
import com.travelbooks3.android.activity.news.data.FollowingPagerData;
import com.travelbooks3.android.activity.news.data.NewPagerData;
import com.travelbooks3.android.activity.search.data.SearchTagPostData;
import com.travelbooks3.android.activity.write.Gps;
import com.travelbooks3.android.activity.write.WriteActivity;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.SPUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;

import static android.view.View.GONE;

/**
 * Created by system777 on 2017-08-07.
 */

public class TripInfoActivity extends AppCompatActivity implements OnMapReadyCallback{

    private int _page = 0;
    private int _viewCount =5;
    private GoogleMap _map;
    int zoomlevel;
    ListView feed_info_list;
    ImageView add_reply_btn;
    LinearLayout add_reply_lay;
    EditText add_reply_edt;
    TextView add_reply_finish;
    ArrayList<MainListData.Photos> dataArr;
    ArrayList<FollowingPagerData.Post.Photos> FollowingdataArr;
    ArrayList<NewPagerData.MyPost.Photos> NewpagerdataArr;
    ArrayList<MyBookmarkData.Photos> MybookmarkdataArr;
    ArrayList<SearchTagPostData.Photos> SearchTagdataArr;
    ArrayList<MyInfoData.Photos> MyinfodataArr;

    ArrayList<ReplyCommentData> ReplycommentdataArr;

    MainFeedInfoAdapter mainFeedInfoAdapter;
    FollowingFeedInfoAdapter followingFeedInfoAdapter;
    NewPagerFeedInfoAdapter newPagerFeedInfoAdapter;
    BookmarkFeedInfoAdapter bookmarkFeedInfoAdapter;
    SearchTagFeedInfoAdapter searchTagFeedInfoAdapter;
    MyInfoFeedInfoAdapter myInfoFeedInfoAdapter;

    ReplyCommentAdapter replyCommentAdapter;

    String trip_uid;
    ListView feed_reply_list;
    String reply_idx;
    LinearLayout more_reply_lay;

    LatLng sydney;
    double mostlatitude =-1000.0;
    double mostlongitude =-1000.0;;
    double leastlatitude =1000.0;
    double leastlongitude =1000.0;
    double templatitude;
    double templongitude;
    double focuslatitude;
    double focuslongitude;
    private PolylineOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripinfo);
        overridePendingTransition(R.anim.grow_from_middle,R.anim.shrink_to_middle);
        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        View footer = getLayoutInflater().inflate(R.layout.activity_tripreply, null, false);
        feed_reply_list = (ListView)footer.findViewById(R.id.reply_listView);
        feed_reply_list.setDivider(null);

        feed_info_list = (ListView)findViewById(R.id.feed_info_list);
        feed_info_list.setDivider(null);
        feed_info_list.addFooterView(footer);
        feed_reply_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        feed_info_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        more_reply_lay = (LinearLayout)footer.findViewById(R.id.more_reply_lay);
        more_reply_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextPage();
            }
        });



        Intent intent = getIntent();
        dataArr = intent.getParcelableArrayListExtra("PhotoArr");
        FollowingdataArr = intent.getParcelableArrayListExtra("FollowingPhotoArr");
        NewpagerdataArr = intent.getParcelableArrayListExtra("NewPagerPhotoArr");
        MybookmarkdataArr = intent.getParcelableArrayListExtra("MybookmarkPhotoArr");
        SearchTagdataArr = intent.getParcelableArrayListExtra("SearchTagPhotoArr");
        MyinfodataArr = intent.getParcelableArrayListExtra("MyinfoPhotoArr");
        trip_uid = intent.getStringExtra("trip_uid");

        LogUtil.d("infoinfo_trip_uid: "+trip_uid);
        ReplycommentdataArr = new ArrayList<>();
        ReplycommentdataArr.clear();
        replycallApi();


        add_reply_btn = (ImageView)findViewById(R.id.add_reply_btn);
        add_reply_lay = (LinearLayout)findViewById(R.id.add_reply_lay);
        add_reply_edt = (EditText)findViewById(R.id.add_reply_edit);
        add_reply_finish = (TextView)findViewById(R.id.add_reply_finish);
        add_reply_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add_Comment=add_reply_edt.getText().toString();
                if(add_Comment.length()!=0)callApi_addReply(trip_uid,add_Comment,reply_idx);
                else {
                    Toast.makeText(getApplicationContext(),"메시지를 입력해 주세요",Toast.LENGTH_SHORT).show();
                }

            }
        });
        add_reply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply_idx=null;
                add_reply_edt.setText(null);
                add_reply_edt.requestFocus();
                add_reply_lay.setVisibility(View.VISIBLE);
                add_reply_btn.setVisibility(GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


            }
        });/*



        feed_reply_list.findViewById(R.id.rere_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply_idx=String.valueOf(ReplycommentdataArr.get(feed_reply_list.getCheckedItemPosition()).idx);
                add_reply_edt.setText(null);
                add_reply_edt.requestFocus();
                add_reply_lay.setVisibility(View.VISIBLE);
                add_reply_btn.setVisibility(GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        });*/

        feed_reply_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                view.findViewById(R.id.rere_txt).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reply_idx=String.valueOf(ReplycommentdataArr.get(ReplycommentdataArr.size() - position - 1).idx);
                        add_reply_edt.setText(null);
                        add_reply_edt.requestFocus();
                        add_reply_lay.setVisibility(View.VISIBLE);
                        add_reply_btn.setVisibility(GONE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });
            }
        });
        replyCommentAdapter = new ReplyCommentAdapter(this,ReplycommentdataArr);

        feed_reply_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final ReplyCommentData data = ReplycommentdataArr.get(ReplycommentdataArr.size() - position -1);


                if(data.user_idx == SPUtil.getInstance().getUserNumber(getApplicationContext()))
                {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(TripInfoActivity.this);
                    alert_confirm.setMessage("댓글을 삭제 하시겠습니까?")
                            .setPositiveButton("예",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    callApi_deleteReply(trip_uid,data.idx);

                                }
                            }).setNegativeButton("아니오",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alert_confirm.create();
                    alertDialog.show();
                    alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //finish();
                        }
                    });

                }
                else
                {
                    LogUtil.d("not my reply");

                }


                return false;
            }
        });


        if(dataArr!=null)
        {
            LogUtil.d("1111111111111111"+dataArr.toString());
            for(int i=0; i<dataArr.size();i++)
            {
                if(dataArr.get(i).m_color.equals("N")){
                    dataArr.remove(i);
                }
            }

            mainFeedInfoAdapter = new MainFeedInfoAdapter(getApplicationContext(),dataArr);
            feed_info_list.setAdapter(mainFeedInfoAdapter);
            feed_info_list.requestFocus();
            feed_info_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                    view.findViewById(R.id.feed_info_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            LatLng focusLatLng = new LatLng(Double.parseDouble(dataArr.get(position).latitude),Double.parseDouble(dataArr.get(position).longitude));
                            _map.animateCamera(CameraUpdateFactory.newLatLngZoom(focusLatLng,18));
                        }
                    });
                }
            });/*
            feed_info_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                    view.findViewById(R.id.feed_info_lay).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            LatLng focusLatLng = new LatLng(Double.parseDouble(dataArr.get(position).latitude),Double.parseDouble(dataArr.get(position).longitude));
                            _map.animateCamera(CameraUpdateFactory.newLatLngZoom(focusLatLng,18));
                            return false;
                        }
                    });
                    return false;
                }
            });*/

        }
        else if(FollowingdataArr!=null)
        {
            LogUtil.d("1111111111111111"+FollowingdataArr.toString());
            for(int i=0; i<FollowingdataArr.size();i++)
            {
                if(FollowingdataArr.get(i).m_color.equals("N")){
                    FollowingdataArr.remove(i);
                }
            }

            followingFeedInfoAdapter = new FollowingFeedInfoAdapter(getApplicationContext(),FollowingdataArr);
            feed_info_list.setAdapter(followingFeedInfoAdapter);
            feed_info_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    view.findViewById(R.id.feed_info_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LatLng focusLatLng = new LatLng(Double.parseDouble(FollowingdataArr.get(position).latitude),Double.parseDouble(FollowingdataArr.get(position).longitude));
                            _map.animateCamera(CameraUpdateFactory.newLatLng(focusLatLng));
                        }
                    });
                }
            });

        }
        else if(NewpagerdataArr!=null)
        {
            LogUtil.d("1111111111111111"+NewpagerdataArr.toString());
            for(int i=0; i<NewpagerdataArr.size();i++)
            {
                if(NewpagerdataArr.get(i).m_color.equals("N")){
                    NewpagerdataArr.remove(i);
                }
            }

            newPagerFeedInfoAdapter = new NewPagerFeedInfoAdapter(getApplicationContext(),NewpagerdataArr);
            feed_info_list.setAdapter(newPagerFeedInfoAdapter);
            feed_info_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    view.findViewById(R.id.feed_info_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LatLng focusLatLng = new LatLng(Double.parseDouble(NewpagerdataArr.get(position).latitude),Double.parseDouble(NewpagerdataArr.get(position).longitude));
                            _map.animateCamera(CameraUpdateFactory.newLatLng(focusLatLng));
                        }
                    });
                }
            });
        }
        else if(MybookmarkdataArr!=null)
        {
            LogUtil.d("1111111111111111"+MybookmarkdataArr.toString());
            for(int i=0; i<MybookmarkdataArr.size();i++)
            {
                if(MybookmarkdataArr.get(i).m_color.equals("N")){
                    MybookmarkdataArr.remove(i);
                }
            }

            bookmarkFeedInfoAdapter = new BookmarkFeedInfoAdapter(getApplicationContext(),MybookmarkdataArr);
            feed_info_list.setAdapter(bookmarkFeedInfoAdapter);
            feed_info_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    view.findViewById(R.id.feed_info_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LatLng focusLatLng = new LatLng(Double.parseDouble(MybookmarkdataArr.get(position).latitude),Double.parseDouble(MybookmarkdataArr.get(position).longitude));
                            _map.animateCamera(CameraUpdateFactory.newLatLng(focusLatLng));
                        }
                    });
                }
            });
        }
        else if(SearchTagdataArr!=null)
        {
            LogUtil.d("1111111111111111"+SearchTagdataArr.toString());
            for(int i=0; i<SearchTagdataArr.size();i++)
            {
                if(SearchTagdataArr.get(i).m_color.equals("N")){
                    SearchTagdataArr.remove(i);
                }
            }

            searchTagFeedInfoAdapter = new SearchTagFeedInfoAdapter(getApplicationContext(),SearchTagdataArr);
            feed_info_list.setAdapter(searchTagFeedInfoAdapter);
            feed_info_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    view.findViewById(R.id.feed_info_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LatLng focusLatLng = new LatLng(Double.parseDouble(SearchTagdataArr.get(position).latitude),Double.parseDouble(SearchTagdataArr.get(position).longitude));
                            _map.animateCamera(CameraUpdateFactory.newLatLng(focusLatLng));
                        }
                    });
                }
            });
        }
        else if(MyinfodataArr!=null)
        {
            LogUtil.d("1111111111111111"+MyinfodataArr.toString());
            for(int i=0; i<MyinfodataArr.size();i++)
            {
                if(MyinfodataArr.get(i).m_color.equals("N")){
                    MyinfodataArr.remove(i);
                }
            }

            myInfoFeedInfoAdapter = new MyInfoFeedInfoAdapter(getApplicationContext(),MyinfodataArr);
            feed_info_list.setAdapter(myInfoFeedInfoAdapter);
            feed_info_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    view.findViewById(R.id.feed_info_lay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LatLng focusLatLng = new LatLng(Double.parseDouble(MyinfodataArr.get(position).latitude),Double.parseDouble(MyinfodataArr.get(position).longitude));
                            _map.animateCamera(CameraUpdateFactory.newLatLng(focusLatLng));
                        }
                    });
                }
            });


        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        options = new PolylineOptions();
        options.color(Color.BLACK);
        options.width(5);

        //feed_info_list.requestFocus();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        _map = googleMap;


        //////////////////////////////////////////////////////////////////////////
        if (dataArr != null) {

            for (int i = 0; i < dataArr.size(); i++) {

                if (!dataArr.get(i).m_color.equals("N")) {

                    if (dataArr.get(i).m_color.equals("R")) {

                        sydney = new LatLng(Double.parseDouble(dataArr.get(i).latitude), Double.parseDouble(dataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));
                        options.add(sydney);

                    } else if (dataArr.get(i).m_color.equals("Y")) {
                        sydney = new LatLng(Double.parseDouble(dataArr.get(i).latitude), Double.parseDouble(dataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                        options.add(sydney);
                    } else if (dataArr.get(i).m_color.equals("B")) {
                        sydney = new LatLng(Double.parseDouble(dataArr.get(i).latitude), Double.parseDouble(dataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));
                        options.add(sydney);
                    }
                }
            }
        } else if (FollowingdataArr != null) {
            for (int i = 0; i < FollowingdataArr.size(); i++) {

                if (!FollowingdataArr.get(i).m_color.equals("N")) {

                    if (FollowingdataArr.get(i).m_color.equals("R")) {

                        sydney = new LatLng(Double.parseDouble(FollowingdataArr.get(i).latitude), Double.parseDouble(FollowingdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));
                        options.add(sydney);

                    } else if (FollowingdataArr.get(i).m_color.equals("Y")) {
                        sydney = new LatLng(Double.parseDouble(FollowingdataArr.get(i).latitude), Double.parseDouble(FollowingdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                        options.add(sydney);
                    } else if (FollowingdataArr.get(i).m_color.equals("B")) {
                        sydney = new LatLng(Double.parseDouble(FollowingdataArr.get(i).latitude), Double.parseDouble(FollowingdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));
                        options.add(sydney);
                    }
                }
            }

        } else if (NewpagerdataArr != null) {
            for (int i = 0; i < NewpagerdataArr.size(); i++) {

                if (!NewpagerdataArr.get(i).m_color.equals("N")) {

                    if (NewpagerdataArr.get(i).m_color.equals("R")) {

                        sydney = new LatLng(Double.parseDouble(NewpagerdataArr.get(i).latitude), Double.parseDouble(NewpagerdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));
                        options.add(sydney);

                    } else if (NewpagerdataArr.get(i).m_color.equals("Y")) {
                        sydney = new LatLng(Double.parseDouble(NewpagerdataArr.get(i).latitude), Double.parseDouble(NewpagerdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                        options.add(sydney);
                    } else if (NewpagerdataArr.get(i).m_color.equals("B")) {
                        sydney = new LatLng(Double.parseDouble(NewpagerdataArr.get(i).latitude), Double.parseDouble(NewpagerdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));
                        options.add(sydney);
                    }
                }
            }


        } else if (MybookmarkdataArr != null) {
            for (int i = 0; i < MybookmarkdataArr.size(); i++) {

                if (!MybookmarkdataArr.get(i).m_color.equals("N")) {

                    if (MybookmarkdataArr.get(i).m_color.equals("R")) {

                        sydney = new LatLng(Double.parseDouble(MybookmarkdataArr.get(i).latitude), Double.parseDouble(MybookmarkdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));
                        options.add(sydney);

                    } else if (MybookmarkdataArr.get(i).m_color.equals("Y")) {
                        sydney = new LatLng(Double.parseDouble(MybookmarkdataArr.get(i).latitude), Double.parseDouble(MybookmarkdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                        options.add(sydney);
                    } else if (MybookmarkdataArr.get(i).m_color.equals("B")) {
                        sydney = new LatLng(Double.parseDouble(MybookmarkdataArr.get(i).latitude), Double.parseDouble(MybookmarkdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));
                        options.add(sydney);
                    }
                }
            }

        } else if (SearchTagdataArr != null) {
            for (int i = 0; i < SearchTagdataArr.size(); i++) {

                if (!SearchTagdataArr.get(i).m_color.equals("N")) {

                    if (SearchTagdataArr.get(i).m_color.equals("R")) {

                        sydney = new LatLng(Double.parseDouble(SearchTagdataArr.get(i).latitude), Double.parseDouble(SearchTagdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));
                        options.add(sydney);

                    } else if (SearchTagdataArr.get(i).m_color.equals("Y")) {
                        sydney = new LatLng(Double.parseDouble(SearchTagdataArr.get(i).latitude), Double.parseDouble(SearchTagdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                        options.add(sydney);
                    } else if (SearchTagdataArr.get(i).m_color.equals("B")) {
                        sydney = new LatLng(Double.parseDouble(SearchTagdataArr.get(i).latitude), Double.parseDouble(SearchTagdataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));
                        options.add(sydney);
                    }
                }
            }
        } else if (MyinfodataArr != null) {
            for (int i = 0; i < MyinfodataArr.size(); i++) {

                if (!MyinfodataArr.get(i).m_color.equals("N")) {

                    if (MyinfodataArr.get(i).m_color.equals("R")) {

                        sydney = new LatLng(Double.parseDouble(MyinfodataArr.get(i).latitude), Double.parseDouble(MyinfodataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_red)));
                        options.add(sydney);

                    } else if (MyinfodataArr.get(i).m_color.equals("Y")) {
                        sydney = new LatLng(Double.parseDouble(MyinfodataArr.get(i).latitude), Double.parseDouble(MyinfodataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_yellow)));
                        options.add(sydney);
                    } else if (MyinfodataArr.get(i).m_color.equals("B")) {
                        sydney = new LatLng(Double.parseDouble(MyinfodataArr.get(i).latitude), Double.parseDouble(MyinfodataArr.get(i).longitude));
                        _map.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_small_navy)));
                        options.add(sydney);
                    }
                }
            }
        }


        ///////////////////////////////////////////////////////////////////////////

         int count =0;

        if (MyinfodataArr != null){
            for (int i = 0; i < MyinfodataArr.size(); i++) {
                if(!MyinfodataArr.get(i).latitude.equals("N")) {

                    count++;

                    templatitude = Double.parseDouble(MyinfodataArr.get(i).latitude);
                    if (mostlatitude < templatitude) mostlatitude = templatitude;
                    if (leastlatitude > templatitude) leastlatitude = templatitude;
                    templongitude = Double.parseDouble(MyinfodataArr.get(i).longitude);
                    if (mostlongitude < templongitude) mostlongitude = templongitude;
                    if (leastlongitude > templongitude) leastlongitude = templongitude;

                }
            }
        focuslatitude = (mostlatitude + leastlatitude) / 2;
        focuslongitude = (mostlongitude + leastlongitude) / 2;
    } else if(SearchTagdataArr != null){
            for (int i = 0; i < SearchTagdataArr.size(); i++) {
                if(!SearchTagdataArr.get(i).latitude.equals("N")) {
                    count++;
                    templatitude = Double.parseDouble(SearchTagdataArr.get(i).latitude);
                    if (mostlatitude < templatitude) mostlatitude = templatitude;
                    if (leastlatitude > templatitude) leastlatitude = templatitude;
                    templongitude = Double.parseDouble(SearchTagdataArr.get(i).longitude);
                    if (mostlongitude < templongitude) mostlongitude = templongitude;
                    if (leastlongitude > templongitude) leastlongitude = templongitude;
                }
            }
            focuslatitude = (mostlatitude + leastlatitude) / 2;
            focuslongitude = (mostlongitude + leastlongitude) / 2;


        }
        else if(MybookmarkdataArr !=null) {
            for (int i = 0; i < MybookmarkdataArr.size(); i++) {
                if(!MybookmarkdataArr.get(i).latitude.equals("N")) {
                    count++;
                    templatitude = Double.parseDouble(MybookmarkdataArr.get(i).latitude);
                    if (mostlatitude < templatitude) mostlatitude = templatitude;
                    if (leastlatitude > templatitude) leastlatitude = templatitude;
                    templongitude = Double.parseDouble(MybookmarkdataArr.get(i).longitude);
                    if (mostlongitude < templongitude) mostlongitude = templongitude;
                    if (leastlongitude > templongitude) leastlongitude = templongitude;
                }
            }
            focuslatitude = (mostlatitude + leastlatitude) / 2;
            focuslongitude = (mostlongitude + leastlongitude) / 2;


        }
        else if(NewpagerdataArr != null) {
            for (int i = 0; i < NewpagerdataArr.size(); i++) {
                if(!NewpagerdataArr.get(i).latitude.equals("N")) {
                    count++;
                    templatitude = Double.parseDouble(NewpagerdataArr.get(i).latitude);
                    if (mostlatitude < templatitude) mostlatitude = templatitude;
                    if (leastlatitude > templatitude) leastlatitude = templatitude;
                    templongitude = Double.parseDouble(NewpagerdataArr.get(i).longitude);
                    if (mostlongitude < templongitude) mostlongitude = templongitude;
                    if (leastlongitude > templongitude) leastlongitude = templongitude;
                }
            }
            focuslatitude = (mostlatitude + leastlatitude) / 2;
            focuslongitude = (mostlongitude + leastlongitude) / 2;
        }
        else if(FollowingdataArr != null) {
            for (int i = 0; i < FollowingdataArr.size(); i++) {
                if(!FollowingdataArr.get(i).latitude.equals("N")) {
                    count++;
                    templatitude = Double.parseDouble(FollowingdataArr.get(i).latitude);
                    if (mostlatitude < templatitude) mostlatitude = templatitude;
                    if (leastlatitude > templatitude) leastlatitude = templatitude;
                    templongitude = Double.parseDouble(FollowingdataArr.get(i).longitude);
                    if (mostlongitude < templongitude) mostlongitude = templongitude;
                    if (leastlongitude > templongitude) leastlongitude = templongitude;
                }
            }
            focuslatitude = (mostlatitude + leastlatitude) / 2;
            focuslongitude = (mostlongitude + leastlongitude) / 2;
        }
        else if(dataArr != null) {
            for (int i = 0; i < dataArr.size(); i++) {
                if(!dataArr.get(i).latitude.equals("N")){
                    count++;
                templatitude = Double.parseDouble(dataArr.get(i).latitude);
                if (mostlatitude < templatitude) mostlatitude = templatitude;
                if (leastlatitude > templatitude) leastlatitude = templatitude;
                templongitude = Double.parseDouble(dataArr.get(i).longitude);
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

    @Override
    public void finish(){
        super.finish();
        this.overridePendingTransition(R.anim.grow_from_middle,R.anim.shrink_to_middle);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            LogUtil.d("listAdapter is null");
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            LogUtil.d("ccccccccccccccccc"+listAdapter.getCount());
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



     private void replycallApi(){
        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();
        client.addParameter("page",_page);
        client.addParameter("viewCount", _viewCount);

        client.setUri(Constants.SERVER_URL_API_COMMENT_SELECT);

        client.addParameter("trip_uid", trip_uid);
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


                if(returnCode == GReturnCode.SUCCESS){                                  //댓글이더있을경우
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "comments");

                    JSONObject obj;

                    ReplyCommentData data = null;
                    try {

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = ReplyCommentData.replyCommentDataJSON(null, obj);

                            ReplycommentdataArr.add(data);

                        }
                        replyCommentAdapter.addItems(ReplycommentdataArr);
                        replyCommentAdapter.notifyDataSetChanged();
                        feed_reply_list.setAdapter(replyCommentAdapter);
                        setListViewHeightBasedOnChildren(feed_reply_list);




                    } catch (Exception e) {
                        LogUtil.d("reply list err = "+e.toString());

                    }
                }else if(returnCode == 292){       //댓글이아예없을경우
                    //setNoDataVisibility(true);
                    more_reply_lay.setVisibility(GONE);
                    replyCommentAdapter.notifyDataSetChanged();
                }else if(returnCode == 202)                                     //더없을경우
                {
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "comments");

                    JSONObject obj;

                    ReplyCommentData data = null;
                    more_reply_lay.setVisibility(GONE);
                    try {

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = ReplyCommentData.replyCommentDataJSON(null, obj);

                            ReplycommentdataArr.add(data);

                        }
                        replyCommentAdapter.addItems(ReplycommentdataArr);
                        replyCommentAdapter.notifyDataSetChanged();
                        feed_reply_list.setAdapter(replyCommentAdapter);
                        setListViewHeightBasedOnChildren(feed_reply_list);

                    } catch (Exception e) {
                        LogUtil.d("reply list err = "+e.toString());

                    }
                }
                setListViewHeightBasedOnChildren(feed_reply_list);

                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    private void callApi_deleteReply(String trip_uid,int idx)
    {
        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_COMMENT+"/"+idx);
        client.addParameter("trip_uid",trip_uid);
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
                if (returnCode == GReturnCode.SUCCESS_COMMENT_DELETE) {
                    LogUtil.d("댓글 삭제 성공");
                    ReplycommentdataArr.clear();
                    replycallApi();


                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }


    private void callApi_addReply(String trip_uid,String comment,String idx)
    {
        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_COMMENT);
        client.addParameter("trip_uid",trip_uid);
        client.addParameter("comment",comment);
        if(idx!=null){
            client.addParameter("idx",idx);
        }
        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
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
                if (returnCode == GReturnCode.SUCCESS_COMMENT_REGISTER) {
                    LogUtil.d("댓글 등록 성공");
                    _viewCount=ReplycommentdataArr.size()+1;
                    ReplycommentdataArr.clear();
                    //_page=1;
                    replycallApi();
                    add_reply_lay.setVisibility(GONE);
                    add_reply_btn.setVisibility(View.VISIBLE);
                    InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);




                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }

    @Override
    public void onBackPressed() {

        if(add_reply_lay.getVisibility()==View.VISIBLE)
        {
            add_reply_lay.setVisibility(GONE);
            add_reply_btn.setVisibility(View.VISIBLE);

        }
        else if(add_reply_lay.getVisibility()== GONE)
        {
            finish();
        }

    }

    private void loadNextPage() {

        //_page++;
        _viewCount +=ReplycommentdataArr.size();
        ReplycommentdataArr.clear();
        replycallApi();

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
