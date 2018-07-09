package com.travelbooks3.android.activity.search.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by system777 on 2017-08-23.
 */

public class PlacePagerData {

    public String profile_photo;
    public String comment;
    public String regdate;
    public String country;
    public String continent;
    public String latitude;
    public String longitude;
    public String name;
    public int user_idx;
    public String trip_uid;
    public int like_count;
    public int reply_comment_count;
    public String addr;
    public String eng_addr;
    public String liker_name;
    public int liker_idx;
    public String like_regdate;
    public String reply_commenter_name;
    public int reply_commenter_idx;
    public String reply_comment;
    public String reply_comment_regdate;
    public String isAuth;
    public String isLike;
    public String isComment;
    public String isBookmark;
    public int zoom_level;
    public int like_idx;
    public int reply_comment_idx;
    public String bookmark_idx;

    public ArrayList<Photos> photoArr;


    public static class Photos{
        public int idx;
        public String photo;
    }




    public static PlacePagerData placePagerJson(PlacePagerData data, JSONObject json) throws Exception{
        if(data == null) data = new PlacePagerData();

        if (!json.isNull("profile_photo"))       data.profile_photo=json.getString("profile_photo");
        if (!json.isNull("comment"))              data.comment=json.getString("comment");
        if (!json.isNull("country"))              data.country=json.getString("country");
        if (!json.isNull("continent"))            data.continent=json.getString("continent");
        if (!json.isNull("latitude"))             data.latitude=json.getString("latitude");
        if (!json.isNull("longitude"))            data.longitude=json.getString("longitude");
        if (!json.isNull("name"))                  data.name=json.getString("name");
        if (!json.isNull("user_idx"))             data.user_idx=json.getInt("user_idx");
        if (!json.isNull("trip_uid"))             data.trip_uid=json.getString("trip_uid");
        if (!json.isNull("regdate"))               data.regdate=json.getString("regdate");
        if (!json.isNull("addr"))                 data.addr=json.getString("addr");
        if (!json.isNull("eng_addr"))            data.eng_addr=json.getString("eng_addr");
        if (!json.isNull("liker_name"))          data.liker_name=json.getString("liker_name");
        if (!json.isNull("like_regdate"))        data.like_regdate=json.getString("like_regdate");
        if (!json.isNull("like_idx"))            data.like_idx=json.getInt("like_idx");
        if (!json.isNull("reply_comment_idx"))  data.reply_comment_idx=json.getInt("reply_comment_idx");
        if (!json.isNull("bookmark_idx"))        data.bookmark_idx=json.getString("bookmark_idx");
        if (!json.isNull("like_count"))          data.like_count=json.getInt("like_count");
        if (!json.isNull("reply_comment_count")) data.reply_comment_count=json.getInt("reply_comment_count");
        if (!json.isNull("isAuth"))              data.isAuth=json.getString("isAuth");
        if (!json.isNull("isLike"))              data.isLike=json.getString("isLike");
        if (!json.isNull("isComment"))           data.isComment=json.getString("isComment");
        if (!json.isNull("isBookmark"))          data.isBookmark=json.getString("isBookmark");
        if (!json.isNull("liker_idx"))         data.liker_idx=json.getInt("liker_idx");
        if (!json.isNull("reply_commenter_name"))         data.reply_commenter_name=json.getString("reply_commenter_name");
        if (!json.isNull("reply_comment"))         data.reply_comment=json.getString("reply_comment");
        if (!json.isNull("reply_commenter_idx"))         data.reply_commenter_idx=json.getInt("reply_commenter_idx");
        if (!json.isNull("reply_comment_regdate"))         data.reply_comment_regdate=json.getString("reply_comment_regdate");




        if(!json.isNull("photos")){

            JSONArray photoJsonArr = json.getJSONArray("photos");
            data.photoArr = new ArrayList<Photos>();
            Photos photos;

            for (int i = 0 ; i < photoJsonArr.length();i++ ){
                JSONObject obj = photoJsonArr.getJSONObject(i);

                photos = new Photos();
                photos.idx = obj.getInt("idx");
                photos.photo = obj.getString("photo");
                data.photoArr.add(photos);
            }
        }



         return data;
    }


}
