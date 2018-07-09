package com.travelbooks3.android.activity.info.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.info.InfoFragment;
import com.travelbooks3.android.activity.info.model.MyInfoData;
import com.travelbooks3.android.activity.main.LikeListFragment;
import com.travelbooks3.android.activity.main.ModifyMainFragment;
import com.travelbooks3.android.activity.main.TripInfoActivity;
import com.travelbooks3.android.activity.news.clickPhoto.ClickPhotoActivity;
import com.travelbooks3.android.activity.news.clickPhoto.MyInfoImgPagerAdapter;
import com.travelbooks3.android.activity.search.SearchTagPostFragment;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.CommonUtil;
import com.travelbooks3.android.util.FormatUtil;
import com.travelbooks3.android.util.HashTag;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.OnSwipeTouchListener;
import com.travelbooks3.android.util.SPUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;
import ra.genius.net.GHttpMethod;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static com.travelbooks3.android.R.id.baseWebviewOver;
import static com.travelbooks3.android.util.FormatUtil.getReplyDateFormat;


public class MyListAdapter extends BaseAdapter{

    private Context context;
        private ArrayList<MyInfoData> dataArr;
        private MyInfoImgPagerAdapter _adapter;
        int a,b;
        int MAX_SIZE =60;
        SpannableString tagsContent;
        String templikeidx;
        ArrayList<Integer> isMore = new ArrayList<Integer>();
    FragmentManager fragmentManager;

        public MyListAdapter(Context context, ArrayList<MyInfoData> arr) {
            this.context = context;
            this.dataArr = arr;

        }

        public void setAddItem(ArrayList<MyInfoData> addItem) {
            dataArr = addItem;
            this.notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return dataArr.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
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


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            final MyInfoData data = dataArr.get(position);


            String[] tag = new String[dataArr.size()];
            tag[position] = dataArr.get(position).comment + " ";
            LogUtil.d(tag[position]);

            ArrayList<int[]> hashtagSpans = getSpans(tag[position], '#');

            SpannableString tagsContent = new SpannableString(tag[position]);

            for (int i = 0; i < hashtagSpans.size(); i++) {
                int[] span = hashtagSpans.get(i);
                int hashTagStart = span[0];
                int hashTagEnd = span[1];

                HashTag hashTag = new HashTag(context);
                hashTag.setOnClickEventListener(new HashTag.ClickEventListener() {
                    @Override
                    public void onClickEvent(String data) {

                        String a = "#"+data;

                        Bundle bundle = new Bundle();
                        bundle.putString("word", a);
                        Fragment f = new SearchTagPostFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();


                    }
                });

                tagsContent.setSpan(hashTag, hashTagStart, hashTagEnd, 0);
            }



            if (convertView == null) {
               /* viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.row_main_fragment_list, null);
                viewHolder.profileImg = (ImageView) convertView.findViewById(R.id.profile_img);
                viewHolder.nameTxt = (TextView) convertView.findViewById(R.id.name_txt);
                viewHolder.timeTxt = (TextView) convertView.findViewById(R.id.time_txt);
                viewHolder.moreLay = (RelativeLayout) convertView.findViewById(R.id.more_lay);
                viewHolder.writeName = (TextView) convertView.findViewById(R.id.write_name_txt);
                viewHolder.sliderLayout = (ViewPager) convertView.findViewById(R.id.image_viewPager);

                ViewGroup.LayoutParams params = viewHolder.sliderLayout.getLayoutParams();
                params.height = CommonUtil.getDisplaySize(context)[0];
                viewHolder.sliderLayout.setLayoutParams(params);

                viewHolder.sliderIndicator = (LinearLayout) convertView.findViewById(R.id.root_indicator_lay);

                viewHolder.like_written_txt = (TextView) convertView.findViewById(R.id.like_written_txt);
                viewHolder.dislike_written_txt = (TextView) convertView.findViewById(R.id.dislike_written_txt);
                viewHolder.write_content_txt = (TextView) convertView.findViewById(R.id.write_content_txt);

           *//* viewHolder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);*//*
           *//* viewHolder.baseMore = (TextView) convertView.findViewById(R.id.txtMore);*//*
                viewHolder.webview = (WebView) convertView.findViewById(R.id.webview);
                viewHolder.baseWebviewOver = (View) convertView.findViewById(baseWebviewOver);
                viewHolder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
                viewHolder.txtAddressEng = (TextView) convertView.findViewById(R.id.txtAddressEng);
                viewHolder.changeAct = (ImageView) convertView.findViewById(R.id.change_act);
                viewHolder.like_lay = (LinearLayout)convertView.findViewById(R.id.like_lay);
                viewHolder.dislike_lay = (LinearLayout)convertView.findViewById(dislike_lay);
                viewHolder.like_button = (ImageView)convertView.findViewById(R.id.like_button);
                viewHolder.dislike_button = (ImageView)convertView.findViewById(R.id.dislike_button);
                viewHolder.bookmark = (ImageView)convertView.findViewById(R.id.bookmark);
                viewHolder.bookmark_txt = (TextView)convertView.findViewById(R.id.bookmark_txt);




                convertView.setTag(viewHolder);*/

                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.row_main_fragment_list, null);
                viewHolder.profileImg = (ImageView) convertView.findViewById(R.id.profile_img);
                viewHolder.nameTxt = (TextView) convertView.findViewById(R.id.name_txt);
                viewHolder.timeTxt = (TextView) convertView.findViewById(R.id.time_txt);
                viewHolder.moreLay = (RelativeLayout) convertView.findViewById(R.id.more_lay);
                viewHolder.writeName = (TextView) convertView.findViewById(R.id.write_name_txt);
                viewHolder.writeName2 = (TextView) convertView.findViewById(R.id.write_name_txt2);
                viewHolder.sliderLayout = (ViewPager) convertView.findViewById(R.id.image_viewPager);
                viewHolder.image_one = (ImageView)convertView.findViewById(R.id.image_one);
                viewHolder.like_btn = (ImageView)convertView.findViewById(R.id.like);
                viewHolder.swipeLay = (LinearLayout)convertView.findViewById(R.id.swipe_lay);

                ViewGroup.LayoutParams params = viewHolder.sliderLayout.getLayoutParams();
                params.height = CommonUtil.getDisplaySize(context)[0];
                viewHolder.sliderLayout.setLayoutParams(params);

                viewHolder.content_lay = (LinearLayout)convertView.findViewById(R.id.content_layout);
                viewHolder.sliderIndicator = (LinearLayout) convertView.findViewById(R.id.root_indicator_lay);
                viewHolder.more_txt_lay = (LinearLayout)convertView.findViewById(R.id.more_txt_lay);
                viewHolder.like_written_txt = (TextView) convertView.findViewById(R.id.like_written_txt);
                viewHolder.write_content_txt = (TextView) convertView.findViewById(R.id.write_content_txt);
                viewHolder.more_txt = (TextView)convertView.findViewById(R.id.more_txt);


           /* viewHolder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);*/
           /* viewHolder.baseMore = (TextView) convertView.findViewById(R.id.txtMore);*/
                viewHolder.webview = (WebView) convertView.findViewById(R.id.webview);
                viewHolder.baseWebviewOver = (View) convertView.findViewById(baseWebviewOver);
                viewHolder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
                viewHolder.txtAddressEng = (TextView) convertView.findViewById(R.id.txtAddressEng);
                 viewHolder.like_lay = (LinearLayout)convertView.findViewById(R.id.like_lay);
                viewHolder.bookmark = (ImageView)convertView.findViewById(R.id.bookmark);
                viewHolder.bookmark_txt = (TextView)convertView.findViewById(R.id.bookmark_txt);
                viewHolder.bookmark_lay = (LinearLayout)convertView.findViewById(R.id.bookmark_lay);





                convertView.setTag(viewHolder);



            } else {

                viewHolder = (ViewHolder) convertView.getTag();

            }

            viewHolder.sliderLayout.setOnTouchListener(null);

            if(data.photoArr.size()<2)
            {
                viewHolder.sliderLayout.setVisibility(View.GONE);
                viewHolder.image_one.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL +data.photoArr.get(0).photo)
                        //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.thumbnail(0.1f)
                        .into(viewHolder.image_one);
                /* RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                 viewHolder.image_one.setLayoutParams(params);
                 notifyDataSetChanged();
                *//* Glide.with(context).load(Constants.SERVER_IMG_URL +data.photoArr.get(0).photo)
                         .override(500,1000).into(viewHolder.image_one);
                *//*

                Glide.with(context)
                         .load(Constants.SERVER_IMG_URL +data.photoArr.get(0).photo)
                         //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                         //.thumbnail(0.1f)
                         .into(viewHolder.image_one_pre)
                         .getSize(new SizeReadyCallback() {
                     @Override
                     public void onSizeReady(int width, int height) {
                         LogUtil.d(String.valueOf(width)+"aaaaaaaaaa"+String.valueOf(height));
                         if(width*1.2<height)
                         {
                             LogUtil.d(String.valueOf(width)+"bbbbbbbb"+String.valueOf(height));
                             Glide.with(context)
                                     .load(Constants.SERVER_IMG_URL +data.photoArr.get(0).photo)
                                     .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                     //.thumbnail(0.1f)
                                     .override(400,400)
                                     .centerCrop()
                                     .into(viewHolder.image_one);
                             notifyDataSetChanged();

                         }
                         else{
                             LogUtil.d(String.valueOf(width)+"ccccccccc"+String.valueOf(height));
                             Glide.with(context)
                                     .load(Constants.SERVER_IMG_URL +data.photoArr.get(0).photo)
                                     .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                     //.thumbnail(0.1f)
                                     .into(viewHolder.image_one);
                             notifyDataSetChanged();
                         }
                     }
                 });*/


                /* if(viewHolder.image_one.getLayoutParams().height> FormatUtil.dipToPixels(context,500f))
                 {
                     LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)FormatUtil.dipToPixels(context,500f));
                    viewHolder.image_one.setLayoutParams(params);
                 }*/

            }
            else
            {
                viewHolder.sliderLayout.setVisibility(View.VISIBLE);
                viewHolder.image_one.setVisibility(View.GONE);
            }


            viewHolder.swipeLay.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/


                    Intent intent = new Intent();
                    intent.setClass(context, TripInfoActivity.class);
                    intent.putExtra("trip_uid", data.trip_uid);
                    intent.putParcelableArrayListExtra("MyinfoPhotoArr",data.photoArr);

                    context.startActivity(intent);
                }
            });

            if (!TextUtils.isEmpty(data.profile_photo)) {
//            if (data.profile_photo.endsWith("gif")) {
//                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(viewHolder.profileImg);
//                Glide.with(context)
//                        .load(Constants.SERVER_IMG_URL + data.profile_photo)
//                        .placeholder(R.drawable.written_face)
//                        .fitCenter()
//                        .thumbnail(0.1f)
//                        .skipMemoryCache(true)
//                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                        .into(imageViewTarget);
//            } else {
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL + data.profile_photo)
                        .placeholder(R.drawable.written_face)
                        .centerCrop()
                        .thumbnail(0.1f)
                        .into(viewHolder.profileImg);
//            }
            } else {
                Glide.with(context)
                        .load(Constants.SERVER_IMG_URL + data.profile_photo)
                        .placeholder(R.drawable.written_face)
                        .error(R.drawable.written_face)
                        .centerCrop()
                        .thumbnail(0.1f)
                        .into(viewHolder.profileImg);
            }

            viewHolder.nameTxt.setText(data.name);
            //viewHolder.timeTxt.setText(calculateTime(FormatStringGetDate(data.regdate)));
            viewHolder.timeTxt.setText(getReplyDateFormat(data.regdate));


            viewHolder.moreLay.setOnClickListener(onClickListener);
            viewHolder.moreLay.setTag(data);
            viewHolder.writeName.setText(data.name);
            viewHolder.bookmark_txt.setText(String.valueOf(data.bookmark_count));
            Log.i("positionTag", String.valueOf(position));

            viewHolder.like_lay.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Fragment like_list = new LikeListFragment();
                    Bundle following_bundle = new Bundle();
                    following_bundle.putString("trip_uid",data.trip_uid);
                    like_list.setArguments(following_bundle);
                    fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                    fragmentTransaction2.replace(R.id.main_base_lay, like_list);
                    fragmentTransaction2.addToBackStack(null);
                    //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction2.commit();

                    return false;
                }
            });


          /*  if("Y".equals(data.isLike) )
            {


                viewHolder.like_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        data.isLike="N";
                        data.like_count=data.like_count-1;
                        LogUtil.d("asdf"+data.like_idx);
                        callApi_unclickLike(String.valueOf(data.like_idx));

                        notifyDataSetChanged();
                    }
                });


            }
            else if("N".equals(data.isLike)){

                viewHolder.like_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.isLike="Y";
                        data.like_count=data.like_count+1;
                        callApi_clickLike(data.trip_uid,position);
                        notifyDataSetChanged();
                    }
                });



            }*/

            if("Y".equals(data.isLike))
            {

                viewHolder.like_btn.setImageResource(R.drawable.heart_red);
                //notifyDataSetChanged();

                viewHolder.like_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        data.isLike="N";
                        data.like_count=data.like_count-1;
                        LogUtil.d("asdf"+data.like_idx);
                        callApi_unclickLike(String.valueOf(data.like_idx));

                        notifyDataSetChanged();
                    }
                });


            }
            else if("N".equals(data.isLike)){
                viewHolder.like_btn.setImageResource(R.drawable.heart);
                //notifyDataSetChanged();

                viewHolder.like_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.isLike="Y";
                        data.like_count=data.like_count+1;
                        callApi_clickLike(data.trip_uid,position);
                        notifyDataSetChanged();
                    }
                });


            }

            //////////////////////////////////////////////////
            if("Y".equals(data.isBookmark)) {
                viewHolder.bookmark.setImageResource(R.drawable.bookmark_red);
                viewHolder.bookmark_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.isBookmark="N";
                        viewHolder.bookmark.setImageResource(R.drawable.info_bookmark);
                        data.bookmark_count=data.bookmark_count-1;
                        callApi_unclickbookmark(String.valueOf(data.bookmark_idx));
                        notifyDataSetChanged();
                    }
                });
            }else{

                viewHolder.bookmark.setImageResource(R.drawable.info_bookmark);
                viewHolder.bookmark_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        viewHolder.bookmark.setImageResource(R.drawable.bookmark_red);
                        data.isBookmark="Y";
                        data.bookmark_count=data.bookmark_count+1;
                        callApi_bookmark(data.trip_uid,position);
                        notifyDataSetChanged();
                    }
                });
            }



            viewHolder.sliderIndicator.removeAllViews();
            LinearLayout imgs[] = new LinearLayout[data.photoArr.size()];

            for (int i = 0; i < data.photoArr.size(); i++) {
                imgs[i] = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_viewpager_circle, null);
                viewHolder.sliderIndicator.addView(imgs[i]);
            }

            viewHolder.sliderIndicator.getChildAt(0).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);

            _adapter = new MyInfoImgPagerAdapter(context, data.photoArr);
            viewHolder.sliderLayout.setAdapter(_adapter);
            viewHolder.sliderLayout.setCurrentItem(0);
            viewHolder.sliderLayout.setOffscreenPageLimit(9);
            viewHolder.sliderLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {
                    LogUtil.d("indicator count [" + position + "(" + num + ")]: " + viewHolder.sliderIndicator.getChildCount());


                    for (int i = 0; i < data.photoArr.size(); i++) {
                        if (i == num) {
                            viewHolder.sliderIndicator.getChildAt(i).findViewById(R.id.page_img).setBackgroundResource(R.drawable.written_photo_dot);
                        } else {
                            try {
                                LogUtil.d("indicator [" + num + "] count [" + position + "] " + viewHolder.sliderIndicator.getChildCount());
                                viewHolder.sliderIndicator
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
                        LogUtil.d(String.valueOf(b));
                        //if(listView.getFirstVisiblePosition()==position)count.put(listView.getFirstVisiblePosition(),viewHolder.sliderLayout.getCurrentItem());

                        if(viewHolder.sliderLayout.getCurrentItem()==viewHolder.sliderLayout.getChildCount()-1)
                        {
                            viewHolder.sliderLayout.setOnTouchListener(new OnSwipeTouchListener(context)
                            {
                                public void onSwipeLeft() {

/*
                MainListData mapData = (MainListData) .getTag();
                //LogUtil.d(mapData.latitude+mapData.longitude);
*/


                                    Intent intent = new Intent();
                                    intent.setClass(context, TripInfoActivity.class);
                                    intent.putExtra("trip_uid", data.trip_uid);
                                    intent.putParcelableArrayListExtra("MyinfoPhotoArr",data.photoArr);

                                    context.startActivity(intent);

                                }

                                public  void onSwipeRight() {
                                    viewHolder.sliderLayout.setCurrentItem(viewHolder.sliderLayout.getCurrentItem()-1);
                                    viewHolder.sliderLayout.setOnTouchListener(null);
                                }
                            });
                        }

                    }

                }
            });

            viewHolder.like_written_txt.setText(data.like_count + "");

            TextView name = new TextView(context);
            name.setText(data.name);

            viewHolder.write_content_txt.setText(tagsContent);
            viewHolder.write_content_txt.setMovementMethod(LinkMovementMethod.getInstance());

            if (tagsContent.length()>MAX_SIZE || viewHolder.write_content_txt.getLineCount()>2)
            {
                viewHolder.write_content_txt.setSingleLine();
                viewHolder.write_content_txt.setHorizontallyScrolling(false);
                viewHolder.more_txt.setVisibility(View.VISIBLE);
                viewHolder.write_content_txt.setVisibility(View.GONE);
                viewHolder.write_content_txt.setVisibility(View.VISIBLE);

            }
            else
            {
                viewHolder.more_txt.setVisibility(View.GONE);
                viewHolder.write_content_txt.setMaxLines(2);
                viewHolder.write_content_txt.setSingleLine(false);
                viewHolder.write_content_txt.setVisibility(View.GONE);
                viewHolder.write_content_txt.setVisibility(View.VISIBLE);

            }

            viewHolder.more_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   LogUtil.d("자세히보기");
                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.putExtra("trip_uid",data.trip_uid);
                    intent.putExtra("comment",data.comment);
                    intent.putExtra("profile_photo",data.profile_photo);
                    intent.putExtra("regdate",data.regdate);
                    intent.putExtra("name",data.name);
                    intent.putExtra("user_idx",data.user_idx);
                    intent.putExtra("like_count",data.like_count);
                    intent.putExtra("disLike_count",data.dislike_count);
                    intent.putExtra("reply_comment_count",data.reply_comment_count);
                    intent.putExtra("isAuth",data.isAuth);
                    intent.putExtra("type",data.type);
                    intent.putExtra("isLike",data.isLike);
                    intent.putExtra("isDisLike", data.isDislike);
                    intent.putExtra("isComment",data.isComment);
                    intent.putExtra("isBookmark",data.isBookmark);
                    intent.putExtra("like_idx",data.like_idx);
                    intent.putExtra("bookmark_idx",data.bookmark_idx);
                    intent.putExtra("bookmark_count",data.bookmark_count);
                    intent.putExtra("zoom_level",data.zoom_level);
                    intent.putParcelableArrayListExtra("myGridPhotoArr",data.photoArr);

                    context.startActivity(intent);*/

                    viewHolder.write_content_txt.setMaxLines(30);
                    viewHolder.content_lay.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    viewHolder.more_txt.setVisibility(View.GONE);
                    isMore.add(position);
                    LogUtil.d(isMore.get(0).toString());



                }
            });


            if(isMore.contains(position))
            {
                viewHolder.write_content_txt.setMaxLines(30);
                viewHolder.content_lay.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                viewHolder.more_txt.setVisibility(View.GONE);
            }else
            {
                if (tagsContent.length()>MAX_SIZE || viewHolder.write_content_txt.getLineCount()>2)
                {
                    viewHolder.write_content_txt.setSingleLine();
                    viewHolder.write_content_txt.setHorizontallyScrolling(false);
                    viewHolder.more_txt.setVisibility(View.VISIBLE);
                    viewHolder.write_content_txt.setVisibility(View.GONE);
                    viewHolder.write_content_txt.setVisibility(View.VISIBLE);

                }
                else
                {
                    viewHolder.more_txt.setVisibility(View.GONE);
                    viewHolder.write_content_txt.setMaxLines(2);
                    viewHolder.write_content_txt.setSingleLine(false);
                    viewHolder.write_content_txt.setVisibility(View.GONE);
                    viewHolder.write_content_txt.setVisibility(View.VISIBLE);

                }
            }



            return convertView;
        }
/*        private String setMapWebview(MyInfoData data) {
            int zoom_level = data.zoom_level;
            zoom_level = zoom_level < 1 ? 14 : zoom_level;


            int w = CommonUtil.ConvertPixelToDp(context, CommonUtil.getDisplaySize(context)[0]);

            String getMapURL = "http://maps.googleapis.com/maps/api/staticmap?zoom=" + zoom_level + "&size=" + w + "x160&markers=size:mid|color:red|" + data.latitude + "," + data.longitude + "&sensor=false";
            LogUtil.d("latitude="+data.latitude.toString());

            return getMapURL;
        }*/


        private class ViewHolder {
            LinearLayout swipeLay;
            ImageView profileImg;
            TextView nameTxt;
            TextView timeTxt;
            RelativeLayout moreLay;


            ViewPager sliderLayout;
            ImageView image_one;

            LinearLayout sliderIndicator;
            LinearLayout more_txt_lay;
            LinearLayout content_lay;
            ImageView like_btn;

            TextView more_txt;

            TextView like_written_txt;
            TextView write_content_txt;
            TextView hash_txt;

            TextView txtComment;
            TextView baseMore;
            WebView webview;
            TextView txtAddress;
            TextView txtAddressEng;
            View baseWebviewOver;
            TextView writeName;
            TextView writeName2;

            LinearLayout like_lay;
            ImageView bookmark;
            TextView bookmark_txt;
            LinearLayout bookmark_lay;

        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.more_lay:
                        final MyInfoData data = (MyInfoData) v.getTag();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        if (data.user_idx == SPUtil.getInstance().getUserNumber(context)) {
                            String MyfeedOption[] = {context.getString(R.string.txt_178), context.getString(R.string.txt_179), context.getString(R.string.txt_219), context.getString(R.string.txt_220)};
                            builder.setItems(MyfeedOption, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            Toast.makeText(context, context.getString(R.string.txt_152), Toast.LENGTH_SHORT).show();
                                            break;
                                        case 1:
                                            Toast.makeText(context, context.getString(R.string.txt_179), Toast.LENGTH_SHORT).show();
                                            break;
                                        case 2:
                                            Bundle bundle1 = new Bundle();
                                            bundle1.putString("trip_uid", data.trip_uid);
                                            bundle1.putString("comment",data.comment);
                                            bundle1.putString("command","info");
                                            fragmentManager =  ((AppCompatActivity) context).getSupportFragmentManager();
                                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            Fragment other = new ModifyMainFragment();
                                            other.setArguments(bundle1);
                                            fragmentTransaction.replace(R.id.main_base_lay, other);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                            break;
                                        case 3:

                                            //callApi_deleteTrip(data.trip_uid);
                                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
                                            alert_confirm.setMessage("포스트를 삭제 하시겠습니까?")
                                                    .setPositiveButton("예",new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            callApi_deleteTrip(data.trip_uid);
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
/*
                                                    Fragment f = new InfoFragment();
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
                                            Toast.makeText(context, context.getString(R.string.txt_177), Toast.LENGTH_SHORT).show();
                                            break;
                                        case 1:
                                            Toast.makeText(context, context.getString(R.string.txt_178), Toast.LENGTH_SHORT).show();
                                            break;
                                        case 2:
                                            Toast.makeText(context, context.getString(R.string.txt_179), Toast.LENGTH_SHORT).show();
                                            break;
                                        case 3:
                                            Toast.makeText(context, context.getString(R.string.txt_2001), Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            });


                        }
                        builder.create().show();
                        break;


                }
            }
        };

        private void sendKakaoTalk(String msg, String imgUrl) {
            try {
                LogUtil.d("sendKakaoTalk, msg : " + msg);
                LogUtil.d("sendKakaoTalk, imgUrl : " + imgUrl);
            } catch (Exception e) {

            }
        }


        private void callApi_clickLike(String trip_uid, final int position)
        {
            GPClient client = new GPClient(context);
            client.addProgress();
            client.setUri(Constants.SERVER_URL_API_USER_LIKE);
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
                    if (returnCode == GReturnCode.SUCCESS_POSTING_LIKE_REGISTER) {
                        JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                        JSONObject json2 = JSONUtil.getJSONObject(json,"like");
                        try {
                            if (!json2.isNull("idx")) dataArr.get(position).like_idx = json2.getInt("idx");
                        }catch (Exception e){

                        }

                        LogUtil.d("좋아요 성공");
                        notifyDataSetChanged();
                    } else {
                        LogUtil.d(returnMessage);
                    }
                    return null;
                }
            });
            GExecutor.getInstance().cancelAndExecute(client);

        }

        private void callApi_unclickLike(String idx)
        {
            GPClient client = new GPClient(context);
            client.addProgress();
            client.setUri(Constants.SERVER_URL_API_USER_LIKE_DELETE+idx);
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
                        notifyDataSetChanged();
                    } else {
                        LogUtil.d(returnMessage);
                    }
                    return null;
                }
            });
            GExecutor.getInstance().cancelAndExecute(client);

        }
        ////////////////////////////////////////////////////////////////////////////

        private void callApi_changeLike(String like_type,String idx)
        {
            GPClient client = new GPClient(context);
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
                        notifyDataSetChanged();
                    } else {
                        LogUtil.d(returnMessage);
                    }
                    return null;
                }
            });
            GExecutor.getInstance().cancelAndExecute(client);

        }



        ///////////////////////////////////////////////////////////////////////////북마크
        private void callApi_bookmark(String trip_uid, final int position)
        {
            GPClient client = new GPClient(context);
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
                            if (!json2.isNull("idx")) dataArr.get(position).bookmark_idx = json2.getInt("idx");

                        }catch (Exception e){
                            e.printStackTrace();

                        }

                        LogUtil.d("북마크 추가 성공");
                        notifyDataSetChanged();
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
            GPClient client = new GPClient(context);
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
                        notifyDataSetChanged();
                    } else {
                        LogUtil.d(returnMessage);
                    }
                    return null;
                }
            });
            GExecutor.getInstance().cancelAndExecute(client);

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
                        Fragment f = new InfoFragment();
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
                    } else {
                        LogUtil.d(returnMessage);
                    }
                    return null;
                }
            });
            GExecutor.getInstance().cancelAndExecute(client);

        }


    }
