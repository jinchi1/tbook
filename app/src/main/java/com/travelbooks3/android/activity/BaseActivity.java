package com.travelbooks3.android.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.travelbooks3.android.R;
import com.travelbooks3.android.TravelbookApp;
import com.travelbooks3.android.activity.info.OtherInfoFragment;
import com.travelbooks3.android.activity.main.ModifyMainFragment;
import com.travelbooks3.android.activity.news.FollowingPagerFragment;
import com.travelbooks3.android.activity.news.NewsFragment;
import com.travelbooks3.android.activity.info.InfoFragment;
import com.travelbooks3.android.activity.main.MainFragment;
import com.travelbooks3.android.activity.search.SearchPagerFragment;
import com.travelbooks3.android.activity.search.SearchTagPostFragment;
import com.travelbooks3.android.activity.write.SelectPhotoActivity;
import com.travelbooks3.android.activity.write.WriteActivity;
import com.travelbooks3.android.util.BackPressCloseHandler;
import com.travelbooks3.android.util.LogUtil;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    public LinearLayout mBtnMenu1;
    public LinearLayout mBtnMenu2;
    public LinearLayout mBtnMenu3;
    public LinearLayout mBtnMenu4;
    public LinearLayout mBtnMenu5;
    private ImageView[] mBtnMenu = new ImageView[5];

    public TextView push_txt;

    private FragmentManager fragmentManager;

    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        push_txt = (TextView)findViewById(R.id.push_txt);

        Bundle bundle = getIntent().getExtras();


        if(bundle == null){
            Toast.makeText(this, "데이타가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        String move = bundle.getString("move");

        if(move.equals("main"))
        {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_base_lay, new MainFragment());
        fragmentManager.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.commit();

        baseInit(0);
        }
        else if(move.equals("info"))
        {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main_base_lay, new InfoFragment());
            fragmentTransaction.commit();

            baseInit(4);
        }
        else if(move.equals("search"))
        {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main_base_lay, new SearchPagerFragment());
            fragmentTransaction.commit();

            baseInit(2);
        }
        else if(move.equals("noti"))
        {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main_base_lay, new NewsFragment());
            fragmentTransaction.commit();

            baseInit(3);
        }
        else if(move.equals("otherinfo"))
        {
            String user_idx = bundle.getString("search_user_idx");
            LogUtil.d("bbbbbbbbbbbbbbbbbbbb"+user_idx);
            Bundle bundle1 = new Bundle();
            bundle1.putString("search_user_idx", user_idx);
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment other = new OtherInfoFragment();
            other.setArguments(bundle1);
            fragmentTransaction.add(R.id.main_base_lay, other);
            fragmentTransaction.commit();

            baseInit(5);
        }
        else if(move.equals("hashtag"))
        {
            String a = bundle.getString("word");
            Bundle bundle1 = new Bundle();
            bundle1.putString("word", a);
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment other = new SearchTagPostFragment();
            other.setArguments(bundle1);
            fragmentTransaction.add(R.id.main_base_lay, other);
            fragmentTransaction.commit();

            baseInit(1);
        }
        else if(move.equals("modify"))
        {
            String trip_uid = bundle.getString("trip_uid");
            String comment = bundle.getString("comment");

            Bundle bundle1 = new Bundle();
            bundle1.putString("trip_uid", trip_uid);
            bundle1.putString("comment",comment);
            fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment other = new ModifyMainFragment();
            other.setArguments(bundle1);
            fragmentTransaction.add(R.id.main_base_lay, other);
            fragmentTransaction.commit();
        }

        IntentFilter intentfilter = new IntentFilter(); intentfilter.addAction("push");

        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                push();
                unregisterReceiver(mReceiver);
            }
        };

        registerReceiver(mReceiver, intentfilter);


    }

    @Override
    protected void onResume() {


        if(((TravelbookApp)getApplicationContext()).getResumeValue()==1)
        {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_base_lay, new MainFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            baseInit(0);
            ((TravelbookApp)getApplicationContext()).setResumeValue(0);
        }

        else if(((TravelbookApp)getApplicationContext()).getResumeValue()==2)
        {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_base_lay, new SearchPagerFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            baseInit(1);
            ((TravelbookApp)getApplicationContext()).setResumeValue(0);
        }

        else if(((TravelbookApp)getApplicationContext()).getResumeValue()==4)
        {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_base_lay, new NewsFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            baseInit(3);
            ((TravelbookApp)getApplicationContext()).setResumeValue(0);
        }


        else if(((TravelbookApp)getApplicationContext()).getResumeValue()==5)
        {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_base_lay, new InfoFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            baseInit(4);
            ((TravelbookApp)getApplicationContext()).setResumeValue(0);
        }



        super.onResume();
    }

    public void test(){
        ((TravelbookApp)getApplicationContext()).test();
    }

    public void push(){
        push_txt.setVisibility(View.VISIBLE);
    }

    public void baseInit(int a){

        mBtnMenu1 = (LinearLayout) findViewById(R.id.btnMenu01);
        mBtnMenu2 = (LinearLayout) findViewById(R.id.btnMenu02);
        mBtnMenu3 = (LinearLayout) findViewById(R.id.btnMenu03);
        mBtnMenu4 = (LinearLayout) findViewById(R.id.btnMenu04);
        mBtnMenu5 = (LinearLayout) findViewById(R.id.btnMenu05);

        ImageView mBtnImg1 = (ImageView)findViewById(R.id.btnImg1);
        ImageView mBtnImg2 = (ImageView)findViewById(R.id.btnImg2);
        ImageView mBtnImg3 = (ImageView)findViewById(R.id.btnImg3);
        ImageView mBtnImg4 = (ImageView)findViewById(R.id.btnImg4);
        ImageView mBtnImg5 = (ImageView)findViewById(R.id.btnImg5);

        mBtnMenu[0] = mBtnImg1;
        mBtnMenu[1] = mBtnImg2;
        mBtnMenu[2] = mBtnImg3;
        mBtnMenu[3] = mBtnImg4;
        mBtnMenu[4] = mBtnImg5;

        mBtnMenu1.setOnClickListener(this);
        mBtnMenu2.setOnClickListener(this);
        mBtnMenu3.setOnClickListener(this);
        mBtnMenu4.setOnClickListener(this);
        mBtnMenu5.setOnClickListener(this);

        if(a==0) btnImg(0);
        if(a==1) btnImg(1);
        if(a==2) btnImg(2);
        if(a==3) btnImg(3);
        if(a==4) btnImg(4);

/*
        Bundle bundle = getIntent().getExtras();
        String move = "";

        if(bundle == null){
            Toast.makeText(this, "데이타가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        move = bundle.getString("move");
        LogUtil.d("getExtra move : "+move);

        switch (move){
            case "main":
                mBtnMenu1.isClickable();
                break;
            case "search":
                mBtnMenu2.isClickable();
                break;
            case "like":
                mBtnMenu4.isClickable();
                break;
            case "info":
                mBtnMenu5.isClickable();
                break;
            default:
                mBtnMenu1.isClickable();
                break;
        }*/
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_base_lay);
        String fragmentTag = fragment.getClass().getSimpleName();

        switch (v.getId()){
            case R.id.btnMenu01:

                if(fragment instanceof MainFragment==false) {
                    btnImg(0);

                    fragmentManager.popBackStack(fragmentTag,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.main_base_lay, new MainFragment());
                    fragmentTransaction.addToBackStack(fragmentTag);
                    //fragmentTransaction.addToBackStack(null);

                    //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.btnMenu02:

                if(fragment instanceof SearchPagerFragment==false) {


                    fragmentManager.popBackStack(fragmentTag,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    btnImg(1);
                    fragmentTransaction.replace(R.id.main_base_lay, new SearchPagerFragment());
                    fragmentTransaction.addToBackStack(fragmentTag);

                    //fragmentTransaction.addToBackStack(null);
                    //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.btnMenu03:
                Intent intent = new Intent(this, SelectPhotoActivity.class);
                startActivity(intent);
                break;


                /*Intent intent = new Intent(this, WriteActivity.class);
                startActivity(intent);
                break;
           */ case R.id.btnMenu04:
                push_txt.setVisibility(View.GONE);
                if(fragment instanceof NewsFragment==false) {

                    btnImg(3);

                    fragmentManager.popBackStack(fragmentTag,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.main_base_lay, new NewsFragment());

                    fragmentTransaction.addToBackStack(fragmentTag);

                    //fragmentTransaction.addToBackStack(null);
                    //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.btnMenu05:
                if(fragment instanceof InfoFragment==false) {


                    btnImg(4);
                    fragmentManager.popBackStack(fragmentTag,FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    fragmentTransaction.replace(R.id.main_base_lay, new InfoFragment());

                    fragmentTransaction.addToBackStack(fragmentTag);

                    //fragmentTransaction.addToBackStack(null);
                    //fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.commit();

                /*
                fragmentTransaction.replace(R.id.main_base_lay, new MypageActivity());
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.commit();*/
                }
                break;
        }
    }

    private void btnImg(int click){
        LogUtil.d("clickBtn is "+click);
        for (int i = 0 ; i < 5; i++){
            if(click == i){
                mBtnMenu[i].setSelected(true);
            }else{
                mBtnMenu[i].setSelected(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(((TravelbookApp)getApplicationContext()).getResumeValue()==6)
        {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_base_lay, new InfoFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            baseInit(4);
            ((TravelbookApp)getApplicationContext()).setResumeValue(0);
        }

        else if(((TravelbookApp)getApplicationContext()).getResumeValue()==0) {
            super.onBackPressed();
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_base_lay);       //현재 최상위 프래그먼트 가져오기

        if (fragment instanceof MainFragment){

            baseInit(0);
        }
        else if (fragment instanceof SearchPagerFragment){

            baseInit(1);
        }
        else if (fragment instanceof NewsFragment)
        {
            baseInit(3);
        }
        else if (fragment instanceof InfoFragment)
        {
            baseInit(4);
        }


    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BaseActivity.this);
            alert_confirm.setMessage("트래블북을 종료 하시겠습니까?")
                    .setPositiveButton("예",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finishAffinity();

                        }
                    }).setNegativeButton("아니오",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alert_confirm.create();
            alertDialog.show();
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //finish();
                }
            });

            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }




}

