package com.travelbooks3.android.activity.info.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.travelbooks3.android.R;
import com.travelbooks3.android.TravelbookApp;
import com.travelbooks3.android.activity.BaseActivity;
import com.travelbooks3.android.activity.write.WriteActivity;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;
import ra.genius.net.GHttpMethod;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.travelbooks3.android.common.Constants.SERVER_URL;

public class AtvSetting extends Fragment {
    TextView logout;
    View view;
    TextView Change_password_Txt;
    TextView Clear_search_Txt;
    TextView Location_rule_Txt;
    TextView Privacy_rule_Txt;
    FragmentManager fragmentManager;
    TextView test_txt;
    TextView push_txt;
    TextView expire_txt;
    TextView like_txt;
    TextView block_txt;
    TextView privacy_txt;
    TextView connect_txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


     /*   setContentView(R.layout.activity_atv_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/

        view = inflater.inflate(R.layout.activity_atv_setting, container, false);
        test_txt = (TextView)view.findViewById(R.id.txtFacebook);
        test_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"서비스 준비중 입니다.",Toast.LENGTH_SHORT).show();
            }
        });
        Privacy_rule_Txt = (TextView)view.findViewById(R.id.txtPrivacyRule);
        Privacy_rule_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment other = new PrivacyRuleFragment();
                fragmentTransaction.replace(R.id.main_base_lay, other);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Location_rule_Txt = (TextView)view.findViewById(R.id.txtLocationRule);
        Location_rule_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment other = new LocationRuleFragment();
                fragmentTransaction.replace(R.id.main_base_lay, other);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        logout = (TextView)view.findViewById(R.id.Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TravelbookApp) getActivity().getApplicationContext()).logout(getActivity(), null);
            }
        });

        Change_password_Txt = (TextView)view.findViewById(R.id.txtPwUpdate);
        Change_password_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment other = new ChangePasswordFragment();
                fragmentTransaction.replace(R.id.main_base_lay, other);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        Clear_search_Txt = (TextView)view.findViewById(R.id.txtClearSearch);
        Clear_search_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callApi_clear_search();
            }
        });

        push_txt = (TextView)view.findViewById(R.id.txtPushNotice);
        push_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment other = new PushModifyFragment();
                fragmentTransaction.replace(R.id.main_base_lay, other);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        expire_txt = (TextView)view.findViewById(R.id.expire_txt);
        expire_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("계정의 모든 게시물과 저장 내역이 사라집니다. 그래도 탈퇴하시겠습니까?")
                        .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callApi_expire();
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alert_confirm.create();
                alertDialog.show();
            }
        });

        like_txt = (TextView)view.findViewById(R.id.txtLikePost);
        like_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"서비스 준비중입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        block_txt = (TextView)view.findViewById(R.id.txtBlockedUsers);
        block_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"서비스 준비중입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        privacy_txt = (TextView)view.findViewById(R.id.txtPrivacyAccount);
        privacy_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"서비스 준비중입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        connect_txt = (TextView)view.findViewById(R.id.txtConnectAccount);
        connect_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"서비스 준비중입니다.",Toast.LENGTH_SHORT).show();
            }
        });






        return view;
    }


    private void callApi_clear_search(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_MYPAGE_DELETE_SEARCH);

        client.setHttpMethod(GHttpMethod.Delete);
        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS){
                    try {

                        Toast.makeText(getContext(),"검색 내역 삭제 성공",Toast.LENGTH_SHORT).show();


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

    private void callApi_expire(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_MYPAGE_EXPIRE);

      client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS_MEMBER_LEAVE){
                    try {

                        Toast.makeText(getContext(),"회원 탈퇴가 완료 되었습니다.",Toast.LENGTH_SHORT).show();
                        ((TravelbookApp) getActivity().getApplicationContext()).logout(getActivity(), null);

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
}
