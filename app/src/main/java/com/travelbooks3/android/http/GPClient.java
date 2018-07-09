package com.travelbooks3.android.http;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.travelbooks3.android.util.CommonUtil;
import com.travelbooks3.android.util.FormatUtil;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.SPUtil;

import java.io.File;

import ra.genius.net.GBean;
import ra.genius.net.GHttpMethod;
import ra.genius.net.http.GHttpClient;
import ra.genius.net.http.handler.IHttpHandler;


public class GPClient extends GHttpClient implements OnCancelListener
{
    private Context mContext;
    private Dialog  mDialog   = null;
    private String  mTag      = "";
    private String  mUrl      = "";
    
    private boolean mIsCancel = false;
    
    
    public GPClient(Context context)
    {
        super();
        mContext = context;
    }
    
    
    public void setTag(String tag)
    {
        mTag = tag;
        LogUtil.e("GPClient -> set    : " + mTag);
    }
    
    
    @Override
    public GHttpClient setUri(String uri)
    {
        mUrl = uri + "?";
        setDefault();
        return super.setUri(uri);
    }
    
    
    public GHttpClient setUri(String uri, boolean isDef)
    {
        mUrl = uri + "?";
        if (isDef)
        {
            setDefault();
        }
        return super.setUri(uri);
    }
    
    
    public void addProgress()
    {
        addProgress(this);
    }
    
    
    public void setDefault()
    {
        String auth_token = SPUtil.getInstance().getAuthToken(mContext);
        
        if (!FormatUtil.isNullorEmpty(auth_token)) addHeader("auth_token", auth_token);
        
        addHeader("language", CommonUtil.getLanguage(mContext));
        
        if (super.mGHttpMethod == GHttpMethod.Post || super.mGHttpMethod == GHttpMethod.Get)
        {
            addParameter("appVersion", CommonUtil.getCurrentVersion(mContext));
            addParameter("os", "A");
            
            addParameter("push_token", SPUtil.getInstance().getRegistPushKey(mContext));
        }
        
        addCookies(SPUtil.getInstance().getCookie(mContext));
        
        addHandler(new IHttpHandler()
        {
            @Override
            public GBean handle(GBean bean)
            {
                if (mIsCancel) return null;
                
                return bean;
            }
        });
    }
    
    
    /**
     * needCancel이 false 일때만 사용
     * 
     * @param //needCancel
     *            통신취소여부
     */
    public void addProgress(OnCancelListener listener)
    {
//        if(listener != null){
//            LogUtil.d("OnCancelListener is not null!");
//            mDialog = ProgressDialogUtil.show(mContext, mDialog);
//            mIsCancel = false;
//
//            if(mDialog != null) mDialog.setOnCancelListener(listener);
//        }else{
//            LogUtil.d("OnCancelListener is null!");
//        }
    }
    
    
    public Dialog getProgress()
    {
        return mDialog;
    }
    
    
    public void removeProgress()
    {
//        ProgressDialogUtil.dismiss(mDialog);
    }
    
    
    @Override
    public void onCancel(DialogInterface dialog)
    {
        // Alert.toastShort(mContext, "사용자에 의해 취소 되었습니다.");
        mIsCancel = true;
        this.cancel();
    }
    
    
    public GHttpClient addCmd(String cmd)
    {
        return addParameter("cmd", cmd);
    }
    
    
    @Override
    public GHttpClient addParameter(String name, Object value)
    {
        if (value instanceof Integer)
        {
            mUrl += name + "=" + (Integer) value + "&";
            LogUtil.d("GPClient " + name + "=" + (Integer) value + "   <-파라미터 추가 Integer");
        }
        else if (value instanceof Boolean)
        {
            mUrl += name + "=" + (Boolean) value + "&";
            LogUtil.d("GPClient " + name + "=" + (Boolean) value + "   <-파라미터 추가 Boolean");
        }
        else if (value instanceof String)
        {
            mUrl += name + "=" + (String) value + "&";
            LogUtil.d("GPClient " + name + "=" + (String) value + "   <-파라미터 추가 String");
        }
        else if (value instanceof Double)
        {
            mUrl += name + "=" + (Double) value + "&";
            LogUtil.d("GPClient " + name + "=" + (Double) value + "   <-파라미터 추가 Double");
        }
        else if (value instanceof Long)
        {
            mUrl += name + "=" + (Long) value + "&";
            LogUtil.d("GPClient " + name + "=" + (Long) value + "   <-파라미터 추가 Long");
        }
        else if (value instanceof Float)
        {
            mUrl += name + "=" + (Float) value + "&";
            LogUtil.d("GPClient " + name + "=" + (Float) value + "   <-파라미터 추가 Float");
        }
        else if (value instanceof File)
        {
            File file = (File) value;
            if (file.isFile())
            {
                mUrl += name + "=" + "파일추가입니다." + "&";
                LogUtil.d("GPClient " + name + "=" + "파일추가입니다.   <-파라미터 추가 File");
            }
            else
            {
                mUrl += name + "=" + "파일없습니다." + "&";
                LogUtil.d("GPClient " + name + "=" + "파일없습니다.   <-파라미터 추가 File");
            }
            
        }
        return super.addParameter(name, value);
    }
    
    
    @Override
    public void cancel()
    {
//        ProgressDialogUtil.dismiss(mDialog);
        super.cancel();
    }
    
    
    @Override
    public void execute()
    {
        LogUtil.e("GPClient -> excute 시작 : " + mTag);
        LogUtil.e("GPClient ============================================================================================================================");
        if (!FormatUtil.isNullorEmpty(mUrl))
        {
            LogUtil.e("GPClient -> " + mUrl.substring(0, mUrl.length() - 1));
        }
        LogUtil.e("GPClient ============================================================================================================================");
        super.execute();
        LogUtil.e("GPClient -> excute 완료 : " + mTag);
//        ProgressDialogUtil.dismiss(mDialog);
        
    }
    
}