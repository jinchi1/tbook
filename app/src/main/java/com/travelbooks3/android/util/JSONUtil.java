package com.travelbooks3.android.util;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * JSON 관련 유틸
 * 
 *         
 */
public class JSONUtil
{
    /**
     * 기본 JSONObject 생성
     * 
     * @param jsonStr
     * @return
     */
    public static JSONObject createObject(String jsonStr)
    {
        JSONObject jsObj = null;
        if (jsonStr == null || "".equals(jsonStr))
        {
            return null;
        }
        try
        {
            jsObj = new JSONObject(jsonStr);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsObj;
    }
    
    
    /**
     * 기본 JSONArray 생성
     * 
     * @param jsonStr
     * @return
     */
    public static JSONArray createArray(String jsonStr)
    {
        JSONArray jsonArray = null;
        if (jsonStr == null || "".equals(jsonStr))
        {
            return null;
        }
        try
        {
            jsonArray = new JSONArray(jsonStr);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonArray;
    }
    
    
    /**
     * 
     * @param jsonStr
     * @param key
     * @return JSONArray (null)
     */
    public static JSONArray getJSONArray(String jsonStr, String key)
    {
        return getJSONArray(createObject(jsonStr), key);
    }
    
    
    public static JSONArray getJSONArray(JSONObject json, String key)
    {
        
        JSONArray arr = null;
        try
        {
            
            if (json == null) return null;
            
            if (!json.has(key)) return null;
            
            arr = json.getJSONArray(key);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return arr;
    }
    
    
    // -------------------------------------------------------------------------------------20121022
    public static JSONObject getJSONObject(String jsonStr, String key)
    {
        return getJSONObject(createObject(jsonStr), key);
    }
    
    
    public static JSONObject getJSONObject(JSONObject json, String key)
    {
        if (json == null) return null;
        
        JSONObject jsObj = null;
        try
        {
            jsObj = json.getJSONObject(key);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsObj;
    }
    
    
    public static JSONObject getJSONObject(JSONArray json, int idx)
    {
        if (json == null) return null;
        
        if (json.length() == 0) return null;
        
        JSONObject jsObj = null;
        try
        {
            jsObj = json.getJSONObject(idx);
        }
        catch (JSONException e)
        {
            // e.printStackTrace();
        }
        return jsObj;
    }
    
    
    public static String getString(JSONArray json, int idx)
    {
        
        String returnValue = "";
        
        if (json != null)
        {
            try
            {
                returnValue = json.getString(idx);
            }
            catch (Exception e)
            {
            }
        }
        
        return returnValue;
    }
    
    
    public static String getString(JSONArray json, String matchKey, String matchValue, String key)
    {
        
        String returnValue = "";
        
        if (json != null)
        {
            for (int i = 0; i < json.length(); i++)
            {
                JSONObject obj = getJSONObject(json, i);
                
                if (getString(obj, matchKey, "").equals(matchValue))
                {
                    returnValue = getString(obj, key);
                }
            }
        }
        
        return returnValue;
    }
    
    
    /**
     * 
     * @param json
     * @param key
     * @param
     * @return
     */
    public static String getString(JSONObject json, String key, String defVal)
    {
        if (json != null)
        {
            if (json.has(key))
            {
                try
                {
                    return json.getString(key).replaceAll("^null$", "");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return defVal;
    }
    
    
    /**
     * 
     * @param json
     * @param key
     * @return getString(json, key, null);
     */
    public static String getString(JSONObject json, String key)
    {
        return getString(json, key, "");
    }
    
    
    /**
     * 
     * @param json
     * @param key
     * @param defVal
     * @return
     */
    public static int getInteger(JSONObject json, String key, int defVal)
    {
        if (json != null)
        {
            if (json.has(key))
            {
                try
                {
                    return json.getInt(key);
                    
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            
        }
        return defVal;
    }
    
    
    /**
     * 
     * @param json
     * @param key
     * @return getInteger(json, key, Integer.MIN_VALUE);
     */
    public static int getInteger(JSONObject json, String key)
    {
        return getInteger(json, key, Integer.MIN_VALUE);
    }
    
    
    /**
     * 
     * @param json
     * @param key
     * @param defVal
     * @return
     */
    public static boolean getBoolean(JSONObject json, String key, boolean defVal)
    {
        if (json != null)
        {
            if (json.has(key))
            {
                try
                {
                    return json.getBoolean(key);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return defVal;
    }
    
    
    /**
     * 
     * @param json
     * @param key
     * @return getBoolean(json, key, false);
     */
    public static boolean getBoolean(JSONObject json, String key)
    {
        return getBoolean(json, key, false);
    }
    
    
    /**
     * 
     * @param json
     * @param key
     * @param defVal
     * @return
     */
    public static long getLong(JSONObject json, String key, long defVal)
    {
        if (json != null)
        {
            if (json.has(key))
            {
                try
                {
                    return json.getLong(key);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return defVal;
    }
    
    
    /**
     * 
     * @param json
     * @param key
     * @return getLong(json, key, Long.MIN_VALUE);
     */
    public static long getLong(JSONObject json, String key)
    {
        return getLong(json, key, Long.MIN_VALUE);
    }
    
    
    /**
     * 
     * @param json
     * @param key
     * @param defVal
     * @return
     */
    public static double getDouble(JSONObject json, String key, double defVal)
    {
        if (json != null)
        {
            if (json.has(key))
            {
                try
                {
                    return json.getDouble(key);
                    
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            
        }
        
        return defVal;
    }
    
    
    /**
     * 
     * @param json
     * @param key
     * @return getDouble(json, key, Double.MIN_VALUE)
     */
    public static double getDouble(JSONObject json, String key)
    {
        return getDouble(json, key, Double.MIN_VALUE);
    }
    
    
    public static Object get(JSONObject json, String key)
    {
        if (json != null)
        {
            if (json.has(key))
            {
                try
                {
                    return json.get(key);
                    
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    
    public static boolean has(JSONObject json, String key)
    {
        if (json != null)
        {
            return json.has(key);
        }
        else
        {
            return false;
        }
    }
    
    
    public static JSONObject put(JSONObject json, String key, Object value)
    {
        if (json == null) return null;
        try
        {
            json.put(key, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return json;
    }
    
    
    public static JSONObject puts(JSONObject json, String key, Object value)
    {
        if(json == null)
            return null;
        try
        {
            if(!json.has(key))
            {
                json.put(key, value);
            }
            else
            {
                json.remove(key);
                json.put(key, value);
            }
        }
        catch (Exception e)
        {
            // e.printStackTrace();
            LogUtil.w(e);
        }
        return json;
    }
    
    
    @SuppressLint("NewApi")
    public static JSONArray put(JSONArray jsonArray, JSONObject json, int index)
    {
        if (jsonArray == null) return null;
        
        try
        {
            jsonArray.remove(index);
            jsonArray.put(index, json);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return jsonArray;
    }
    
}
