package com.travelbooks3.android.activity.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.travelbooks3.android.BuildConfig;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.BaseActivity;
import com.travelbooks3.android.fcm.MyFirebaseInstanceIDService;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.Alert;
import com.travelbooks3.android.util.CommonUtil;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.util.FormatUtil;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.travelbooks3.android.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;


public class AtvLogin extends AppCompatActivity
{

    private AppCompatEditText _edtID;
    private AppCompatEditText _edtPW;
                                       
    private Button          _btnLogin  = null;
    private LinearLayout    _btnRegist = null;


    private TextView        _txtFindIdPwd;
                            
    private List<Button>    _list      = new ArrayList<Button>();
    private Button          _btnLogin01;
    private Button          _btnLogin02;
    private Button          _btnLogin03;
    private Button          _btnLogin04;
    private Button          _btnLogin05;
    private LoginButton     _loginButton;

    private String email       = "";
    private String sns_user_id = "";
                            
    private CallbackManager callbackManager;
                            
                            
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_login);



        _edtID = (AppCompatEditText) findViewById(R.id.edtID);
        _edtPW = (AppCompatEditText) findViewById(R.id.edtPW);
        _edtPW.setImeOptions(EditorInfo.IME_ACTION_DONE);
        _edtPW.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                   validateCheck();
                }
                return false;
            }
        });

        _btnLogin = (Button) findViewById(R.id.btnLogin);//로그인버튼
        _btnRegist = (LinearLayout) findViewById(R.id.btnRegist);

        _txtFindIdPwd = (TextView) findViewById(R.id.txtFindIdPwd);

        _btnLogin01 = (Button) findViewById(R.id.btnLogin01);
        _btnLogin02 = (Button) findViewById(R.id.btnLogin02);
        _btnLogin03 = (Button) findViewById(R.id.btnLogin03);
        _btnLogin04 = (Button) findViewById(R.id.btnLogin04);
        _btnLogin05 = (Button) findViewById(R.id.btnLogin05);

        _loginButton = (LoginButton) findViewById(R.id.btnRegistFacebook);

        init();
        settingListener();
    }

    
    private void init()
    {
        
        _list.add(_btnLogin01);
        _list.add(_btnLogin02);
        _list.add(_btnLogin03);
        _list.add(_btnLogin04);
        _list.add(_btnLogin05);
        
        findViewById(R.id.baseTest).setVisibility(View.GONE);
        
        _txtFindIdPwd.setText(Html.fromHtml("<u>" + getString(R.string.txt_008) + "</u>"));
        
        if (!BuildConfig.isLogin)
        {
            findViewById(R.id.baseTest).setVisibility(View.GONE);
        }
    }
                       

    private void settingListener()
    {
        
        if (_loginButton != null)
        {
            callbackManager = CallbackManager.Factory.create();
            
            _loginButton.setReadPermissions("public_profile,email,user_about_me,user_friends,read_custom_friendlists,user_status");
            _loginButton.registerCallback(callbackManager,
            
            new FacebookCallback<LoginResult>()
            {
                
                @Override
                
                public void onSuccess(LoginResult loginResult)
                {
                    // 로그인이 성공되었을때 호출
                    
                    LogUtil.d("onSuccess, loginResult : " + loginResult.getAccessToken().getToken());
                    LogUtil.d("onSuccess, loginResult : " + loginResult.getAccessToken().getUserId());
                    LogUtil.d("onSuccess, loginResult : " + loginResult.getAccessToken().toString());
                    
                    GraphRequest request = GraphRequest.newMeRequest(
                    
                    loginResult.getAccessToken(),
                    
                    new GraphRequest.GraphJSONObjectCallback()
                    {
                        
                        @Override
                        
                        public void onCompleted(
                        
                        JSONObject object,
                        
                        GraphResponse response)
                        {
                            try
                            {
                                sns_user_id = (String) response.getJSONObject().get("id");// 페이스북 아이디값
                                
                                String name = (String) response.getJSONObject().get("name");// 페이스북 이름
                                
                                email = (String) response.getJSONObject().get("email");// 이메일
                                
                                String gender = (String) response.getJSONObject().get("gender");// 이메일
                                
                                LogUtil.json("response", response.getJSONObject());
                                LogUtil.d("sns_user_id : " + sns_user_id);
                                LogUtil.d("name : " + name);
                                LogUtil.d("email : " + email);

                                callApi_RegisterSns(email, name, "", sns_user_id);
                                
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                    
                    Bundle parameters = new Bundle();
                    
                    parameters.putString("fields", "id,name,email,gender,birthday,public_key");
                    
                    request.setParameters(parameters);
                    
                    request.executeAsync();
                }
                
                
                @Override
                public void onCancel()
                {
                    Toast.makeText(AtvLogin.this, "로그인을 취소 하였습니다!", Toast.LENGTH_SHORT).show();
                }
                
                
                @Override
                public void onError(FacebookException exception)
                {
                    LogUtil.d("facebook error : "+exception);
                    Toast.makeText(AtvLogin.this, "에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
                }
                
            });
            
        }
        
        for (int i = 0; i < _list.size(); i++)
        {
            final int index = i;
            _list.get(i).setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    if (index == 0) callApi_Login("Dnfdlrmekd@gmail.com", "wktnwjd1!");
                    if (index == 1) callApi_Login("unique36@naver.com", "!top6202");
                    if (index == 2) callApi_Login("ndyoon89@naver.com", "89rhaehfdl@");
                    if (index == 3) callApi_Login("12059896@naver.com", "k12059896@");
                    if (index == 4) callApi_Login("dev.swingman@gmail.com", "!top6202");
                }
            });
        }

        _btnLogin.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
           /*     String id = _edtID.getText().toString();
                String pw = _edtPW.getText().toString();*/
                validateCheck();

            }
        });
        
        _btnRegist.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(AtvLogin.this, AtvRegist.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_MEMBERSHIP);
//                startActivity(intent);
            }
        });

        _txtFindIdPwd.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(AtvLogin.this, AtvFindPW.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_FIND_PASSWORD);

            }
        });
    }

    private void validateCheck(){
        String id = _edtID.getText().toString().trim();
        String pw = _edtPW.getText().toString().trim();

        LogUtil.d("password ["+pw +"]"+ " = "+FormatUtil.isPasswordValidate(pw));



        if(id.contains(" ")){
            _edtID.requestFocus();
            _edtID.setError(getString(R.string.txt_214));
            return;
        }

        if(pw.contains(" ")){
            _edtPW.requestFocus();
            _edtPW.setError(getString(R.string.txt_214));
            _edtPW.setText("");
            return;
        }

        if (FormatUtil.isNullorEmpty(id))
        {
            _edtID.requestFocus();
            _edtID.setError(getString(R.string.txt_001));
            return;
        }

        if(!FormatUtil.isEmailPattern(id)){
            _edtID.requestFocus();
            _edtID.setError(getString(R.string.txt_199));
            return;
        }

        if (FormatUtil.isNullorEmpty(pw))
        {
            _edtPW.requestFocus();
            _edtPW.setError(getString(R.string.txt_003));
            return;
        }
        if (FormatUtil.isContainsKorean(pw))
        {
            _edtPW.requestFocus();
            _edtPW.setError(getString(R.string.txt_102));
            return;
        }

//        if (!FormatUtil.isPasswordValidate(pw))
//        {
//            _edtPW.requestFocus();
//            _edtPW.setError(getString(R.string.txt_200));
//            return;
//        }

        callApi_Login(id, pw);
    }
    
    
    private void callApi_Login(final String id, String pw)
    {
        callApi_Login(id, pw, "");
    }
    

    private void callApi_Login(final String id,  String pw, String sns_user_id)
    {
        String push_token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        LogUtil.d("callApi_Login id : "+id+", pw : "+pw);
        GPClient client = new GPClient(AtvLogin.this);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_LOGIN);
        
        client.addParameter("id", id);
        client.addParameter("pwd", pw);

        if(push_token!=null) client.addParameter("push_token",push_token);
        if (!FormatUtil.isNullorEmpty(sns_user_id))
        {
            client.addParameter("sns_type", "F");
            client.addParameter("sns_user_id", sns_user_id);
        }
        
        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler()
        {
            @Override
            public void failedExecute(GBean bean)
            {
//                LogUtil.d("failedExecute = "+bean.getException().getMessage());
                Toast.makeText(AtvLogin.this, getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            }
        });
        client.addHandler(new GResultHandler()
        {
            
            @Override
            public GBean result(final GBean bean, Object result, final int returnCode, final String returnMessage, final JSONObject responseData)
            {
                
                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);
                
                String auth_token = (String) bean.get(GHttpConstants.AUTH_TOKEN);
                
                LogUtil.d("return auth_token : " + auth_token);
                
                if (!FormatUtil.isNullorEmpty(auth_token))
                {
                    // auth_token 로컬 저장
                    SPUtil.getInstance().setAuthToken(getBaseContext(), auth_token);
                }
                
                if (returnCode == GReturnCode.SUCCESS_LOGIN)
                {
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONObject user = JSONUtil.getJSONObject(json, "user");
                    
                    LogUtil.d("user_idx", JSONUtil.getInteger(user, "user_idx"));
                    LogUtil.d("user",user.toString());
                    LogUtil.d("id",id);

                    SPUtil.getInstance().setUserNumber(AtvLogin.this, JSONUtil.getInteger(user, "user_idx"));
                    SPUtil.getInstance().setUserInfo(AtvLogin.this, user);
                    SPUtil.getInstance().setUserID(AtvLogin.this, id);
                    SPUtil.getInstance().setIsLogin(AtvLogin.this, true);

                    // 메인화면으로 이동 - 요청에 의해 메시지 표시 생략
                    Intent intent = new Intent();
                    intent.setClass(AtvLogin.this, BaseActivity.class);
                    intent.putExtra("move","main");
                    startActivity(intent);
                    finish();
                }
                else
                {
//                    Alert alert = new Alert();
//                    alert.showAlert(AtvLogin.this, returnMessage);
                    AlertDialog.Builder builder = new AlertDialog.Builder(AtvLogin.this);
                    builder.setMessage(returnMessage);
                    builder.setCancelable(false);
                    builder.setPositiveButton(getString(R.string.txt_211), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                
                return null;
            }
            
        });
        GExecutor.getInstance().cancelAndExecute(client);
    }
    
    
    private void callApi_RegisterSns(String id, String name, String pwd, final String sns_user_id)
    {
        String country = CommonUtil.getCountry(AtvLogin.this);
        
        GPClient client = new GPClient(this);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_REGISTER);
        
        client.addParameter("id", id);
        client.addParameter("name", name);

        client.addParameter("pwd", pwd);
        client.addParameter("country", country);
        client.addParameter("push", "0");
        client.addParameter("sns_type", "F");
        client.addParameter("sns_user_id", sns_user_id);
        
        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_ARRAY));
        client.addHandler(new GFailedHandler()
        {
            @Override
            public void failedExecute(GBean bean)
            {
            
            }
        });
        client.addHandler(new GResultHandler()
        {
            
            @Override
            public GBean result(final GBean bean, Object result, final int returnCode, String returnMessage, final JSONObject responseData)
            {
                
                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);
                
                if (returnCode == GReturnCode.SUCCESS_JOIN)
                {
                    Alert alert = new Alert();
                    alert.setOnCloseListener(new Alert.OnCloseListener()
                    {
                        @Override
                        public void onClose(DialogInterface dialog, int which, Object data)
                        {
                            if (which == Alert.BUTTON1)
                            {
                                // finish();
                                callApi_Login(email, "", sns_user_id);
                            }
                        }
                    });
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    
                    String message = JSONUtil.getString(json, "invitation_word");
                    message = message.toString().replace("|", "\n");
                    alert.showAlert(AtvLogin.this, message, false, getString(R.string.txt_142));
                }
                else if (returnCode == GReturnCode.EXISTS)
                {
                    callApi_Login(email, "", sns_user_id);
                }
                else
                {
                    Alert alert = new Alert();
                    alert.showAlert(AtvLogin.this, returnMessage);
                }
                
                return null;
            }
            
        });
        GExecutor.getInstance().cancelAndExecute(client);
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogUtil.d("onActivityResult is " + requestCode + ", " + resultCode);

        if (requestCode == Constants.REQUEST_CODE_MEMBERSHIP && resultCode == RESULT_OK) {
            String id = data.getStringExtra("id");
            String pwd = data.getStringExtra("pwd");
            LogUtil.d("onActivityResult id : " + id + ", pwd : " + pwd);

            if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(pwd)) {
                callApi_Login(id, pwd);
            }
        }else if(requestCode == Constants.REQUEST_CODE_FIND_PASSWORD && resultCode == RESULT_OK){
            //_edtPW.setText("");

        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


}
