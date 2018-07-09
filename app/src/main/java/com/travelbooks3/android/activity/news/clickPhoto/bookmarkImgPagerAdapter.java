package com.travelbooks3.android.activity.news.clickPhoto;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.info.model.MyBookmarkData;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by system777 on 2017-09-01.
 */

public class bookmarkImgPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MyBookmarkData.Photos> imgUrlArr;
    private ArrayList<MyBookmarkData.Photos> imgUrlArr2;


    public bookmarkImgPagerAdapter(ClickPhotoActivity context, ArrayList<MyBookmarkData.Photos> imgUrlArr2){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imgUrlArr2 = imgUrlArr2;
    }

/*
    public ImgPagerAdapter(ClickPhotoActivity context, ArrayList<NewPagerData.MyPost.Photos> imgUrlArr){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imgUrlArr = imgUrlArr;
    }*/



    @Override
    public int getCount() {
        return imgUrlArr2.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.clickphoto_viewpager, null);
        ImageView img = (ImageView) view.findViewById(R.id.viewPager_img);

        LogUtil.d("url = "+ Constants.SERVER_IMG_URL+imgUrlArr2.get(position).photo);

        Glide.with(context).load(Constants.SERVER_IMG_URL+imgUrlArr2.get(position).photo).centerCrop().into(img);

        container.addView(view,0);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}