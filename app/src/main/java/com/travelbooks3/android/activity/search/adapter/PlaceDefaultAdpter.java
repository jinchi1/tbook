package com.travelbooks3.android.activity.search.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.search.SearchPlacePostFragment;
import com.travelbooks3.android.activity.search.data.PlaceDefaultData;
import com.travelbooks3.android.util.Header;
import com.travelbooks3.android.util.LogUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by system777 on 2017-09-18.
 */

 public class PlaceDefaultAdpter extends BaseAdapter
    {

        private Context context;
        private ArrayList<PlaceDefaultData> dataArr;
        String addr;
        String eng_addr;
        FragmentManager fragmentManager;


        public  PlaceDefaultAdpter(Context context, ArrayList<PlaceDefaultData> arr){
            this.context = context;
            this.dataArr = arr;
        }

        public void addItems(ArrayList<PlaceDefaultData> addItem) {
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
            final PlaceDefaultData data = dataArr.get(position);

            if(v == null)
            {
                v = LayoutInflater.from(context).inflate(R.layout.row_place_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.addr = (TextView)v.findViewById(R.id.place_txt);
                viewHolder.layout = (LinearLayout)v.findViewById(R.id.layout);
               //viewHolder.eng_addr = (TextView)v.findViewById(R.id.place_eng_txt);

                v.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
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

           // viewHolder.eng_addr.setText(eng_addr);
            //this.notifyDataSetChanged();


            return v;
        }



        private static class ViewHolder {

            public TextView addr;
            public TextView eng_addr;
            public LinearLayout layout;

        }


}


