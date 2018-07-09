package com.travelbooks3.android.activity.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.news.clickPhoto.ClickPhotoActivity;
import com.travelbooks3.android.activity.search.data.SearchTagPostData;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by system777 on 2017-09-21.
 */

public class SearchTagPostAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SearchTagPostData> dataArr;

    public SearchTagPostAdapter(Context context, ArrayList<SearchTagPostData> arr) {
        this.context = context;
        this.dataArr = arr;

    }

    public void addItems(ArrayList<SearchTagPostData> addItem) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = convertView;
        ViewHolder viewHolder;
        final SearchTagPostData data = dataArr.get(position);
        LogUtil.i("data",dataArr.get(position).trip_uid);

        if(v == null){
            v = LayoutInflater.from(context).inflate(R.layout.item_bookmark, null);
            viewHolder= new ViewHolder();
            viewHolder.contentImg1 = (ImageView)v.findViewById(R.id.grid_image);

            v.setTag(viewHolder);

        }else {

            viewHolder = (ViewHolder) v.getTag();

        }

        String photo = "";
        photo = data.photoArr.get(0).photo;
        LogUtil.i("oooo",photo);
        //Glide.with(context).load(Constants.SERVER_IMG_URL + photo).fitCenter().into(viewHolder.contentImg1);
        Glide.with(context)
                .load(Constants.SERVER_IMG_URL + photo)
                .placeholder(R.drawable.pin_color_box)
                .thumbnail(0.1f)
                .centerCrop()
                .into(viewHolder.contentImg1);
//            }
        viewHolder.contentImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ClickPhotoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("trip_uid",data.trip_uid);
                intent.putExtra("comment",data.comment);
                intent.putExtra("profile_photo",data.profile_photo);
                intent.putExtra("regdate",data.regdate);
                intent.putExtra("name",data.name);
                intent.putExtra("user_idx",data.user_idx);
                intent.putExtra("like_count",data.like_count);
                intent.putExtra("reply_comment_count",data.reply_comment_count);
                intent.putExtra("isAuth",data.isAuth);
                intent.putExtra("type",data.type);
                intent.putExtra("isLike",data.isLike);
                intent.putExtra("isDisLike",data.isDislike);
                intent.putExtra("isComment",data.isComment);
                intent.putExtra("isBookmark",data.isBookmark);
                intent.putExtra("like_idx",data.like_idx);
                intent.putExtra("bookmark_idx",data.bookmark_idx);
                intent.putExtra("bookmark_count",data.bookmark_count);
                intent.putExtra("zoom_level",data.zoom_level);
                intent.putParcelableArrayListExtra("search_tag_post_photoArr",data.photoArr);


                context.startActivity(intent);

            }
        });

        //this.notifyDataSetChanged();
        return v;
    }



    private static class ViewHolder {
        public ImageView contentImg1;

    }
}



