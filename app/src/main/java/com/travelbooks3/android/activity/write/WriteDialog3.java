package com.travelbooks3.android.activity.write;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pl.wheelview.WheelView;
import com.travelbooks3.android.R;
import com.travelbooks3.android.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by system777 on 2017-11-28.
 */

public class WriteDialog3 extends AppCompatActivity {
    WheelView wheelView;
    WheelView wheelView2;
    TextView hour_txt;
    TextView minute_txt;
    TextView done_txt;
    TextView cancel_txt;
    ImageView startPhoto_img;
    ImageView selectedPhoto_img;
    ImageView trans_img;

    String startPhoto;
    String selectedPhoto;
    String trans;
    String subcomment;
    int hour;
    int min;
    String markerColor;
    double latitude;
    double longitude;
    String address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new_select3);
        wheelView = (WheelView)findViewById(R.id.wv);
        wheelView2 = (WheelView)findViewById(R.id.wv2);
        hour_txt = (TextView)findViewById(R.id.hour_txt);
        minute_txt = (TextView)findViewById(R.id.minute_txt);
        trans_img = (ImageView)findViewById(R.id.trans_image);
        startPhoto_img = (ImageView)findViewById(R.id.start_photo_image);
        selectedPhoto_img = (ImageView)findViewById(R.id.end_photo_image);
        done_txt = (TextView)findViewById(R.id.done_txt);
        cancel_txt = (TextView)findViewById(R.id.cancel_txt3);

        Intent intent = getIntent();
        startPhoto = intent.getStringExtra("startPhoto");
        selectedPhoto = intent.getStringExtra("selectedPhoto");
        trans = intent.getStringExtra("trans");
        subcomment = intent.getStringExtra("subcomment");
        markerColor = intent.getStringExtra("marker");
        latitude = intent.getDoubleExtra("latitude",0.0);
        longitude = intent.getDoubleExtra("longitude",0.0);
        address = intent.getStringExtra("address");
        if(intent.hasExtra("hour")) {
            String temphour = intent.getStringExtra("hour");
            if(!temphour.equals("N"))
            {
                hour = Integer.parseInt(temphour);
                wheelView.setDefault(hour);
            }

        }
        if(intent.hasExtra("min")) {
            String tempmin = intent.getStringExtra("min");
            if(!tempmin.equals("N"))
            {
                min = Integer.parseInt(tempmin);
                wheelView2.setDefault(min);
            }

        }

        done_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour_string;
                String min_string;
                if(hour==0&min==0)
                {
                    hour_string = "N";
                    min_string = "N";
                    LogUtil.d(hour_string+min_string+"aaaaaaaaaaaaaaaaaaaaaa");
                }
                else
                {
                    hour_string = String.valueOf(hour);
                    min_string = String.valueOf(min);
                }

                Intent intent1 = new Intent();
                intent1.putExtra("latitude",String.valueOf(latitude));
                intent1.putExtra("longitude",String.valueOf(longitude));
                intent1.putExtra("marker",markerColor);
                intent1.putExtra("address",address);
                intent1.putExtra("trans",trans);
                intent1.putExtra("subcomment",subcomment);
                intent1.putExtra("hour",hour_string);
                intent1.putExtra("min",min_string);
                setResult(1,intent1);
                finish();
            }
        });

        switch (trans)
        {
            case "0":

                break;
            case "1":
                trans_img.setImageResource(R.drawable.subway_shadow);
                break;
            case "2":
                trans_img.setImageResource(R.drawable.bus_shadow);
                break;
            case "3":
                trans_img.setImageResource(R.drawable.train_shadow);
                break;
            case "4":
                trans_img.setImageResource(R.drawable.car_shadow);
                break;
            case "5":
                trans_img.setImageResource(R.drawable.airplane_shadow);
                break;
            case "6":

                break;
            case "7":
                trans_img.setImageResource(R.drawable.bicycle_shadow);
                break;
            case "8":
                trans_img.setImageResource(R.drawable.walk_shadow);
                break;
            case "9":
                trans_img.setImageResource(R.drawable.boat_shadow);
                break;

        }

        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Glide.with(this)
                .load(startPhoto)
                .placeholder(R.drawable.grey)
                .centerCrop()
                .thumbnail(0.5f)
                //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.thumbnail(0.1f)
                .into(startPhoto_img);

        Glide.with(this)
                .load(selectedPhoto)
                .placeholder(R.drawable.grey)
                .centerCrop()
                .thumbnail(0.5f)
                //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.thumbnail(0.1f)
                .into(selectedPhoto_img);


        ArrayList<String> a = new ArrayList<String>();
        ArrayList<String> b = new ArrayList<String>();

        for(int i =0; i<100; i++)
        {
            a.add(String.valueOf(i));
        }
        for(int j = 0; j<60; j++)
        {
            b.add(String.valueOf(j));
        }


        wheelView.setData(a);
        wheelView2.setData(b);

        wheelView.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {

                if(text.equals("99")) {
                    hour = 0;
                }
                else {
                    hour = Integer.valueOf(text)+1;
                }

                hour_txt.setText(String.valueOf(hour)+"h");
            }

            @Override
            public void selecting(int id, String text) {

                if(text.equals("99")) {
                    hour = 0;
                }
                else {
                    hour = Integer.valueOf(text)+1;
                }

                hour_txt.setText(String.valueOf(hour)+"h");
            }
        });

        wheelView2.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {

                if(text.equals("59")) {
                    min = 0;
                }
                else {
                    min = Integer.valueOf(text)+1;
                }

                minute_txt.setText(String.valueOf(min)+"m");
            }

            @Override
            public void selecting(int id, String text) {

                if(text.equals("59")) {
                    min = 0;
                }
                else {
                    min = Integer.valueOf(text)+1;
                }

                minute_txt.setText(String.valueOf(min)+"m");
            }
        });


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(2);
        finish();
    }
}
