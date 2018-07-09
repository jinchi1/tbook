package com.travelbooks3.android.util;

import android.content.Context;
import com.travelbooks3.android.common.Constants;

import org.apache.commons.httpclient.Cookie;
import org.json.JSONException;
import org.json.JSONObject;


public class SPUtil extends SPUtilBase
{
    private static final String TAG                   = "Travelbooks2_SPU";
                                                      
    private static SPUtil helper                = new SPUtil();
    private Cookie[]            cookies               = null;
                                                      
    public static final String  NAME_SYS              = "spu.sys";
    public static final String  NAME_USER             = "spu.user";
                                                      
    private static final String SPU_K_IS_LOGIN        = "SPU_K_IS_LOGIN";
    private static final String SPU_K_IS_LOGIN_AUTO   = "SPU_K_IS_LOGIN_AUTO";
    private static final String SPU_K_IS_SOUND        = "SPU_K_IS_SOUND";
    private static final String SPU_K_IS_VIBRATOR     = "SPU_K_IS_VIBRATOR";
    private static final String SPU_K_IS_PUSH         = "SPU_K_IS_PUSH";
    private static final String SPU_K_IS_POP          = "SPU_K_IS_POP";
    private static final String SPU_K_IS_LOCATION     = "SPU_K_IS_LOCATION";
                                                      
    private static final String SPU_K_PUSH_TYPE       = "SPU_K_PUSH_TYPE";
    private static final String SPU_K_COOKIE          = "SPU_K_COOKIE";
    private static final String SPU_K_REGISTKEY       = "SPU_K_REGISTKEY";
    private static final String SPU_K_USER_INFO       = "SPU_K_USER_INFO";
    private static final String SPU_K_LOGIN_TYPE      = "SPU_K_LOGIN_TYPE";
                                                      
    private static final String SPU_K_AUTH_TOKEN      = "SPU_K_AUTH_TOKEN";
                                                      
    private static final String SPU_K_START_INDEX     = "SPU_K_START_INDEX";
                                                      
    private static final String SPU_K_USER_Address    = "SPU_K_USER_Address";
                                                      
    private static final String SPU_K_USER_Location   = "SPU_K_USER_Location";
                                                      
    private static final String SPU_K_USER_Country    = "SPU_K_USER_Country";
                                                      
    private static final String SPU_K_USER_Language   = "SPU_K_USER_Language";
                                                      
    private static final String SPU_K_Guide_VIEW      = "SPU_K_Guide_VIEW";
    private static final String SPU_K_AD_URL          = "SPU_K_AD_URL";
                                                      
    private static final String SPU_K_USER_LoginTime  = "SPU_K_USER_LoginTime";
    private static final String SPU_K_USER_LimitID    = "SPU_K_USER_LimitID";
                                                      
    private static final String SPU_K_USER_ID         = "SPU_K_USER_ID";
    private static final String SPU_K_USER_FBID       = "SPU_K_USER_FBID";
    private static final String SPU_K_USER_PW         = "SPU_K_USER_PW";
    private static final String SPU_K_USER_SESSION_ID = "SPU_K_USER_SESSION_ID";
    private static final String SPU_K_USER_NUMBER     = "SPU_K_USER_NUMBER";
                                                      
    private static final String SPU_K_MARKET_URL      = "SPU_K_MARKET_URL";
    private static final String SPU_K_MARKET_URL_IOS  = "SPU_K_MARKET_URL_IOS";
                                                      
    private static final String SPU_K_NEW_COUNT       = "SPU_K_NEW_COUNT";
                                                      
    private static final String SPU_K_SERVER_VERSION  = "SPU_K_SERVER_VERSION";
                                                      
                                                      
    public static SPUtil getInstance()
    {
        if (helper == null)
        {
            helper = new SPUtil();
        }
        return helper;
    }
    

    public void setCookie(Context context, Cookie[] c)
    {
        String strCookie = "";
        LogUtil.d(TAG, "COOKE SAVE============================================================================");
        this.cookies = c;
        
        for (int i = 0; i < c.length; i++)
        {
            JSONObject job = new JSONObject();
            
            try
            {
                job.put("cookieDomain", c[i].getDomain());
                job.put("cookieName", c[i].getName());
                job.put("cookiePath", c[i].getPath());
                job.put("cookieValue", c[i].getValue());
                
                LogUtil.d(TAG, "i : " + i);
                LogUtil.d(TAG, "job.toString() : " + job.toString());
                
                strCookie = FormatUtil.isNullorEmpty(strCookie) ? job.toString() : strCookie + "///" + job.toString();
                
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            
        }
        writeString(context, NAME_SYS, SPU_K_COOKIE, strCookie);
        
        LogUtil.d(TAG, "COOKE SAVE============================================================================");
    }
    

    public Cookie[] getCookie(Context context)
    {
        LogUtil.d(TAG, "COOKE READ============================================================================");
        if (this.cookies == null)
        {
            String strCookie = readString(context, NAME_SYS, SPU_K_COOKIE, "");
            if (FormatUtil.isNullorEmpty(strCookie))
            {
                LogUtil.d(TAG, "COOKE READ 없음=======================================================================");
                return null;
            }
            
            String strCookies[] = strCookie.split("///");
            
            Cookie[] resultCookie = new Cookie[strCookies.length];
            
            for (int i = 0; i < strCookies.length; i++)
            {
                JSONObject job = JSONUtil.createObject(strCookies[i]);
                LogUtil.d(TAG, "i : " + i);
                LogUtil.d(TAG, "job.toString() : " + job.toString());
                
                Cookie cookieTemp = new Cookie();
                cookieTemp.setDomain(JSONUtil.getString(job, "cookieDomain"));
                cookieTemp.setName(JSONUtil.getString(job, "cookieName"));
                cookieTemp.setPath(JSONUtil.getString(job, "cookiePath"));
                cookieTemp.setValue(JSONUtil.getString(job, "cookieValue"));
                
                resultCookie[i] = cookieTemp;
            }
            LogUtil.d(TAG, "COOKE READ FILE ======================================================================");
            this.cookies = resultCookie;
        }
        else
        {
            LogUtil.d(TAG, "COOKE READ MEMORY ====================================================================");
        }
        return this.cookies;
    }
    

    public void removeCookie(Context context)
    {
        
        LogUtil.d(TAG, "COOKE REMOVE==========================================================================");
//        this.cookies = null;
        deleteData(context, NAME_SYS, SPU_K_COOKIE);
    }
    
    
    // ***************************************************************************************************************************************
    // 푸쉬키
    // ***************************************************************************************************************************************
    /**
     * @name 푸쉬키
     * @param context
     * @return
     */
    public String getRegistPushKey(Context context)
    {
        return readString(context, NAME_SYS, SPU_K_REGISTKEY, "");
    }
    
    
    public void setRegistPushKey(Context context, String key)
    {
        writeString(context, NAME_SYS, SPU_K_REGISTKEY, key);
    }
    
    
    // ***************************************************************************************************************************************
    // API Auth Token
    // ***************************************************************************************************************************************
    public void setAuthToken(Context context, String auth_token)
    {
        writeString(context, NAME_SYS, SPU_K_AUTH_TOKEN, auth_token);
    }
    
    
    public String getAuthToken(Context context)
    {
        return readString(context, NAME_SYS, SPU_K_AUTH_TOKEN, "");
    }
    
    
    // ***************************************************************************************************************************************
    // 광고 본 유무
    // ***************************************************************************************************************************************
    public void setAdView(Context context, boolean isGuide)
    {
        writeBoolean(context, NAME_SYS, SPU_K_Guide_VIEW, isGuide);
    }
    
    
    public boolean getAdView(Context context)
    {
        return readBoolean(context, NAME_SYS, SPU_K_Guide_VIEW, false);
    }
    
    
    // ***************************************************************************************************************************************
    // 로그인
    // ***************************************************************************************************************************************
    public void setIsLogin(Context context, boolean isLogin)
    {
        writeBoolean(context, NAME_SYS, SPU_K_IS_LOGIN, isLogin);
    }
    
    
    public boolean getIsLogin(Context context)
    {
        return readBoolean(context, NAME_SYS, SPU_K_IS_LOGIN, false);
    }
    

    public void setIsAutoLogin(Context context, boolean isAutoLogin)
    {
        writeBoolean(context, NAME_SYS, SPU_K_IS_LOGIN_AUTO, isAutoLogin);
    }

    public boolean getIsAutoLogin(Context context)
    {
        return readBoolean(context, NAME_SYS, SPU_K_IS_LOGIN_AUTO, true);
    }
    
    
    /**
     * 로그인 타입 <br>
     * LOGIN_TYPE_NORMAL = 1 , LOGIN_TYPE_AGENT = 2
     * 
     * @param context
     * @return
     */
    
    // public int getLoginType(Context context)
    // {
    // return readInt(context, NAME_SYS, SPU_K_LOGIN_TYPE, -1);
    // }
    //
    // public void setLoginType(Context context, int type)
    // {
    // writeInt(context, NAME_SYS, SPU_K_LOGIN_TYPE, type);
    // }
    
    // ***************************************************************************************************************************************
    // 로그인 5회 이상 틀릴시 5분 접속 제한
    // ***************************************************************************************************************************************
    public void setUserLoginTime(Context context, long time)
    {
        writeLong(context, NAME_USER, SPU_K_USER_LoginTime, time);
    }
    
    
    public long getUserLoginTime(Context context)
    {
        return readLong(context, NAME_USER, SPU_K_USER_LoginTime);
    }
    
    
    public String getUserLimitID(Context context)
    {
        return readString(context, NAME_USER, SPU_K_USER_LimitID, "");
    }
    
    
    public void setUserLimitID(Context context, String key)
    {
        writeString(context, NAME_USER, SPU_K_USER_LimitID, key);
    }
    
    
    // ***************************************************************************************************************************************
    // 푸시 관련
    // ***************************************************************************************************************************************
    public void setIsOnPush(Context context, boolean isOnSound)
    {
        writeBoolean(context, NAME_SYS, SPU_K_IS_PUSH, isOnSound);
    }
    
    
    public boolean getIsOnPush(Context context)
    {
        return readBoolean(context, NAME_SYS, SPU_K_IS_PUSH, true);
    }
    
    
    public void setIsOnSound(Context context, boolean isOnSound)
    {
        writeBoolean(context, NAME_SYS, SPU_K_IS_SOUND, isOnSound);
    }
    
    
    public boolean getIsOnSound(Context context)
    {
        return readBoolean(context, NAME_SYS, SPU_K_IS_SOUND, true);
    }
    
    
    public void setIsOnVib(Context context, boolean isOnVib)
    {
        writeBoolean(context, NAME_SYS, SPU_K_IS_VIBRATOR, isOnVib);
    }
    
    
    public boolean getIsOnVib(Context context)
    {
        return readBoolean(context, NAME_SYS, SPU_K_IS_VIBRATOR, true);
    }
    
    
    public void setIsOnPop(Context context, boolean isOnPop)
    {
        writeBoolean(context, NAME_SYS, SPU_K_IS_POP, isOnPop);
    }
    
    
    public boolean getIsOnPop(Context context)
    {
        return readBoolean(context, NAME_SYS, SPU_K_IS_POP, false);
    }
    
    
    // ***************************************************************************************************************************************
    // 유저정보
    // ***************************************************************************************************************************************
    public JSONObject getUserInfo(Context context)
    {
        return readJSONObject(context, NAME_USER, SPU_K_USER_INFO);
    }
    
    
    public void setUserInfo(Context context, JSONObject json)
    {
        if (json != null) writeString(context, NAME_USER, SPU_K_USER_INFO, json.toString());
    }
    
    
    public void removeUserInfo(Context context)
    {
        deleteData(context, NAME_USER, SPU_K_USER_INFO);
    }
    
    
    // ***************************************************************************************************************************************
    // 유저정보 위치 정보
    // ***************************************************************************************************************************************
    public JSONObject getUserAddressInfo(Context context)
    {
        return readJSONObject(context, NAME_USER, SPU_K_USER_Address);
    }
    
    
    public void setUserAddressInfo(Context context, JSONObject json)
    {
        if (json != null) writeString(context, NAME_USER, SPU_K_USER_Address, json.toString());
    }
    
    
    // ***************************************************************************************************************************************
    // 설정된 언어 정보
    // ***************************************************************************************************************************************
    public String getUserLanguage(Context context)
    {
        return readString(context, NAME_USER, SPU_K_USER_Language, Constants.LANG_KOR);
    }
    
    
    public void setUserLanguage(Context context, String language)
    {
        writeString(context, NAME_USER, SPU_K_USER_Language, language);
    }
    
    
    // ***************************************************************************************************************************************
    // 국가 정보
    // ***************************************************************************************************************************************
    public String getUserCountry(Context context)
    {
        return readString(context, NAME_USER, SPU_K_USER_Country, "");
    }
    
    
    public void setUserCountry(Context context, String country)
    {
        writeString(context, NAME_USER, SPU_K_USER_Country, country);
    }
    
    
    /**
     * 
     * @param context
     * @return 1 지역설정 0 gps
     */
    public int getLocationType(Context context)
    {
        return readInt(context, NAME_USER, SPU_K_USER_Location, 0);
    }
    
    
    /**
     * 
     * @param context
     * @param type
     *            1 지역설정 0 gps
     */
    public void setLocationType(Context context, int type)
    {
        writeInt(context, NAME_USER, SPU_K_USER_Location, type);
    }
    
    
    /**
     * 시작 페이지 인덱스 조회
     * 
     * @param context
     * @return
     */
    public int getStartIndex(Context context)
    {
        return readInt(context, NAME_USER, SPU_K_START_INDEX, 0);
    }
    
    
    /**
     * 시작 페이지 인덱스 세팅
     * 
     * @param context
     * @param type
     */
    public void setStartIndex(Context context, int type)
    {
        writeInt(context, NAME_USER, SPU_K_START_INDEX, type);
    }
    
    
    // ***************************************************************************************************************************************
    // 실제 계정 아이디
    // ***************************************************************************************************************************************
    /**
     * @name 아이디
     * @param context
     * @return
     */
    public String getUserID(Context context)
    {
        return readString(context, NAME_SYS, SPU_K_USER_ID, "");
    }
    
    
    public void setUserID(Context context, String key)
    {
        writeString(context, NAME_SYS, SPU_K_USER_ID, key);
    }
    
    
    // ***************************************************************************************************************************************
    // 실제 계정 패스워드
    // ***************************************************************************************************************************************
    /**
     * @name 패스워드
     * @param context
     * @return
     */
    public String getUserPW(Context context)
    {
        return readString(context, NAME_SYS, SPU_K_USER_PW, "");
    }
    
    
    public void setUserPW(Context context, String key)
    {
        writeString(context, NAME_SYS, SPU_K_USER_PW, key);
    }
    
    
    // ***************************************************************************************************************************************
    // SessionID
    // ***************************************************************************************************************************************
    /**
     * @name SessionID
     * @param context
     * @return
     */
    public String getUserSessionID(Context context)
    {
        return readString(context, NAME_SYS, SPU_K_USER_SESSION_ID, "");
    }
    
    
    public void setUserSessionID(Context context, String key)
    {
        writeString(context, NAME_SYS, SPU_K_USER_SESSION_ID, key);
    }
    
    
    // ***************************************************************************************************************************************
    // 실제 계정 페이스 북 아이디
    // ***************************************************************************************************************************************
    /**
     * @name 아이디
     * @param context
     * @return
     */
    public String getUserFBID(Context context)
    {
        return readString(context, NAME_SYS, SPU_K_USER_FBID, "");
    }
    
    
    public void setUserFBID(Context context, String key)
    {
        writeString(context, NAME_SYS, SPU_K_USER_FBID, key);
    }
    
    
    // ***************************************************************************************************************************************
    // 실제 계정 회원 번호
    // ***************************************************************************************************************************************
    /**
     * @name 회원 번호
     * @param context
     * @return
     */
    public int getUserNumber(Context context)
    {
        return readInt(context, NAME_SYS, SPU_K_USER_NUMBER, 0);
    }
    
    
    public void setUserNumber(Context context, int key)
    {
        writeInt(context, NAME_SYS, SPU_K_USER_NUMBER, key);
    }
    
    
    // ***************************************************************************************************************************************
    // 로그아웃 처리
    // ***************************************************************************************************************************************
    public void logOut(Context context)
    {
        
        removeCookie(context);
        removeUserInfo(context);
        
        deleteData(context, NAME_SYS, SPU_K_USER_NUMBER);
        deleteData(context, NAME_SYS, SPU_K_USER_ID);
        deleteData(context, NAME_SYS, SPU_K_IS_LOGIN);
        deleteData(context, NAME_SYS, SPU_K_NEW_COUNT);
        deleteData(context, NAME_SYS, SPU_K_AUTH_TOKEN);
    }
    
    
    // ***************************************************************************************************************************************
    // 마켓url
    // ***************************************************************************************************************************************
    public String getMarketUrl(Context context)
    {
        return readString(context, NAME_SYS, SPU_K_MARKET_URL, Constants.MARKET_URL);
    }
    
    
    public void setMarketUrl(Context context, String url)
    {
        writeString(context, NAME_SYS, SPU_K_MARKET_URL, url);
    }
    
    
    public String getMarketUrlIOS(Context context)
    {
        return readString(context, NAME_SYS, SPU_K_MARKET_URL_IOS, Constants.MARKET_URL);
    }
    
    
    public void setMarketUrlIOS(Context context, String url)
    {
        writeString(context, NAME_SYS, SPU_K_MARKET_URL_IOS, "");
    }
    
    
    public void setServerVersion(Context context, String version)
    {
        writeString(context, NAME_SYS, SPU_K_SERVER_VERSION, version);
    }
    
    
    public String getServerVersion(Context context)
    {
        return readString(context, NAME_SYS, SPU_K_SERVER_VERSION, "");
    }
    
    
    // ***************************************************************************************************************************************
    // 푸시타입 : -1알림끄기, 1 모든사람, 2:내가팔로우한사람
    // ***************************************************************************************************************************************
    public int getPushType(Context context)
    {
        return readInt(context, NAME_SYS, SPU_K_PUSH_TYPE, 1);
    }
    
    
    public void setPushType(Context context, int type)
    {
        writeInt(context, NAME_SYS, SPU_K_PUSH_TYPE, type);
    }
    
    
    // ***************************************************************************************************************************************
    // 위치 정보 동의
    // ***************************************************************************************************************************************
    public boolean getAgreeLocation(Context context)
    {
        return readBoolean(context, NAME_SYS, SPU_K_IS_LOCATION, false);
    }
    
    
    public void setAgreeLocation(Context context, boolean isAgree)
    {
        writeBoolean(context, NAME_SYS, SPU_K_IS_LOCATION, isAgree);
    }
    
    
    // ***************************************************************************************************************************************
    // 톡 뉴카운트
    // ***************************************************************************************************************************************
    public void setNewCount(Context context, int newCount)
    {
        writeInt(context, NAME_SYS, SPU_K_NEW_COUNT, newCount);
    }
    
    
    public int getNewCount(Context context)
    {
        return readInt(context, NAME_SYS, SPU_K_NEW_COUNT, 0);
    }
    
}
