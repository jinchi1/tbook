package com.travelbooks3.android.activity.info.adapter;

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
import com.travelbooks3.android.activity.info.model.FollowingListData;
import com.travelbooks3.android.activity.search.adapter.UserAdapter;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.SPUtil;

import org.json.JSONObject;

import java.util.ArrayList;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;
import ra.genius.net.GHttpMethod;

import static android.R.color.white;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.travelbooks3.android.R.color.color_d92051;

/**
 * Created by system777 on 2017-10-12.
 */

public class FollowingListAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<FollowingListData> dataArr;
    String name;
    String introduce;
    String profile_photo;
    FragmentManager fragmentManager;



    public  FollowingListAdapter(Context context, ArrayList<FollowingListData> arr){
        this.context = context;
        this.dataArr = arr;
    }

    public void addItems(ArrayList<FollowingListData> addItem) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        final ViewHolder viewHolder;
        final FollowingListData data = dataArr.get(position);

        if(v == null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.row_following_info, null);
            viewHolder = new ViewHolder();

            viewHolder.followBtn = (LinearLayout)v.findViewById(R.id.follow_lay);
            viewHolder.layout = (LinearLayout)v.findViewById(R.id.layout);
            viewHolder.name = (TextView)v.findViewById(R.id.name_txt);
            viewHolder.introduce = (TextView)v.findViewById(R.id.introduce_txt) ;
            viewHolder.profile_photo = (ImageView)v.findViewById(R.id.profile_img);
            viewHolder.followTxt = (TextView)v.findViewById(R.id.follow_txt);


            v.setTag(viewHolder);

        }else
            {

            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(data.isAuth!=null) {
            if (data.isAuth.equals("Y")) {
                viewHolder.followBtn.setVisibility(View.INVISIBLE);
            }
        }
        else{
            viewHolder.followBtn.setVisibility(View.INVISIBLE);
        }
        name = data.name;
        introduce = data.introduce;
        profile_photo = data.profile_photo;
        viewHolder.name.setText(name);
        viewHolder.introduce.setText(introduce);
        if (data.isFollow.equals("N"))
        {
            viewHolder.followTxt.setText("팔로우");
            viewHolder.followBtn.setBackgroundResource(R.drawable.my_page_edit);
        }
        else if (data.isFollow.equals("Y"))
        {

            viewHolder.followTxt.setText("팔로잉");
            viewHolder.followBtn.setBackgroundResource(R.drawable.follow2);
            viewHolder.followTxt.setTextColor(getApplicationContext().getResources().getColor(color_d92051));
        }

        viewHolder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.isFollow.equals("N"))
                {
                    callApi_follow(data.user_idx,position);
                    viewHolder.followTxt.setText("팔로잉");
                    viewHolder.followBtn.setBackgroundResource(R.drawable.follow2);
                    viewHolder.followTxt.setTextColor(getApplicationContext().getResources().getColor(color_d92051));
                    data.isFollow="Y";


                }
                else if(data.isFollow.equals("Y"))
                {
                    callApi_Unfollow(data.user_idx);
                    viewHolder.followTxt.setText("팔로우");
                    viewHolder.followBtn.setBackgroundResource(R.drawable.my_page_edit);
                    viewHolder.followTxt.setTextColor(getApplicationContext().getResources().getColor(white));
                    data.isFollow="N";

                }
            }
        });

        //viewHolder.eng_addr.setText(eng_addr);
        if (!TextUtils.isEmpty(profile_photo)) {
            Glide.with(getApplicationContext())
                    .load(Constants.SERVER_IMG_URL + profile_photo)
                    .placeholder(R.drawable.written_face)
                    .centerCrop()
                    //.thumbnail(0.1f)
                    //.skipMemoryCache(true)
                    //.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(viewHolder.profile_photo);
//            }
        } else {
            Glide.with(getApplicationContext())
                    .load(Constants.SERVER_IMG_URL + profile_photo)
                    .placeholder(R.drawable.written_face)
                    //.error(R.drawable.written_face)
                    .centerCrop()
                    //.thumbnail(0.1f)
                    //.skipMemoryCache(true)
                    //.diskCacheStrategy(DiskCacheStrategy.RESULT)
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


//        this.notifyDataSetChanged();


        return v;
    }

    private static class ViewHolder {

        public LinearLayout layout;
        public TextView name;
        public TextView introduce;
        public ImageView profile_photo;
        public TextView followTxt;
        LinearLayout followBtn;

    }

    private void callApi_follow(String user_idx, final int position)
    {
        GPClient client = new GPClient(this.context);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_USER_FOLLOW);
        client.addParameter("follower_idx",user_idx);
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

                if (returnCode == GReturnCode.SUCCESS_FOLLOW_REGISTER) {
                    LogUtil.d("팔로우 성공");
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONObject json2 = JSONUtil.getJSONObject(json,"follow");
                    try {
                        if (!json2.isNull("idx"))  dataArr.get(position).follow_idx = json2.getString("idx");
                        LogUtil.d("follow idx"+dataArr.get(position).follow_idx);
                        LogUtil.d("follow name"+dataArr.get(position).name);

                        notifyDataSetChanged();
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                  } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }
    private void callApi_Unfollow(String follow_idx)
    {
        GPClient client = new GPClient(this.context);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_USER_FOLLOW+follow_idx);
        client.setHttpMethod(GHttpMethod.Delete);
        //client.addParameter("idx",follow_idx);
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

                if (returnCode == GReturnCode.SUCCESS_FOLLOW_DELETE) {
                    LogUtil.d("팔로우 취소성공");

                    notifyDataSetChanged();

                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }


}
