package com.travelbooks3.android.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.info.InfoFragment;
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

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.travelbooks3.android.common.Constants.SERVER_URL;

/**
 * Created by system777 on 2017-10-31.
 */

public class ModifyMainFragment extends Fragment {

    View view;
    EditText modify_comment;
    LinearLayout modify_button;
    String comment;
    String trip_uid;
    String command;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        trip_uid=getArguments().getString("trip_uid");
        comment=getArguments().getString("comment");
        command= getArguments().getString("command");
        if(command==null) command="null";

          view = inflater.inflate(R.layout.activity_feed_change, container, false);
        modify_comment = (EditText)view.findViewById(R.id.chage_comment_edt);/*
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
*/

        if(comment!=null)
        {
            modify_comment.setText(comment);
        }
        modify_button = (LinearLayout)view.findViewById(R.id.modify_btn);
        modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callApi_feed_change(trip_uid);
            }
        });
        return view;
    }

    private void callApi_feed_change(String trip_uid){

        comment = modify_comment.getText().toString();


        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();

        client.setUri(SERVER_URL + "/trip/tripinfo/"+trip_uid+"/update");
        client.addParameter("comment",comment);

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


                if(returnCode == GReturnCode.SUCCESS_POSTING_UPDATE){
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Toast.makeText(getApplicationContext(),"게시물 수정 완료",Toast.LENGTH_SHORT).show();
                    if(command.equals("main"))
                    {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment other = new MainFragment();
                        fragmentTransaction.replace(R.id.main_base_lay, other);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        InputMethodManager inputManager = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(modify_comment.getWindowToken(), 0);


                    }
                    else if(command.equals("info"))
                    {

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment other = new InfoFragment();
                        fragmentTransaction.replace(R.id.main_base_lay, other);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        InputMethodManager inputManager = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(modify_comment.getWindowToken(), 0);


                    }
                    else {
                        getActivity().finish();
                    }


                }else if (returnCode == GReturnCode.FEED_MODIFY_MISSING_REQUIRED_DATA) {
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Toast.makeText(getApplicationContext(),"필수 데이터 누락",Toast.LENGTH_SHORT).show();

                }
                else {
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

}
