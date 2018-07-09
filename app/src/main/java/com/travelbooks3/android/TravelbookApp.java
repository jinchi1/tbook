package com.travelbooks3.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.travelbooks3.android.activity.login.AtvLogin;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.SPUtil;

/**
 * Created by system777 on 2017-06-21.
 */

public class TravelbookApp extends  MultiDexApplication{//MultiDexApplication

    @Override
    public void onCreate() {
        super.onCreate();
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }


    public void test(){
        LogUtil.d("application 테스트 중");
//        Toast.makeText(this,"테스트",Toast.LENGTH_SHORT).show();

    }

    public void logout(Activity activity, Intent intent){
        LogUtil.intent("test logout", intent);

        SPUtil.getInstance().logOut(this);

        Intent newIntent = new Intent(this, AtvLogin.class);
        newIntent.setAction("LOGOUT");

        if(intent != null) if(intent.getExtras() != null) newIntent.putExtras(intent.getExtras());

        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(newIntent);

        activity.finishAffinity();
    }

    private int resumeValue;

    public int getResumeValue(){
        return resumeValue;
    }
    public void setResumeValue(int value){
        this.resumeValue = value;
    }
}
