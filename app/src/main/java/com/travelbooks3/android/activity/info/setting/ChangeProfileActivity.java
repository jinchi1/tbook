package com.travelbooks3.android.activity.info.setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rahul.media.main.MediaFactory;
import com.rahul.media.model.Define;
import com.travelbooks3.android.R;
import com.travelbooks3.android.TravelbookApp;
import com.travelbooks3.android.activity.BaseActivity;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.ImageUtil;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;
import ra.genius.net.GHttpMethod;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system777 on 2017-10-10.
 */

public class ChangeProfileActivity extends Activity {

    ImageView profile_photo;
    TextView profile_modify;
    EditText edit_name;
    EditText edit_introduce;
    EditText edit_email;
    String profile_photo_path;
    String profile_name;
    String profile_change_name;
    String profile_introduce;
    String profile_email;
    String profile_change_email;
    LinearLayout change_profile_lay;



    private MediaFactory mediaFactory;
    MediaFactory.MediaBuilder mediaBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chage_profile);

        Define.MEDIA_PROVIDER = getString(R.string.image_provider);
        Define.ACTIONBAR_COLOR = getResources().getColor(R.color.colorPrimary);

        change_profile_lay = (LinearLayout)findViewById(R.id.change_profile_lay);
        profile_photo = (ImageView)findViewById(R.id.profile_photo);
        edit_name = (EditText)findViewById(R.id.profile_name);
        edit_introduce = (EditText)findViewById(R.id.profile_introduce);
        edit_email = (EditText)findViewById(R.id.profile_email);
        profile_modify = (TextView)findViewById(R.id.txtView_modify);



        Intent change = getIntent();
        profile_photo_path = change.getStringExtra("profile_photo");
        profile_name = change.getStringExtra("name");
        profile_email = change.getStringExtra("email");
        profile_introduce = change.getStringExtra("introduce");

        if(profile_name!=null) edit_name.setText(profile_name);
        if(profile_introduce!=null) edit_introduce.setText(profile_introduce);
        if(profile_email!=null) edit_email.setText(profile_email);



        change_profile_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
                inputManager.hideSoftInputFromWindow(edit_email.getWindowToken(), 0);
                inputManager.hideSoftInputFromWindow(edit_introduce.getWindowToken(), 0);

            }
        });

        if (!TextUtils.isEmpty(profile_photo_path)) {
            Glide.with(getApplicationContext())
                    .load(Constants.SERVER_IMG_URL + profile_photo_path)
                    .placeholder(R.drawable.written_face)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(profile_photo);
//            }
        } else {
            Glide.with(getApplicationContext())
                    .load(Constants.SERVER_IMG_URL + profile_photo_path)
                    .placeholder(R.drawable.written_face)
                    .error(R.drawable.written_face)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(profile_photo);
        }

        profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"aaaaaaaa",Toast.LENGTH_SHORT).show();
                final CharSequence[] items = { "사진 촬영", "앨범에서 사진 선택", "기본 이미지로 변경" };
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChangeProfileActivity.this);

                // 제목셋팅
                alertDialogBuilder.setTitle("프로필");
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                switch (id)
                                {
                                    case 0:
                                        mediaBuilder = new MediaFactory.MediaBuilder(ChangeProfileActivity.this)
                                                .setPickCount(1)
                                                //.doCropping()
                                                //.isSquareCrop(true)
                                                .fromCamera();
                                        mediaFactory = MediaFactory.create().start(mediaBuilder);
                                        break;
                                    case 1:
                                        mediaBuilder = new MediaFactory.MediaBuilder(ChangeProfileActivity.this)
                                                .setPickCount(1)
                                                //.doCropping()
                                                //.isSquareCrop(true)
                                                .fromGallery();
                                        mediaFactory = MediaFactory.create().start(mediaBuilder);
                                        break;
                                    case 2:
                                        callApi_profile_photo_delete();
                                        break;
                                }

                                dialog.dismiss();
                            }
                        });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();


            }
        });

        profile_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_name.getText().toString()!=null) profile_change_name = edit_name.getText().toString();
                if(edit_introduce.getText().toString()!=null) profile_introduce = edit_introduce.getText().toString();
                if(edit_email.getText().toString()!=null) profile_change_email = edit_email.getText().toString();

                               callApi_profile_change();


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<String> pathArrayList = mediaFactory.onActivityResult(requestCode, resultCode, data);
        for (int i = 0; i < pathArrayList.size(); i++) {
            Log.i("get :",pathArrayList.get(i));
        }

        if (!pathArrayList.isEmpty()) {
            callApi_profile_photo_upload(pathArrayList);
        }


        /*Intent goWrite = new Intent(this,ChangeProfileActivity.class);
        goWrite.putStringArrayListExtra("path",pathArrayList);
        goWrite.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(goWrite);
        finish();
*/


    }


    private void callApi_profile_photo_upload(final ArrayList<String> pathArrayList){
        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_MYPAGE_UPDATE_PHOTO);



        //if(pathArrayList.isEmpty())
        if(pathArrayList.get(0)!=null) {
            client.addParameter("file01", ImageUtil.toFile(pathArrayList.get(0).toString()));
            LogUtil.d("aaaaaaaaaaaaaaaaaaaaa"+pathArrayList.get(0));
            LogUtil.d("bbbbbbbbbbbbbbbbbbbbbb"+pathArrayList.get(0).toString());
        }
        LogUtil.d("aaaaaaaa"+pathArrayList.get(0));

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                LogUtil.d("사진 전송실패");
            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS_PROFILE_PHOTO_UPDATE){
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Glide.with(getApplicationContext())
                            .load(pathArrayList.get(0))
                            .placeholder(R.drawable.written_face)
                            .centerCrop()
                            .thumbnail(0.1f)
                            .into(profile_photo);
                }else {
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    private void callApi_profile_photo_delete(){
        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_MYPAGE_DELETE_PROFILE_PHOTO);
        client.setHttpMethod(GHttpMethod.Delete);
        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                LogUtil.d("사진 삭제실패");
            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS_PROFILE_PHOTO_DELETE){
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                     Glide.with(getApplicationContext())
                            .load(R.drawable.written_face)
                            .placeholder(R.drawable.written_face)
                            .centerCrop()
                            .thumbnail(0.1f)
                            .into(profile_photo);


                }else {
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }


    private void callApi_profile_change(){
        GPClient client = new GPClient(getApplicationContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_MYPAGE_USER_INFO);

        if(profile_change_name!=null&&!profile_change_name.equals(profile_name))
        {
            LogUtil.d("xxx"+profile_change_name);
            client.addParameter("name",profile_change_name);
        }
        else {}
        if(profile_email!=null&&!profile_change_email.equals(profile_email))client.addParameter("email",profile_change_email);
        if(profile_introduce!=null)client.addParameter("introduce",profile_introduce);


        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {
                LogUtil.d("프로필 수정 실패");
            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS_PROFILE_UPDATE){
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Toast.makeText(getApplicationContext(),"프로필수정성공",Toast.LENGTH_SHORT).show();
                    ((TravelbookApp)getApplicationContext()).setResumeValue(5);
                    finish();


                }else if(returnCode == 401){
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Toast.makeText(getApplicationContext(),"필수 데이터가 누락 되었습니다",Toast.LENGTH_SHORT).show();
                }else if(returnCode == 407){
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Toast.makeText(getApplicationContext(),"사용이 불가능한 닉네임입니다.",Toast.LENGTH_SHORT).show();
                }else if(returnCode == 406){
                    LogUtil.d("returnCode : " + returnCode);
                    LogUtil.d("returnMessage : " + returnMessage);
                    Toast.makeText(getApplicationContext(),"사용이 불가능한 이메일입니다.",Toast.LENGTH_SHORT).show();
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(ChangeProfileActivity.this, BaseActivity.class);
        intent.putExtra("move","info");
        startActivity(intent);
        finish();
    }
}
