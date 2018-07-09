package com.travelbooks3.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.login.AtvLogin;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.CommonUtil;
import com.travelbooks3.android.util.FormatUtil;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.SPUtil;

import org.json.JSONObject;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
       init();
    }

    private void init(){
        String country = SPUtil.getInstance().getUserCountry(this);
        LogUtil.d("country : " + country);

        if (FormatUtil.isNullorEmpty(country))
        {
            country = CommonUtil.getCountry(this);

            SPUtil.getInstance().setUserCountry(this, country);
        }

        CommonUtil.setLanguage(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNetwork();
            }
        }, 1500);
    }

    private void checkNetwork(){
        if(CommonUtil.isNetworkOn(this)){
            callApi_AppVersion();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.txt_159);
            builder.setPositiveButton(getString(R.string.txt_1006), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkNetwork();
                }
            });
            builder.setNegativeButton(getString(R.string.txt_1007), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    private void callApi_AppVersion(){
        GPClient client = new GPClient(this);
        client.setUri(Constants.SERVER_URL_API_APP_VERSION);

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                LogUtil.d("HttpStatus : "+bean.getHttpStatus());
                LogUtil.d("Exception : "+bean.getException());
                //movePageHome();
            }
        });
        client.addHandler(new GResultHandler() {
            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {
                LogUtil.d("returnCode : "+returnCode);
                LogUtil.d("returnMessage : "+returnMessage);

                if(returnCode == GReturnCode.SUCCESS){
                    try {
                        JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                        if(CommonUtil.getCurrentVersion(getApplicationContext()).equals(json.getString("app_version"))){
                            movePageHome();
                        }
                        else {
                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(IntroActivity.this);
                            alert_confirm.setMessage("트래블북을 최신버전으로 업데이트 해주세요.")
                                    .setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(Constants.MARKET_URL));
                                            startActivity(intent);
                                        }
                                    }).setNegativeButton("종료", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            AlertDialog alertDialog = alert_confirm.create();
                            alertDialog.show();
                        }

                    } catch (Exception e) {
                        LogUtil.d("list err = "+e.toString());

                    }
                }else {

                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);

                }

                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    private void movePageHome(){
        Intent intent = null;

        // 로그인 여부 체크
        if (SPUtil.getInstance().getIsLogin(IntroActivity.this))
        {
            intent = new Intent(this, BaseActivity.class);
            int index = SPUtil.getInstance().getStartIndex(IntroActivity.this);
            LogUtil.d("index : "+index);

            switch (index)
            {
                case 0:
                    intent.putExtra("move", "main");
//                    intent.setClass(IntroActivity.this, AtvMain.class);
                    break;
                case 1:
                    intent.putExtra("move", "search");
//                    intent.setClass(IntroActivity.this, AtvSearchMap.class);
                    break;
                case 2:
                    intent.putExtra("move", "main");
//                    intent.setClass(IntroActivity.this, AtvMain.class);
//                    intent.setAction("GO_WRITE");
                    break;
                case 3:
                    intent.putExtra("move", "like");
//                    intent.setClass(IntroActivity.this, AtvLike.class);
                    break;
                case 4:
                    intent.putExtra("move", "info");
//                    intent.setClass(IntroActivity.this, AtvMyinfo.class);
//                    intent.putExtra("is_top", "Y");
                    break;
                default:
                    intent.putExtra("move", "main");
                    break;
            }

        }
        else
        {
            intent = new Intent(IntroActivity.this, AtvLogin.class);
        }

        startActivity(intent);
        finish();
    }
}
