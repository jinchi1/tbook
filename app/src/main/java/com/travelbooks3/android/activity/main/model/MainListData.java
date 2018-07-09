package com.travelbooks3.android.activity.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by system777 on 2017-06-27.
 */

public class MainListData {

    public String profile_photo;
    public String comment;
    public String update;
    public String country;
    public String continent;
    public String latitude;
    public String longitude;
    public String name;
    public int user_idx;
    public String trip_uid;
    public ArrayList<Photos> photoArr;
    public String addr;
    public String eng_addr;
    public String liker_name;
    public String like_regdate;
    public ArrayList<Comments> commentArr;
    public String like_idx;
    public int reply_comment_idx;
    public String bookmark_idx;
    public int like_count;
    public int dislike_count;
    public int reply_comment_count;
    public String isAuth;
    public String isLike;
    public String isDislike;
    public String isComment;
    public String isBookmark;
    public String type;
    public int zoom_level;
    public int bookmark_count;
    public String regdate;
    public static long duration;


    /*public static class Photos{
        public int idx;
        public String photo;
    }*/

    public static class Photos implements Parcelable {
        public int idx;
        public String photo;
        public String country;
        public String continent;
        public String addr;
        public String eng_addr;
        public String latitude;
        public String longitude;
        public String m_color;
        public int trans_idx;
        public String hour;
        public String min;
        public String route_comment;

        public Photos() {

        }

        public Photos(int idx,String photo,String country, String continent, String addr, String eng_addr, String latitude, String longitude, String m_color, int trans_idx, String hour, String min, String route_comment) {

            this.idx = idx;
            this.photo = photo;
            this.country = country;
            this.continent = continent;
            this.addr = addr;
            this.eng_addr = eng_addr;
            this.latitude = latitude;
            this.longitude = longitude;
            this.m_color = m_color;
            this.trans_idx = trans_idx;
            this.hour = hour;
            this.min = min;
            this.route_comment = route_comment;

        }
        public Photos(Parcel in){
            readFromParcel(in);
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeInt(idx);
            dest.writeString(photo);
            dest.writeString(country);
            dest.writeString(continent);
            dest.writeString(addr);
            dest.writeString(eng_addr);
            dest.writeString(latitude);
            dest.writeString(longitude);
            dest.writeString(m_color);
            dest.writeInt(trans_idx);
            dest.writeString(hour);
            dest.writeString(min);
            dest.writeString(route_comment);
        }

        public void readFromParcel(Parcel in){
            idx = in.readInt();
            photo = in.readString();
            country = in.readString();
            continent = in.readString();
            addr = in.readString();
            eng_addr = in.readString();
            latitude = in.readString();
            longitude = in.readString();
            m_color = in.readString();
            trans_idx = in.readInt();
            hour = in.readString();
            min = in.readString();
            route_comment = in.readString();
        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
            public Photos createFromParcel(Parcel in) {
                return new Photos(in);
            }

            public Photos[] newArray(int size) {
                return new Photos[size];
            }
        };

    }

    public static class Comments{
        public int idx;
        public String comment;
        public String regdate;
        public String name;
    }

    public static MainListData mainListJson(MainListData data, JSONObject json) throws Exception{
        if(data == null) data = new MainListData();

        if (!json.isNull("profile_photo"))       data.profile_photo=json.getString("profile_photo");
        if (!json.isNull("comment"))              data.comment=json.getString("comment");
        if (!json.isNull("update"))               data.update=json.getString("update");
        if (!json.isNull("country"))              data.country=json.getString("country");
        if (!json.isNull("continent"))            data.continent=json.getString("continent");
        if (!json.isNull("latitude"))             data.latitude=json.getString("latitude");
        if (!json.isNull("longitude"))            data.longitude=json.getString("longitude");
        if (!json.isNull("name"))                  data.name=json.getString("name");
        if (!json.isNull("user_idx"))             data.user_idx=json.getInt("user_idx");
        if (!json.isNull("trip_uid"))             data.trip_uid=json.getString("trip_uid");
        if (!json.isNull("regdate"))               data.regdate=json.getString("regdate");

        /* if (!json.isNull("regdate")) {
            data.regdate = json.getString("regdate");
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            date.setDate(date.getDate()-1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",java.util.Locale.getDefault());
            Date date1 = dateFormat.parse(data.regdate);
            duration = date.getTime()-date1.getTime();
            data.time1 = Long.toString(duration);

          }*/

        if(!json.isNull("photos")){

            JSONArray photoJsonArr = json.getJSONArray("photos");
            data.photoArr = new ArrayList<Photos>();
            Photos photos;

            for (int i = 0 ; i < photoJsonArr.length();i++ ){
                JSONObject obj = photoJsonArr.getJSONObject(i);

                photos = new Photos();
                photos.idx = obj.getInt("idx");
                photos.photo = obj.getString("photo");
                photos.country = obj.getString("country");
                photos.continent = obj.getString("continent");
                photos.addr = obj.getString("addr");
                photos.eng_addr = obj.getString("eng_addr");
                photos.latitude = obj.getString("latitude");
                photos.longitude = obj.getString("longitude");
                photos.m_color = obj.getString("m_color");
                photos.trans_idx = obj.getInt("trans_idx");
                photos.hour = obj.getString("hour");
                photos.min = obj.getString("min");
                photos.route_comment = obj.getString("route_comment");

                data.photoArr.add(photos);
            }
        }

        if (!json.isNull("addr"))                 data.addr=json.getString("addr");
        if (!json.isNull("eng_addr"))            data.eng_addr=json.getString("eng_addr");
        if (!json.isNull("liker_name"))          data.liker_name=json.getString("liker_name");
        if (!json.isNull("like_regdate"))        data.like_regdate=json.getString("like_regdate");

        if(!json.isNull("comments")){

            JSONArray photoJsonArr = json.getJSONArray("comments");
            data.commentArr = new ArrayList<Comments>();
            Comments comment;

            for (int i = 0 ; i < photoJsonArr.length();i++ ){
                JSONObject obj = photoJsonArr.getJSONObject(i);

                comment = new Comments();
                comment.idx = obj.getInt("idx");
                comment.comment = obj.getString("comment");
                comment.regdate = obj.getString("regdate");
                comment.name = obj.getString("name");

                data.commentArr.add(comment);
            }
        }

        if (!json.isNull("like_idx"))            data.like_idx=json.getString("like_idx");
        if (!json.isNull("reply_comment_idx"))  data.reply_comment_idx=json.getInt("reply_comment_idx");
        if (!json.isNull("bookmark_idx"))        data.bookmark_idx=json.getString("bookmark_idx");
        if (!json.isNull("like_count"))          data.like_count=json.getInt("like_count");
        if (!json.isNull("disLike_count"))      data.dislike_count=json.getInt("disLike_count");
        if (!json.isNull("reply_comment_count")) data.reply_comment_count=json.getInt("reply_comment_count");
        if (!json.isNull("isAuth"))              data.isAuth=json.getString("isAuth");
        if (!json.isNull("isLike"))              data.isLike=json.getString("isLike");
        if (!json.isNull("isDisLike"))          data.isDislike=json.getString("isDisLike");
        if (!json.isNull("isComment"))           data.isComment=json.getString("isComment");
        if (!json.isNull("isBookmark"))          data.isBookmark=json.getString("isBookmark");
        if (!json.isNull("type"))                data.type=json.getString("type");
        if (!json.isNull("zoom_level"))         data.zoom_level=json.getInt("zoom_level");
        if (!json.isNull("bookmark_count"))     data.bookmark_count=json.getInt("bookmark_count");
       return data;
    }
}
