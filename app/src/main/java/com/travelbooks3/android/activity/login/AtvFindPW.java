package com.travelbooks3.android.activity.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.travelbooks3.android.util.FormatUtil;
import com.travelbooks3.android.util.LogUtil;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;


public class AtvFindPW extends AppCompatActivity {
    private EditText _edtName;
    private EditText _edtEmail;
    private Button _btnTemPwd;
    private TextView _txtBack;
    private LinearLayout _layBack;
    //	private TextView		_resultMsg;
    private LinearLayout _baseFind;
//	private LinearLayout	_baseResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_find_pw);

        _edtName = (EditText) findViewById(R.id.edtName);
        _edtEmail = (EditText) findViewById(R.id.edtEmail);
        _edtEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                validateCheck();
                return false;
            }
        });

        _btnTemPwd = (Button) findViewById(R.id.btnTemPwd);
        _txtBack = (TextView) findViewById(R.id.go_login_txt);
        _layBack = (LinearLayout) findViewById(R.id.go_login_lay);
        _baseFind = (LinearLayout) findViewById(R.id.baseFind);
//		_baseResult = (LinearLayout) findViewById(R.id.baseResult);
//		_resultMsg = (TextView) findViewById(R.id.resultMsg);

        init();
    }


    private void init() {
        _txtBack.setText(Html.fromHtml("<u>" + getString(R.string.txt_209) + "</u>"));

        configureListener();
    }


    private void configureListener() {
        _btnTemPwd.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                validateCheck();
            }
        });
        _layBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void validateCheck() {

        String name = _edtName.getText().toString();
        String id = _edtEmail.getText().toString();

        if (FormatUtil.isNullorEmpty(name)) {
            _edtName.requestFocus();
            _edtName.setError(getString(R.string.txt_002));
            return;
        }

        if (name.contains(" ")) {
            _edtName.requestFocus();
            _edtName.setError(getString(R.string.txt_214));
            return;
        }

        if (!FormatUtil.isNameEngValidate(name)) {
            _edtName.requestFocus();
            _edtName.setError(getString(R.string.txt_198));
            return;
        }

        if (FormatUtil.isNullorEmpty(id)) {
            _edtEmail.requestFocus();
            _edtEmail.setError(getString(R.string.txt_215));
            return;
        }

        if (!FormatUtil.isEmailPattern(id)) {
            _edtEmail.requestFocus();
            _edtEmail.setError(getString(R.string.txt_149));
            return;
        }

        if (FormatUtil.isContainsKorean(id)) {
            _edtEmail.requestFocus();
            _edtEmail.setError(getString(R.string.txt_149));
            return;
        }

        callApi_IssueTemporaryPwd(id, name);
    }


    private void callApi_IssueTemporaryPwd(String id, String name) {
        _edtName.setEnabled(false);
        _edtEmail.setEnabled(false);
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);
        // _baseFind.setVisibility(View.GONE);
        // _baseResult.setVisibility(View.VISIBLE);
        // _resultMsg.setText("임시 비밀번호가 발급되었습니다.");
        GPClient client = new GPClient(this);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_ISSUE_TEMPORARY_PWD);

        client.addParameter("id", id);
        client.addParameter("name", name);

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                progress.setVisibility(View.GONE);
                _edtName.setEnabled(true);
                _edtEmail.setEnabled(true);
            }
        });

        client.addHandler(new GResultHandler() {

            @Override
            public GBean result(final GBean bean, Object result, final int returnCode, String returnMessage, final JSONObject responseData) {
                progress.setVisibility(View.GONE);
                _edtName.setEnabled(true);
                _edtEmail.setEnabled(true);
                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);
                if (returnCode == GReturnCode.SUCCESS_TEMP_PASS_TITLE) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AtvFindPW.this);
                    builder.setMessage(returnMessage.replace("[TRAVELBOOK]", ""));
                    builder.setPositiveButton(getString(R.string.txt_211), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                    /*returnMessage = returnMessage.replace("[TRAVELBOOK]", "");
                    Alert alert = new Alert();
                    alert.setOnCloseListener(new Alert.OnCloseListener() {
                        @Override
                        public void onClose(DialogInterface dialog, int which, Object data) {
                            if (which == Alert.BUTTON1) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    });
                    alert.showAlert(AtvFindPW.this, returnMessage);*/
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AtvFindPW.this);
                    builder.setMessage(returnMessage);
                    builder.setPositiveButton(getString(R.string.txt_211), null);
                    builder.show();
                }
                return null;
            }

        });

        GExecutor.getInstance().cancelAndExecute(client);
    }

}
