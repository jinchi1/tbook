package com.travelbooks3.android.activity.news.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.travelbooks3.android.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by system777 on 2017-08-09.
 */

public class FollowingPagerData {


    public String trip_uid;
    public String regdate;
    public String type;
    public String following_user_name;
    public int target_user_idx;
    public String target_user_name;
    public int following_user_idx;
    public String following_profile_photo;
    public String target_profile_photo;
    public static Post post;
    public static ArrayList<Post> postArr;




    public static class Post {
        public String comment;
        public String upddate;
        public String country;
        public String continent;
        public String latitude;
        public String longitude;
        public String name;
        public int user_idx;
        public String profile_photo;
        public String regdate;
        public String trip_uid;
        public int like_count;
        public int disLike_count;
        public int reply_comment_count;
        public int bookmark_count;
        public String addr;
        public String eng_addr;
        public int zoom_level;
        public String liker_name;
        public String liker_idxs;
        public String like_regdate;
        public String reply_commenter_name;
        public String reply_commenter_idxs;
        public String reply_comment;
        public String reply_comment_regdate;
        public String isAuth;
        public String isLike;
        public String isDisLike;
        public String isComment;
        public String isBookmark;
        public int like_idx;
        public int reply_comment_idx;
        public int bookmark_idx;
        public ArrayList<Photos> photoArr;

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



    }


    public static FollowingPagerData FollowingPagerdataJSON(FollowingPagerData data, JSONObject json) throws  Exception{
        if(data == null) data = new FollowingPagerData();


        if(!json.isNull("trip_uid")) data.trip_uid = json.getString("trip_uid");
        if(!json.isNull("regdate")) data.regdate = json.getString("regdate");
        if(!json.isNull("type")) data.type = json.getString("type");
        if(!json.isNull("following_user_name")) data.following_user_name = json.getString("following_user_name");
        if(!json.isNull("target_user_idx")) data.target_user_idx = json.getInt("target_user_idx");
        if(!json.isNull("target_user_name")) data.target_user_name = json.getString("target_user_name");
        if(!json.isNull("following_user_idx")) data.following_user_idx = json.getInt("following_user_idx");
        if(!json.isNull("following_profile_photo")) data.following_profile_photo = json.getString("following_profile_photo");
        if(!json.isNull("target_profile_photo")) data.target_profile_photo = json.getString("target_profile_photo");


        if(!json.isNull("post")){

            post = new Post();
            postArr = new ArrayList<Post>();
            JSONObject obj = json.getJSONObject("post");
            //Log.i("testTag1234",obj.getString("comment"));

            if(!obj.isNull("comment")) post.comment = obj.getString("comment");
            if(!obj.isNull("upddate"))  post.upddate = obj.getString("update");
            if(!obj.isNull("country"))post.country = obj.getString("country");
            if(!obj.isNull("continent")) post.continent = obj.getString("continent");
            if(!obj.isNull("latitude")) post.latitude = obj.getString("latitude");
            if(!obj.isNull("longitude"))  post.longitude = obj.getString("longitude");
            if(!obj.isNull("name"))  post.name = obj.getString("name");
            if(!obj.isNull("user_idx")) post.user_idx = obj.getInt("user_idx");
            if(!obj.isNull("profile_photo")) post.profile_photo = obj.getString("profile_photo");
            if(!obj.isNull("regdate"))  post.regdate = obj.getString("regdate");
            if(!obj.isNull("trip_uid"))  post.trip_uid = obj.getString("trip_uid");
            if(!obj.isNull("like_count")) post.like_count = obj.getInt("like_count");
            if(!obj.isNull("disLike_count")) post.disLike_count = obj.getInt("disLike_count");
            if(!obj.isNull("reply_comment_count"))  post.reply_comment_count = obj.getInt("reply_comment_count");
            if(!obj.isNull("bookmark_count"))   post.bookmark_count = obj.getInt("bookmark_count");
            if(!obj.isNull("addr"))  post.addr = obj.getString("addr");
            if(!obj.isNull("eng_addr"))  post.eng_addr = obj.getString("eng_addr");
            if(!obj.isNull("zoom_level"))  post.zoom_level = obj.getInt("zoom_level");
            if(!obj.isNull("liker_name"))  post.liker_name = obj.getString("liker_name");
            if(!obj.isNull("liker_idxs"))  post.liker_idxs = obj.getString("liker_idxs");
            if(!obj.isNull("like_regdate"))  post.like_regdate = obj.getString("like_regdate");
            if(!obj.isNull("reply_commenter_name"))  post.reply_commenter_name = obj.getString("reply_commenter_name");
            if(!obj.isNull("reply_commenter_idxs"))  post.reply_commenter_idxs = obj.getString("reply_commenter_idxs");
            if(!obj.isNull("reply_comment"))  post.reply_comment = obj.getString("reply_comment");
            if(!obj.isNull("reply_comment_regdate"))   post.reply_comment_regdate = obj.getString("reply_comment_regdate");
            if(!obj.isNull("isAuth"))   post.isAuth = obj.getString("isAuth");
            if(!obj.isNull("isLike"))  post.isLike = obj.getString("isLike");
            if(!obj.isNull("isDisLike"))  post.isDisLike = obj.getString("isDisLike");
            if(!obj.isNull("isComment"))  post.isComment = obj.getString("isComment");
            if(!obj.isNull("isBookmark"))  post.isBookmark = obj.getString("isBookmark");
            if(!obj.isNull("like_idx"))  post.like_idx = obj.getInt("like_idx");
            if(!obj.isNull("reply_comment_idx"))  post.reply_comment_idx = obj.getInt("reply_comment_idx");
            if(!obj.isNull("bookmark_idx"))  post.bookmark_idx = obj.getInt("bookmark_idx");
            if(!obj.isNull("photos")){

                    JSONArray photoJsonArr = obj.getJSONArray("photos");
                    post.photoArr = new ArrayList<Post.Photos>();
                    Post.Photos photos;

                    for (int j = 0 ; j < photoJsonArr.length();j++ ){
                        JSONObject Photo_obj = photoJsonArr.getJSONObject(j);

                        photos = new Post.Photos();
                        photos.idx = Photo_obj.getInt("idx");
                        photos.photo = Photo_obj.getString("photo");
                        photos.country = Photo_obj.getString("country");
                        photos.continent = Photo_obj.getString("continent");
                        photos.addr = Photo_obj.getString("addr");
                        photos.eng_addr = Photo_obj.getString("eng_addr");
                        photos.latitude = Photo_obj.getString("latitude");
                        photos.longitude = Photo_obj.getString("longitude");
                        photos.m_color = Photo_obj.getString("m_color");
                        photos.trans_idx = Photo_obj.getInt("trans_idx");
                        photos.hour = Photo_obj.getString("hour");
                        photos.min = Photo_obj.getString("min");
                        photos.route_comment = Photo_obj.getString("route_comment");

                        post.photoArr.add(photos);

                        Log.i("phototest",photos.photo);
                    }
                }

        }

        return data;
    }

}

