package com.travelbooks3.android.activity.write;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.travelbooks3.android.R;

/**
 * Created by system777 on 2017-12-04.
 */

public class WriteDialog2_modify  extends AppCompatActivity implements View.OnClickListener{

    TextView next_txt;
    TextView cancel_txt;
    LinearLayout dialog2_lay;
    LinearLayout train_lay;
    LinearLayout bus_lay;
    LinearLayout subway_lay;
    LinearLayout car_lay;
    LinearLayout airplane_lay;
    LinearLayout ship_lay;
    LinearLayout bicycle_lay;
    LinearLayout walk_lay;
    LinearLayout cancel_lay;
    ImageView startPhoto_img;
    ImageView endPhoto_img;
    ImageView trans_img;
    EditText subComment_edt;

    String trans;
    String subcomment;
    String startPhoto;
    String selectedPhoto;
    double latitude;
    double longitude;
    String address;
    String markerColor;


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(2);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new_select2);
        dialog2_lay = (LinearLayout)findViewById(R.id.dialog2_lay);
        train_lay= (LinearLayout)findViewById(R.id.train_lay);
        bus_lay = (LinearLayout)findViewById(R.id.bus_lay);
        subway_lay = (LinearLayout)findViewById(R.id.subway_lay);
        car_lay = (LinearLayout)findViewById(R.id.car_lay);
        airplane_lay = (LinearLayout)findViewById(R.id.airplane_lay);
        ship_lay = (LinearLayout)findViewById(R.id.ship_lay);
        bicycle_lay = (LinearLayout)findViewById(R.id.bicycle_lay);
        walk_lay = (LinearLayout)findViewById(R.id.walk_lay);
        cancel_lay = (LinearLayout)findViewById(R.id.cancel_lay);
        subComment_edt = (EditText)findViewById(R.id.Edit_subComment);
        startPhoto_img = (ImageView)findViewById(R.id.start_photo_image);
        endPhoto_img = (ImageView)findViewById(R.id.end_photo_image);
        trans_img = (ImageView)findViewById(R.id.trans_image);

        dialog2_lay.setOnClickListener(this);
        train_lay.setOnClickListener(this);
        bus_lay.setOnClickListener(this);
        subway_lay.setOnClickListener(this);
        car_lay.setOnClickListener(this);
        airplane_lay.setOnClickListener(this);
        ship_lay.setOnClickListener(this);
        bicycle_lay.setOnClickListener(this);
        walk_lay.setOnClickListener(this);
        cancel_lay.setOnClickListener(this);

        Intent intent = getIntent();


        startPhoto = intent.getStringExtra("startPhoto");
        selectedPhoto = intent.getStringExtra("selectedPhoto");
        latitude = intent.getDoubleExtra("latitude",0.0);
        longitude = intent.getDoubleExtra("longitude",0.0);
        address = intent.getStringExtra("address");
        markerColor = intent.getStringExtra("marker");
        trans = intent.getStringExtra("trans");
        subcomment = intent.getStringExtra("subcomment");
        if(trans==null)
        {
            trans = "0";
        }
        if(subcomment!=null&&!subcomment.equals("ND-0702-1206-1230")) subComment_edt.setText(subcomment);

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
                .into(endPhoto_img);





        next_txt= (TextView)findViewById(R.id.next_txt2);
        cancel_txt = (TextView)findViewById(R.id.cancel_txt2);
        next_txt.setText("완료");
        next_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subcomment = subComment_edt.getText().toString();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(subComment_edt.getWindowToken(), 0);

                Intent intent = new Intent(WriteDialog2_modify.this,WriteDialog3.class);
                intent.putExtra("startPhoto",startPhoto);
                intent.putExtra("selectedPhoto",selectedPhoto);
                intent.putExtra("latitude",String.valueOf(latitude));
                intent.putExtra("longitude",String.valueOf(longitude));
                intent.putExtra("address",address);
                intent.putExtra("marker",markerColor);
                intent.putExtra("trans",trans);
                intent.putExtra("subcomment",subcomment);
                setResult(3,intent);
                finish();
                overridePendingTransition(0,R.anim.slide_right);
            }
        });

        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switch (trans)
        {
            case "0":
                break;
            case "1":
                subway_lay.performClick();
                break;
            case "2":
                bus_lay.performClick();
                break;
            case "3":
                train_lay.performClick();
                break;
            case "4":
                car_lay.performClick();
                break;
            case "5":
                airplane_lay.performClick();
                break;
            case "6":
                break;
            case "7":
                bicycle_lay.performClick();
                break;
            case "8":
                walk_lay.performClick();
                break;
            case "9":
                ship_lay.performClick();
                break;

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.train_lay:
                train_lay.setBackgroundResource(R.drawable.write_box_border2_red);
                bus_lay.setBackgroundResource(R.drawable.write_box_border2);
                subway_lay.setBackgroundResource(R.drawable.write_box_border2);
                car_lay.setBackgroundResource(R.drawable.write_box_border2);
                airplane_lay.setBackgroundResource(R.drawable.write_box_border2);
                ship_lay.setBackgroundResource(R.drawable.write_box_border2);
                bicycle_lay.setBackgroundResource(R.drawable.write_box_border2);
                walk_lay.setBackgroundResource(R.drawable.write_box_border2);
                trans = "3";
                trans_img.setImageResource(R.drawable.train_shadow);

                break;
            case R.id.bus_lay:

                train_lay.setBackgroundResource(R.drawable.write_box_border2);
                bus_lay.setBackgroundResource(R.drawable.write_box_border2_red);
                subway_lay.setBackgroundResource(R.drawable.write_box_border2);
                car_lay.setBackgroundResource(R.drawable.write_box_border2);
                airplane_lay.setBackgroundResource(R.drawable.write_box_border2);
                ship_lay.setBackgroundResource(R.drawable.write_box_border2);
                bicycle_lay.setBackgroundResource(R.drawable.write_box_border2);
                walk_lay.setBackgroundResource(R.drawable.write_box_border2);
                trans = "2";
                trans_img.setImageResource(R.drawable.bus_shadow);


                break;
            case R.id.subway_lay:

                train_lay.setBackgroundResource(R.drawable.write_box_border2);
                bus_lay.setBackgroundResource(R.drawable.write_box_border2);
                subway_lay.setBackgroundResource(R.drawable.write_box_border2_red);
                car_lay.setBackgroundResource(R.drawable.write_box_border2);
                airplane_lay.setBackgroundResource(R.drawable.write_box_border2);
                ship_lay.setBackgroundResource(R.drawable.write_box_border2);
                bicycle_lay.setBackgroundResource(R.drawable.write_box_border2);
                walk_lay.setBackgroundResource(R.drawable.write_box_border2);
                trans = "1";
                trans_img.setImageResource(R.drawable.subway_shadow);

                break;
            case R.id.car_lay:

                train_lay.setBackgroundResource(R.drawable.write_box_border2);
                bus_lay.setBackgroundResource(R.drawable.write_box_border2);
                subway_lay.setBackgroundResource(R.drawable.write_box_border2);
                car_lay.setBackgroundResource(R.drawable.write_box_border2_red);
                airplane_lay.setBackgroundResource(R.drawable.write_box_border2);
                ship_lay.setBackgroundResource(R.drawable.write_box_border2);
                bicycle_lay.setBackgroundResource(R.drawable.write_box_border2);
                walk_lay.setBackgroundResource(R.drawable.write_box_border2);
                trans = "4";
                trans_img.setImageResource(R.drawable.car_shadow);

                break;
            case R.id.airplane_lay:

                train_lay.setBackgroundResource(R.drawable.write_box_border2);
                bus_lay.setBackgroundResource(R.drawable.write_box_border2);
                subway_lay.setBackgroundResource(R.drawable.write_box_border2);
                car_lay.setBackgroundResource(R.drawable.write_box_border2);
                airplane_lay.setBackgroundResource(R.drawable.write_box_border2_red);
                ship_lay.setBackgroundResource(R.drawable.write_box_border2);
                bicycle_lay.setBackgroundResource(R.drawable.write_box_border2);
                walk_lay.setBackgroundResource(R.drawable.write_box_border2);
                trans = "5";
                trans_img.setImageResource(R.drawable.airplane_shadow);

                break;
            case R.id.ship_lay:

                train_lay.setBackgroundResource(R.drawable.write_box_border2);
                bus_lay.setBackgroundResource(R.drawable.write_box_border2);
                subway_lay.setBackgroundResource(R.drawable.write_box_border2);
                car_lay.setBackgroundResource(R.drawable.write_box_border2);
                airplane_lay.setBackgroundResource(R.drawable.write_box_border2);
                ship_lay.setBackgroundResource(R.drawable.write_box_border2_red);
                bicycle_lay.setBackgroundResource(R.drawable.write_box_border2);
                walk_lay.setBackgroundResource(R.drawable.write_box_border2);
                trans = "9";
                trans_img.setImageResource(R.drawable.boat_shadow);

                break;
            case R.id.bicycle_lay:

                train_lay.setBackgroundResource(R.drawable.write_box_border2);
                bus_lay.setBackgroundResource(R.drawable.write_box_border2);
                subway_lay.setBackgroundResource(R.drawable.write_box_border2);
                car_lay.setBackgroundResource(R.drawable.write_box_border2);
                airplane_lay.setBackgroundResource(R.drawable.write_box_border2);
                ship_lay.setBackgroundResource(R.drawable.write_box_border2);
                bicycle_lay.setBackgroundResource(R.drawable.write_box_border2_red);
                walk_lay.setBackgroundResource(R.drawable.write_box_border2);
                trans = "7";
                trans_img.setImageResource(R.drawable.bicycle_shadow);

                break;
            case R.id.walk_lay:

                train_lay.setBackgroundResource(R.drawable.write_box_border2);
                bus_lay.setBackgroundResource(R.drawable.write_box_border2);
                subway_lay.setBackgroundResource(R.drawable.write_box_border2);
                car_lay.setBackgroundResource(R.drawable.write_box_border2);
                airplane_lay.setBackgroundResource(R.drawable.write_box_border2);
                ship_lay.setBackgroundResource(R.drawable.write_box_border2);
                bicycle_lay.setBackgroundResource(R.drawable.write_box_border2);
                walk_lay.setBackgroundResource(R.drawable.write_box_border2_red);
                trans = "8";
                trans_img.setImageResource(R.drawable.walk_shadow);

                break;
            case R.id.cancel_lay:

                train_lay.setBackgroundResource(R.drawable.write_box_border2);
                bus_lay.setBackgroundResource(R.drawable.write_box_border2);
                subway_lay.setBackgroundResource(R.drawable.write_box_border2);
                car_lay.setBackgroundResource(R.drawable.write_box_border2);
                airplane_lay.setBackgroundResource(R.drawable.write_box_border2);
                ship_lay.setBackgroundResource(R.drawable.write_box_border2);
                bicycle_lay.setBackgroundResource(R.drawable.write_box_border2);
                walk_lay.setBackgroundResource(R.drawable.write_box_border2);
                trans = "0";
                trans_img.setImageResource(0);

                break;

            case R.id.dialog2_lay:

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(subComment_edt.getWindowToken(), 0);

                break;


        }

    }
}
