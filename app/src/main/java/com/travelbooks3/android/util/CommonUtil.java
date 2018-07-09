package com.travelbooks3.android.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.travelbooks3.android.R;
import com.travelbooks3.android.common.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CommonUtil
{
    
    /**
     * 시스템에 설정된 언어
     * 
     * @param context
     * @return
     */
    public static String getLanguage(Context context)
    {
        Locale systemLocale = context.getResources().getConfiguration().locale;
        String language = systemLocale.getLanguage();
        
        return language;
    }
    
    
    /**
     * 시스템에 설정된 국가 정보
     * 
     * @param context
     * @return
     */
    public static String getCountry(Context context)
    {
        Locale systemLocale = context.getResources().getConfiguration().locale;
        String country = systemLocale.getCountry();
        
        return country;
    }
    
    
    public static void setLanguage(Context context)
    {
        String tag = SPUtil.getInstance().getUserLanguage(context);
        
        Locale en = Locale.US;
        
        if (tag.equals(Constants.LANG_KOR))
        {
            en = Locale.KOREAN;
        }
        else if (tag.equals(Constants.LANG_JPN))
        {
            en = Locale.JAPANESE;
        }
        else if (tag.equals(Constants.LANG_ENG))
        {
            en = Locale.US;
        }
        else if (tag.equals(Constants.LANG_CHG1))
        {
            en = Locale.CHINA;
        }
        else if (tag.equals(Constants.LANG_CHG2))
        {
            en = Locale.TAIWAN;
        }
        else if (tag.equals(Constants.LANG_ESP))
        {
            en = new Locale("es", "ES");
        }
        else if (tag.equals(Constants.LANG_RUS))
        {
            en = new Locale("ru", "RU");
        }
        else if (tag.equals(Constants.LANG_DEU))
        {
            en = Locale.GERMANY;
        }
        else if (tag.equals(Constants.LANG_FRN))
        {
            en = Locale.FRANCE;
        }
        
        LogUtil.d("tag : " + tag);
        LogUtil.d("en : " + en.toString());
        
        Configuration config = new Configuration();
        config.locale = en;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
    
    
    /**
     * 현재 버전을 가져온다(versionName)
     * 
     * @param context
     * @return
     */
    public static String getCurrentVersion(Context context)
    {
        try
        {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
            return "";
        }
    }
    
    
    /**
     * @name 디바이스고유키
     * @param context
     * @return
     */
    public static String getDeviceKey(Context context)
    {
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return getDeviceKey(context, telephonyMgr);
    }
    
    
    public static String getDeviceKey(Context context, TelephonyManager telephonyMgr)
    {
        
        String deviceId = telephonyMgr.getDeviceId();
        
        if (deviceId == null) deviceId = "";
        
        // String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        // if(androidId == null)
        // androidId = "";
        
        // String returnId = deviceId + "/" + androidId;
        
        return deviceId;
    }
    
    
    public static boolean isNetworkOn(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        
        if (mobile == null || wifi == null) return false;
        else if (mobile == null) return wifi.isConnected();
        else if (wifi == null) return mobile.isConnected();
        else return mobile.isConnected() || wifi.isConnected();
    }
    
    
    /**
     * 현재 디바이스의 전화번호를 가져온다.
     * 
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context)
    {
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return getPhoneNumber(context, telephonyMgr);
    }
    
    
    public static String getPhoneNumber(Context context, TelephonyManager telephonyMgr)
    {
        String phoneNumber = telephonyMgr.getLine1Number();
        if (!FormatUtil.isNullorEmpty(phoneNumber))
        {
            phoneNumber = phoneNumber.replace("+82", "0").replaceAll("-", "");
        }
        else phoneNumber = "";
        return phoneNumber;
    }
    
    
    public static String getTelecomCode(Context context)
    {
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return getTelecomCode(context, telephonyMgr);
    }
    
    
    public static String getTelecomCode(Context context, TelephonyManager telephonyMgr)
    {
        String telecomCode = telephonyMgr.getSimOperatorName();
        
        if (!FormatUtil.isNullorEmpty(telecomCode)) telecomCode = telecomCode.replaceAll(" ", "");
        else telecomCode = "";
        return telecomCode;
    }
    
    
    /**
     * 연락처에 저장된 핸드폰 번호를 수집
     * 
     * @param context
     * @return
     */
    public static ArrayList<String> getContactList(Context context)
    {
        ArrayList<String> list = new ArrayList<String>();
        
        String[] arrProjection =
        { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
        String[] arrPhoneProjection =
        { ContactsContract.CommonDataKinds.Phone.NUMBER };
        
        // get user list
        Cursor clsCursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, arrProjection, ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1", null, null);
        
        Cursor clsPhoneCursor = null;
        String strContactId = "";
        String number = "";
        
        while (clsCursor.moveToNext())
        {
            strContactId = clsCursor.getString(0);
            
            // phone number
            clsPhoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrPhoneProjection, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + strContactId, null, null);
            
            while (clsPhoneCursor.moveToNext())
            {
                number = clsPhoneCursor.getString(0).replace("-", "");
                
                list.add(number);
                break;
            }
            
            clsPhoneCursor.close();
        }
        clsCursor.close();
        
        return list;
    }
    
    
    /**
     * 연락처의 이름/번호 조회
     * 
     * @param context
     * @return
     */
    public static ArrayList<String[]> getContactNameList(Context context)
    {
        ArrayList<String[]> list = new ArrayList<String[]>();
        
        String[] arrProjection =
        { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
        String[] arrPhoneProjection =
        { ContactsContract.CommonDataKinds.Phone.NUMBER };
        
        // get user list
        Cursor clsCursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, arrProjection, ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1", null, null);
        
        Cursor clsPhoneCursor = null;
        String strContactId = "";
        
        String[] row = null;
        
        while (clsCursor.moveToNext())
        {
            row = new String[2];
            strContactId = clsCursor.getString(0);
            row[0] = clsCursor.getString(1);
            
            LogUtil.d("row[0] : " + row[0]);
            
            // phone number
            clsPhoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrPhoneProjection, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + strContactId, null, null);
            
            while (clsPhoneCursor.moveToNext())
            {
                row[1] = clsPhoneCursor.getString(0).replace("-", "");
                LogUtil.d("row[1] : " + row[1]);
                
                list.add(row);
                break;
            }
            
            clsPhoneCursor.close();
        }
        clsCursor.close();
        
        return list;
    }
    
    
    /**
     * 키패드 숨김
     * 
     * @param view
     *            해당 뷰
     * @param isHide
     *            true : 숨김, false : 보임
     */
    public static void hideKeyPad(View view, boolean isHide)
    {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isHide) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        else imm.showSoftInput(view, 0);
    }
    
    
    /**
     * 현재 이 어플이 활성중인지 나타냄
     * 
     * @param context
     * @param className
     * @return
     */
    public static RunningTaskInfo getAtv(Context context, ComponentName className)
    {
        return getAtv("DEVSWING_LOG", context, className);
    }
    
    
    /**
     * 현재 이 어플이 활성중인지 나타냄
     * 
     * @param tag
     * @param context
     * @param className
     * @return
     */
    public static RunningTaskInfo getAtv(String tag, Context context, ComponentName className)
    {
        RunningTaskInfo retVal = null;
        LogUtil.e("pkageName----->" + className.getPackageName());
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> info = activityManager.getRunningTasks(100);
        for (RunningTaskInfo runningTaskInfo : info)
        {
            String baseAct = runningTaskInfo.baseActivity.getClassName().toString();
            String topAct = runningTaskInfo.topActivity.getClassName().toString();
            LogUtil.e("topActivity----->" + topAct);
            LogUtil.e("baseActivity----->" + baseAct);
            
            if (baseAct.contains(className.getPackageName()))
            {
                retVal = runningTaskInfo;
                break;
            }
        }
        if (info != null)
        {
            info.clear();
        }
        if (activityManager != null)
        {
            activityManager = null;
        }
        return retVal;
    }
    
    
    public static boolean isPackageInstalled(Context ctx, String pkgName)
    {
        try
        {
            ctx.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    
    /**
     * 카카오톡 메세지 보내기
     */
    public static void sendKakao(Context context, String eventName)
    {
        try
        {
            eventName = eventName.replace("#", "");
            String inviteMsg = String.format("[%s]에서 귀하를 초대합니다.", eventName);
            String url = "http://2url.kr/X9H";
            
            String link = "kakaolink://sendurl?msg=" + inviteMsg + "&url=" + url + "&appid=com.devswing.quickguide&appver=1.0";
            
            Uri uri = Uri.parse(link);
            Intent intent = new Intent(Intent.ACTION_SEND, uri);
            
            context.startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(context, context.getString(R.string.txt_1004), Toast.LENGTH_SHORT).show();
        }
    }
    
    
    /**
     * 마이피플 메세지 보내기
     */
    public static void sendMyPeople(Context context, String eventName)
    {
        try
        {
            eventName = eventName.replace("#", "");
            String inviteMsg = String.format("[%s]에서 귀하를 초대합니다.", eventName);
            String url = "http://2url.kr/X9H";
            
            String link = "myp://sendMessage?message=" + inviteMsg + "&url=" + url + "&appid=com.devswing.appname=&appName=appname";
            
            Uri uri = Uri.parse(link);
            Intent intent = new Intent(Intent.ACTION_SEND, uri);
            
            context.startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(context, context.getString(R.string.txt_1005), Toast.LENGTH_SHORT).show();
        }
    }
    
    
    public static void shareMessage(Context context, String titleStr, String contentsStr)
    {
        String shareUrl = titleStr;
        shareUrl += "\n\n" + contentsStr;
        
        String message = shareUrl;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setData(Uri.parse(message));
        intent.setType("text/plain");
        context.startActivity(intent);
    }

    public static int[] getDisplaySize(Context context)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getMetrics(displayMetrics);

        int mDeviceSize[] = new int[2];
        mDeviceSize[0] = displayMetrics.widthPixels;
        mDeviceSize[1] = displayMetrics.heightPixels;

        return mDeviceSize;
    }
    
    public static int ConvertDpToPx(Context context, int dp)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    
    
    public static int ConvertPixelToDp(Context context, int pixels)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return ((int) (pixels / displayMetrics.density));
    }
    
    
    public static String findAddress(Context context, double lat, double lng)
    {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;
        String currentLocationAddress = "";
        try
        {
            if (geocoder != null)
            {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0)
                {
                    // 주소
                    currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    
                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress);
                    // .append("#");
                    // bf.append(lat).append("#");
                    // bf.append(lng);
                }
            }
            
        }
        catch (IOException e)
        {
        
        }
        return bf.toString();
    }
    
    
    public static String findAddressEng(Context context, double lat, double lng)
    {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        List<Address> address;
        String currentLocationAddress = "";
        try
        {
            if (geocoder != null)
            {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0)
                {
                    // 주소
                    currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    
                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress);
                    // .append("#");
                    // bf.append(lat).append("#");
                    // bf.append(lng);
                }
            }
            
        }
        catch (IOException e)
        {
        
        }
        return bf.toString();
    }


}
