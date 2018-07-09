package com.travelbooks3.android.activity.search.data;

/**
 * Created by system777 on 2017-09-18.
 */

import org.json.JSONObject;


public class AllDefaultData {

    public String search_type;
    public String idx;
    public String addr;
    public String eng_addr;
    public String tag;
    public String count;
    public String user_idx;
    public String name;
    public String introduce;
    public String profile_photo;
    public String post_recent_count;
    public String latitude;
    public String longitude;

    public static AllDefaultData AllDefaultJson(AllDefaultData data, JSONObject json) throws Exception {
        if (data == null) data = new AllDefaultData();

        if (!json.isNull("search_type")) data.search_type = json.getString("search_type");
        if (!json.isNull("idx")) data.idx = json.getString("idx");
        if (!json.isNull("addr")) data.addr = json.getString("addr");
        if (!json.isNull("eng_adr")) data.eng_addr = json.getString("eng_adr");
        if (!json.isNull("latitude")) data.latitude = json.getString("latitude");
        if (!json.isNull("longitude")) data.longitude = json.getString("longitude");
        if (!json.isNull("tag")) data.tag = json.getString("tag");
        if (!json.isNull("count")) data.count = json.getString("count");
        if (!json.isNull("user_idx")) data.user_idx = json.getString("user_idx");
        if (!json.isNull("name")) data.name = json.getString("name");
        if (!json.isNull("introduce")) data.introduce = json.getString("introduce");
        if (!json.isNull("profile_photo")) data.profile_photo = json.getString("profile_photo");
        if (!json.isNull("post_recent_count")) data.post_recent_count = json.getString("post_recent_count");



        return data;
    }

}