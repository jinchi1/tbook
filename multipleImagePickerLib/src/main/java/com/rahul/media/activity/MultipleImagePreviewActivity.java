package com.rahul.media.activity;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lyft.android.scissors.CropView;
import com.rahul.media.R;
import com.rahul.media.adapters.ImageListRecycleAdapter;
import com.rahul.media.imagemodule.ImageAlbumListActivity;
import com.rahul.media.model.CustomGallery;
import com.rahul.media.model.Define;
import com.rahul.media.utils.BitmapDecoder;
import com.rahul.media.utils.MediaUtility;
import com.rahul.media.utils.SquareImageView;
import com.rahul.media.utils.ViewPagerSwipeLess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.logging.Handler;

import crop.Crop;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static android.graphics.Bitmap.CompressFormat.PNG;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

/**
 * Created by rahul on 22/5/15.
 */
public class MultipleImagePreviewActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 200;
    private AlertDialog alertDialog;
    private CropView mPager;
    private LinkedHashMap<String, CustomGallery> dataT;
    //private CustomPagerAdapter adapter;
    private ImageListRecycleAdapter mImageListAdapter;
    private boolean isCrop;
    //private boolean isSquareCrop;
    private int pickCount = Define.MIN_MEDIA_COUNT;
    //private int aspectX, aspectY;
    RecyclerView mRecycleView;
    ImageView ratio;
    private static final float[] ASPECT_RATIOS = { 1f, 6f/4f, 16f/9f };
    private static final String[] ASPECT_LABELS = { "1:1", "6:4", "16:9" };
    private int selectedRatio = 0;
    int a = 0;

    public void showAlertDialog(Context mContext, String text) {

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
    public void onBackPressed() {
        Intent data2 = new Intent();
        setResult(RESULT_CANCELED, data2);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiimage_preview);
        mPager = (CropView) findViewById(R.id.pager);
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





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Define.ACTIONBAR_COLOR);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dataT = new LinkedHashMap<>();

       /* adapter = new CustomPagerAdapter(dataT);
        adapter.notifyDataSetChanged();*/
    /*    mPager.setCurrentItem(0);
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(0);
*/

        /*mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        mImageListAdapter = new ImageListRecycleAdapter(this, dataT);
        mRecycleView = (RecyclerView)findViewById(R.id.image_hlistview);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecycleView.setAdapter(mImageListAdapter);

        //mPager.extensions().load(mImageListAdapter.mItems.get(0).sdcardPath);

        mImageListAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                mPager.extensions().load(mImageListAdapter.mItems.get(position).sdcardPath);
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
            isCrop = getIntent().getExtras().getBoolean("crop");
            //isSquareCrop = getIntent().getExtras().getBoolean("isSquareCrop");
        } catch (Exception e) {
            e.printStackTrace();
        }

        pickCount = getIntent().getIntExtra("pickCount", Define.MIN_MEDIA_COUNT);
        openGallery();

        //mPager.extensions().load(mImageListAdapter.mItems.get(0).sdcardPath);

    }

    private void openGallery() {
        if (dataT.size() == pickCount) {
            showAlertDialog(MultipleImagePreviewActivity.this, "You can select max " + pickCount + " images.");
        } else {
            Intent i = new Intent(this, ImageAlbumListActivity.class);
            i.putExtra("pickCount", pickCount - dataT.size());
            startActivityForResult(i, PICK_IMAGE);
        }
    }

    private class ProcessAllImages extends AsyncTask<Void, Void, Void> {

        private ArrayList<String> stringArrayList;

        ProcessAllImages(ArrayList<String> stringArrayList) {
            this.stringArrayList = stringArrayList;
        }

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MultipleImagePreviewActivity.this);
            mProgressDialog.setMessage("Processing images ...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < stringArrayList.size(); i++) {
                CustomGallery item = new CustomGallery();

                item.sdcardPath = stringArrayList.get(i);
                //item.sdCardUri = Uri.parse(stringArrayList.get(i));

                item.sdcardPath = BitmapDecoder.getBitmap(stringArrayList.get(i), MultipleImagePreviewActivity.this);
                //item.sdCardUri = (Uri.parse(item.sdcardPath));

                dataT.put(item.sdcardPath, item);
                Log.i("papa",item.sdcardPath);


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            //adapter.customNotify(dataT);
            mImageListAdapter.customNotify(dataT);
            mPager.extensions().load(stringArrayList.get(0));
            if(stringArrayList.size()==1){
                ratio.setVisibility(View.VISIBLE);
            }
            //matrix=mPager.getMatrix();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_IMAGE) {
                ArrayList<String> allPath = data.getStringArrayListExtra(Define.INTENT_PATH);

                if (allPath != null && !allPath.isEmpty()) {
                    if (pickCount == 1) {
                        dataT.clear();
                        new ProcessAllImages(allPath).execute();
                    } else {
                        new ProcessAllImages(allPath).execute();
                    }

                }
            } else if (requestCode == Crop.REQUEST_CROP) {
/*
                final File croppedFile = new File(getCacheDir(), mImageListAdapter.mItems.get(a).sdcardPath);

                rx.Observable<Void> onSave = rx.Observable.from(mPager.extensions()
                        .crop()
                        .quality(100)
                        .format(JPEG)
                        .into(croppedFile))
                        .subscribeOn(io())
                        .observeOn(mainThread());

                String imagePath = mImageListAdapter.mItems.get(a).sdcardPath;
                CustomGallery item = new CustomGallery();
                item.sdcardPath = croppedFile.getPath();


                //dataT.remove(imagePath);
                dataT.put(imagePath, item);
                mImageListAdapter.customNotify(dataT);
                mImageListAdapter.notifyDataSetChanged();
                mRecycleView.setAdapter(mImageListAdapter);
                mPager.extensions().load(mImageListAdapter.mItems.get(a).sdcardPath);*/
                /*   try {
                    Uri mTargetImageUri = (Uri) data.getExtras().get(MediaStore.EXTRA_OUTPUT);
                    if (mTargetImageUri != null) {
                        String imagePath = mImageListAdapter.mItems.get(mPager.getCurrentItem()).sdcardPath;
                        CustomGallery item = new CustomGallery();
                        item.sdcardPath = mTargetImageUri.getPath();
                        item.sdCardUri = mTargetImageUri;

                        //dataT.remove(imagePath);
                        //dataT.put(mTargetImageUri.getPath(), item);
                        dataT.put(imagePath,item);
                        adapter.customNotify(dataT);
                        mImageListAdapter.customNotify(dataT);
                    }
                } catch (Exception e) {
                    String invalidImageText = (String) data.getExtras().get("invalid_image");
                    if (invalidImageText != null)
                        showAlertDialog(MultipleImagePreviewActivity.this, invalidImageText);
                }*/
            }
        } else {
            if (dataT != null && dataT.size() > 0) {
            } else {
                Intent data2 = new Intent();
                setResult(RESULT_CANCELED, data2);
                finish();
            }
        }
    }

    /*private class CustomPagerAdapter extends PagerAdapter {

        LayoutInflater mLayoutInflater;
        ArrayList<CustomGallery> dataT;

        CustomPagerAdapter(LinkedHashMap<String, CustomGallery> dataT) {
            this.dataT = new ArrayList<CustomGallery>(dataT.values());
            mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        void customNotify(LinkedHashMap<String, CustomGallery> dataHashmap) {
            dataT.clear();
            ArrayList<CustomGallery> dataT2 = new ArrayList<CustomGallery>(dataHashmap.values());
            this.dataT.addAll(dataT2);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return dataT.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.image_pager_item, container, false);

            final SquareImageView imageView = (SquareImageView) itemView.findViewById(R.id.full_screen_image);
            Glide.with(imageView.getContext())
                    .load("file://" + dataT.get(position).sdcardPath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.override(400,400)
                    .thumbnail(0.1f)
                    .into(imageView);

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }*/

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

            /*if(dataT.size() ==1) {
                final File croppedFile = new File(getCacheDir(), "jpeg");


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


            }*/

            ///////////////////////////////////////////////////////////
            ArrayList<CustomGallery> mArrayList = new ArrayList<>(dataT.values());
            if (mArrayList.size() > 0) {
                ArrayList<String> allPath = new ArrayList<>();
                for (int i = 0; i < mArrayList.size(); i++) {
                    allPath.add(mArrayList.get(i).sdcardPath);
                    Log.i("path",mArrayList.get(i).sdcardPath);
                }

                Intent data = new Intent().putStringArrayListExtra(Define.INTENT_PATH, allPath);
                setResult(RESULT_OK, data);
                finish();
            }
        } else if (id == android.R.id.home) {
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
        } else if (id == R.id.action_camera) {
            dataT.clear();
            ratio.setVisibility(View.GONE);
            openGallery();
        } else if (id == R.id.action_crop) {

            if (dataT.size() == 1) {
                Toast.makeText(getApplicationContext(), "1장의 사진일경우 자동으로 crop됩니다", Toast.LENGTH_SHORT).show();
            }
            else{
                final File croppedFile = new File(getCacheDir(), a+"image"+"jpeg");
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
