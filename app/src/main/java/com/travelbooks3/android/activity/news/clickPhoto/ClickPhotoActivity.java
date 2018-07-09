package com.travelbooks3.android.activity.news.clickPhoto;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelbooks3.android.R;
import com.travelbooks3.android.TravelbookApp;
import com.travelbooks3.android.activity.BaseActivity;
import com.travelbooks3.android.activity.info.model.MyBookmarkData;
import com.travelbooks3.android.activity.info.model.MyInfoData;
import com.travelbooks3.android.activity.main.ModifyMainFragment;
import com.travelbooks3.android.activity.main.TripInfoActivity;
import com.travelbooks3.android.activity.main.adapter.MainImgPagerAdapter;
import com.travelbooks3.android.activity.main.model.MainListData;
import com.travelbooks3.android.activity.news.data.FollowingPagerData;
import com.travelbooks3.android.activity.news.data.NewPagerData;
import com.travelbooks3.android.activity.search.SearchTagPostFragment;
import com.travelbooks3.android.activity.search.data.SearchTagPostData;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.HashTag;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.OnSwipeTouchListener;
import com.travelbooks3.android.util.SPUtil;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;
import ra.genius.net.GHttpMethod;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static com.travelbooks3.android.util.FormatUtil.getReplyDateFormat;
import static com.travelbooks3.android.util.FormatUtil.getSpans;
import static java.util.Objects.isNull;

/**
 * Created by system777 on 2017-08-24.
 */

public class ClickPhotoActivity extends AppCompatActivity{

    Context context;
    String profile_photo;
    String comment;
    String regdate;
    String name;
    int user_idx;
    String trip_uid;
    int like_count;
    int reply_comment_count;
    int bookmark_count;
    String isAuth;
    String type;
    String isLike;
    String isDisLike;
    String isComment;
    String isBookmark;
    int like_idx;
    int bookmark_idx;
    int zoom_level;

    LinearLayout swipeLay;
    TextView name_txt;
    TextView time_txt;
    ViewPager sliderLayout;
    ImageView bookmark;
    TextView bookmark_txt;
    TextView write_name_txt;
    TextView write_content_txt;
    ScrollView scrollView;


    ImageView profile_Image;
    LinearLayout like_lay;
    LinearLayout bookmark_lay;
    ImageView like_button;
    TextView like_written_txt;
    LinearLayout sliderIndicator;
    RelativeLayout more_lay;
    ImageView image_one;

    ArrayList<FollowingPagerData.Post.Photos> photoArr;
    ArrayList<NewPagerData.MyPost.Photos> myphotoArr;
    ArrayList<MyBookmarkData.Photos> myBookmarkArr;
    ArrayList<SearchTagPostData.Photos> photoArr2;
    ArrayList<MyInfoData.Photos> myGridPhotoArr;
    ArrayList<MainListData.Photos> mainPhotoArr;

    FragmentManager fragmentManager;

    public static class Photos implements Parcelable {
        public int idx;
        public String photo;
        public String country;
        public String continent;
        public String addr;
        public String eng_addr;
        public String latitude;
        public String longitude;
        public String m_color;
        public int trans_idx;
        public String hour;
        public String min;
        public String route_comment;

        public Photos() {

        }

        public Photos(int idx,String photo,String country, String continent, String addr, String eng_addr, String latitude, String longitude, String m_color, int trans_idx, String hour, String min, String route_comment) {

            this.idx = idx;
            this.photo = photo;
            this.country = country;
            this.continent = continent;
            this.addr = addr;
            this.eng_addr = eng_addr;
            this.latitude = latitude;
            this.longitude = longitude;
            this.m_color = m_color;
            this.trans_idx = trans_idx;
            this.hour = hour;
            this.min = min;
            this.route_comment = route_comment;

        }
        public Photos(Parcel in){
            readFromParcel(in);
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeInt(idx);
            dest.writeString(photo);
            dest.writeString(country);
            dest.writeString(continent);
            dest.writeString(addr);
            dest.writeString(eng_addr);
            dest.writeString(latitude);
            dest.writeString(longitude);
            dest.writeString(m_color);
            dest.writeInt(trans_idx);
            dest.writeString(hour);
            dest.writeString(min);
            dest.writeString(route_comment);
        }

        public void readFromParcel(Parcel in){
            idx = in.readInt();
            photo = in.readString();
            country = in.readString();
            continent = in.readString();
            addr = in.readString();
            eng_addr = in.readString();
            latitude = in.readString();
            longitude = in.readString();
            m_color = in.readString();
            trans_idx = in.readInt();
            hour = in.readString();
            min = in.readString();
            route_comment = in.readString();
        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
            public Photos createFromParcel(Parcel in) {
                return new Photos(in);
            }

            public Photos[] newArray(int size) {
                return new Photos[size];
            }
        };

    }


    private ImgPagerAdapter adapter;
    private MyImgPagerAdapter myadapter;
    private bookmarkImgPagerAdapter bookmarkImgPagerAdapter;
    private MyInfoImgPagerAdapter myInfoImgPagerAdapter;
    private SearchTagPostImgPagerAdapter searchTagPostImgPagerAdapter;
    private MainImgPhotoAdapter mainImgPhotoAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_photo);
        context = getApplicationContext();
        Intent intent = getIntent();



        trip_uid = intent.getExtras().getString("trip_uid");
        profile_photo = intent.getExtras().getString("profile_photo");
        regdate = intent.getExtras().getString("regdate");
        name = intent.getExtras().getString("name");
        user_idx = intent.getExtras().getInt("user_idx");
        comment = intent.getExtras().getString("comment");
        like_count = intent.getExtras().getInt("like_count");
        bookmark_count = intent.getExtras().getInt("bookmark_count");
        reply_comment_count = intent.getExtras().getInt("reply_comment_count");
        type = intent.getExtras().getString("type");
        isLike = intent.getExtras().getString("isLike");
        isDisLike = intent.getExtras().getString("isDisLike");
        isComment = intent.getExtras().getString("isComment");
        isBookmark = intent.getExtras().getString("isBookmark");
        like_idx = intent.getExtras().getInt("like_idx");
        bookmark_idx = intent.getExtras().getInt("bookmark_idx");
        zoom_level = intent.getExtras().getInt("zoom_level");



        photoArr = getIntent().getParcelableArrayListExtra("photoArr");
        myphotoArr = getIntent().getParcelableArrayListExtra("myphotoArr");
        myBookmarkArr = getIntent().getParcelableArrayListExtra("bookmarkphotoArr");
        photoArr2 = getIntent().getParcelableArrayListExtra("search_tag_post_photoArr");
        myGridPhotoArr = getIntent().getParcelableArrayListExtra("myGridPhotoArr");
        mainPhotoArr = getIntent().getParcelableArrayListExtra("mainPhotoArr");

        LogUtil.d("hellow"+trip_uid);

        scrollView = (ScrollView)findViewById(R.id.scrollView2);
        swipeLay = (LinearLayout)findViewById(R.id.swipe_lay);
        name_txt =(TextView)findViewById(R.id.name_txt);
        time_txt =(TextView)findViewById(R.id.time_txt);
        bookmark =(ImageView)findViewById(R.id.bookmark);
        bookmark_txt =(TextView)findViewById(R.id.bookmark_txt);
        write_name_txt = (TextView)findViewById(R.id.write_name_txt);
        write_content_txt = (TextView)findViewById(R.id.write_content_txt);
        like_lay = (LinearLayout)findViewById(R.id.like_lay);
        like_button = (ImageView)findViewById(R.id.like);
        like_written_txt = (TextView)findViewById(R.id.like_written_txt);
        bookmark_lay = (LinearLayout)findViewById(R.id.bookmark_lay);
        profile_Image =(ImageView)findViewById(R.id.profile_img);
        sliderLayout = (ViewPager)findViewById(R.id.image_viewPager2);
        sliderIndicator = (LinearLayout)findViewById(R.id.root_indicator_lay);
        more_lay = (RelativeLayout)findViewById(R.id.more_lay);
        image_one = (ImageView)findViewById(R.id.image_one);


        name_txt.setText(name);
        time_txt.setText(getReplyDateFormat(regdate));
        bookmark_txt.setText(String.valueOf(bookmark_count));

        write_name_txt.setText(name);
        like_written_txt.setText(String.valueOf(like_count));

        more_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("bbbbbbbbbbbbbbbbbbbbb"+user_idx);

                AlertDialog.Builder builder = new AlertDialog.Builder(ClickPhotoActivity.this);

                if (user_idx == SPUtil.getInstance().getUserNumber(context)) {
                    String MyfeedOption[] = {context.getString(R.string.txt_178), context.getString(R.string.txt_179), context.getString(R.string.txt_219), context.getString(R.string.txt_220)};
                    builder.setItems(MyfeedOption, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:

                                    Intent modify = new Intent(ClickPhotoActivity.this, BaseActivity.class);
                                    modify.putExtra("trip_uid",trip_uid);
                                    modify.putExtra("comment",comment);
                                    ((TravelbookApp)getApplicationContext()).setResumeValue(10);
                                    startActivity(modify);

                                    break;
                                case 3:
                                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ClickPhotoActivity.this);
                                    alert_confirm.setMessage("포스트를 삭제 하시겠습니까?")
                                            .setPositiveButton("예",new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    callApi_deleteTrip(trip_uid);

                                                    if(photoArr!=null)      ((TravelbookApp)getApplicationContext()).setResumeValue(4);             //following
                                                    else if(myphotoArr!=null)      ((TravelbookApp)getApplicationContext()).setResumeValue(4);     //mynoti
                                                    else if(myBookmarkArr!=null) ((TravelbookApp)getApplicationContext()).setResumeValue(5);      //bookmark
                                                    else if(photoArr2!=null)   ((TravelbookApp)getApplicationContext()).setResumeValue(2);         //searchtag
                                                    else if(myGridPhotoArr!=null) ((TravelbookApp)getApplicationContext()).setResumeValue(5);     //info
                                                    else if(mainPhotoArr!=null) ((TravelbookApp)getApplicationContext()).setResumeValue(1);       //main


                                                    //notifyDataSetChanged();
                                                        /*Fragment f = new MainFragment();
                                                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                        fragmentTransaction.replace(R.id.main_base_lay, f);
                                                        //fragmentTransaction.addToBackStack(null);
                                                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                        fragmentTransaction.commit();
*/
                                                }
                                            }).setNegativeButton("아니오",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                /*                Fragment f = new MainFragment();
                                                fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.main_base_lay, f);
                                                fragmentTransaction.addToBackStack(null);
                                                //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                fragmentTransaction.commit();*/
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

                                        /*
                                        callApi_deleteTrip(data.trip_uid);
                                        notifyDataSetChanged();
                                        Fragment f = new MainFragment();
                                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.main_base_lay, f);
                                        //fragmentTransaction.addToBackStack(null);
                                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                        fragmentTransaction.commit();
*/
                                    break;
                            }
                        }
                    });
                } else {
                    String MyfeedOption[] = {context.getString(R.string.txt_177), context.getString(R.string.txt_178), context.getString(R.string.txt_179), context.getString(R.string.txt_2001)};
                    builder.setItems(MyfeedOption, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });


                }
                builder.create().show();
            }
        });

        if (!TextUtils.isEmpty(profile_photo)) {
            Glide.with(this)
                    .load(Constants.SERVER_IMG_URL + profile_photo)
                    .placeholder(R.drawable.written_face)
                    .centerCrop()
                    .thumbnail(0.1f)
                    //.skipMemoryCache(true)
                    //.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(profile_Image);
//            }
        } else {
            Glide.with(this)
                    .load(Constants.SERVER_IMG_URL + profile_photo)
                    .placeholder(R.drawable.written_face)
                    .error(R.drawable.written_face)
                    .centerCrop()
                    .thumbnail(0.1f)
                    //.skipMemoryCache(true)
                    //.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(profile_Image);
        }

        sliderIndicator.removeAllViews();

        if(photoArr!=null) {
            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[photoArr.size()];

            for (int i = 0; i < photoArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            adapter = new ImgPagerAdapter(this, photoArr);

            sliderLayout.setAdapter(adapter);

            sliderLayout.setCurrentItem(0);
            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < photoArr.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            } catch (Exception e) {
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    if (state == SCROLL_STATE_IDLE) {
                        //LogUtil.d("indicator stated [" + position + "(" + a + ")]: ");
                         if(sliderLayout.getCurrentItem()==sliderLayout.getChildCount()-1)
                        {
                            sliderLayout.setOnTouchListener(new OnSwipeTouchListener(context)
                            {
                                public void onSwipeLeft() {

                                    Intent intent = new Intent();
                                    intent.setClass(context, TripInfoActivity.class);
                                    intent.putExtra("trip_uid", trip_uid);
                                    intent.putParcelableArrayListExtra("PhotoArr",photoArr);

                                    startActivity(intent);

                                }

                                public  void onSwipeRight() {
                                    sliderLayout.setCurrentItem(sliderLayout.getCurrentItem()-1);
                                    sliderLayout.setOnTouchListener(null);
                                }
                            });
                        }

                    }

                }
            });

            swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/


                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("FollowingPhotoArr",photoArr);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });


            sliderLayout.setOnTouchListener(null);


            if(photoArr.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +photoArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }

        }else if(myphotoArr!=null){
            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[myphotoArr.size()];

            for (int i = 0; i < myphotoArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            myadapter = new MyImgPagerAdapter(this, myphotoArr);

            sliderLayout.setAdapter(myadapter);

            sliderLayout.setCurrentItem(0);
            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < myphotoArr.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            }catch(Exception e){
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    if (state == SCROLL_STATE_IDLE) {
                        //LogUtil.d("indicator stated [" + position + "(" + a + ")]: ");
                        if(sliderLayout.getCurrentItem()==sliderLayout.getChildCount()-1)
                        {
                            sliderLayout.setOnTouchListener(new OnSwipeTouchListener(context)
                            {
                                public void onSwipeLeft() {

                                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                                    intent.putParcelableArrayListExtra("NewPagerPhotoArr",myphotoArr);
                                    intent.putExtra("trip_uid",trip_uid);
                                    LogUtil.d("infoinfoinfo_trip_uid :"+trip_uid);
                                    startActivity(intent);

                                }

                                public  void onSwipeRight() {
                                    sliderLayout.setCurrentItem(sliderLayout.getCurrentItem()-1);
                                    sliderLayout.setOnTouchListener(null);
                                }
                            });
                        }

                    }
                }
            });


            swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/

                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("NewPagerPhotoArr",myphotoArr);
                    intent.putExtra("trip_uid",trip_uid);
                    LogUtil.d("infoinfoinfo_trip_uid :"+trip_uid);
                    startActivity(intent);
                }
            });

            sliderLayout.setOnTouchListener(null);


            if(myphotoArr.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +myphotoArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }

        }else if(myBookmarkArr!=null)
        {

            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[myBookmarkArr.size()];

            for (int i = 0; i < myBookmarkArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            bookmarkImgPagerAdapter = new bookmarkImgPagerAdapter(this, myBookmarkArr);

            sliderLayout.setAdapter(bookmarkImgPagerAdapter);

            sliderLayout.setCurrentItem(0);
            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < myBookmarkArr.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            } catch (Exception e) {
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == SCROLL_STATE_IDLE) {
                        //LogUtil.d("indicator stated [" + position + "(" + a + ")]: ");
                        if(sliderLayout.getCurrentItem()==sliderLayout.getChildCount()-1)
                        {
                            sliderLayout.setOnTouchListener(new OnSwipeTouchListener(context)
                            {
                                public void onSwipeLeft() {

                                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                                    intent.putParcelableArrayListExtra("MybookmarkPhotoArr",myBookmarkArr);
                                    intent.putExtra("trip_uid",trip_uid);
                                    startActivity(intent);

                                }

                                public  void onSwipeRight() {
                                    sliderLayout.setCurrentItem(sliderLayout.getCurrentItem()-1);
                                    sliderLayout.setOnTouchListener(null);
                                }
                            });
                        }

                    }
                }
            });

            swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/

                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("MybookmarkPhotoArr",myBookmarkArr);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });

            sliderLayout.setOnTouchListener(null);


            if(myBookmarkArr.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +myBookmarkArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }
        }else if(mainPhotoArr!=null)
        {

            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[mainPhotoArr.size()];

            for (int i = 0; i < mainPhotoArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            mainImgPhotoAdapter = new MainImgPhotoAdapter(this, mainPhotoArr);

            sliderLayout.setAdapter(mainImgPhotoAdapter);

            sliderLayout.setCurrentItem(0);

            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < mainPhotoArr.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            } catch (Exception e) {
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    if (state == SCROLL_STATE_IDLE) {
                        //LogUtil.d("indicator stated [" + position + "(" + a + ")]: ");
                        if(sliderLayout.getCurrentItem()==sliderLayout.getChildCount()-1)
                        {
                            sliderLayout.setOnTouchListener(new OnSwipeTouchListener(context)
                            {
                                public void onSwipeLeft() {

                                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                                    intent.putParcelableArrayListExtra("PhotoArr",mainPhotoArr);
                                    intent.putExtra("trip_uid",trip_uid);
                                    startActivity(intent);

                                }

                                public  void onSwipeRight() {
                                    sliderLayout.setCurrentItem(sliderLayout.getCurrentItem()-1);
                                    sliderLayout.setOnTouchListener(null);
                                }
                            });
                        }

                    }
                }
            });
            swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/

                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("PhotoArr",mainPhotoArr);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });

            sliderLayout.setOnTouchListener(null);


            if(mainPhotoArr.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +mainPhotoArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }
        }
        else if(myGridPhotoArr!=null)
        {

            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[myGridPhotoArr.size()];

            for (int i = 0; i < myGridPhotoArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            myInfoImgPagerAdapter = new MyInfoImgPagerAdapter(this, myGridPhotoArr);

            sliderLayout.setAdapter(myInfoImgPagerAdapter);

            sliderLayout.setCurrentItem(0);

            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < myGridPhotoArr.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            } catch (Exception e) {
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    if (state == SCROLL_STATE_IDLE) {
                        //LogUtil.d("indicator stated [" + position + "(" + a + ")]: ");
                        if(sliderLayout.getCurrentItem()==sliderLayout.getChildCount()-1)
                        {
                            sliderLayout.setOnTouchListener(new OnSwipeTouchListener(context)
                            {
                                public void onSwipeLeft() {

                                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                                    intent.putParcelableArrayListExtra("MyinfoPhotoArr",myGridPhotoArr);
                                    intent.putExtra("trip_uid",trip_uid);
                                    startActivity(intent);
                                }

                                public  void onSwipeRight() {
                                    sliderLayout.setCurrentItem(sliderLayout.getCurrentItem()-1);
                                    sliderLayout.setOnTouchListener(null);
                                }
                            });
                        }

                    }
                }
            });

            swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/

                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("MyinfoPhotoArr",myGridPhotoArr);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });

            sliderLayout.setOnTouchListener(null);


            if(myGridPhotoArr.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +myGridPhotoArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }
        }

        else if(photoArr2!=null)
        {

            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[photoArr2.size()];

            for (int i = 0; i < photoArr2.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            searchTagPostImgPagerAdapter = new SearchTagPostImgPagerAdapter(this, photoArr2);

            sliderLayout.setAdapter(searchTagPostImgPagerAdapter);

            sliderLayout.setCurrentItem(0);

            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < photoArr2.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            } catch (Exception e) {
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    if (state == SCROLL_STATE_IDLE) {
                        //LogUtil.d("indicator stated [" + position + "(" + a + ")]: ");
                        if(sliderLayout.getCurrentItem()==sliderLayout.getChildCount()-1)
                        {
                            sliderLayout.setOnTouchListener(new OnSwipeTouchListener(context)
                            {
                                public void onSwipeLeft() {


                                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                                    intent.putParcelableArrayListExtra("SearchTagPhotoArr",photoArr2);
                                    intent.putExtra("trip_uid",trip_uid);
                                    startActivity(intent);
                                }

                                public  void onSwipeRight() {
                                    sliderLayout.setCurrentItem(sliderLayout.getCurrentItem()-1);
                                    sliderLayout.setOnTouchListener(null);
                                }
                            });
                        }

                    }
                }
            });


            swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/

                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("SearchTagPhotoArr",photoArr2);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });
            sliderLayout.setOnTouchListener(null);


            if(photoArr2.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +photoArr2.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }
        }

        if("Y".equals(isLike) )
        {
            like_button.setImageResource(R.drawable.heart_red);
            like_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isLike="N";
                    like_count=like_count-1;
                    like_button.setImageResource(R.drawable.heart);
                    callApi_unclickLike("L",String.valueOf(like_idx));
                    onResume();
                }
            });


        }
        else if("N".equals(isLike)){

            like_button.setImageResource(R.drawable.heart);
            like_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLike="Y";
                    like_count=like_count+1;
                    like_button.setImageResource(R.drawable.heart_red);
                    callApi_clickLike(trip_uid,"L");
                    onResume();
                }
            });

        }

        if("Y".equals(isBookmark)) {
            bookmark.setImageResource(R.drawable.bookmark_red);
            bookmark_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isBookmark="N";
                    bookmark.setImageResource(R.drawable.info_bookmark);
                    bookmark_count=bookmark_count-1;
                    callApi_unclickbookmark(String.valueOf(bookmark_idx));
                    onResume();
                }
            });
        }else{
            bookmark.setImageResource(R.drawable.info_bookmark);
            bookmark_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookmark.setImageResource(R.drawable.bookmark_red);
                    isBookmark="Y";
                    bookmark_count=bookmark_count+1;
                    callApi_bookmark(trip_uid);
                    onResume();
                }
            });
        }


////////////////////////////////////////////////////////////해쉬태그
        String tag = "";
        tag = comment + " ";
        LogUtil.d(tag);

        ArrayList<int[]> hashtagSpans = getSpans(tag, '#');

        SpannableString tagsContent = new SpannableString(tag);

        for (int i = 0; i < hashtagSpans.size(); i++) {
            int[] span = hashtagSpans.get(i);
            int hashTagStart = span[0];
            int hashTagEnd = span[1];

            HashTag hashTag = new HashTag(this);
            hashTag.setOnClickEventListener(new HashTag.ClickEventListener() {
                @Override
                public void onClickEvent(String data) {
                    String a = "#"+data;                            //해쉬태그클릭해서 서치태그포스트프래그먼트이동할때 던져줄 번들


                    Intent bundle = new Intent();
                    //bundle.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    bundle.setClass(ClickPhotoActivity.this, BaseActivity.class);
                    bundle.putExtra("word", a);
                    bundle.putExtra("move","hashtag");
                    startActivity(bundle);

                }
            });

            tagsContent.setSpan(hashTag, hashTagStart, hashTagEnd, 0);
        }

        write_content_txt.setText(tagsContent);
        write_content_txt.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private ArrayList<int[]> getSpans(String body, char prefix){
        ArrayList<int[]> spans = new ArrayList<>();

        Pattern pattern= Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return  spans;



    }





    private void callApi_clickLike(String trip_uid, String like_type)
    {
        LogUtil.d("clickLike API");
        GPClient client = new GPClient(this);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_USER_LIKE);
        client.addParameter("trip_uid",trip_uid);
        client.addParameter("like_type",like_type);
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
                if (returnCode == GReturnCode.SUCCESS_POSTING_LIKE_REGISTER) {
                    LogUtil.d("좋아요 성공");
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONObject json2 = JSONUtil.getJSONObject(json,"like");
                    try {
                        if (!json2.isNull("idx")) like_idx = json2.getInt("idx");
                    }catch (Exception e)
                    {

                    }

                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }

    private void callApi_unclickLike(String like_type,String idx)
    {
        LogUtil.d("UNclickLike API");
        GPClient client = new GPClient(this);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_USER_LIKE_DELETE+idx);
        client.addParameter("like_type",like_type);
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
                if (returnCode == GReturnCode.SUCCESS_POSTING_LIKE_REGISTER) {
                    LogUtil.d("좋아요 삭제성공");

                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }

    private void callApi_changeLike(String like_type,String idx)
    {
        GPClient client = new GPClient(this);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_USER_LIKE_UPDATE+idx);
        client.addParameter("like_type",like_type);
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
                if (returnCode == GReturnCode.SUCCESS_POSTING_LIKE_UPDATE) {
                    LogUtil.d("좋아요 수정성공");
                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }

    private void callApi_bookmark(String trip_uid)
    {
        GPClient client = new GPClient(this);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_BOOKMARK);
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
                if (returnCode == GReturnCode.SUCCESS_POSTING_BOOKMARK_REGISTER) {
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONObject json2 = JSONUtil.getJSONObject(json,"bookmark");
                    try {
                        if (!json2.isNull("idx")) bookmark_idx = json2.getInt("idx");
                    }catch (Exception e){
                        e.printStackTrace();

                    }

                    LogUtil.d("북마크 추가 성공");

                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }

    private void callApi_unclickbookmark(String idx)
    {
        GPClient client = new GPClient(this);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_BOOKMARK_DELETE+idx);
        client.setHttpMethod(GHttpMethod.Delete);
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
                if (returnCode == GReturnCode.SUCCESS_POSTING_BOOKMARK_DELETE) {
                    LogUtil.d("북마크 삭제 성공");
                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }





    @Override
    protected void onResume() {
        super.onResume();


        name_txt.setText(name);
        time_txt.setText(getReplyDateFormat(regdate));
        bookmark_txt.setText(String.valueOf(bookmark_count));

        write_name_txt.setText(name);
        like_written_txt.setText(String.valueOf(like_count));

        more_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("bbbbbbbbbbbbbbbbbbbbb"+user_idx);

                AlertDialog.Builder builder = new AlertDialog.Builder(ClickPhotoActivity.this);

                if (user_idx == SPUtil.getInstance().getUserNumber(context)) {
                    String MyfeedOption[] = {context.getString(R.string.txt_178), context.getString(R.string.txt_179), context.getString(R.string.txt_219), context.getString(R.string.txt_220)};
                    builder.setItems(MyfeedOption, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:

                                    Intent modify = new Intent(ClickPhotoActivity.this, BaseActivity.class);
                                    modify.putExtra("move","modify");
                                    modify.putExtra("trip_uid",trip_uid);
                                    modify.putExtra("comment",comment);

                                    startActivity(modify);

                                    break;
                                case 3:
                                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ClickPhotoActivity.this);
                                    alert_confirm.setMessage("포스트를 삭제 하시겠습니까?")
                                            .setPositiveButton("예",new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    callApi_deleteTrip(trip_uid);

                                                    if(photoArr!=null)      ((TravelbookApp)getApplicationContext()).setResumeValue(4);             //following
                                                    else if(myphotoArr!=null)      ((TravelbookApp)getApplicationContext()).setResumeValue(4);     //mynoti
                                                    else if(myBookmarkArr!=null) ((TravelbookApp)getApplicationContext()).setResumeValue(5);      //bookmark
                                                    else if(photoArr2!=null)   ((TravelbookApp)getApplicationContext()).setResumeValue(2);         //searchtag
                                                    else if(myGridPhotoArr!=null) ((TravelbookApp)getApplicationContext()).setResumeValue(5);     //info
                                                    else if(mainPhotoArr!=null) ((TravelbookApp)getApplicationContext()).setResumeValue(1);       //main


                                                    //notifyDataSetChanged();
                                                        /*Fragment f = new MainFragment();
                                                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                        fragmentTransaction.replace(R.id.main_base_lay, f);
                                                        //fragmentTransaction.addToBackStack(null);
                                                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                        fragmentTransaction.commit();
*/
                                                }
                                            }).setNegativeButton("아니오",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                /*                Fragment f = new MainFragment();
                                                fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.main_base_lay, f);
                                                fragmentTransaction.addToBackStack(null);
                                                //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                fragmentTransaction.commit();*/
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

                                        /*
                                        callApi_deleteTrip(data.trip_uid);
                                        notifyDataSetChanged();
                                        Fragment f = new MainFragment();
                                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.main_base_lay, f);
                                        //fragmentTransaction.addToBackStack(null);
                                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                        fragmentTransaction.commit();
*/
                                    break;
                            }
                        }
                    });
                } else {
                    String MyfeedOption[] = {context.getString(R.string.txt_177), context.getString(R.string.txt_178), context.getString(R.string.txt_179), context.getString(R.string.txt_2001)};
                    builder.setItems(MyfeedOption, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });


                }
                builder.create().show();
            }
        });


        if (!TextUtils.isEmpty(profile_photo)) {
            Glide.with(this)
                    .load(Constants.SERVER_IMG_URL + profile_photo)
                    .placeholder(R.drawable.written_face)
                    .centerCrop()
                    .thumbnail(0.1f)
                    //.skipMemoryCache(true)
                    //.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(profile_Image);
//            }
        } else {
            Glide.with(this)
                    .load(Constants.SERVER_IMG_URL + profile_photo)
                    .placeholder(R.drawable.written_face)
                    .error(R.drawable.written_face)
                    .centerCrop()
                    .thumbnail(0.1f)
                    //.skipMemoryCache(true)
                    //.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(profile_Image);
        }

        sliderIndicator.removeAllViews();

        if(photoArr!=null) {
            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[photoArr.size()];

            for (int i = 0; i < photoArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            adapter = new ImgPagerAdapter(this, photoArr);

            sliderLayout.setAdapter(adapter);

            sliderLayout.setCurrentItem(0);

            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < photoArr.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            } catch (Exception e) {
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    if (state == SCROLL_STATE_IDLE) {
                        //LogUtil.d("indicator stated [" + position + "(" + a + ")]: ");
                        if(sliderLayout.getCurrentItem()==sliderLayout.getChildCount()-1)
                        {
                            sliderLayout.setOnTouchListener(new OnSwipeTouchListener(context)
                            {
                                public void onSwipeLeft() {


                                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                                    intent.putParcelableArrayListExtra("FollowingPhotoArr",photoArr);
                                    intent.putExtra("trip_uid",trip_uid);
                                    startActivity(intent);
                                }

                                public  void onSwipeRight() {
                                    sliderLayout.setCurrentItem(sliderLayout.getCurrentItem()-1);
                                    sliderLayout.setOnTouchListener(null);
                                }
                            });
                        }

                    }
                }
            });


            swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/

                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("FollowingPhotoArr",photoArr);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });

            sliderLayout.setOnTouchListener(null);


            if(photoArr.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +photoArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }

        }else if(myphotoArr!=null){
            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[myphotoArr.size()];

            for (int i = 0; i < myphotoArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            myadapter = new MyImgPagerAdapter(this, myphotoArr);

            sliderLayout.setAdapter(myadapter);

            sliderLayout.setCurrentItem(0);
            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < myphotoArr.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            }catch(Exception e){
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == SCROLL_STATE_IDLE) {
                        //LogUtil.d("indicator stated [" + position + "(" + a + ")]: ");
                        if(sliderLayout.getCurrentItem()==sliderLayout.getChildCount()-1)
                        {
                            sliderLayout.setOnTouchListener(new OnSwipeTouchListener(context)
                            {
                                public void onSwipeLeft() {


                                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                                    intent.putParcelableArrayListExtra("NewPagerPhotoArr",myphotoArr);
                                    intent.putExtra("trip_uid",trip_uid);
                                    startActivity(intent);
                                }

                                public  void onSwipeRight() {
                                    sliderLayout.setCurrentItem(sliderLayout.getCurrentItem()-1);
                                    sliderLayout.setOnTouchListener(null);
                                }
                            });
                        }

                    }
                }
            });

         swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/

                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("NewPagerPhotoArr",myphotoArr);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });

            sliderLayout.setOnTouchListener(null);


            if(myphotoArr.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +myphotoArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }

        }else if(myBookmarkArr!=null)
        {

            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[myBookmarkArr.size()];

            for (int i = 0; i < myBookmarkArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            bookmarkImgPagerAdapter = new bookmarkImgPagerAdapter(this, myBookmarkArr);

            sliderLayout.setAdapter(bookmarkImgPagerAdapter);

            sliderLayout.setCurrentItem(0);
            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < myBookmarkArr.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            } catch (Exception e) {
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == SCROLL_STATE_IDLE) {
                        //LogUtil.d("indicator stated [" + position + "(" + a + ")]: ");
                        if(sliderLayout.getCurrentItem()==sliderLayout.getChildCount()-1)
                        {
                            sliderLayout.setOnTouchListener(new OnSwipeTouchListener(context)
                            {
                                public void onSwipeLeft() {


                                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                                    intent.putParcelableArrayListExtra("MybookmarkPhotoArr",myBookmarkArr);
                                    intent.putExtra("trip_uid",trip_uid);
                                    startActivity(intent);
                                }

                                public  void onSwipeRight() {
                                    sliderLayout.setCurrentItem(sliderLayout.getCurrentItem()-1);
                                    sliderLayout.setOnTouchListener(null);
                                }
                            });
                        }

                    }
                }
            });

            swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/

                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("MybookmarkPhotoArr",myBookmarkArr);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });
            sliderLayout.setOnTouchListener(null);


            if(myBookmarkArr.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +myBookmarkArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }

        }else if(mainPhotoArr!=null)
        {

            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[mainPhotoArr.size()];

            for (int i = 0; i < mainPhotoArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            mainImgPhotoAdapter = new MainImgPhotoAdapter(this, mainPhotoArr);

            sliderLayout.setAdapter(mainImgPhotoAdapter);

            sliderLayout.setCurrentItem(0);
            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < mainPhotoArr.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            } catch (Exception e) {
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });


            swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/
                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("PhotoArr",mainPhotoArr);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });
            sliderLayout.setOnTouchListener(null);


            if(mainPhotoArr.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +mainPhotoArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }
        }
        else if(myGridPhotoArr!=null)
        {

            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[myGridPhotoArr.size()];

            for (int i = 0; i < myGridPhotoArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            myInfoImgPagerAdapter = new MyInfoImgPagerAdapter(this, myGridPhotoArr);

            sliderLayout.setAdapter(myInfoImgPagerAdapter);

            sliderLayout.setCurrentItem(0);
            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < myGridPhotoArr.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            } catch (Exception e) {
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });


            swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/
                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("MyinfoPhotoArr",myGridPhotoArr);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });
            sliderLayout.setOnTouchListener(null);


            if(myGridPhotoArr.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +myGridPhotoArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }
        }

        else if(photoArr2!=null)
        {

            sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[photoArr2.size()];

            for (int i = 0; i < photoArr2.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_viewpager_circle, null);
                sliderIndicator.addView(imgs[i]);
            }

            sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);


            searchTagPostImgPagerAdapter = new SearchTagPostImgPagerAdapter(this, photoArr2);

            sliderLayout.setAdapter(searchTagPostImgPagerAdapter);

            sliderLayout.setCurrentItem(0);
            sliderLayout.setOffscreenPageLimit(9);
            sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {


                    for (int i = 0; i < photoArr2.size(); i++) {
                        if (i == num) {
                            sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                sliderIndicator
                                        .getChildAt(i)
                                        .findViewById(R.id.page_img)
                                        .setBackgroundResource(R.drawable.written_photo_dot_off);
                            } catch (Exception e) {
                                LogUtil.d("익셉션!!!");
                            }
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

                     swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/
                    Intent intent = new Intent(ClickPhotoActivity.this, TripInfoActivity.class);
                    intent.putParcelableArrayListExtra("SearchTagPhotoArr",photoArr2);
                    intent.putExtra("trip_uid",trip_uid);
                    startActivity(intent);
                }
            });

            sliderLayout.setOnTouchListener(null);


            if(photoArr2.size()<2)
            {
                sliderLayout.setVisibility(View.GONE);
                image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +photoArr2.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(image_one);
            }
        }

        if("Y".equals(isLike))
        {
            like_button.setImageResource(R.drawable.heart_red);
            like_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like_button.setImageResource(R.drawable.heart);
                    isLike="N";
                    like_count=like_count-1;

                    callApi_unclickLike("L",String.valueOf(like_idx));
                    onResume();
                }
            });

         }
        else if("N".equals(isLike)){
            like_button.setImageResource(R.drawable.heart);
            like_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLike="Y";
                    like_count=like_count+1;
                    like_button.setImageResource(R.drawable.heart_red);
                    callApi_clickLike(trip_uid,"L");
                    onResume();
                }
            });

        }


        if("Y".equals(isBookmark)) {

            bookmark.setImageResource(R.drawable.bookmark_red);
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isBookmark="N";
                    bookmark_count=bookmark_count-1;
                    bookmark.setImageResource(R.drawable.info_bookmark);
                    callApi_unclickbookmark(String.valueOf(bookmark_idx));
                    onResume();
                }
            });
        }else{

            bookmark.setImageResource(R.drawable.info_bookmark);
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isBookmark="Y";
                    bookmark.setImageResource(R.drawable.bookmark_red);
                    bookmark_count=bookmark_count+1;
                    callApi_bookmark(trip_uid);
                    onResume();
                }
            });
        }


////////////////////////////////////////////////////////////해쉬태그
        String tag = "";
        tag = comment + " ";
        LogUtil.d(tag);

        ArrayList<int[]> hashtagSpans = getSpans(tag, '#');

        SpannableString tagsContent = new SpannableString(tag);

        for (int i = 0; i < hashtagSpans.size(); i++) {
            int[] span = hashtagSpans.get(i);
            int hashTagStart = span[0];
            int hashTagEnd = span[1];

            HashTag hashTag = new HashTag(this);
            hashTag.setOnClickEventListener(new HashTag.ClickEventListener() {
                @Override
                public void onClickEvent(String data) {
                    String a = "#"+data;                            //해쉬태그클릭해서 서치태그포스트프래그먼트이동할때 던져줄 번들


                    Intent bundle = new Intent();
                    //bundle.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    bundle.setClass(ClickPhotoActivity.this, BaseActivity.class);
                    bundle.putExtra("word", a);
                    bundle.putExtra("move","hashtag");
                    startActivity(bundle);


                }
            });

            tagsContent.setSpan(hashTag, hashTagStart, hashTagEnd, 0);
        }

        write_content_txt.setText(tagsContent);
        write_content_txt.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void callApi_deleteTrip(String trip_uid)
    {
        GPClient client = new GPClient(context);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_TRIP_DELETE+trip_uid);
        client.setHttpMethod(GHttpMethod.Delete);
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
                if (returnCode == GReturnCode.SUCCESS_POSTING_DELETE) {
                    LogUtil.d("포스팅 삭제 성공");
                    finish();
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
      /*  if(photoArr!=null)      ((TravelbookApp)getApplicationContext()).setResumeValue(4);             //following
        else if(myphotoArr!=null)      ((TravelbookApp)getApplicationContext()).setResumeValue(4);     //mynoti
        else if(myBookmarkArr!=null) ((TravelbookApp)getApplicationContext()).setResumeValue(5);      //bookmark
        else if(photoArr2!=null)   ((TravelbookApp)getApplicationContext()).setResumeValue(2);         //searchtag
        else if(myGridPhotoArr!=null) ((TravelbookApp)getApplicationContext()).setResumeValue(5);     //info
        else if(mainPhotoArr!=null) ((TravelbookApp)getApplicationContext()).setResumeValue(1);       //main*/
        super.onBackPressed();
    }
}
