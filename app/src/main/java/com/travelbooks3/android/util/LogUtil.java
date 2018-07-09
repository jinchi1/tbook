package com.travelbooks3.android.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.travelbooks3.android.BuildConfig;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;


public class LogUtil
{
    private static String  TAG    = "Travelbooks_Log";

    private static boolean ENABLE = BuildConfig.isLogin;

    private static final int STACK_TRACE_INDEX = 3;

    public static void Toast(String str)
    {
        if(ENABLE == false)
            return;
    }

    public static void d(){
        if(ENABLE == false)
            return;

        StackTraceElement ste = Thread.currentThread().getStackTrace()[STACK_TRACE_INDEX];
        String trace = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        Log.d("[" + trace + "]","Activity Name = " + ste.getClassName());
    }


    public static void d(String title, String str){
        if(ENABLE == false)
            return;

        StackTraceElement ste = Thread.currentThread().getStackTrace()[STACK_TRACE_INDEX];
        String trace = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        Log.d("[" + trace + "]", "["+title+"] "+str);
    }

    public static void d(String str){
        if(ENABLE== false)
            return;

        StackTraceElement ste = Thread.currentThread().getStackTrace()[STACK_TRACE_INDEX];
        String trace = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        Log.d("[" + trace + "]", str);
    }

    public static void e(String tag, String str)
    {
        if(ENABLE == false)
            return;

        StackTraceElement ste = Thread.currentThread().getStackTrace()[STACK_TRACE_INDEX];
        String trace = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        Log.e("[" + trace + "]", "["+tag+"] "+str);

    }


    public static void e(String tag, int msg)
    {
        if(ENABLE == false)
            return;

        StackTraceElement ste = Thread.currentThread().getStackTrace()[STACK_TRACE_INDEX];
        String trace = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        Log.e("[" + trace + "]", "["+tag+"] "+msg);
    }


    public static void e(String tag, boolean msg)
    {
        if(ENABLE == false)
            return;

        StackTraceElement ste = Thread.currentThread().getStackTrace()[STACK_TRACE_INDEX];
        String trace = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        Log.e("[" + trace + "]", "["+tag+"] "+msg);
    }


    public static void e(String msg)
    {
        if(ENABLE == false)
            return;

        StackTraceElement ste = Thread.currentThread().getStackTrace()[STACK_TRACE_INDEX];
        String trace = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        Log.e("[" + trace + "]", msg);
    }


    public static void e(int msg)
    {
        e("" + msg);
    }


    public static void e(boolean msg)
    {
        e("" + msg);
    }


    public static void d(String tag, int msg)
    {
        if (ENABLE) Log.d(tag, msg + "");
    }


    public static void d(String tag, boolean msg)
    {
        if (ENABLE) Log.d(tag, msg + "");
    }


    public static void i(String tag, String str)
    {
        if (ENABLE)
        {
            Log.i(tag, str);
        }
    }


    public static void i(String msg)
    {
        i(TAG, "" + msg);
    }


    public static void i(int msg)
    {
        i("" + msg);
    }


    public static void i(boolean msg)
    {
        i("" + msg);
    }


    public static void w(Exception e)
    {
        if (ENABLE) e.printStackTrace();
    }


    public static void errorTrace(String TAG, int logTypeID, Exception e)
    {
        e.printStackTrace();
    }


    /**
     * File 에 메세지 저장
     *
     * @param message
     */
    public static void write(String message)
    {
        if (!ENABLE) return;
        try
        {
            String fileMessage = FormatUtil.FormatDateGetString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS") + "\t" + message + "\n";

            File file = new File(Environment.getExternalStorageDirectory(), String.format("log_%s.txt", FormatUtil.FormatDateGetString(new Date(), "yyyyMMdd")));

            if (!file.exists()) file.createNewFile();

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(fileMessage);
            out.flush();
            out.close();
            e("파일쓰기 : " + message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {

        }
    }


    public static void jsonArray(JSONArray data)
    {
        jsonArray(TAG, data);
    }


    public static void jsonArray(String TAG, JSONArray data)
    {
        if (!ENABLE) return;
        d("=============================================================================================================================");
        if (data == null) d(TAG, "jsonArray is null");
        else d(TAG, data.toString());
        d("=============================================================================================================================");
    }


    public static void json(JSONObject data)
    {
        json(TAG, data);
    }


    public static void json(String TAG, JSONObject data)
    {
        if (!ENABLE) return;
        d("=============================================================================================================================");
        if (data == null) d(TAG, "json is null");
        else d(TAG, data.toString());
        d("=============================================================================================================================");
    }


    public static void intent(String TAG, Intent intent)
    {
        if (!ENABLE) return;

        i(TAG, "=============================================================================================================================");
        if (intent == null)
        {
            d(TAG, "intent is null");
            i(TAG, "=============================================================================================================================");
            return;
        }
        if (intent.getData() != null) d(TAG, "intent data : " + intent.getData().toString());
        try
        {
            d(TAG, "intent action : " + intent.getAction());
            Bundle extras = intent.getExtras();
            if (extras != null)
            {
                Set<String> ks = extras.keySet();
                Iterator<String> iterator = ks.iterator();
                while (iterator.hasNext())
                {
                    String key = iterator.next();
                    // Log.d(TAG, "KEY : " + key);

                    Object keyObj = extras.get(key);
                    if (keyObj instanceof String)
                    {
                        String keyString = (String) keyObj;
                        d(TAG, key + " : " + keyString);
                    }
                    else if (keyObj instanceof Integer)
                    {
                        Integer keyInt = (Integer) keyObj;
                        d(TAG, key + " : " + keyInt);
                    }
                    else if (keyObj instanceof Boolean)
                    {
                        Boolean keyBoolean = (Boolean) keyObj;
                        d(TAG, key + " : " + keyBoolean);
                    }
                    else
                    {
                        d(TAG, key + " : Unknown Type..");
                    }
                }
            }
        }
        catch (Exception e)
        {
            // e.printStackTrace();
            i(TAG, "=============================================================================================================================");
            return;
        }
        i(TAG, "=============================================================================================================================");
    }


    public static void intent(Intent intent)
    {
        intent(TAG, intent);
    }
}
