package com.travelbooks3.android.activity.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.travelbooks3.android.activity.info.InfoFragment;
import com.travelbooks3.android.activity.info.OtherInfoFragment;
import com.travelbooks3.android.activity.news.clickPhoto.ClickPhotoActivity;
import com.travelbooks3.android.activity.news.data.FollowingPagerData;
import com.travelbooks3.android.activity.news.data.NewPagerData;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.ClickSpan;
import com.travelbooks3.android.util.FormatUtil;
import com.travelbooks3.android.util.HashTag;
import com.travelbooks3.android.util.LogUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.travelbooks3.android.activity.news.data.NewPagerData.post;
import static com.travelbooks3.android.util.FormatUtil.getReplyDateFormat;

/**
 * Created by system777 on 2017-08-11.
 */

public class NewPagerAdapter extends  RecyclerView.Adapter<NewPagerAdapter.ViewHolder>{

    private Context context;
    private ArrayList<NewPagerData> MydataArr;
    private ArrayList<NewPagerData.MyPost> MypostArr;
    private FragmentManager fragmentManager;

    public  NewPagerAdapter(Context context, ArrayList<NewPagerData> arr, ArrayList<NewPagerData.MyPost> parr){
        this.context = context;
        this.MydataArr = arr;
        this.MypostArr = parr;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImg;
        public TextView contentTxt;
        public TextView regdateTxt;

        public LinearLayout photoLayout;

        public ImageView contentImg1;
        public ImageView contentImg2;
        public ImageView contentImg3;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImg = (ImageView)itemView.findViewById(R.id.news_pager_row_profile_img2);
            contentTxt = (TextView)itemView.findViewById(R.id.news_pager_content_row_txt2);
            regdateTxt = (TextView)itemView.findViewById(R.id.news_pager_regdate_txt2);
            photoLayout = (LinearLayout)itemView.findViewById(R.id.photo_layout2);

            contentImg1 = (ImageView)itemView.findViewById(R.id.news_pager_row_img12);
            contentImg2 = (ImageView)itemView.findViewById(R.id.news_pager_row_img22);
            contentImg3 = (ImageView)itemView.findViewById(R.id.news_pager_row_img32);
        }
    }
    public void addItems(ArrayList<NewPagerData> data, ArrayList<NewPagerData.MyPost> postdata){
        MydataArr = data;
        MypostArr = postdata;
        this.notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_pager_following2, parent, false);
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
    public void onBindViewHolder(NewPagerAdapter.ViewHolder holder, int position) {
       final NewPagerData Mydata = MydataArr.get(position);
        final NewPagerData.MyPost myPost = MypostArr.get(position);


        holder.regdateTxt.setText(FormatUtil.getReplyDateFormat(Mydata.regdate));

        String trip_like;
        String trip_comment;
        String follow;
        trip_like=Mydata.target_user_name+"님이 "+Mydata.user_name+"님의 게시물을 좋아합니다.";
        trip_comment =Mydata.target_user_name+"님이 "+Mydata.user_name+"님의 게시물의 댓글을 남겼습니다.";
        follow=Mydata.target_user_name+"님이 "+Mydata.user_name+"님을 팔로우 합니다.";
        final String[] liketag = new String[MydataArr.size()];
        String[] commenttag = new String[MydataArr.size()];
        String[] followtag = new String[MydataArr.size()];
        liketag[position] = trip_like;
        commenttag[position] = trip_comment;
        followtag[position] = follow;
        LogUtil.d(liketag[position]);
        LogUtil.d(commenttag[position]);
        LogUtil.d(followtag[position]);


        holder.regdateTxt.setText(getReplyDateFormat(Mydata.regdate));

        final ArrayList<int[]> hashtagSpans1 = getSpans(liketag[position], '님');
        ArrayList<int[]> hashtagSpans2 = getSpans(commenttag[position], '님');
        ArrayList<int[]> hashtagSpans3 = getSpans(followtag[position], '님');


        final SpannableString tagsContent1 = new SpannableString(liketag[position]);
        SpannableString tagsContent2 = new SpannableString(commenttag[position]);
        SpannableString tagsContent3 = new SpannableString(followtag[position]);


        for (int i = 0; i < hashtagSpans1.size(); i++) {
            int[] span = hashtagSpans1.get(i);
            final int hashTagStart = span[0];
            final int hashTagEnd = span[1];

            final ClickSpan hashTag = new ClickSpan(context);
            hashTag.setOnClickEventListener(new ClickSpan.ClickEventListener() {
                @Override
                public void onClickEvent(String data) {
                    LogUtil.d("ssssssssssssssssssssssssssssssssssssssss"+data);


                    if(data.equals(Mydata.target_user_name)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("search_user_idx", String.valueOf(Mydata.target_user_idx));
                        Fragment f = new OtherInfoFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
                    }
                    else if(data.equals(Mydata.user_name)){
                       /* Bundle bundle = new Bundle();
                        bundle.putString("search_user_idx", String.valueOf(Mydata.user_idx));
                        Fragment f = new OtherInfoFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();*/
                        Fragment f = new InfoFragment();
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
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
                public void onClickEvent(String data) {

                    if(data.equals(Mydata.target_user_name)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("search_user_idx", String.valueOf(Mydata.target_user_idx));
                        Fragment f = new OtherInfoFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
                    }
                    else if(data.equals(Mydata.user_name)){
                  /*      Bundle bundle = new Bundle();
                        bundle.putString("search_user_idx", String.valueOf(Mydata.user_idx));
                        Fragment f = new OtherInfoFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();*/

                        Fragment f = new InfoFragment();
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
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
                public void onClickEvent(String data) {

                    if(data.equals(Mydata.target_user_name)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("search_user_idx", String.valueOf(Mydata.target_user_idx));
                        Fragment f = new OtherInfoFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
                    }
                    else if(data.equals(Mydata.user_name)){
                   /*     Bundle bundle = new Bundle();
                        bundle.putString("search_user_idx", String.valueOf(Mydata.user_idx));
                        Fragment f = new OtherInfoFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();*/

                        Fragment f = new InfoFragment();
                        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();
                    }

                }
            });

            tagsContent3.setSpan(hashTag, hashTagStart, hashTagEnd-1, 0);
        }




        holder.photoLayout.setVisibility(View.GONE);
        holder.contentImg1.setVisibility(View.GONE);
        holder.contentImg2.setVisibility(View.GONE);
        holder.contentImg3.setVisibility(View.GONE);


        if (!TextUtils.isEmpty(Mydata.target_profile_photo)) {
            Glide.with(context)
                    .load(Constants.SERVER_IMG_URL + Mydata.target_profile_photo)
                    .fitCenter()
                    .thumbnail(0.1f)
                    .into(holder.profileImg);
//            }
        } else {
            Glide.with(context)
                    .load(Constants.SERVER_IMG_URL + Mydata.target_profile_photo)
                    .placeholder(R.drawable.written_face)
                    .error(R.drawable.written_face)
                    .fitCenter()
                    .thumbnail(0.1f)
                    .into(holder.profileImg);
        }


        if("L".equals(Mydata.type)) {

            holder.contentTxt.setText(tagsContent1);
            holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());



            for (int i = 0; i < myPost.photoArr.size(); i++) {
                String[] photo = new String[myPost.photoArr.size()];
                photo[i] = myPost.photoArr.get(i).photo;

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

                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("trip_uid",Mydata.trip_uid);
                    intent.putExtra("profile_photo",Mydata.profile_photo);
                    intent.putExtra("regdate",myPost.regdate);
                    intent.putExtra("name",Mydata.user_name);
                    intent.putExtra("user_idx",Integer.parseInt(Mydata.user_idx));
                    intent.putExtra("comment", myPost.comment);
                    intent.putExtra("like_count", myPost.like_count);
                    intent.putExtra("disLike_count", myPost.disLike_count);
                    intent.putExtra("reply_comment_count", myPost.reply_comment_count);
                    intent.putExtra("isAuth", myPost.isAuth);
                    intent.putExtra("type",Mydata.type);
                    intent.putExtra("isLike", myPost.isLike);
                    intent.putExtra("isDisLike", myPost.isDisLike);
                    intent.putExtra("isComment", myPost.isComment);
                    intent.putExtra("isBookmark", myPost.isBookmark);
                    intent.putExtra("like_idx", myPost.like_idx);
                    intent.putExtra("bookmark_idx", myPost.bookmark_idx);
                    intent.putExtra("bookmark_count",myPost.bookmark_count);
                    intent.putExtra("zoom_level",myPost.zoom_level);
                    intent.putParcelableArrayListExtra("myphotoArr",myPost.photoArr);

                    LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+Mydata.user_idx);


                    context.startActivity(intent);

                }
            });
            holder.contentImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("trip_uid",Mydata.trip_uid);
                    intent.putExtra("profile_photo",Mydata.profile_photo);
                    intent.putExtra("regdate",myPost.regdate);
                    intent.putExtra("name",Mydata.user_name);
                    intent.putExtra("user_idx",Integer.parseInt(Mydata.user_idx));
                    intent.putExtra("comment", myPost.comment);
                    intent.putExtra("like_count", myPost.like_count);
                    intent.putExtra("disLike_count", myPost.disLike_count);
                    intent.putExtra("reply_comment_count", myPost.reply_comment_count);
                    intent.putExtra("isAuth", myPost.isAuth);
                    intent.putExtra("type",Mydata.type);
                    intent.putExtra("isLike", myPost.isLike);
                    intent.putExtra("isDisLike", myPost.isDisLike);
                    intent.putExtra("isComment", myPost.isComment);
                    intent.putExtra("isBookmark", myPost.isBookmark);
                    intent.putExtra("like_idx", myPost.like_idx);
                    intent.putExtra("bookmark_idx", myPost.bookmark_idx);
                    intent.putExtra("bookmark_count",myPost.bookmark_count);
                    intent.putExtra("zoom_level",myPost.zoom_level);
                    intent.putParcelableArrayListExtra("myphotoArr",myPost.photoArr);



                    context.startActivity(intent);
                }
            });

            holder.contentImg3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("trip_uid",Mydata.trip_uid);
                    intent.putExtra("profile_photo",Mydata.profile_photo);
                    intent.putExtra("regdate",myPost.regdate);
                    intent.putExtra("name",Mydata.user_name);
                    intent.putExtra("user_idx",Integer.parseInt(Mydata.user_idx));
                    intent.putExtra("comment", myPost.comment);
                    intent.putExtra("like_count", myPost.like_count);
                    intent.putExtra("disLike_count", myPost.disLike_count);
                    intent.putExtra("reply_comment_count", myPost.reply_comment_count);
                    intent.putExtra("isAuth", myPost.isAuth);
                    intent.putExtra("type",Mydata.type);
                    intent.putExtra("isLike", myPost.isLike);
                    intent.putExtra("isDisLike", myPost.isDisLike);
                    intent.putExtra("isComment", myPost.isComment);
                    intent.putExtra("isBookmark", myPost.isBookmark);
                    intent.putExtra("like_idx", myPost.like_idx);
                    intent.putExtra("bookmark_idx", myPost.bookmark_idx);
                    intent.putExtra("bookmark_count",myPost.bookmark_count);
                    intent.putExtra("zoom_level",myPost.zoom_level);
                    intent.putParcelableArrayListExtra("myphotoArr",myPost.photoArr);



                    context.startActivity(intent);

                }
            });



        }
        else if("C".equals(Mydata.type))
        {


            holder.contentTxt.setText(tagsContent2);
            holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());



            for (int i = 0; i < myPost.photoArr.size(); i++) {
                String[] photo = new String[myPost.photoArr.size()];
                photo[i] = myPost.photoArr.get(i).photo;

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

                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("trip_uid",Mydata.trip_uid);
                    intent.putExtra("profile_photo",Mydata.profile_photo);
                    intent.putExtra("regdate",myPost.regdate);
                    intent.putExtra("name",Mydata.user_name);
                    intent.putExtra("user_idx",Integer.parseInt(Mydata.user_idx));
                    intent.putExtra("comment", myPost.comment);
                    intent.putExtra("like_count", myPost.like_count);
                    intent.putExtra("disLike_count", myPost.disLike_count);
                    intent.putExtra("reply_comment_count", myPost.reply_comment_count);
                    intent.putExtra("isAuth", myPost.isAuth);
                    intent.putExtra("type",Mydata.type);
                    intent.putExtra("isLike", myPost.isLike);
                    intent.putExtra("isDisLike", myPost.isDisLike);
                    intent.putExtra("isComment", myPost.isComment);
                    intent.putExtra("isBookmark", myPost.isBookmark);
                    intent.putExtra("like_idx", myPost.like_idx);
                    intent.putExtra("bookmark_idx", myPost.bookmark_idx);
                    intent.putExtra("bookmark_count",myPost.bookmark_count);
                    intent.putExtra("zoom_level",myPost.zoom_level);
                    intent.putParcelableArrayListExtra("myphotoArr",myPost.photoArr);

                    LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+Mydata.user_idx);


                    context.startActivity(intent);

                }
            });
            holder.contentImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("trip_uid",Mydata.trip_uid);
                    intent.putExtra("profile_photo",Mydata.profile_photo);
                    intent.putExtra("regdate",myPost.regdate);
                    intent.putExtra("name",Mydata.user_name);
                    intent.putExtra("user_idx",Integer.parseInt(Mydata.user_idx));
                    intent.putExtra("comment", myPost.comment);
                    intent.putExtra("like_count", myPost.like_count);
                    intent.putExtra("disLike_count", myPost.disLike_count);
                    intent.putExtra("reply_comment_count", myPost.reply_comment_count);
                    intent.putExtra("isAuth", myPost.isAuth);
                    intent.putExtra("type",Mydata.type);
                    intent.putExtra("isLike", myPost.isLike);
                    intent.putExtra("isDisLike", myPost.isDisLike);
                    intent.putExtra("isComment", myPost.isComment);
                    intent.putExtra("isBookmark", myPost.isBookmark);
                    intent.putExtra("like_idx", myPost.like_idx);
                    intent.putExtra("bookmark_idx", myPost.bookmark_idx);
                    intent.putExtra("bookmark_count",myPost.bookmark_count);
                    intent.putExtra("zoom_level",myPost.zoom_level);
                    intent.putParcelableArrayListExtra("myphotoArr",myPost.photoArr);



                    context.startActivity(intent);
                }
            });

            holder.contentImg3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClickPhotoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("trip_uid",Mydata.trip_uid);
                    intent.putExtra("profile_photo",Mydata.profile_photo);
                    intent.putExtra("regdate",myPost.regdate);
                    intent.putExtra("name",Mydata.user_name);
                    intent.putExtra("user_idx",Integer.parseInt(Mydata.user_idx));
                    intent.putExtra("comment", myPost.comment);
                    intent.putExtra("like_count", myPost.like_count);
                    intent.putExtra("disLike_count", myPost.disLike_count);
                    intent.putExtra("reply_comment_count", myPost.reply_comment_count);
                    intent.putExtra("isAuth", myPost.isAuth);
                    intent.putExtra("type",Mydata.type);
                    intent.putExtra("isLike", myPost.isLike);
                    intent.putExtra("isDisLike", myPost.isDisLike);
                    intent.putExtra("isComment", myPost.isComment);
                    intent.putExtra("isBookmark", myPost.isBookmark);
                    intent.putExtra("like_idx", myPost.like_idx);
                    intent.putExtra("bookmark_idx", myPost.bookmark_idx);
                    intent.putExtra("bookmark_count",myPost.bookmark_count);
                    intent.putExtra("zoom_level",myPost.zoom_level);
                    intent.putParcelableArrayListExtra("myphotoArr",myPost.photoArr);



                    context.startActivity(intent);

                }
            });



        }
        else if("F".equals(Mydata.type)){

            holder.contentTxt.setText(tagsContent3);
            holder.contentTxt.setMovementMethod(LinkMovementMethod.getInstance());


        }
        else{
            LogUtil.d("못가져왔당~!~~!~!~!~!!");
        }
        }






    @Override
    public int getItemCount() {
        LogUtil.d("------------내소식반환-----------갯수"+MydataArr.size());
        return  MydataArr.size();
    }
}
