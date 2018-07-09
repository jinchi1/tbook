package com.travelbooks3.android.activity.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;

import com.travelbooks3.android.R;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.Alert;
import com.travelbooks3.android.util.CommonUtil;
import com.travelbooks3.android.util.FormatUtil;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;


public class AtvRegist extends AppCompatActivity {
    private AppCompatEditText _edtEmail;
    private AppCompatEditText _edtName;
    private AppCompatEditText _edtPwd;

    private Button _btnRegist;
    private TextView _txtGoLogin;
    private LinearLayout _layGoLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.atv_regist2);

        _edtName = (AppCompatEditText) findViewById(R.id.edtName);
        _edtEmail = (AppCompatEditText) findViewById(R.id.edtEmail);
        _edtPwd = (AppCompatEditText) findViewById(R.id.edtPwd);
        _edtPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (checkName() && checkEmail() && checkPwd()) {
                        callApi_Register();
                    }
                }
                return false;
            }
        });

        _btnRegist = (Button) findViewById(R.id.btnRegist);
        _layGoLogin = (LinearLayout) findViewById(R.id.go_login_lay);
        _txtGoLogin = (TextView) findViewById(R.id.go_login_txt);

        configureListener();
    }

    private void configureListener() {

        _edtName.setOnFocusChangeListener(focusChangeListener);
        _edtEmail.setOnFocusChangeListener(focusChangeListener);
        _edtPwd.setOnFocusChangeListener(focusChangeListener);

        _btnRegist.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkName() && checkEmail() && checkPwd()) {
                    callApi_Register();
                }
            }
        });

        _txtGoLogin.setText(Html.fromHtml("<u>" + getString(R.string.txt_209) + "</u>"));

        _layGoLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        _edtName.addTextChangedListener(watch);
//        _edtEmail.addTextChangedListener(watch);
//        _edtPwd.addTextChangedListener(watch);
    }

//    TextWatcher watch = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            ((AppCompatEditText)s).setError(null);
//        }
//    };


    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (hasFocus == true) {

//                Snackbar snackbar = null;

                switch (v.getId()) {
                    case R.id.edtName:
                        if(TextUtils.isEmpty(_edtName.getText().toString())){
                            _edtName.setError(getString(R.string.txt_198));
                        }
//                        snackbar = Snackbar.make(v, R.string.txt_198, Snackbar.LENGTH_SHORT);
                        break;
                    case R.id.edtEmail:
                        if(TextUtils.isEmpty(_edtEmail.getText().toString())){
                            _edtEmail.setError(getString(R.string.txt_199));
                        }
//                        snackbar = Snackbar.make(v, R.string.txt_199, Snackbar.LENGTH_SHORT);
                        break;
                    case R.id.edtPwd:
                        if(TextUtils.isEmpty(_edtPwd.getText().toString())){
                            _edtPwd.setError(getString(R.string.txt_200));
                        }
//                        snackbar = Snackbar.make(v, R.string.txt_200, Snackbar.LENGTH_SHORT);
                        break;
                }

//                View snackView = snackbar.getView();
//                snackView.setBackgroundColor(getResources().getColor(R.color.color_4167b2));
//                snackbar.show();
            }
        }
    };

    private boolean checkName() {
        String name = _edtName.getText().toString();

        //null check
        if (FormatUtil.isNullorEmpty(name)) {
            LogUtil.d("name is null = [" + name + "]");
//                    new Alert().showAlert(AtvRegist.this, getString(R.string.txt_002));
            _edtName.requestFocus();
            _edtName.setError(getString(R.string.txt_002));
            return false;
        }

        LogUtil.d(name + " 이름 정규화 패턴 = " + FormatUtil.isNameEngValidate(name));
        LogUtil.d(name + " 이름 한글 패턴 = " + FormatUtil.isContainsKorean(name));

        if (!FormatUtil.isNameEngValidate(name)) {
            _edtName.requestFocus();
            _edtName.setError(getString(R.string.txt_198));
            return false;
        }

        if (FormatUtil.isContainsKorean(name)) {
            _edtName.requestFocus();
            _edtName.setError(getString(R.string.txt_216));
            return false;
        }
        return true;
    }

    private boolean checkEmail() {
        String email = _edtEmail.getText().toString();

        if (FormatUtil.isNullorEmpty(email)) {
            _edtEmail.requestFocus();
            _edtEmail.setError(getString(R.string.txt_001));
            return false;
        }

        LogUtil.d("이메일 패턴 체크  = " + FormatUtil.isEmailPattern(email));

        if (!FormatUtil.isEmailPattern(email)) {
            _edtEmail.requestFocus();
            _edtEmail.setError(getString(R.string.txt_149));
            return false;
        }
        return true;
    }

    private boolean checkPwd() {
        String pwd = _edtPwd.getText().toString();

        if (FormatUtil.isNullorEmpty(pwd)) {
//                    new Alert().showAlert(AtvRegist.this, getString(R.string.txt_003));
            _edtPwd.requestFocus();
            _edtPwd.setError(getString(R.string.txt_003));
            return false;
        }

        if (FormatUtil.isContainsKorean(pwd)) {
            _edtPwd.requestFocus();
            _edtPwd.setError(getString(R.string.txt_102));
            return false;
        }

        if (!FormatUtil.isPasswordValidate(pwd)) {
            _edtPwd.requestFocus();
            _edtPwd.setError(getString(R.string.txt_200));
            return false;
        }
        return true;
    }

    // 요청에 의해 back 키 입력 시 바로 메인화면으로 이동
    // @Override
    // public void onBackPressed()
    // {
    //
    // Alert alert = new Alert();
    // alert.setOnCloseListener(new OnCloseListener()
    // {
    // @Override
    // public void onClose(DialogInterface dialog, int which, Object data)
    // {
    // if (which == Alert.BUTTON1)
    // {
    // finish();
    // }
    // }
    // });
    // alert.showAlert(AtvRegist.this, getString(R.string.txt_150), false, getString(R.string.txt_142), getString(R.string.txt_027));
    //
    // }


    private void callApi_Register() {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        final String id = _edtEmail.getText().toString();
        String name = _edtName.getText().toString();
        final String pwd = _edtPwd.getText().toString();
        String country = CommonUtil.getCountry(AtvRegist.this);

        GPClient client = new GPClient(AtvRegist.this);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_REGISTER);

        client.addParameter("id", id);
        client.addParameter("name", name);
        client.addParameter("pwd", pwd);
        client.addParameter("country", country);
        client.addParameter("push", "0");

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_ARRAY));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                progressBar.setVisibility(View.GONE);
            }
        });
        client.addHandler(new GResultHandler() {

            @Override
            public GBean result(final GBean bean, final Object result, final int returnCode, String returnMessage, final JSONObject responseData) {
                progressBar.setVisibility(View.GONE);
                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);

                if (returnCode == GReturnCode.SUCCESS_JOIN) {
                    /*Alert alert = new Alert();
                    alert.setOnCloseListener(new Alert.OnCloseListener() {

                        @Override
                        public void onClose(DialogInterface dialog, int which, Object data) {
                            if (which == Alert.BUTTON1) {
                                Intent intent = new Intent();
                                intent.putExtra("id", id);
                                intent.putExtra("pwd", pwd);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });

                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);

                    String message = JSONUtil.getString(json, "invitation_word");
                    message = message.toString().replace("|", "\n");
                    alert.showAlert(AtvRegist.this, message, false, getString(R.string.txt_142));*/

                    AlertDialog.Builder builder = new AlertDialog.Builder(AtvRegist.this);
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    String message = returnMessage;
                   // message = message.toString().replace("|", "\n");
                    builder.setMessage(message);

                    builder.setPositiveButton(getString(R.string.txt_211), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra("id", id);
                            intent.putExtra("pwd", pwd);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                    builder.show();
                } else {
//                    Alert alert = new Alert();
//                    alert.showAlert(AtvRegist.this, returnMessage);

                    AlertDialog.Builder builder = new AlertDialog.Builder(AtvRegist.this);
                    builder.setMessage(returnMessage);
                    builder.setPositiveButton(getString(R.string.txt_1006), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _edtName.setText("");
                            _edtEmail.setText("");
                            _edtPwd.setText("");
                            _edtName.requestFocus();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(getString(R.string.txt_217), new DialogInterface.OnClickListener() {
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
}
