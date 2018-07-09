package com.travelbooks3.android.activity.info.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.travelbooks3.android.R;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.FormatUtil;
import com.travelbooks3.android.util.LogUtil;

import org.json.JSONObject;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system777 on 2017-10-31.
 */

public class ChangePasswordFragment extends Fragment {

    View view;
    EditText now_password_edt;
    EditText new_password_edt;
    EditText new_password_confirm_edt;
    LinearLayout chage_button;

    String now_password;
    String new_password;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_chage_password, container, false);
        now_password_edt = (EditText)view.findViewById(R.id.now_password_edt);
        new_password_edt = (EditText)view.findViewById(R.id.new_password_edt);
        new_password_confirm_edt = (EditText)view.findViewById(R.id.new_password_confirm_edt);
        chage_button = (LinearLayout)view.findViewById(R.id.chagne_btn);

        chage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkPwd())
                {
                    callApi_password_change();
                }
            }
        });

        return view;
    }

    private void callApi_password_change(){
        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_MYPAGE_UPDATE_PWD);
        client.addParameter("pwd",now_password);
        client.addParameter("newPwd",new_password);
        LogUtil.d("now :"+now_password);
        LogUtil.d("new :"+new_password);

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                LogUtil.d("서버 연결 실패");
            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS_PASSWORD_UPDATE){
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Toast.makeText(getApplicationContext(),"비밀번호 변경 완료",Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment other = new AtvSetting();
                    fragmentTransaction.replace(R.id.main_base_lay, other);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();




                }else if (returnCode == GReturnCode.MISSING_REQUIRED_DATA) {
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Toast.makeText(getApplicationContext(),"필수 데이터 누락",Toast.LENGTH_SHORT).show();

                }else if (returnCode == GReturnCode.CONFIRM_NOW_PASSWORD) {
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Toast.makeText(getApplicationContext(),"현재 비밀번호가 맞지 않습니다",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    private boolean checkPwd() {
        new_password = new_password_edt.getText().toString();
        now_password = now_password_edt.getText().toString();

        if (FormatUtil.isNullorEmpty(new_password)) {
//                    new Alert().showAlert(AtvRegist.this, getString(R.string.txt_003));
            new_password_edt.requestFocus();
            new_password_edt.setError(getString(R.string.txt_003));
            return false;
        }

        if (FormatUtil.isContainsKorean(new_password)) {
            new_password_edt.requestFocus();
            new_password_edt.setError(getString(R.string.txt_102));
            return false;
        }

        if (!FormatUtil.isPasswordValidate(new_password)) {
            new_password_edt.requestFocus();
            new_password_edt.setError(getString(R.string.txt_200));
            return false;
        }
        if (!new_password.equals(new_password_confirm_edt.getText().toString()))
        {
            new_password_confirm_edt.requestFocus();
            new_password_confirm_edt.setError("비밀번호 확인이 틀립니다");
            return false;
        }
      return true;
    }
}
