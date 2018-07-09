package com.travelbooks3.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SPUtilBase
{
	private SharedPreferences getSharedPreferences(Context context, String dataFileName)
	{
		SharedPreferences common = context.getSharedPreferences(dataFileName, Activity.MODE_PRIVATE);
		return common;
	}

	public void writeString(Context context, String dataFileName, String dataKeyName, String value)
	{
		Editor editor = getSharedPreferences(context, dataFileName).edit();
		editor.putString(dataKeyName, value);
		editor.commit();
	}

	public void writeInt(Context context, String dataFileName, String dataKeyName, int value)
	{
		Editor editor = getSharedPreferences(context, dataFileName).edit();
		editor.putInt(dataKeyName, value);
		editor.commit();
	}

	public void writeLong(Context context, String dataFileName, String dataKeyName, long value)
	{
		Editor editor = getSharedPreferences(context, dataFileName).edit();
		editor.putLong(dataKeyName, value);
		editor.commit();
	}

	public void writeFloat(Context context, String dataFileName, String dataKeyName, float value)
	{
		Editor editor = getSharedPreferences(context, dataFileName).edit();
		editor.putFloat(dataKeyName, value);
		editor.commit();
	}

	public void writeBoolean(Context context, String dataFileName, String dataKeyName, boolean value)
	{
		Editor editor = getSharedPreferences(context, dataFileName).edit();
		editor.putBoolean(dataKeyName, value);
		editor.commit();
	}

	public void writeMap(Context context, String dataFileName, HashMap<String, ?> map)
	{
		Editor editor = getSharedPreferences(context, dataFileName).edit();

		for (String key : map.keySet())
		{
			Object value = map.get(key);

			if(value instanceof Integer)
			{
				editor.putInt(key, (Integer) value);
			}
			else if(value instanceof String)
			{
				editor.putString(key, (String) value);
			}
			else if(value instanceof Boolean)
			{
				editor.putBoolean(key, (Boolean) value);
			}
			else if(value instanceof Long)
			{
				editor.putLong(key, (Long) value);
			}
			else if(value instanceof Float)
			{
				editor.putFloat(key, (Float) value);
			}
		}

		editor.commit();
	}

	public void removeMap(Context context, String dataFileName, HashMap<String, ?> map)
	{
		Editor editor = getSharedPreferences(context, dataFileName).edit();

		for (String key : map.keySet())
		{
			editor.remove(key);
		}

		editor.commit();
	}

	public void writeJSONObject(Context context, String dataFileName, String dataKeyName, JSONObject json)
	{
		if(json != null)
		{
			writeString(context, dataFileName, dataKeyName, json.toString());
		}
	}

	public void writeJSONArray(Context context, String dataFileName, String dataKeyName, JSONArray json)
	{
		if(json != null)
		{
			writeString(context, dataFileName, dataKeyName, json.toString());
		}
	}

	public String readString(Context context, String dataFileName, String dataKeyName)
	{
		return readString(context, dataFileName, dataKeyName, null);
	}

	public String readString(Context context, String dataFileName, String dataKeyName, String defualt)
	{
		String ret = getSharedPreferences(context, dataFileName).getString(dataKeyName, defualt);
		return ret;
	}

	public int readInt(Context context, String dataFileName, String dataKeyName)
	{
		return readInt(context, dataFileName, dataKeyName, 0);
	}

	public int readInt(Context context, String dataFileName, String dataKeyName, int def)
	{
		int ret = getSharedPreferences(context, dataFileName).getInt(dataKeyName, def);
		return ret;
	}

	public long readLong(Context context, String dataFileName, String dataKeyName)
	{
		return getSharedPreferences(context, dataFileName).getLong(dataKeyName, 0);
	}

	public float readFloat(Context context, String dataFileName, String dataKeyName, float def)
	{
		float ret = getSharedPreferences(context, dataFileName).getFloat(dataKeyName, def);
		return ret;
	}

	public float readFloat(Context context, String dataFileName, String dataKeyName)
	{
		return readFloat(context, dataFileName, dataKeyName, 0.0f);
	}

	public boolean readBoolean(Context context, String dataFileName, String dataKeyName, boolean def)
	{
		boolean ret = getSharedPreferences(context, dataFileName).getBoolean(dataKeyName, def);
		return ret;
	}

	public boolean readBoolean(Context context, String dataFileName, String dataKeyName)
	{
		return readBoolean(context, dataFileName, dataKeyName, false);
	}

	public HashMap<String, ?> readAll(Context context, String dataFileName)
	{
		return (HashMap<String, ?>) getSharedPreferences(context, dataFileName).getAll();
	}

	public void deleteData(Context context, String dataFileName, String dataKeyName)
	{
		deleteData(context, dataFileName, new String[] { dataKeyName });
	}

	public JSONObject readJSONObject(Context context, String dataFileName, String dataKeyName)
	{
		String val = readString(context, dataFileName, dataKeyName);
		return createJSONObject(val);
	}

	public JSONArray readJSONArray(Context context, String dataFileName, String dataKeyName)
	{
		String val = readString(context, dataFileName, dataKeyName);
		return createJSONArray(val);
	}

	public void deleteData(Context context, String dataFileName, String[] dataKeyNames)
	{
		Editor editor = getSharedPreferences(context, dataFileName).edit();
		for (int i = 0; i < dataKeyNames.length; i++)
		{
			editor.remove(dataKeyNames[i]);
		}
		editor.commit();
	}

	public void clearData(Context context, String dataFileName)
	{
		Editor editor = getSharedPreferences(context, dataFileName).edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 기본 JSONObject 생성
	 * 
	 * @param jsonStr
	 * @return
	 */
	private JSONObject createJSONObject(String jsonStr)
	{
		JSONObject jsObj = null;
		if(jsonStr == null || "".equals(jsonStr))
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
	private JSONArray createJSONArray(String jsonStr)
	{
		JSONArray jsArray = null;
		if(jsonStr == null || "".equals(jsonStr))
		{
			return null;
		}
		try
		{
			jsArray = new JSONArray(jsonStr);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return jsArray;
	}
}
