package com.travelbooks3.android.activity.write;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.travelbooks3.android.R;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by system777 on 2017-09-04.
 */

public class WritePhotoPagerAdapter extends PagerAdapter{

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> imgUrlArr;


    public WritePhotoPagerAdapter(WriteActivity context, ArrayList<String> imgUrlArr){
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

        View view = inflater.inflate(R.layout.write_photo, null);
        ImageView img = (ImageView) view.findViewById(R.id.write_image);

        Glide.with(context).load(imgUrlArr.get(position)).into(img);

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

    @Override
    public float getPageWidth(int position) {
        return (1.0f);

        //return super.getPageWidth(position);
    }
}
