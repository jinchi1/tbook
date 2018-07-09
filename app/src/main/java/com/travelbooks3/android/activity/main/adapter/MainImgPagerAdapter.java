package com.travelbooks3.android.activity.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.main.TripInfoActivity;
import com.travelbooks3.android.activity.main.model.MainListData;
import com.travelbooks3.android.activity.news.clickPhoto.ClickPhotoActivity;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.OnSwipeTouchListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by system777 on 2017-06-28.
 */

public class MainImgPagerAdapter extends PagerAdapter{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MainListData.Photos> imgUrlArr;
    private ArrayList<ClickPhotoActivity.Photos> imgUrlArr2;

    public MainImgPagerAdapter(Context context, ArrayList<MainListData.Photos> imgUrlArr){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imgUrlArr = imgUrlArr;
    }

    @Override
    public int getCount() {
        return imgUrlArr.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.row_img_pager , null);
        ImageView img = (ImageView) view.findViewById(R.id.row_viewPager_img);

        LogUtil.d("url = "+Constants.SERVER_IMG_URL+imgUrlArr.get(position).photo);

        Glide.with(context).load(Constants.SERVER_IMG_URL+imgUrlArr.get(position).photo).into(img); //웹에서받아올때

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
