package com.travelbooks3.android.activity.main.model;

import org.json.JSONObject;

/**
 * Created by system777 on 2017-06-30.
 */

public class MainFriendsData {
    public String photo;
    public String name;
    public String idx;

    public static MainFriendsData MainFriendsJSON(MainFriendsData data, JSONObject json) throws  Exception{
        if(data == null) data = new MainFriendsData();

        if(!json.isNull("photo")) data.photo = json.getString("photo");
        if(!json.isNull("name"))  data.name = json.getString("name");
        if(!json.isNull("idx"))   data.idx = json.getString("idx");

        return data;
    }
}