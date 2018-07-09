package com.travelbooks3.android.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;


public class ImageUtil
{
    public static int[] getDisplaySize(Context context)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getMetrics(displayMetrics);

        int[] deviceSize = new int[2];
        deviceSize[0] = displayMetrics.widthPixels;
        deviceSize[1] = displayMetrics.heightPixels;

        return deviceSize;
    }


    public static float dipToPixel(Context context, float dip)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }


    public static float spToPixel(Context context, float sp)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }


    /**
     * 비트맵을 파일로 저장합니다.
     *
     * @param bitmap
     *            비트맵 이미지
     * @param strFilePath
     *            저장 경로
     */
    public synchronized static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, Bitmap.CompressFormat format)
    {
        LogUtil.e("SaveBitmapToFileCache (strFilePath) : " + strFilePath);

        File fileCacheItem = new File(strFilePath);
        FileOutputStream fos = null;

        try
        {
            fileCacheItem.createNewFile();
            fos = new FileOutputStream(fileCacheItem);

            bitmap.compress(format, 100, fos);
        }
        catch (Exception e)
        {
            if (fileCacheItem != null) fileCacheItem.delete();
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null) fos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    public static File toFile(String path)
    {
        File file = null;
        if (path != null && !"".equals(path)) file = new File(path);
        return file;
    }


    // public static File toFile(Context context, Uri uri)
    // {
    // return toFile(uriToMediaPath(context, uri));
    // }

    /**
     * 이미지 회전 각도를 알아옴.
     *
     * @param filepath
     * @return 각도
     */
    public synchronized static int getPhotoOrientationDegree(String filepath)
    {
        int degree = 0;
        ExifInterface exif = null;

        if (filepath == null)
        {
            return degree;
        }

        try
        {
            exif = new ExifInterface(filepath);
        }
        catch (IOException e)
        {
            LogUtil.e("Error: " + e.getMessage());
        }

        if (exif != null)
        {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            if (orientation != -1)
            {
                switch (orientation)
                {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        LogUtil.e("Photo Degree: " + degree);
        return degree;
    }


    /**
     * 이미지를 특정 각도로 회전
     *
     * @param bitmap
     * @param degrees
     * @return
     */
    public synchronized static Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws OutOfMemoryError
    {
        if (degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            if (bitmap != b2)
            {
                bitmap.recycle();
                bitmap = b2;
            }
        }

        return bitmap;
    }


    public synchronized static String getRotatedImage(String path, int degrees, String fileName)
    {
        if (path == null || path.trim().length() == 0 || path.trim().equals("")) return null;

        Bitmap bmp = null;
        boolean retval = false;
        try
        {
            if (path == null || path.equals(""))
            {
                retval = false;
            }
            else
            {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                bmp = BitmapFactory.decodeFile(path, options);
                retval = (options.outWidth > 720 || options.outHeight > 1280);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        finally
        {
            if (bmp != null)
            {
                bmp.recycle();
                bmp = null;
            }
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap originBitmap = null;
        Bitmap resultBitmap = null;
        int count = 0;

        if (retval)
        {
            count = 1;
        }

        boolean isSafe = false;

        while (originBitmap == null && !isSafe)
        {
            try
            {
                originBitmap = BitmapFactory.decodeFile(path, opts);
            }
            catch (OutOfMemoryError e)
            {
                LogUtil.e("Error(first): " + e.getMessage());

                count++;

                if (originBitmap != null) originBitmap.recycle();

                originBitmap = null;
            }
            catch (Exception e)
            {
                LogUtil.e("Error(first e): " + e.getMessage());
                isSafe = true;
            }
            finally
            {
                opts.inSampleSize = 1 * count;

            }
        }

        if (originBitmap != null) originBitmap.recycle();

        originBitmap = null;

        if (isSafe) return null;

        if (degrees != 0)
        {
            while (resultBitmap == null && count < 5)
            {
                try
                {
                    originBitmap = BitmapFactory.decodeFile(path, opts);

                    Matrix m = new Matrix();
                    m.postRotate(degrees, (float) originBitmap.getWidth() / 2, (float) originBitmap.getHeight() / 2);
                    resultBitmap = Bitmap.createBitmap(originBitmap, 0, 0, originBitmap.getWidth(), originBitmap.getHeight(), m, true);
                }
                catch (OutOfMemoryError e)
                {
                    LogUtil.e("Error: " + e.getMessage());

                    count++;

                    if (resultBitmap != null) resultBitmap.recycle();

                    if (originBitmap != null) originBitmap.recycle();

                    resultBitmap = null;
                    originBitmap = null;
                }
                finally
                {
                    if (resultBitmap == null)
                    {
                        opts.inSampleSize = 2 * count;
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }

        SaveBitmapToFileCache(resultBitmap, fileName);

        if (originBitmap != null) originBitmap.recycle();

        originBitmap = null;

        if (resultBitmap != null) resultBitmap.recycle();

        resultBitmap = null;

        return fileName;
    }


    public static boolean checkImage(String path)
    {
        return getImageSize(path) != null;
    }


    public static int[] getImageSize(String path)
    {
        int[] size = null;
        try
        {
            if (path == null || path.equals(""))
            {
                return null;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            if (options.outWidth <= 0 || options.outHeight <= 0) return null;

            size = new int[2];
            size[0] = options.outWidth;
            size[1] = options.outHeight;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return size;
    }


    /**
     * 실제 위치
     *
     * @param atv
     * @param uriPath
     * @return
     */
    public static String getRealImagePath(final Activity atv, Uri uriPath)
    {
        String[] proj =
                { MediaStore.Images.Media.DATA };
        Cursor cursor = atv.managedQuery(uriPath, proj, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        return path;
    }


    /**
     * 비트맵을 파일로 저장합니다.
     *
     * @param bitmap
     *            비트맵 이미지
     * @param strFilePath
     *            저장 경로
     */
    public static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath)
    {
        SaveBitmapToFileCache(bitmap, strFilePath, CompressFormat.JPEG);
    }


    /**
     * 뷰화면에 꽉찬이미지 (넘어갈경우 잘림)
     *
     * @param matrix
     * @param viewWidth
     * @param viewHeight
     * @param imgWidth
     * @param imgHeight
     * @return
     */
    public static Matrix computeMatrixFitX(Matrix matrix, float viewWidth, float viewHeight, float imgWidth, float imgHeight)
    {
        LogUtil.d("////computeMatrixFitX");
        LogUtil.d("////viewWidth  : " + viewWidth);
        LogUtil.d("////imgWidth   : " + imgWidth);
        LogUtil.d("////imgHeight  : " + imgHeight);

        if (matrix == null)
        {
            matrix = new Matrix();
        }
        matrix.reset();
        float scale = 0.0f;
        // float trans = 0.0f;
        // float vRate = viewHeight / viewWidth;
        // if(vRate > imgHeight / imgWidth)
        // {
        // 가로가 길 경우
        scale = viewWidth / imgWidth;
        matrix.setScale(scale, scale);
        // }
        // else
        // {
        // // 세로가 길 경우
        // scale = viewWidth / imgWidth;
        // trans = (float) (-Math.abs((viewHeight - (imgHeight * scale))) /
        // 2.0);
        // matrix.setScale(scale, scale);
        // matrix.postTranslate(0, trans);
        // }
        return matrix;
    }


    public static Matrix computeMatrixCenterCrop(Matrix matrix, float viewWidth, float viewHeight, float imageWidth, float imageHeight)
    {
        matrix = new Matrix();

        float xratio = viewWidth / imageWidth;
        float yratio = viewHeight / imageHeight;

        float maxScale = Math.max(xratio, yratio);

        float scaleWidth = imageWidth * maxScale;
        float scaleHeight = imageHeight * maxScale;

        matrix.postScale(maxScale, maxScale);
        matrix.postTranslate(viewWidth / 2 - scaleWidth / 2, viewHeight / 2 - scaleHeight / 2);
        return matrix;
    }


    public static Bitmap getMaskedBitmap(Context context, int _srcResId, float _roundInPixel)
    {
        Bitmap srcBitmap = BitmapFactory.decodeResource(context.getResources(), _srcResId);

        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), srcBitmap);

        roundedDrawable.setCornerRadius(_roundInPixel);
        roundedDrawable.setAntiAlias(true);

        return roundedDrawable.getBitmap();
    }
}
