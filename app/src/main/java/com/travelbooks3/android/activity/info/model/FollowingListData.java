package com.travelbooks3.android.activity.info.model;

import org.json.JSONObject;

/**
 * Created by system777 on 2017-10-12.
 */

public class FollowingListData {

    public String user_idx;
    public String name;
    public String introduce;
    public String follow_idx;
    public String profile_photo;
    public String isFollow;
    public String isAuth;



    public static FollowingListData FollowingListJson(FollowingListData data, JSONObject json) throws Exception {
        if (data == null) data = new FollowingListData();

        if (!json.isNull("user_idx")) data.user_idx = json.getString("user_idx");
        if (!json.isNull("name")) data.name = json.getString("name");
        if (!json.isNull("introduce")) data.introduce = json.getString("introduce");
        if (!json.isNull("profile_photo")) data.profile_photo = json.getString("profile_photo");
        if (!json.isNull("follow_idx")) data.follow_idx = json.getString("follow_idx");
        if (!json.isNull("isFollow")) data.isFollow = json.getString("isFollow");
        if (!json.isNull("isAuth")) data.isAuth = json.getString("isAuth");


        return data;
    }

}
