package com.travelbooks3.android.activity.main.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.travelbooks3.android.activity.main.model.MainFriendsData;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by system777 on 2017-06-20.
 */

public class MainFriendAdapter extends RecyclerView.Adapter<MainFriendAdapter.ViewHolder> implements View.OnClickListener{
    private Context context;
    private FragmentActivity activity;
    private ArrayList<MainFriendsData> friendsArr;

    public MainFriendAdapter(Context context, FragmentActivity activity, ArrayList<MainFriendsData> arr){
        this.context = context;
        this.activity = activity;
        friendsArr = arr;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView friend_img;
        private TextView friend_name;
        private LinearLayout friend_lay;

        public ViewHolder(View itemView){
            super(itemView);
            friend_img = (ImageView) itemView.findViewById(R.id.friend_profile_img);
            friend_name = (TextView) itemView.findViewById(R.id.friend_name);
            friend_lay = (LinearLayout)itemView.findViewById(R.id.friend_lay);
        }
    }

    public void addItems(ArrayList<MainFriendsData> data){
        friendsArr = data;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_main_fragment_friends , null);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MainFriendsData data = friendsArr.get(position);
      //  Glide.with(context).load(Constants.SERVER_IMG_URL + data.profile_photo).error(R.drawable.home_partner).into(holder.friend_img);

        if (!TextUtils.isEmpty(data.photo)) {
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
                    .load(Constants.SERVER_IMG_URL + data.photo)
                    .placeholder(R.drawable.written_face)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.friend_img);
//            }
        } else {
            Glide.with(context)
                    .load(Constants.SERVER_IMG_URL + data.photo)
                    .placeholder(R.drawable.written_face)
                    .error(R.drawable.written_face)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.friend_img);
        }

        LogUtil.d("프로필포토"+data.photo+"/////"+data.name);
        holder.friend_name.setText(data.name);
        holder.friend_lay.setTag(data.idx);
        holder.friend_lay.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return friendsArr.size();
    }

    @Override
    public void onClick(View v) {
//        MainFriendsData data = (MainFriendsData) v.getTag();
        String idx = (String) v.getTag();

        Bundle bundle = new Bundle();
        bundle.putString("search_user_idx",idx);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment f = new OtherInfoFragment();
        f.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_base_lay, f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
