package com.travelbooks3.android.activity.search.data;

import org.json.JSONObject;

/**
 * Created by system777 on 2017-09-19.
 */

public class UserDefaultData {
    public String user_idx;
    public String name;
    public String introduce;
    public String profile_photo;
    public String post_recent_count;
    public String follower;
    public String following;
    public String posting;


    public static UserDefaultData UserDefaultJson(UserDefaultData data, JSONObject json) throws Exception {
        if (data == null) data = new UserDefaultData();

        if (!json.isNull("user_idx")) data.user_idx = json.getString("user_idx");
        if (!json.isNull("name")) data.name = json.getString("name");
        if (!json.isNull("introduce")) data.introduce = json.getString("introduce");
        if (!json.isNull("profile_photo")) data.profile_photo = json.getString("profile_photo");
        if (!json.isNull("post_recent_count")) data.post_recent_count = json.getString("post_recent_count");
        if (!json.isNull("follower")) data.follower = json.getString("follower");
        if (!json.isNull("following")) data.following = json.getString("following");
        if (!json.isNull("posting")) data.posting = json.getString("posting");


        return data;
    }

}
