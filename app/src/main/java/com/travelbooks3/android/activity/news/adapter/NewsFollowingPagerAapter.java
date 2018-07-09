package com.travelbooks3.android.activity.news.adapter;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.BaseActivity;
import com.travelbooks3.android.activity.info.InfoFragment;
import com.travelbooks3.android.activity.info.OtherInfoFragment;
import com.travelbooks3.android.activity.main.adapter.MainImgPagerAdapter;
import com.travelbooks3.android.activity.news.FollowingPagerFragment;
import com.travelbooks3.android.activity.news.clickPhoto.ClickPhotoActivity;
import com.travelbooks3.android.activity.news.data.FollowingPagerData;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.ClickSpan;
import com.travelbooks3.android.util.FormatUtil;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.SPUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by system777 on 2017-06-24.
 */

public class NewsFollowingPagerAapter extends RecyclerView.Adapter<NewsFollowingPagerAapter.ViewHolder> {

    private Context context;
    private ArrayList<FollowingPagerData> dataArr;
    private ArrayList<FollowingPagerData.Post> postArr;
    MainImgPagerAdapter _adapter;
    FragmentManager fragmentManager;


    public  NewsFollowingPagerAapter(Context context, ArrayList<FollowingPagerData> arr, ArrayList<FollowingPagerData.Post> parr){
        this.context = context;
        this.dataArr = arr;
        this.postArr = parr;
        }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImg;
        public TextView contentTxt;
        public TextView regdateTxt;
        public ImageView contentImg1;
        public ImageView contentImg2;
        public ImageView contentImg3;
        public LinearLayout photoLayout;

        public String photoarr[];

        public ViewHolder(View itemView) {
            super(itemView);
            profileImg = (ImageView)itemView.findViewById(R.id.news_pager_row_profile_img);
            contentTxt = (TextView)itemView.findViewById(R.id.news_pager_content_row_txt);
            regdateTxt = (TextView)itemView.findViewById(R.id.news_pager_regdate_txt);
            photoLayout = (LinearLayout)itemView.findViewById(R.id.photo_layout);

            contentImg1 = (ImageView)itemView.findViewById(R.id.news_pager_row_img1);
            contentImg2 = (ImageView)itemView.findViewById(R.id.news_pager_row_img2);
            contentImg3 = (ImageView)itemView.findViewById(R.id.news_pager_row_img3);

        }
    }
    public void addItems(ArrayList<FollowingPagerData> data, ArrayList<FollowingPagerData.Post> postdata){
        dataArr = data;
        postArr = postdata;
        this.notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_pager_following, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }


    private ArrayList<int[]> getSpans(String body, char prefix){
        ArrayList<int[]> spans = new ArrayList<>();

        Pattern pattern= Pattern.compile("\\w+"+prefix);
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
    public void onBindViewHolder(NewsFollowingPagerAapter.ViewHolder holder, int position) {
        final FollowingPagerData data = dataArr.get(position);
        final FollowingPagerData.Post post = postArr.get(position);


        String trip_like;
        String trip_comment;
        String follow;

        holder.regdateTxt.setText(FormatUtil.getReplyDateFormat(data.regdate));

        trip_like=data.following_user_name+"님이 "+data.target_user_name+"님의 게시물을 좋아합니다.";
        trip_comment =data.following_user_name+"님이 "+data.target_user_name+"님의 게시물의 댓글을 남겼습니다.";
        follow=data.following_user_name+"님이 "+data.target_user_name+"님을 팔로우 합니다.";
        String[] liketag = new String[dataArr.size()];
        String[] commenttag = new String[dataArr.size()];
        String[] followtag = new String[dataArr.size()];
        liketag[position] = trip_like;
        commenttag[position] = trip_comment;
        followtag[position] = follow;
        LogUtil.d(liketag[position]);
        LogUtil.d(commenttag[position]);
        LogUtil.d(followtag[position]);

        ArrayList<int[]> hashtagSpans1 = getSpans(liketag[position], '님');
        ArrayList<int[]> hashtagSpans2 = getSpans(commenttag[position], '님');
        ArrayList<int[]> hashtagSpans3 = getSpans(followtag[position], '님');


        SpannableString tagsContent1 = new SpannableString(liketag[position]);
        SpannableString tagsContent2 = new SpannableString(commenttag[position]);
        SpannableString tagsContent3 = new SpannableString(followtag[position]);


        for (int i = 0; i < hashtagSpans1.size(); i++) {
            int[] span = hashtagSpans1.get(i);
            int hashTagStart = span[0];
            int hashTagEnd = span[1];

            ClickSpan hashTag = new ClickSpan(context);
            hashTag.setOnClickEventListener(new ClickSpan.ClickEventListener() {
                @Override
                public void onClickEvent(String worddata) {


                    if(worddata.equals(data.target_user_name))
                    {
                        if(data.target_user_idx!= SPUtil.getInstance().getUserNumber(context))
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("search_user_idx", String.valueOf(data.target_user_idx));
                            Fragment f = new OtherInfoFragment();
                            f.setArguments(bundle);
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();
                        }
                        else if(data.target_user_idx == SPUtil.getInstance().getUserNumber(context))
                        {
                            Fragment f = new InfoFragment();
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();
                        }
                    }
                    else if(worddata.equals(data.following_user_name)){

                        if(data.following_user_idx!=SPUtil.getInstance().getUserNumber(context)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("search_user_idx", String.valueOf(data.following_user_idx));
                            Fragment f = new OtherInfoFragment();
                            f.setArguments(bundle);
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();
                        }
                        else if(data.following_user_idx==SPUtil.getInstance().getUserNumber(context)){
                            Fragment f = new InfoFragment();
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();

                        }
                    }


                }
            });

            tagsContent1.setSpan(hashTag, hashTagStart, hashTagEnd-1, 0);
        }
        for (int i = 0; i < hashtagSpans2.size(); i++) {
            int[] span = hashtagSpans2.get(i);
            int hashTagStart = span[0];
            int hashTagEnd = span[1];

            ClickSpan hashTag = new ClickSpan(context);
            hashTag.setOnClickEventListener(new ClickSpan.ClickEventListener() {
                @Override
                public void onClickEvent(String worddata) {

                   /* if(worddata.equals(data.target_user_name)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("search_user_idx", String.valueOf(data.target_user_idx));
                        Fragment f = new OtherInfoFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
                    }
                    else if(worddata.equals(data.following_user_name)){
                        Bundle bundle = new Bundle();
                        bundle.putString("search_user_idx", String.valueOf(data.following_user_idx));
                        Fragment f = new OtherInfoFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
                    }*/
                    if(worddata.equals(data.target_user_name))
                    {
                        if(data.target_user_idx!= SPUtil.getInstance().getUserNumber(context))
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("search_user_idx", String.valueOf(data.target_user_idx));
                            Fragment f = new OtherInfoFragment();
                            f.setArguments(bundle);
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();
                        }
                        else if(data.target_user_idx == SPUtil.getInstance().getUserNumber(context))
                        {
                            Fragment f = new InfoFragment();
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();
                        }
                    }
                    else if(worddata.equals(data.following_user_name)){

                        if(data.following_user_idx!=SPUtil.getInstance().getUserNumber(context)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("search_user_idx", String.valueOf(data.following_user_idx));
                            Fragment f = new OtherInfoFragment();
                            f.setArguments(bundle);
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();
                        }
                        else if(data.following_user_idx==SPUtil.getInstance().getUserNumber(context)){
                            Fragment f = new InfoFragment();
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();

                        }
                    }

                }
            });

            tagsContent2.setSpan(hashTag, hashTagStart, hashTagEnd-1, 0);
        }
        for (int i = 0; i < hashtagSpans3.size(); i++) {
            int[] span = hashtagSpans3.get(i);
            int hashTagStart = span[0];
            int hashTagEnd = span[1];

            ClickSpan hashTag = new ClickSpan(context);
            hashTag.setOnClickEventListener(new ClickSpan.ClickEventListener() {
                @Override
                public void onClickEvent(String worddata) {

                    /*if(worddata.equals(data.target_user_name)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("search_user_idx", String.valueOf(data.target_user_idx));
                        Fragment f = new OtherInfoFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
                    }
                    else if(worddata.equals(data.following_user_name)){
                        Bundle bundle = new Bundle();
                        bundle.putString("search_user_idx", String.valueOf(data.following_user_idx));
                        Fragment f = new OtherInfoFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
                    }*/
                    if(worddata.equals(data.target_user_name))
                    {
                        if(data.target_user_idx!= SPUtil.getInstance().getUserNumber(context))
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("search_user_idx", String.valueOf(data.target_user_idx));
                            Fragment f = new OtherInfoFragment();
                            f.setArguments(bundle);
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();
                        }
                        else if(data.target_user_idx == SPUtil.getInstance().getUserNumber(context))
                        {
                            Fragment f = new InfoFragment();
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();
                        }
                    }
                    else if(worddata.equals(data.following_user_name)){

                        if(data.following_user_idx!=SPUtil.getInstance().getUserNumber(context)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("search_user_idx", String.valueOf(data.following_user_idx));
                            Fragment f = new OtherInfoFragment();
                            f.setArguments(bundle);
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();
                        }
                        else if(data.following_user_idx==SPUtil.getInstance().getUserNumber(context)){
                            Fragment f = new InfoFragment();
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();

                        }
                    }

                }
            });

            tagsContent3.setSpan(hashTag, hashTagStart, hashTagEnd-1, 0);
        }



        holder.photoLayout.setVisibility(View.GONE);
        holder.contentImg1.setVisibility(View.GONE);
        holder.contentImg2.setVisibility(View.GONE);
        holder.contentImg3.setVisibility(View.GONE);


        if (!TextUtils.isEmpty(data.following_profile_photo)) {
            Glide.with(context)
                    .load(Constants.SERVER_IMG_URL + data.following_profile_photo)
                    .fitCenter()
                    .thumbnail(0.1f)
                    .into(holder.profileImg);
//            }
        } else {
            Glide.with(context)
                    .load(Constants.SERVER_IMG_URL + data.following_profile_photo)
                    .placeholder(R.drawable.written_face)
                    .error(R.drawable.written_face)
                    .fitCenter()
                    .thumbnail(0.1f)
                    .into(holder.profileImg);
        }


        if ("L".equals(data.type)){
            holder.contentTxt.setText(tagsContent1);
            holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());

            for (int i = 0; i < post.photoArr.size(); i++) {
                 String[] photo = new String[post.photoArr.size()];
                    photo[i] = post.photoArr.get(i).photo;

                if(i==0){
                    holder.photoLayout.setVisibility(View.VISIBLE);
                    holder.contentImg1.setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constants.SERVER_IMG_URL + photo[i]).fitCenter().into(holder.contentImg1);
                }
                else if(i==1){
                    holder.contentImg2.setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constants.SERVER_IMG_URL + photo[i]).fitCenter().into(holder.contentImg2);
                }
                else if(i==2){
                    holder.contentImg3.setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constants.SERVER_IMG_URL + photo[i]).fitCenter().into(holder.contentImg3);
                }
            }

            holder.contentImg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.d(data.trip_uid);

                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("trip_uid",data.trip_uid);
                    intent.putExtra("profile_photo",data.target_profile_photo);
                    intent.putExtra("regdate",post.regdate);
                    intent.putExtra("name",data.target_user_name);
                    intent.putExtra("user_idx",data.target_user_idx);
                    intent.putExtra("comment",post.comment);
                    intent.putExtra("like_count",post.like_count);
                    intent.putExtra("disLike_count",post.disLike_count);
                    intent.putExtra("reply_comment_count",post.reply_comment_count);
                    intent.putExtra("isAuth",post.isAuth);
                    intent.putExtra("type",data.type);
                    intent.putExtra("isLike",post.isLike);
                    intent.putExtra("isDisLike", post.isDisLike);
                    intent.putExtra("isComment",post.isComment);
                    intent.putExtra("isBookmark",post.isBookmark);
                    intent.putExtra("like_idx",post.like_idx);
                    intent.putExtra("bookmark_idx",post.bookmark_idx);
                    intent.putExtra("bookmark_count",post.bookmark_count);
                    intent.putExtra("zoom_level",post.zoom_level);
                    intent.putParcelableArrayListExtra("photoArr",post.photoArr);




                    context.startActivity(intent);

                }
            });
            holder.contentImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("trip_uid",data.trip_uid);
                    intent.putExtra("profile_photo",data.target_profile_photo);
                    intent.putExtra("regdate",post.regdate);
                    intent.putExtra("name",data.target_user_name);
                    intent.putExtra("user_idx",data.target_user_idx);
                    intent.putExtra("comment",post.comment);
                    intent.putExtra("like_count",post.like_count);
                    intent.putExtra("disLike_count",post.disLike_count);
                    intent.putExtra("reply_comment_count",post.reply_comment_count);
                    intent.putExtra("isAuth",post.isAuth);
                    intent.putExtra("type",data.type);
                    intent.putExtra("isLike",post.isLike);
                    intent.putExtra("isDisLike", post.isDisLike);
                    intent.putExtra("isComment",post.isComment);
                    intent.putExtra("isBookmark",post.isBookmark);
                    intent.putExtra("like_idx",post.like_idx);
                    intent.putExtra("bookmark_idx",post.bookmark_idx);
                    intent.putExtra("bookmark_count",post.bookmark_count);
                    intent.putExtra("zoom_level",post.zoom_level);
                    intent.putParcelableArrayListExtra("photoArr",post.photoArr);





                    context.startActivity(intent);

                }
            });

            holder.contentImg3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("photo3","click");
                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("trip_uid",data.trip_uid);
                    intent.putExtra("profile_photo",data.target_profile_photo);
                    intent.putExtra("regdate",post.regdate);
                    intent.putExtra("name",data.target_user_name);
                    intent.putExtra("user_idx",data.target_user_idx);
                    intent.putExtra("comment",post.comment);
                    intent.putExtra("like_count",post.like_count);
                    intent.putExtra("disLike_count",post.disLike_count);
                    intent.putExtra("reply_comment_count",post.reply_comment_count);
                    intent.putExtra("isAuth",post.isAuth);
                    intent.putExtra("type",data.type);
                    intent.putExtra("isLike",post.isLike);
                    intent.putExtra("isDisLike", post.isDisLike);
                    intent.putExtra("isComment",post.isComment);
                    intent.putExtra("isBookmark",post.isBookmark);
                    intent.putExtra("like_idx",post.like_idx);
                    intent.putExtra("bookmark_idx",post.bookmark_idx);
                    intent.putExtra("bookmark_count",post.bookmark_count);
                    intent.putExtra("zoom_level",post.zoom_level);
                    intent.putParcelableArrayListExtra("photoArr",post.photoArr);






                    context.startActivity(intent);

                }
            });



            for (int i = 0; i < dataArr.size(); i++) {
                LogUtil.d("[" + i + "]");
            }
        }else if("C".equals(data.type))
        {
            holder.contentTxt.setText(tagsContent2);
            holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());

            for (int i = 0; i < post.photoArr.size(); i++) {
                String[] photo = new String[post.photoArr.size()];
                photo[i] = post.photoArr.get(i).photo;

                if(i==0){
                    holder.photoLayout.setVisibility(View.VISIBLE);
                    holder.contentImg1.setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constants.SERVER_IMG_URL + photo[i]).fitCenter().into(holder.contentImg1);
                }
                else if(i==1){
                    holder.contentImg2.setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constants.SERVER_IMG_URL + photo[i]).fitCenter().into(holder.contentImg2);
                }
                else if(i==2){
                    holder.contentImg3.setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constants.SERVER_IMG_URL + photo[i]).fitCenter().into(holder.contentImg3);
                }
            }

            holder.contentImg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.d(data.trip_uid);

                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("trip_uid",data.trip_uid);
                    intent.putExtra("profile_photo",data.target_profile_photo);
                    intent.putExtra("regdate",post.regdate);
                    intent.putExtra("name",data.target_user_name);
                    intent.putExtra("user_idx",data.target_user_idx);
                    intent.putExtra("comment",post.comment);
                    intent.putExtra("like_count",post.like_count);
                    intent.putExtra("disLike_count",post.disLike_count);
                    intent.putExtra("reply_comment_count",post.reply_comment_count);
                    intent.putExtra("isAuth",post.isAuth);
                    intent.putExtra("type",data.type);
                    intent.putExtra("isLike",post.isLike);
                    intent.putExtra("isDisLike", post.isDisLike);
                    intent.putExtra("isComment",post.isComment);
                    intent.putExtra("isBookmark",post.isBookmark);
                    intent.putExtra("like_idx",post.like_idx);
                    intent.putExtra("bookmark_idx",post.bookmark_idx);
                    intent.putExtra("bookmark_count",post.bookmark_count);
                    intent.putExtra("zoom_level",post.zoom_level);
                    intent.putParcelableArrayListExtra("photoArr",post.photoArr);




                    context.startActivity(intent);

                }
            });
            holder.contentImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("trip_uid",data.trip_uid);
                    intent.putExtra("profile_photo",data.target_profile_photo);
                    intent.putExtra("regdate",post.regdate);
                    intent.putExtra("name",data.target_user_name);
                    intent.putExtra("user_idx",data.target_user_idx);
                    intent.putExtra("comment",post.comment);
                    intent.putExtra("like_count",post.like_count);
                    intent.putExtra("disLike_count",post.disLike_count);
                    intent.putExtra("reply_comment_count",post.reply_comment_count);
                    intent.putExtra("isAuth",post.isAuth);
                    intent.putExtra("type",data.type);
                    intent.putExtra("isLike",post.isLike);
                    intent.putExtra("isDisLike", post.isDisLike);
                    intent.putExtra("isComment",post.isComment);
                    intent.putExtra("isBookmark",post.isBookmark);
                    intent.putExtra("like_idx",post.like_idx);
                    intent.putExtra("bookmark_idx",post.bookmark_idx);
                    intent.putExtra("bookmark_count",post.bookmark_count);
                    intent.putExtra("zoom_level",post.zoom_level);
                    intent.putParcelableArrayListExtra("photoArr",post.photoArr);





                    context.startActivity(intent);

                }
            });

            holder.contentImg3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("photo3","click");
                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("trip_uid",data.trip_uid);
                    intent.putExtra("profile_photo",data.target_profile_photo);
                    intent.putExtra("regdate",post.regdate);
                    intent.putExtra("name",data.target_user_name);
                    intent.putExtra("user_idx",data.target_user_idx);
                    intent.putExtra("comment",post.comment);
                    intent.putExtra("like_count",post.like_count);
                    intent.putExtra("disLike_count",post.disLike_count);
                    intent.putExtra("reply_comment_count",post.reply_comment_count);
                    intent.putExtra("isAuth",post.isAuth);
                    intent.putExtra("type",data.type);
                    intent.putExtra("isLike",post.isLike);
                    intent.putExtra("isDisLike", post.isDisLike);
                    intent.putExtra("isComment",post.isComment);
                    intent.putExtra("isBookmark",post.isBookmark);
                    intent.putExtra("like_idx",post.like_idx);
                    intent.putExtra("bookmark_idx",post.bookmark_idx);
                    intent.putExtra("bookmark_count",post.bookmark_count);
                    intent.putExtra("zoom_level",post.zoom_level);
                    intent.putParcelableArrayListExtra("photoArr",post.photoArr);






                    context.startActivity(intent);

                }
            });


            for (int i = 0; i < dataArr.size(); i++) {
                LogUtil.d("[" + i + "]");
            }

        }else if("F".equals(data.type)){
            holder.contentTxt.setText(tagsContent3);
            holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());


            for (int i = 0; i < dataArr.size(); i++) {
                LogUtil.d("[" + i + "]");
            }

        }
        else {
            LogUtil.d("못갔고왔당!!!!!!!!!!!!!!!!!" + data.type
            );

        }


    }



    @Override
    public int getItemCount() {
        LogUtil.d("------------반환-----------갯수"+dataArr.size());
        return  dataArr.size();

    }

    /*
    public void addItems(JSONArray items){
        if(items != null && items.length() != 0){
            int itemSize = items.length();
            for (int i = 0 ; i < itemSize ; i++){
                JSONObject obj = JSONUtil.getJSONObject(items, i );

                LogUtil.d("[" +
                        ""+i+"] = "+obj.toString());
                LogUtil.d("------------------");
            }

        }

    }*/






}
