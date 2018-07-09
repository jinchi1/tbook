package com.travelbooks3.android.activity.search.data;

import org.json.JSONObject;

/**
 * Created by system777 on 2017-09-19.
 */

public class TagDefaultData {

    public String tag;
    public String count;

    public static TagDefaultData TagDefaultJson(TagDefaultData data, JSONObject json) throws Exception {
        if (data == null) data = new TagDefaultData();

        if (!json.isNull("tag")) data.tag = json.getString("tag");
        if (!json.isNull("count")) data.count = json.getString("count");

        return data;
    }

}
