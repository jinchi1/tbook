package com.travelbooks3.android.activity.info.setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.travelbooks3.android.R;
import com.travelbooks3.android.TravelbookApp;
import com.travelbooks3.android.activity.write.WriteActivity;
import com.travelbooks3.android.activity.write.WriteDialog;
import com.travelbooks3.android.activity.write.WriteDialog2;
import com.travelbooks3.android.activity.write.WriteDialog2_modify;
import com.travelbooks3.android.activity.write.WriteDialog3;
import com.travelbooks3.android.activity.write.WriteDialog_onephoto;
import com.travelbooks3.android.activity.write.WritePhotoPagerAdapter;
import com.travelbooks3.android.activity.write.WritePhotoPagerNewAdapter;
import com.travelbooks3.android.activity.write.WritePreviewDialog;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.Alert;
import com.travelbooks3.android.util.ImageUtil;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;


public class WriteTestActivity extends AppCompatActivity {

    ViewPager viewPager;
    ImageView background;
    ImageView markerImage;
    ImageView transImage;
    LinearLayout time_lay;
    LinearLayout write_lay;
    TextView hour_txt;
    TextView min_txt;
    TextView write_txt;
    TextView preview_txt;
    EditText comment_edt;
    WritePhotoPagerNewAdapter adapter;
    int selectedPhotoPosition = 0;
    ProgressDialog progressDialog;
    ArrayList<String> photoArr;
    ArrayList<String> markerArr;
    ArrayList<String> transArr;
    ArrayList<String> latitudeArr;
    ArrayList<String> longitudeArr;
    ArrayList<String> addressArr;
    ArrayList<String> subCommentArr;
    ArrayList<String> hourArr;
    ArrayList<String> minArr;
    ArrayList<String> photoIdx;

    String startPhoto;
    String selectedPhoto;
    String markerColor;
    String latitude;
    String longitude;
    String address;
    String trans;
    String subcomment;
    String hour;
    String min;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new);

        Intent i = getIntent();
        photoArr= i.getStringArrayListExtra("path");
        markerArr = new ArrayList<String>();
        transArr = new ArrayList<String>();
        latitudeArr = new ArrayList<String>();
        longitudeArr = new ArrayList<String>();
        addressArr = new ArrayList<String>();
        subCommentArr = new ArrayList<String>();
        hourArr = new ArrayList<String>();
        minArr = new ArrayList<String>();
        photoIdx = new ArrayList<String>();

        try {

            LogUtil.d(photoArr.get(0));

        }catch (Exception e)
        {
           finish();
        }

        markerColor = "N";
        latitude = "N";
        longitude = "N";
        address = "N";

        for(int l=0;l<photoArr.size();l++)
        {
            markerArr.add("N");
            transArr.add("0");
            latitudeArr.add("N");
            longitudeArr.add("N");
            addressArr.add("N");
            subCommentArr.add("ND-0702-1206-1230");
            hourArr.add("N");
            minArr.add("N");
        }


        write_lay = (LinearLayout)findViewById(R.id.write_lay);

        background = (ImageView)findViewById(R.id.TextView_background_image);
        markerImage = (ImageView)findViewById(R.id.marker_image);
        transImage = (ImageView)findViewById(R.id.trans_image);
        time_lay = (LinearLayout)findViewById(R.id.time_lay);
        hour_txt = (TextView)findViewById(R.id.hour_txt);
        min_txt = (TextView)findViewById(R.id.minute_txt);
        write_txt = (TextView)findViewById(R.id.write_txt);
        preview_txt = (TextView)findViewById(R.id.preview_txt);
        comment_edt = (EditText)findViewById(R.id.Edt_comment);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("업로드중");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        markerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPhoto = null;


                if(markerArr.get(selectedPhotoPosition).equals("N")) {

                    for (int i = selectedPhotoPosition - 1; i > -1; i--) {
                        LogUtil.d(String.valueOf(i));
                        if (!markerArr.get(i).equals("N")) {
                            startPhoto = photoArr.get(i);
                            break;
                        }

                    }

                    if (startPhoto != null) {
                        Intent intent = new Intent(WriteTestActivity.this, WriteDialog.class);
                        intent.putExtra("startPhoto", startPhoto);
                        intent.putExtra("selectedPhoto", photoArr.get(selectedPhotoPosition));
                        intent.putExtra("marker", markerArr.get(selectedPhotoPosition));
                        intent.putExtra("latitude", latitudeArr.get(selectedPhotoPosition));
                        intent.putExtra("longitude", longitudeArr.get(selectedPhotoPosition));
                        intent.putExtra("address", addressArr.get(selectedPhotoPosition));
                        startActivityForResult(intent, 1);
                        overridePendingTransition(R.anim.slide_left, 0);
                    } else {
                        Intent intent = new Intent(WriteTestActivity.this, WriteDialog_onephoto.class);
                        intent.putExtra("selectedPhoto", photoArr.get(selectedPhotoPosition));
                        intent.putExtra("marker", markerArr.get(selectedPhotoPosition));
                        intent.putExtra("latitude", latitudeArr.get(selectedPhotoPosition));
                        intent.putExtra("longitude", longitudeArr.get(selectedPhotoPosition));
                        intent.putExtra("address", addressArr.get(selectedPhotoPosition));
                        startActivityForResult(intent, 0);
                        overridePendingTransition(R.anim.slide_left, 0);

                    }
                }
                else {
                    Intent intent = new Intent(WriteTestActivity.this, WriteDialog_onephoto.class);
                    intent.putExtra("selectedPhoto", photoArr.get(selectedPhotoPosition));
                    intent.putExtra("marker", markerArr.get(selectedPhotoPosition));
                    intent.putExtra("latitude", latitudeArr.get(selectedPhotoPosition));
                    intent.putExtra("longitude", longitudeArr.get(selectedPhotoPosition));
                    intent.putExtra("address", addressArr.get(selectedPhotoPosition));
                    startActivityForResult(intent, 0);
                    overridePendingTransition(R.anim.slide_left, 0);

                }




            }


        });

        transImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPhoto = null;

                if(transArr.get(selectedPhotoPosition).equals("0")) {

                    for (int i = selectedPhotoPosition - 1; i > -1; i--) {
                        LogUtil.d(String.valueOf(i));
                        if (!markerArr.get(i).equals("N")) {
                            startPhoto = photoArr.get(i);
                            break;
                        }

                    }

                    if (startPhoto != null && !markerArr.get(selectedPhotoPosition).equals("N")) {
                        Intent intent = new Intent(WriteTestActivity.this, WriteDialog2.class);
                        intent.putExtra("startPhoto", startPhoto);
                        intent.putExtra("selectedPhoto", photoArr.get(selectedPhotoPosition));
                        intent.putExtra("marker", markerArr.get(selectedPhotoPosition));
                        intent.putExtra("latitude", Double.parseDouble(latitudeArr.get(selectedPhotoPosition)));
                        intent.putExtra("longitude", Double.parseDouble(longitudeArr.get(selectedPhotoPosition)));
                        intent.putExtra("address", addressArr.get(selectedPhotoPosition));
                        intent.putExtra("trans", transArr.get(selectedPhotoPosition));
                        intent.putExtra("address", addressArr.get(selectedPhotoPosition));
                        intent.putExtra("subcomment", subCommentArr.get(selectedPhotoPosition));

                        startActivityForResult(intent, 1);
                        overridePendingTransition(R.anim.slide_left, 0);
                    } else if (startPhoto != null && markerArr.get(selectedPhotoPosition).equals("N")) {
                        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WriteTestActivity.this);
                        alert_confirm.setMessage("주소정보를 입력해 주세요.");
                        AlertDialog alertDialog = alert_confirm.create();
                        alertDialog.show();

                    } else {

                        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WriteTestActivity.this);
                        alert_confirm.setMessage("시작지점 사진에서는 입력할수 없습니다.");
                        AlertDialog alertDialog = alert_confirm.create();
                        alertDialog.show();

                    }

                }
                else
                {
                    for (int i = selectedPhotoPosition - 1; i > -1; i--) {
                        LogUtil.d(String.valueOf(i));
                        if (!markerArr.get(i).equals("N")) {
                            startPhoto = photoArr.get(i);
                            break;
                        }

                    }

                    Intent intent = new Intent(WriteTestActivity.this, WriteDialog2_modify.class);
                    intent.putExtra("startPhoto", startPhoto);
                    intent.putExtra("selectedPhoto", photoArr.get(selectedPhotoPosition));
                    intent.putExtra("marker", markerArr.get(selectedPhotoPosition));
                    intent.putExtra("latitude", Double.parseDouble(latitudeArr.get(selectedPhotoPosition)));
                    intent.putExtra("longitude", Double.parseDouble(longitudeArr.get(selectedPhotoPosition)));
                    intent.putExtra("address", addressArr.get(selectedPhotoPosition));
                    intent.putExtra("trans", transArr.get(selectedPhotoPosition));
                    intent.putExtra("address", addressArr.get(selectedPhotoPosition));
                    intent.putExtra("subcomment", subCommentArr.get(selectedPhotoPosition));

                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_left, 0);


                }

            }
        });

        time_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhoto = null;

                for(int i=selectedPhotoPosition-1;i>-1;i--)
                {
                    LogUtil.d(String.valueOf(i));
                    if(!markerArr.get(i).equals("N"))
                    {
                        startPhoto = photoArr.get(i);
                        break;
                    }

                }

                if(startPhoto!=null&&!markerArr.get(selectedPhotoPosition).equals("N")) {
                    Intent intent = new Intent(WriteTestActivity.this, WriteDialog3.class);
                    intent.putExtra("startPhoto", startPhoto);
                    intent.putExtra("selectedPhoto", photoArr.get(selectedPhotoPosition));
                    intent.putExtra("marker",markerArr.get(selectedPhotoPosition));
                    intent.putExtra("latitude",Double.parseDouble(latitudeArr.get(selectedPhotoPosition)));
                    intent.putExtra("longitude",Double.parseDouble(longitudeArr.get(selectedPhotoPosition)));
                    intent.putExtra("address",addressArr.get(selectedPhotoPosition));
                    intent.putExtra("trans",transArr.get(selectedPhotoPosition));
                    intent.putExtra("subcomment",subCommentArr.get(selectedPhotoPosition));
                    intent.putExtra("hour",hourArr.get(selectedPhotoPosition));
                    intent.putExtra("min",minArr.get(selectedPhotoPosition));
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_left, 0);
                }
                else if(startPhoto!=null&&markerArr.get(selectedPhotoPosition).equals("N"))
                {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WriteTestActivity.this);
                    alert_confirm.setMessage("주소정보를 입력해 주세요.");
                    AlertDialog alertDialog = alert_confirm.create();
                    alertDialog.show();

                }
                else {

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WriteTestActivity.this);
                    alert_confirm.setMessage("시작지점 사진에서는 입력할수 없습니다.");
                    AlertDialog alertDialog = alert_confirm.create();
                    alertDialog.show();

                }
            }
        });

        write_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(comment_edt.getWindowToken(), 0);
            }
        });

        viewPager = (ViewPager)findViewById(R.id.write_new_viewPager);
        viewPager.removeAllViews();
        adapter = new WritePhotoPagerNewAdapter(this, photoArr);
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);
        //viewPager.setPadding(40,0,40,0);
        viewPager.setOffscreenPageLimit(2);
        //viewPager.setPageMargin((getResources().getDisplayMetrics().widthPixels /-9));
        viewPager.setPageMargin((int) dipToPixels(getApplicationContext(),5.f));
        viewPager.setPadding((int)dipToPixels(getApplicationContext(),60.0f),0,(int)dipToPixels(getApplicationContext(),55.0f),0);

        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int num) {


                selectedPhotoPosition = num;
                startPhoto = null;
               if(markerArr.get(selectedPhotoPosition).equals("N"))markerImage.setImageResource(R.drawable.map_pin_grey);
               else if(markerArr.get(selectedPhotoPosition).equals("R"))markerImage.setImageResource(R.drawable.map_pin_red);
               else if(markerArr.get(selectedPhotoPosition).equals("Y"))markerImage.setImageResource(R.drawable.map_pin_yellow);
               else if(markerArr.get(selectedPhotoPosition).equals("B"))markerImage.setImageResource(R.drawable.map_pin_navy);

                if(transArr.get(selectedPhotoPosition).equals("0"))transImage.setImageResource(R.drawable.choice_transport);
                else if(transArr.get(selectedPhotoPosition).equals("1"))transImage.setImageResource(R.drawable.subway);
                else if(transArr.get(selectedPhotoPosition).equals("2"))transImage.setImageResource(R.drawable.bus);
                else if(transArr.get(selectedPhotoPosition).equals("3"))transImage.setImageResource(R.drawable.train);
                else if(transArr.get(selectedPhotoPosition).equals("4"))transImage.setImageResource(R.drawable.car);
                else if(transArr.get(selectedPhotoPosition).equals("5"))transImage.setImageResource(R.drawable.airplane);
                else if(transArr.get(selectedPhotoPosition).equals("9"))transImage.setImageResource(R.drawable.boat);
                else if(transArr.get(selectedPhotoPosition).equals("7"))transImage.setImageResource(R.drawable.bicycle);
                else if(transArr.get(selectedPhotoPosition).equals("8"))transImage.setImageResource(R.drawable.walk);

                if(!hourArr.get(selectedPhotoPosition).equals("N"))
                {
                    hour_txt.setText(hourArr.get(selectedPhotoPosition)+"h");
                    hour_txt.setTextColor(Color.parseColor("#000000"));
                }else
                {
                    hour_txt.setText("0h");
                    hour_txt.setTextColor(Color.parseColor("#CCCCCC"));
                }
                if(!minArr.get(selectedPhotoPosition).equals("N"))
                {
                    min_txt.setText(minArr.get(selectedPhotoPosition)+"m");
                    min_txt.setTextColor(Color.parseColor("#000000"));
                }else
                {
                    min_txt.setText("0m");
                    min_txt.setTextColor(Color.parseColor("#CCCCCC"));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        preview_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WriteTestActivity.this, WritePreviewDialog.class);
                intent.putStringArrayListExtra("marker",markerArr);
                intent.putStringArrayListExtra("trans",transArr);
                intent.putStringArrayListExtra("latitude",latitudeArr);
                intent.putStringArrayListExtra("longitude",longitudeArr);
                intent.putStringArrayListExtra("address",addressArr);
                intent.putStringArrayListExtra("hour",hourArr);
                intent.putStringArrayListExtra("min",minArr);
                intent.putStringArrayListExtra("subcomment",subCommentArr);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left, 0);
            }
        });


        write_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callApi();
            }
        });

    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode)
        {

            case 0:


                if(data.hasExtra("marker"))  markerColor = data.getStringExtra("marker");
                if(data.hasExtra("latitude")) latitude = data.getStringExtra("latitude");
                if(data.hasExtra("longitude")) longitude = data.getStringExtra("longitude");
                if(data.hasExtra("address")) address = data.getStringExtra("address");
                markerArr.set(selectedPhotoPosition,markerColor);
                latitudeArr.set(selectedPhotoPosition,latitude);
                longitudeArr.set(selectedPhotoPosition,longitude);
                addressArr.set(selectedPhotoPosition,address);

                if(markerArr.get(selectedPhotoPosition).equals("N"))markerImage.setImageResource(R.drawable.map_pin_grey);
                else if(markerArr.get(selectedPhotoPosition).equals("R"))markerImage.setImageResource(R.drawable.map_pin_red);
                else if(markerArr.get(selectedPhotoPosition).equals("Y"))markerImage.setImageResource(R.drawable.map_pin_yellow);
                else if(markerArr.get(selectedPhotoPosition).equals("B"))markerImage.setImageResource(R.drawable.map_pin_navy);

                if(transArr.get(selectedPhotoPosition).equals("0"))transImage.setImageResource(R.drawable.choice_transport);
                else if(transArr.get(selectedPhotoPosition).equals("1"))transImage.setImageResource(R.drawable.subway);
                else if(transArr.get(selectedPhotoPosition).equals("2"))transImage.setImageResource(R.drawable.bus);
                else if(transArr.get(selectedPhotoPosition).equals("3"))transImage.setImageResource(R.drawable.train);
                else if(transArr.get(selectedPhotoPosition).equals("4"))transImage.setImageResource(R.drawable.car);
                else if(transArr.get(selectedPhotoPosition).equals("5"))transImage.setImageResource(R.drawable.airplane);
                else if(transArr.get(selectedPhotoPosition).equals("9"))transImage.setImageResource(R.drawable.boat);
                else if(transArr.get(selectedPhotoPosition).equals("7"))transImage.setImageResource(R.drawable.bicycle);
                else if(transArr.get(selectedPhotoPosition).equals("8"))transImage.setImageResource(R.drawable.walk);

                break;

            case 1:

                if(data.hasExtra("marker"))  markerColor = data.getStringExtra("marker");
                if(data.hasExtra("latitude"))    latitude = data.getStringExtra("latitude");
                if(data.hasExtra("longitude")) longitude = data.getStringExtra("longitude");
                if(data.hasExtra("address"))   address = data.getStringExtra("address");
                if(data.hasExtra("trans"))  trans = data.getStringExtra("trans");
                if(data.hasExtra("subcomment")) subcomment = data.getStringExtra("subcomment");
                if(data.hasExtra("hour"))  hour = data.getStringExtra("hour");
                if(data.hasExtra("min"))  min = data.getStringExtra("min");
                markerArr.set(selectedPhotoPosition,markerColor);
                latitudeArr.set(selectedPhotoPosition,latitude);
                longitudeArr.set(selectedPhotoPosition,longitude);
                addressArr.set(selectedPhotoPosition,address);
                transArr.set(selectedPhotoPosition,trans);
                hourArr.set(selectedPhotoPosition,hour);
                minArr.set(selectedPhotoPosition,min);
                subCommentArr.set(selectedPhotoPosition,subcomment);

                if(markerArr.get(selectedPhotoPosition).equals("N"))markerImage.setImageResource(R.drawable.map_pin_grey);
                else if(markerArr.get(selectedPhotoPosition).equals("R"))markerImage.setImageResource(R.drawable.map_pin_red);
                else if(markerArr.get(selectedPhotoPosition).equals("Y"))markerImage.setImageResource(R.drawable.map_pin_yellow);
                else if(markerArr.get(selectedPhotoPosition).equals("B"))markerImage.setImageResource(R.drawable.map_pin_navy);

                if(transArr.get(selectedPhotoPosition).equals("0"))transImage.setImageResource(R.drawable.choice_transport);
                else if(transArr.get(selectedPhotoPosition).equals("1"))transImage.setImageResource(R.drawable.subway);
                else if(transArr.get(selectedPhotoPosition).equals("2"))transImage.setImageResource(R.drawable.bus);
                else if(transArr.get(selectedPhotoPosition).equals("3"))transImage.setImageResource(R.drawable.train);
                else if(transArr.get(selectedPhotoPosition).equals("4"))transImage.setImageResource(R.drawable.car);
                else if(transArr.get(selectedPhotoPosition).equals("5"))transImage.setImageResource(R.drawable.airplane);
                else if(transArr.get(selectedPhotoPosition).equals("9"))transImage.setImageResource(R.drawable.boat);
                else if(transArr.get(selectedPhotoPosition).equals("7"))transImage.setImageResource(R.drawable.bicycle);
                else if(transArr.get(selectedPhotoPosition).equals("8"))transImage.setImageResource(R.drawable.walk);

                if(!hourArr.get(selectedPhotoPosition).equals("N"))
                {
                    hour_txt.setText(hourArr.get(selectedPhotoPosition)+"h");
                    hour_txt.setTextColor(Color.parseColor("#000000"));

                }
                if(!minArr.get(selectedPhotoPosition).equals("N"))
                {
                    min_txt.setText(minArr.get(selectedPhotoPosition)+"m");
                    min_txt.setTextColor(Color.parseColor("#000000"));
                }
                if(hourArr.get(selectedPhotoPosition).equals("N")&&minArr.get(selectedPhotoPosition).equals("N"))
                {
                    hour_txt.setText("0h");
                    min_txt.setText("0m");
                    hour_txt.setTextColor(Color.parseColor("#CCCCCC"));
                    min_txt.setTextColor(Color.parseColor("#CCCCCC"));
                }
                break;

            case 2:

                break;

            case 3:
                if(data.hasExtra("marker"))  markerColor = data.getStringExtra("marker");
                if(data.hasExtra("latitude"))    latitude = data.getStringExtra("latitude");
                if(data.hasExtra("longitude")) longitude = data.getStringExtra("longitude");
                if(data.hasExtra("address"))   address = data.getStringExtra("address");
                if(data.hasExtra("trans"))  trans = data.getStringExtra("trans");
                if(data.hasExtra("subcomment")) subcomment = data.getStringExtra("subcomment");
                markerArr.set(selectedPhotoPosition,markerColor);
                latitudeArr.set(selectedPhotoPosition,latitude);
                longitudeArr.set(selectedPhotoPosition,longitude);
                addressArr.set(selectedPhotoPosition,address);
                transArr.set(selectedPhotoPosition,trans);
                subCommentArr.set(selectedPhotoPosition,subcomment);

                if(markerArr.get(selectedPhotoPosition).equals("N"))markerImage.setImageResource(R.drawable.map_pin_grey);
                else if(markerArr.get(selectedPhotoPosition).equals("R"))markerImage.setImageResource(R.drawable.map_pin_red);
                else if(markerArr.get(selectedPhotoPosition).equals("Y"))markerImage.setImageResource(R.drawable.map_pin_yellow);
                else if(markerArr.get(selectedPhotoPosition).equals("B"))markerImage.setImageResource(R.drawable.map_pin_navy);

                if(transArr.get(selectedPhotoPosition).equals("0"))transImage.setImageResource(R.drawable.choice_transport);
                else if(transArr.get(selectedPhotoPosition).equals("1"))transImage.setImageResource(R.drawable.subway);
                else if(transArr.get(selectedPhotoPosition).equals("2"))transImage.setImageResource(R.drawable.bus);
                else if(transArr.get(selectedPhotoPosition).equals("3"))transImage.setImageResource(R.drawable.train);
                else if(transArr.get(selectedPhotoPosition).equals("4"))transImage.setImageResource(R.drawable.car);
                else if(transArr.get(selectedPhotoPosition).equals("5"))transImage.setImageResource(R.drawable.airplane);
                else if(transArr.get(selectedPhotoPosition).equals("9"))transImage.setImageResource(R.drawable.boat);
                else if(transArr.get(selectedPhotoPosition).equals("7"))transImage.setImageResource(R.drawable.bicycle);
                else if(transArr.get(selectedPhotoPosition).equals("8"))transImage.setImageResource(R.drawable.walk);

                break;

        }

    }

    private void callApi(){
        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_TRIP_CREATE_PHOTO);
        progressDialog.show();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(comment_edt.getWindowToken(), 0);




        for(int i=0;i<photoArr.size();i++)
        {
            if(i<9) client.addParameter("file0"+String.valueOf(i+1), ImageUtil.toFile(photoArr.get(i)));
            else client.addParameter("file"+String.valueOf(i+1),ImageUtil.toFile(photoArr.get(i)));

        }

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                LogUtil.d("사진 전송실패");
                progressDialog.cancel();
                Toast.makeText(getApplicationContext(),"업로드 실패",Toast.LENGTH_SHORT).show();
            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS_PHOTO_REGISTER){
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);

                    String trip_uid = JSONUtil.getString(json, "trip_uid");
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "tripPhoto");
                    JSONObject aa;
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        try {
                            aa = jsonArray.getJSONObject(i);
                            photoIdx.add(aa.getString("idx"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        LogUtil.d("photoIdx = "+i+photoIdx.get(i));
                    }

                    callApi_TripCreate(trip_uid);

                }else {
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    private void callApi_TripCreate(String trip_uid)
    {

        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_TRIP_CREATE);
        // trip_uid
        client.addParameter("trip_uid", trip_uid);

        for(int i=0;i<photoIdx.size();i++)
        {
            client.addParameter("photos["+i+"].idx",photoIdx.get(i));
            client.addParameter("photos["+i+"].latitude",latitudeArr.get(i));
            client.addParameter("photos["+i+"].longitude",longitudeArr.get(i));
            client.addParameter("photos["+i+"].addr",addressArr.get(i));
            client.addParameter("photos["+i+"].m_color",markerArr.get(i));
            client.addParameter("photos["+i+"].trans_idx",Integer.parseInt(transArr.get(i)));
            client.addParameter("photos["+i+"].route_comment",subCommentArr.get(i));
            client.addParameter("photos["+i+"].hour",hourArr.get(i));
            client.addParameter("photos["+i+"].min",minArr.get(i));

            //client.addParameter("route["+i+"].trans_idx",transArr.get(i));
            LogUtil.d("sssssssssssssssssss"+photoIdx.get(i));
            LogUtil.d("sssssssssssssssssss"+latitudeArr.get(i));
            LogUtil.d("sssssssssssssssssss"+longitudeArr.get(i));
            LogUtil.d("sssssssssssssssssss"+addressArr.get(i));
            LogUtil.d("sssssssssssssssssss"+markerArr.get(i));
            LogUtil.d("sssssssssssssssssss"+transArr.get(i));
            LogUtil.d("sssssssssssssssssss"+subCommentArr.get(i));
            LogUtil.d("sssssssssssssssssss"+hourArr.get(i));
            LogUtil.d("sssssssssssssssssss"+minArr.get(i));

        }

        client.addParameter("comment", comment_edt.getText());

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler()
        {
            @Override
            public void failedExecute(GBean bean)
            {
                progressDialog.cancel();
            }
        });
        client.addHandler(new GResultHandler()
        {

            @Override
            public GBean result(final GBean bean, Object result, final int returnCode, String returnMessage, final JSONObject responseData)
            {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);

                Alert alert = new Alert();

                if (returnCode == GReturnCode.SUCCESS_POSTING_REGISTER)
                {
                       /* alert.setOnCloseListener(new Alert.OnCloseListener()
                        {
                            @Override
                            public void onClose(DialogInterface dialog, int which, Object data)
                            {
                                finish();
                            }
                        });
                        alert.showAlert(getApplicationContext(), returnMessage);*/
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);



              /*      InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(comment.getWindowToken(), 0);*/

                    progressDialog.cancel();
                    ((TravelbookApp)getApplicationContext()).setResumeValue(1);
                    finish();



                }
                else
                {

                   /* alert.showAlert(getApplicationContext(), returnMessage);*/

                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);

                }

                return null;

            }

        });
        GExecutor.getInstance().cancelAndExecute(client);
    }
}
