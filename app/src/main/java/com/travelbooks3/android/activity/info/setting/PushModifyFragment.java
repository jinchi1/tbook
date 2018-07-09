package com.travelbooks3.android.activity.info.setting;

import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.travelbooks3.android.R;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.LogUtil;

import org.json.JSONObject;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;
import ra.genius.net.GHttpMethod;

/**
 * Created by system777 on 2017-12-08.
 */

public class PushModifyFragment extends Fragment {

    View view;
    TextView push_all_txt;
    TextView push_follow_txt;
    TextView push_no_txt;
    TextView done_txt;
    String push_type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_push_modify, container, false);
        push_all_txt = (TextView)view.findViewById(R.id.push_all_txt);
        push_follow_txt = (TextView)view.findViewById(R.id.push_follow_txt);
        push_no_txt = (TextView)view.findViewById(R.id.push_no_txt);
        done_txt = (TextView)view.findViewById(R.id.done_txt);


        push_all_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                push_type = "ALL";
                push_all_txt.setTextColor(Color.parseColor("#D92051"));
                push_follow_txt.setTextColor(Color.parseColor("#CCCCCC"));
                push_no_txt.setTextColor(Color.parseColor("#CCCCCC"));
            }
        });

        push_follow_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                push_type = "MY";

                push_all_txt.setTextColor(Color.parseColor("#CCCCCC"));
                push_follow_txt.setTextColor(Color.parseColor("#D92051"));
                push_no_txt.setTextColor(Color.parseColor("#CCCCCC"));            }
        });

        push_no_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                push_type = "OFF";

                push_all_txt.setTextColor(Color.parseColor("#CCCCCC"));
                push_follow_txt.setTextColor(Color.parseColor("#CCCCCC"));
                push_no_txt.setTextColor(Color.parseColor("#D92051"));
            }
        });

        done_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callApi_push_modify();
            }
        });

        callApi_push_info();

        return view;
    }

    private void callApi_push_info(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_MYPAGE_PUSH_INFO);

        client.setHttpMethod(GHttpMethod.Get);
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
                        JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                        JSONObject jsonObject = json.getJSONObject("user");
                        push_type = jsonObject.getString("push_type");
                        switch (push_type)
                        {
                            case "ALL":
                                push_all_txt.performClick();
                                break;
                            case "MY":
                                push_follow_txt.performClick();
                                break;
                            case "OFF":
                                push_no_txt.performClick();
                                break;
                            default:
                                break;
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

    private void callApi_push_modify(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_MYPAGE_PUSH_MODIFY);

        client.setHttpMethod(GHttpMethod.Post);
        client.addParameter("push_type",push_type);
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

                        Toast.makeText(getContext(),"푸시설정 변경 성공",Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment other = new AtvSetting();
                        fragmentTransaction.replace(R.id.main_base_lay, other);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } catch (Exception e) {
                        LogUtil.d("list err = "+e.toString());

                    }
                }else {

                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Toast.makeText(getContext(),"푸시설정 변경 실패",Toast.LENGTH_SHORT).show();


                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

}
