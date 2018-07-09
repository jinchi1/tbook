package com.travelbooks3.android.activity.write;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.rahul.media.main.MediaFactory;
import com.rahul.media.model.CustomGallery;
import com.rahul.media.model.Define;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.info.setting.WriteTestActivity;
import com.travelbooks3.android.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;



public class SelectPhotoActivity extends Activity {



    private MediaFactory mediaFactory;
    private static final int REQUEST_MICROPHONE = 3;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_CAMERA = 1;

    private GestureDetector gestureScanner;
    MediaFactory.MediaBuilder mediaBuilder;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureScanner.onTouchEvent(event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Define.MEDIA_PROVIDER = getString(R.string.image_provider);
        Define.ACTIONBAR_COLOR = getResources().getColor(R.color.colorPrimary);

        mediaBuilder = null;

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(SelectPhotoActivity.this);
        alert_confirm.setMessage("어떤방식으로 사진을 가져오시겠습니까?")
                .setPositiveButton("카메라",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaBuilder = new MediaFactory.MediaBuilder(SelectPhotoActivity.this)
                                .setPickCount(10)
                                .doCropping()
                                .isSquareCrop(true)
                                .fromCamera();

                        mediaFactory = MediaFactory.create().start(mediaBuilder);

                    }
                }).setNegativeButton("앨범",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mediaBuilder = new MediaFactory.MediaBuilder(SelectPhotoActivity.this)
                        .setPickCount(10)
                        .doCropping()
                        .isSquareCrop(true)
                        .fromGallery();

                mediaFactory = MediaFactory.create().start(mediaBuilder);
            }
        });

        AlertDialog alertDialog = alert_confirm.create();
        alertDialog.show();
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
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
        Intent goWrite = new Intent(this,WriteTestActivity.class);
        goWrite.putStringArrayListExtra("path",pathArrayList);
        goWrite.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(goWrite);
        finish();



    }
/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }*/
}