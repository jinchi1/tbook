package com.travelbooks3.android.activity.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelbooks3.android.R;

import com.travelbooks3.android.activity.main.model.MainListData;

import java.util.ArrayList;

/**
 * Created by system777 on 2017-10-18.
 */

public class MainFeedInfoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MainListData.Photos> dataArr;

    public  MainFeedInfoAdapter(Context context, ArrayList<MainListData.Photos> arr){
        this.context = context;
        this.dataArr = arr;
    }

    public void addItems(ArrayList<MainListData.Photos> addItem) {
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
        final MainListData.Photos data = dataArr.get(position);

            if (v == null) {
                v = LayoutInflater.from(context).inflate(R.layout.activity_feed_info, null);
                viewHolder = new ViewHolder();
                viewHolder.feed_info_address = (TextView) v.findViewById(R.id.feed_info_address);
                viewHolder.feed_info_count = (TextView) v.findViewById(R.id.feed_info_count);
                viewHolder.feed_info_marker = (ImageView) v.findViewById(R.id.feed_info_marker);
                viewHolder.feed_info_trans_img = (ImageView) v.findViewById(R.id.feed_info_transport_img);
                viewHolder.info_feed_layout = (LinearLayout) v.findViewById(R.id.feed_info_layout);
                viewHolder.info_subComment = (TextView) v.findViewById(R.id.feed_info_subComment);
                viewHolder.info_time = (TextView) v.findViewById(R.id.feed_info_time);
                viewHolder.info_transfer = (TextView) v.findViewById(R.id.feed_info_transport);
                viewHolder.info_minute = (TextView)v.findViewById(R.id.feed_info_minute);
                viewHolder.feed_info_lay = (LinearLayout)v.findViewById(R.id.feed_info_lay);




                v.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
        if (position==0)viewHolder.info_feed_layout.setVisibility(View.GONE);
        else if(position!=0)viewHolder.info_feed_layout.setVisibility(View.VISIBLE);

        if (!data.addr.equals("N")) viewHolder.feed_info_address.setText(data.addr);
        if (!data.route_comment.equals("ND-0702-1206-1230"))
            viewHolder.info_subComment.setText(data.route_comment);
        switch (data.trans_idx) {
            case 0:
                viewHolder.info_transfer.setText("이동수단 미기재");
                viewHolder.feed_info_trans_img.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.info_transfer.setText("지하철");
                viewHolder.feed_info_trans_img.setVisibility(View.VISIBLE);
                viewHolder.feed_info_trans_img.setImageResource(R.drawable.subway);
                break;
            case 2:
                viewHolder.info_transfer.setText("버스");
                viewHolder.feed_info_trans_img.setVisibility(View.VISIBLE);
                viewHolder.feed_info_trans_img.setImageResource(R.drawable.bus);
                break;
            case 3:
                viewHolder.info_transfer.setText("기차");
                viewHolder.feed_info_trans_img.setVisibility(View.VISIBLE);
                viewHolder.feed_info_trans_img.setImageResource(R.drawable.train);
                break;
            case 4:
                viewHolder.info_transfer.setText("자동차");
                viewHolder.feed_info_trans_img.setVisibility(View.VISIBLE);
                viewHolder.feed_info_trans_img.setImageResource(R.drawable.car);
                break;
            case 5:
                viewHolder.info_transfer.setText("비행기");
                viewHolder.feed_info_trans_img.setVisibility(View.VISIBLE);
                viewHolder.feed_info_trans_img.setImageResource(R.drawable.airplane);
                break;
            case 6:
                viewHolder.info_transfer.setText("트램");
                viewHolder.feed_info_trans_img.setVisibility(View.VISIBLE);
                viewHolder.feed_info_trans_img.setImageResource(R.drawable.tram);
                break;
            case 7:
                viewHolder.info_transfer.setText("자전거");
                viewHolder.feed_info_trans_img.setVisibility(View.VISIBLE);
                viewHolder.feed_info_trans_img.setImageResource(R.drawable.bicycle);
                break;
            case 8:
                viewHolder.info_transfer.setText("도보");
                viewHolder.feed_info_trans_img.setVisibility(View.VISIBLE);
                viewHolder.feed_info_trans_img.setImageResource(R.drawable.walk);
                break;
            case 9:
                viewHolder.info_transfer.setText("배");
                viewHolder.feed_info_trans_img.setVisibility(View.VISIBLE);
                viewHolder.feed_info_trans_img.setImageResource(R.drawable.boat);
                break;
        }
        switch (data.m_color)
        {
            case "N":
                viewHolder.feed_info_lay.setVisibility(View.GONE);
                viewHolder.info_feed_layout.setVisibility(View.GONE);
                break;
            case "R":
                viewHolder.feed_info_marker.setImageResource(R.drawable.map_pin_red);
                break;
            case "Y":
                viewHolder.feed_info_marker.setImageResource(R.drawable.map_pin_yellow);
                viewHolder.feed_info_address.setTextColor(Color.parseColor("#FFBB00"));
                viewHolder.feed_info_count.setTextColor(Color.parseColor("#FFBB00"));
                break;
            case "B":
                viewHolder.feed_info_marker.setImageResource(R.drawable.map_pin_navy);
                viewHolder.feed_info_address.setTextColor(Color.parseColor("#003755"));
                viewHolder.feed_info_count.setTextColor(Color.parseColor("#003755"));
                break;
        }
        switch (data.hour)
        {
            case "N":
                viewHolder.info_time.setVisibility(View.GONE);
                break;
            default:
                viewHolder.info_time.setText(data.hour+" hour");
                break;
        }
        switch (data.min)
        {
            case "N":
                viewHolder.info_minute.setVisibility(View.GONE);
                break;
            default:
                viewHolder.info_minute.setText(data.min+" min");
        }

        switch (position)
        {
            case 0:
                viewHolder.feed_info_count.setText("1 st.");
                break;
            case 1:
                viewHolder.feed_info_count.setText("2 nd.");
                break;
            case 2:
                viewHolder.feed_info_count.setText("3 rd.");
                break;
            default :
                viewHolder.feed_info_count.setText(String.valueOf(position+1)+" th.");
                break;
        }


        return v;

    }

    private static class ViewHolder {

        public LinearLayout info_feed_layout;
        public TextView info_transfer;
        public TextView info_time;
        public TextView info_subComment;
        public TextView feed_info_count;
        public TextView feed_info_address;
        public ImageView feed_info_marker;
        public ImageView feed_info_trans_img;
        public TextView info_minute;
        public LinearLayout feed_info_lay;


    }
}
