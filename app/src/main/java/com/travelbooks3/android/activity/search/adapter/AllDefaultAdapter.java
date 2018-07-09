package com.travelbooks3.android.activity.search.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.info.InfoFragment;
import com.travelbooks3.android.activity.info.OtherInfoFragment;
import com.travelbooks3.android.activity.search.SearchPlacePostFragment;
import com.travelbooks3.android.activity.search.SearchTagPostFragment;
import com.travelbooks3.android.activity.search.data.AllDefaultData;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.SPUtil;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system777 on 2017-09-18.
 */

public class AllDefaultAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<AllDefaultData> dataArr;
        String addr;
        String eng_addr;
        String name;
        String introduce;
        String tag;
        String count;
        String profile_photo;
        FragmentManager fragmentManager;

        public  AllDefaultAdapter(Context context, ArrayList<AllDefaultData> arr){
        this.context = context;
        this.dataArr = arr;
    }

    public void addItems(ArrayList<AllDefaultData> addItem) {
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

        convertView = null;
        View v = convertView;
        ViewHolder viewHolder;
        final AllDefaultData data = dataArr.get(position);

        viewHolder = new ViewHolder();

        if(v == null)
        {


            if(data.search_type.equals("1")){
                v = LayoutInflater.from(context).inflate(R.layout.row_people_pager, null);
                viewHolder.name = (TextView)v.findViewById(R.id.name_txt);
                viewHolder.introduce = (TextView)v.findViewById(R.id.introduce_txt);
                viewHolder.layout = (LinearLayout)v.findViewById(R.id.layout);
                viewHolder.profile_photo = (ImageView)v.findViewById(R.id.profile_img);
                name = data.name;
                introduce = data.introduce;
                profile_photo = data.profile_photo;
                viewHolder.name.setText(name);
                viewHolder.introduce.setText(introduce);
                if (!TextUtils.isEmpty(profile_photo)) {
                    Glide.with(getApplicationContext())
                            .load(Constants.SERVER_IMG_URL + profile_photo)
                            .placeholder(R.drawable.written_face)
                            .centerCrop()
                            .thumbnail(0.1f)
                            .into(viewHolder.profile_photo);
//            }
                } else {
                    Glide.with(getApplicationContext())
                            .load(Constants.SERVER_IMG_URL + profile_photo)
                            .placeholder(R.drawable.written_face)
                            .error(R.drawable.written_face)
                            .centerCrop()
                            .thumbnail(0.1f)
                            .into(viewHolder.profile_photo);
                }
                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.d("sssssssssssssssssss"+String.valueOf(data.user_idx));

                        if(SPUtil.getInstance().getUserNumber(context)!=Integer.parseInt(data.user_idx)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("search_user_idx", data.user_idx);
                            bundle.putString("search_type", "1");
                            Fragment f = new OtherInfoFragment();
                            f.setArguments(bundle);
                            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, f);
                            fragmentTransaction.addToBackStack(null);
                            //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();
                        }
                        else if(SPUtil.getInstance().getUserNumber(context)==Integer.parseInt(data.user_idx))
                        {

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



            }
            else if(data.search_type.equals("2")){
                v = LayoutInflater.from(context).inflate(R.layout.row_tag_pager, null);
                viewHolder.tag = (TextView)v.findViewById(R.id.tag_txt);
                viewHolder.count = (TextView)v.findViewById(R.id.count_txt);
                viewHolder.layout = (LinearLayout)v.findViewById(R.id.layout);
                tag = data.tag;
                count = data.count;
                viewHolder.tag.setText(tag);
                viewHolder.count.setText("게시물 "+count+"개");

                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.d(String.valueOf(data.tag));

                        Bundle bundle = new Bundle();
                        bundle.putString("word",data.tag);
                        Fragment f= new SearchTagPostFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();

                    }
                });

            }
            else if(data.search_type.equals("3")){
                v = LayoutInflater.from(context).inflate(R.layout.row_place_pager, null);
                viewHolder.addr = (TextView)v.findViewById(R.id.place_txt);
                //viewHolder.eng_addr = (TextView)v.findViewById(R.id.place_eng_txt);
                viewHolder.layout = (LinearLayout)v.findViewById(R.id.layout);
                addr = data.addr;
                eng_addr = data.eng_addr;
                viewHolder.addr.setText(addr);

                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.d(String.valueOf(data.addr));

                        Bundle bundle = new Bundle();
                        bundle.putString("word",data.addr);
                        bundle.putString("latitude",data.latitude);
                        bundle.putString("longitude",data.longitude);
                        Fragment f= new SearchPlacePostFragment();
                        f.setArguments(bundle);
                        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, f);
                        fragmentTransaction.addToBackStack(null);
                        //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();

                    }
                });


            }

            //v.setTag(viewHolder);


        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        // viewHolder.eng_addr.setText(eng_addr);
        //this.notifyDataSetChanged();


        return v;
    }



    private static class ViewHolder {

        public TextView addr;
        public TextView eng_addr;
        public TextView name;
        public TextView introduce;
        public TextView tag;
        public TextView count;
        public ImageView profile_photo;
        public LinearLayout layout;
    }


}
