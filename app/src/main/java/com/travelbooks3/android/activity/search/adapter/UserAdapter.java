package com.travelbooks3.android.activity.search.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.info.InfoFragment;
import com.travelbooks3.android.activity.info.OtherInfoFragment;
import com.travelbooks3.android.activity.search.data.UserDefaultData;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.SPUtil;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system777 on 2017-09-19.
 */

public class UserAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<UserDefaultData> dataArr;
    String name;
    String introduce;
    String profile_photo;
    FragmentManager fragmentManager;


    public  UserAdapter(Context context, ArrayList<UserDefaultData> arr){
        this.context = context;
        this.dataArr = arr;
    }

    public void addItems(ArrayList<UserDefaultData> addItem) {
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
        final UserDefaultData data = dataArr.get(position);

        if(v == null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.row_people_pager, null);
            viewHolder = new ViewHolder();
            viewHolder.layout = (LinearLayout)v.findViewById(R.id.layout);
            viewHolder.name = (TextView)v.findViewById(R.id.name_txt);
            viewHolder.introduce = (TextView)v.findViewById(R.id.introduce_txt) ;
            viewHolder.profile_photo = (ImageView)v.findViewById(R.id.profile_img);

            v.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        name = data.name;
        introduce = data.introduce;
        profile_photo = data.profile_photo;
        viewHolder.name.setText(name);
        viewHolder.introduce.setText(introduce);
        //viewHolder.eng_addr.setText(eng_addr);
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
                LogUtil.d("sssssssssssssss"+String.valueOf(data.user_idx));

                LogUtil.d(String.valueOf(SPUtil.getInstance().getUserNumber(context)));

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


        //this.notifyDataSetChanged();


        return v;
    }

    private static class ViewHolder {

        public LinearLayout layout;
        public TextView name;
        public TextView introduce;
        public ImageView profile_photo;

    }


}