package com.rahul.media.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.lyft.android.scissors.CropView;
import com.msupport.MSupport;
import com.msupport.MSupportConstants;
import com.rahul.media.R;
import com.rahul.media.adapters.ImageListRecycleAdapter;
import com.rahul.media.adapters.ImagePreviewAdapter;
import com.rahul.media.model.CustomGallery;
import com.rahul.media.model.Define;
import com.rahul.media.utils.BitmapDecoder;
import com.rahul.media.utils.MediaUtility;
import com.rahul.media.utils.ViewPagerSwipeLess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import crop.Crop;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

/**
 * Class to pick image from camera
 * Created by rahul on 22/5/15.
 */
public class CameraPickActivity extends AppCompatActivity {
    private static final int ACTION_REQUEST_CAMERA = 201;
    private CropView mPager;
    private LinkedHashMap<String, CustomGallery> dataT;
    private ImagePreviewAdapter imagePreviewAdapter;
    private ImageListRecycleAdapter mImageListAdapter;
    private Uri userPhotoUri;
    private boolean isCrop, isSquareCrop;
    private int pickCount = 1;
    private AlertDialog alertDialog;
    private int aspectX, aspectY;
    RecyclerView mRecycleView;
    ImageView ratio;
    private static final float[] ASPECT_RATIOS = { 1f, 6f/4f, 16f/9f };
    private static final String[] ASPECT_LABELS = { "1:1", "6:4", "16:9" };
    private int selectedRatio = 0;
    int a;



    private void showAlertDialog(Context mContext, String text) {

        alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(text)
                .setCancelable(false).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        }).create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Define.ACTIONBAR_COLOR);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPager = (CropView) findViewById(R.id.pager);
        dataT = new LinkedHashMap<>();
        //imagePreviewAdapter = new ImagePreviewAdapter(CameraPickActivity.this, dataT);
        ratio = (ImageView)findViewById(R.id.ratio_fab);
        ratio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final float oldRatio = mPager.getImageRatio();
                selectedRatio = (selectedRatio + 1) % ASPECT_RATIOS.length;

                // Since the animation needs to interpolate to the native
                // ratio, we need to get that instead of using 0
                float newRatio = ASPECT_RATIOS[selectedRatio];
                if (Float.compare(0, newRatio) == 0) {
                    newRatio = mPager.getImageRatio();
                }

                ObjectAnimator viewportRatioAnimator = ObjectAnimator.ofFloat(mPager, "viewportRatio", oldRatio, newRatio)
                        .setDuration(420);

                viewportRatioAnimator.start();

            }
        });
        mImageListAdapter = new ImageListRecycleAdapter(this, dataT);
        mRecycleView = (RecyclerView) findViewById(R.id.image_hlistview);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecycleView.setAdapter(mImageListAdapter);
        mImageListAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPager.extensions().load(mImageListAdapter.mItems.get(position).sdcardPath);
                float ratio = 1f;
                ObjectAnimator viewportRatioAnimator = ObjectAnimator.ofFloat(mPager, "viewportRatio", mPager.getImageRatio(), ratio);
                //autoCancel(viewportRatioAnimator);
                //viewportRatioAnimator.addListener(animatorListener);
                viewportRatioAnimator.start();
                a=position;
            }
        });
        mRecycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE) mImageListAdapter.notifyDataSetChanged();

            }
        });

        try {
            aspectX = getIntent().getIntExtra("aspect_x", 1);
            aspectY = getIntent().getIntExtra("aspect_y", 1);
        } catch (Exception ignored) {

        }
        try {
            isCrop = getIntent().getExtras().getBoolean("crop");
            isSquareCrop = getIntent().getExtras().getBoolean("isSquareCrop");
        } catch (Exception e) {
            e.printStackTrace();
        }

        pickCount = getIntent().getIntExtra("pickCount", Define.MIN_MEDIA_COUNT);
        openCamera(false);
    }

    @Override
    public void onBackPressed() {
        Intent data2 = new Intent();
        setResult(RESULT_CANCELED, data2);
        super.onBackPressed();
    }

    private void openCamera(boolean isPermission) {
        String[] permissionSet = {MSupportConstants.WRITE_EXTERNAL_STORAGE, MSupportConstants.CAMERA};
        if (isPermission) {
            cameraIntent();
        } else {
            boolean isCameraPermissionGranted;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isCameraPermissionGranted = MSupport.checkMultiplePermission(CameraPickActivity.this, permissionSet, MSupportConstants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else
                isCameraPermissionGranted = true;
            if (isCameraPermissionGranted) {
                cameraIntent();
            }
        }
    }

    private void cameraIntent() {
        if (dataT.size() == pickCount) {
            showAlertDialog(CameraPickActivity.this, "You can select max " + pickCount + " images.");
        } else
            try {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    Uri imageFile = MediaUtility.createImageFile(CameraPickActivity.this);
                    userPhotoUri = FileProvider.getUriForFile(CameraPickActivity.this, Define.MEDIA_PROVIDER,
                            new File(imageFile.getPath()));

                    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        // only for gingerbread and newer versions
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, userPhotoUri);
                        userPhotoUri = imageFile;
                    } else {
                       /* Camera camera = Camera.open();
                        Camera.Parameters p;
                        p = camera.getParameters();

                        int width = 0,height =0;
                        int pwidth = 0,pheight =0;


                        for(int i =0; i<p.getSupportedPreviewSizes().size();i++) {
                            Log.d("size", String.valueOf(p.getSupportedPreviewSizes().get(i).width)+String.valueOf(p.getSupportedPreviewSizes().get(i).height));
                            if(p.getSupportedPreviewSizes().get(i).width == p.getSupportedPreviewSizes().get(i).height) {
                                width = p.getSupportedPreviewSizes().get(i).width;
                                height = p.getSupportedPreviewSizes().get(i).height;
                                Log.d("aaaaa",String.valueOf(width)+String.valueOf(height));
                            }
                        }

                        for(int j = 0; j<p.getSupportedPictureSizes().size();j++)
                        {
                            Log.d("picture", String.valueOf(p.getSupportedPictureSizes().get(j).width)+String.valueOf(p.getSupportedPictureSizes().get(j).height));
                            if(p.getSupportedPictureSizes().get(j).width == p.getSupportedPictureSizes().get(j).height) {
                                pwidth = p.getSupportedPictureSizes().get(j).width;
                                pheight = p.getSupportedPictureSizes().get(j).height;
                                Log.d("aaaaa",String.valueOf(pwidth)+String.valueOf(pheight));
                            }
                        }
                        Log.d("ccccc",String.valueOf(width)+String.valueOf(height)+String.valueOf(pwidth)+String.valueOf(pheight));

                        p.setPreviewSize(width,height);
                        p.setPictureSize(pwidth,pheight);
                        camera.setParameters(p);
                        camera.startPreview();
*/
                        userPhotoUri = imageFile;
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, userPhotoUri);
                    }

                    startActivityForResult(takePictureIntent, ACTION_REQUEST_CAMERA);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlertDialog(CameraPickActivity.this, "Device does not support camera.");
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MSupportConstants.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                ArrayList<String> deniedPermissionList = new ArrayList<>();
                boolean isAllPermissionGranted = true;
                for (int i = 0; i < grantResults.length; i++) {
                    int results = grantResults[i];
                    String permission = permissions[i];
                    if (results != PackageManager.PERMISSION_GRANTED) {
                        isAllPermissionGranted = false;
                        deniedPermissionList.add(MSupportConstants.getPermissionRationaleMessage(permission));
                    }
                }
                if (isAllPermissionGranted) {
                    openCamera(true);
                } else {
                    String message = "Requested Permission not granted";
                    if (!deniedPermissionList.isEmpty()) {
                        message = "You need to grant access to " + deniedPermissionList.get(0);
                        for (int i = 1; i < deniedPermissionList.size(); i++) {
                            message = message + ", " + deniedPermissionList.get(i);
                        }
                        message = message + " to access app features";
                    }
                    Toast.makeText(CameraPickActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private class ProcessImageView extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            CustomGallery item = new CustomGallery();

            item.sdcardPath = userPhotoUri.getPath();
            item.sdCardUri = userPhotoUri;

            item.sdcardPath = BitmapDecoder.getBitmap(userPhotoUri.getPath(), CameraPickActivity.this);
            item.sdCardUri = (Uri.parse(item.sdcardPath));

            dataT.put(item.sdcardPath, item);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //imagePreviewAdapter.customNotify(dataT);
            mImageListAdapter.customNotify(dataT);
            if(dataT.size()==1){
                ratio.setVisibility(View.VISIBLE);
            }
            mPager.extensions().load(mImageListAdapter.mItems.get(0).sdcardPath);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == ACTION_REQUEST_CAMERA) {

                if (pickCount == 1) {
                    dataT.clear();
                }
                if (userPhotoUri != null) {
                    new ProcessImageView().execute();
                }

            } else if (requestCode == Crop.REQUEST_CROP) {
         /*       final File croppedFile = new File(getCacheDir(), "crop_image"+a);



                rx.Observable<Void> onSave = rx.Observable.from(mPager.extensions()
                        .crop()
                        .quality(100)
                        .format(JPEG)
                        .into(croppedFile))
                        .subscribeOn(io())
                        .observeOn(mainThread());

                String imagePath = mImageListAdapter.mItems.get(a).sdcardPath;
                final CustomGallery itema = new CustomGallery();
                itema.sdcardPath = croppedFile.getPath();


                //dataT.remove(imagePath);
                dataT.put(imagePath, itema);
          *//*  mImageListAdapter.customNotify(dataT);
            mImageListAdapter.notifyDataSetChanged();
*//*
                //mPager.getImageBitmap().recycle();
                //mPager = new CropView(this);

                android.os.Handler b = new android.os.Handler();
                b.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mImageListAdapter.customNotify(dataT);
                        mImageListAdapter.notifyDataSetChanged();


                        mPager.extensions().load(mImageListAdapter.mItems.get(a).sdcardPath);

                    }
                },500);
*/
                //mPager.extensions().load(itema.sdcardPath);
            }
        } else {
            if (dataT == null || dataT.size() == 0) {
                Intent data2 = new Intent();
                setResult(RESULT_CANCELED, data2);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        menu.findItem(R.id.action_crop).setVisible(isCrop);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_ok) {
            if (dataT.size() == 1){
                final File croppedFile = new File(getCacheDir(), mImageListAdapter.mItems.get(a).sdcardPath + "croped");


            rx.Observable<Void> onSave = rx.Observable.from(mPager.extensions()
                    .crop()
                    .quality(100)
                    .format(JPEG)
                    .into(croppedFile))
                    .subscribeOn(io())
                    .observeOn(mainThread());

            String imagePath = mImageListAdapter.mItems.get(a).sdcardPath;
            final CustomGallery itema = new CustomGallery();
            itema.sdcardPath = croppedFile.getPath();


            //dataT.remove(imagePath);
            dataT.put(imagePath, itema);
          /*  mImageListAdapter.customNotify(dataT);
            mImageListAdapter.notifyDataSetChanged();
*/
            //mPager.getImageBitmap().recycle();
            //mPager = new CropView(this);

            android.os.Handler b = new android.os.Handler();

        }
            ////////////////////////////////////////////////////
            ArrayList<CustomGallery> mArrayList = new ArrayList<>(dataT.values());
            if (mArrayList.size() > 0) {
                ArrayList<String> allPath = new ArrayList<>();
                for (int i = 0; i < mArrayList.size(); i++) {
                    allPath.add(mArrayList.get(i).sdcardPath);
                }

                Intent data = new Intent().putStringArrayListExtra(Define.INTENT_PATH, allPath);
                setResult(RESULT_OK, data);
                finish();
            } else {
                showAlertDialog(CameraPickActivity.this, "Please select an image.");
            }
        } else if (id == android.R.id.home) {
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
        } else if (id == R.id.action_camera) {
            openCamera(false);
            ratio.setVisibility(View.GONE);
        } else if (id == R.id.action_crop) {

            if (dataT.size() == 1) {
                Toast.makeText(getApplicationContext(),"1장의 사진일경우 자동으로 crop됩니다",Toast.LENGTH_SHORT).show();
            } else {
                final File croppedFile = new File(getCacheDir(), a+"camera"+"jpeg");


                rx.Observable<Void> onSave = rx.Observable.from(mPager.extensions()
                        .crop()
                        .quality(100)
                        .format(JPEG)
                        .into(croppedFile))
                        .subscribeOn(io())
                        .observeOn(mainThread());

                String imagePath = mImageListAdapter.mItems.get(a).sdcardPath;
                final CustomGallery itema = new CustomGallery();
                itema.sdcardPath = croppedFile.getPath();


                //dataT.remove(imagePath);
                dataT.put(imagePath, itema);
          /*  mImageListAdapter.customNotify(dataT);
            mImageListAdapter.notifyDataSetChanged();
*/
                //mPager.getImageBitmap().recycle();
                //mPager = new CropView(this);

                android.os.Handler b = new android.os.Handler();
                b.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mImageListAdapter.customNotify(dataT);
                        mImageListAdapter.notifyDataSetChanged();


                        mPager.extensions().load(mImageListAdapter.mItems.get(a).sdcardPath);

                    }
                }, 500);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
