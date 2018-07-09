package com.travelbooks3.android.http;

import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.json.JSONObject;

import ra.genius.net.GBean;
import ra.genius.net.http.codec.IHttpDecoder;


public class GJSONDecoder implements IHttpDecoder
{
    private int mJsonType = 1;
    
    
    public GJSONDecoder(int jsonType)
    {
        super();
        mJsonType = jsonType;
    }
    
    
    @Override
    public GBean decode(int httpStatus, String result, Cookie[] cookies, Header[] responseHeaders)
    {
        String auth_token = "";
        
        if (responseHeaders != null)
        {
            for (int i = 0; i < responseHeaders.length; i++)
            {
                Header header = responseHeaders[i];
                LogUtil.d("해더 " + i + " -> " + header.toString());
                
                if (header.getName().equals("auth_token"))
                {
                    auth_token = header.getValue();
                }
            }
        }
        else
        {
            LogUtil.d("해더 -> 없음");
        }
        
        LogUtil.d("원본 --> " + result);
        
        GBean bean = new GBean();
        try
        {
            JSONObject json = new JSONObject(result);
            
            JSONObject header = JSONUtil.getJSONObject(json, "header");
            JSONObject body = JSONUtil.getJSONObject(json, "body");
            
            
            bean.put(GHttpConstants.AUTH_TOKEN, auth_token);

            bean.put(GHttpConstants.RESPONSE_DATA, json);
            
            bean.put(GHttpConstants.RETURN_CODE, JSONUtil.getInteger(header, "result", -99999));
            
            bean.put(GHttpConstants.RETURN_MESSAGE, JSONUtil.getString(header, "message", ""));
            
            switch (mJsonType)
            {
                case GHttpConstants.TYPE_OBJECT:
                    bean.put(GHttpConstants.RESULT_ENTITY, JSONUtil.getJSONObject(json, "entity"));
                    break;
                case GHttpConstants.TYPE_ARRAY:
                    bean.put(GHttpConstants.RESULT_ENTITY, JSONUtil.getJSONArray(json, "entity"));
                    bean.put(GHttpConstants.TOTAL_COUNT, JSONUtil.getInteger(json, "totalCount", -9999));
                    break;
                case GHttpConstants.TYPE_STRING:
                    bean.put(GHttpConstants.RESULT_ENTITY, JSONUtil.getString(json, "entity", ""));
                    break;
            }
            
            bean.put(GHttpConstants.RESPONSE_HEADER, header);

            bean.put(GHttpConstants.RESPONSE_BODY, body);
            
            LogUtil.e("가공 --> " + json.toString());
            
            bean.put(GHttpConstants.COOKIES, cookies);
        }
        catch (Exception e)
        {
            bean.putException(e);
        }
        return bean;
    }
    
}
