package com.travelbooks3.android.activity.info.model;

import org.json.JSONObject;

/**
 * Created by system777 on 2017-09-25.
 */

public class Mapdata {

    String idx;
    String trip_uid;
    String regdate;
    String country;
    String latitude;
    String longitude;


    public static Mapdata MapdataListJson(Mapdata data, JSONObject json) throws Exception {

        if(data == null) data = new Mapdata();
        if (!json.isNull("idx"))       data.idx=json.getString("idx");
        if (!json.isNull("trip_uid"))       data.trip_uid=json.getString("trip_uid");
        if (!json.isNull("regdate"))       data.regdate=json.getString("regdate");
        if (!json.isNull("country"))       data.country=json.getString("country");
        if (!json.isNull("latitude"))       data.latitude=json.getString("latitude");
        if (!json.isNull("longitude"))       data.longitude=json.getString("longitude");




        return data;
    }


}
