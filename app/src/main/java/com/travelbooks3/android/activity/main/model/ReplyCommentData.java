package com.travelbooks3.android.activity.main.model;

import org.json.JSONObject;

/**
 * Created by system777 on 2017-10-19.
 */

public class ReplyCommentData {

    public int idx;
    public int user_idx;
    public String comment;
    public String regdate;
    public String name;
    public String isAuth;
    public int like_Count;
    public int target_idx;
    public String isLike;
    public String profile_photo;

    public static ReplyCommentData replyCommentDataJSON(ReplyCommentData data, JSONObject json) throws Exception{
        if (data == null) data = new ReplyCommentData();


        if (!json.isNull("comment"))              data.comment=json.getString("comment");
        if (!json.isNull("regdate"))              data.regdate=json.getString("regdate");
        if (!json.isNull("name"))              data.name=json.getString("name");
        if (!json.isNull("isAuth"))              data.isAuth=json.getString("isAuth");
        if (!json.isNull("isLike"))              data.isLike=json.getString("isLike");
        if (!json.isNull("profile_photo"))              data.profile_photo=json.getString("profile_photo");
        if (!json.isNull("idx"))              data.idx=json.getInt("idx");
        if (!json.isNull("user_idx"))              data.user_idx=json.getInt("user_idx");
        if (!json.isNull("like_Count"))              data.like_Count=json.getInt("like_Count");
        if (!json.isNull("target_idx"))              data.target_idx=json.getInt("target_idx");


        return data;

    }

}
