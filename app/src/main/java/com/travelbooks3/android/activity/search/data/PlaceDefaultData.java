package com.travelbooks3.android.activity.search.data;

import org.json.JSONObject;

/**
 * Created by system777 on 2017-09-18.
 */

public class PlaceDefaultData {

    public String idx;
    public String addr;
    public String eng_addr;
    public String latitude;
    public String longitude;


    public static PlaceDefaultData PlaceDefaultJson(PlaceDefaultData data, JSONObject json) throws Exception {
        if (data == null) data = new PlaceDefaultData();

        if (!json.isNull("idx")) data.idx = json.getString("idx");
        if (!json.isNull("addr")) data.addr = json.getString("addr");
        if (!json.isNull("eng_adr")) data.eng_addr = json.getString("eng_adr");
        if (!json.isNull("latitude")) data.latitude = json.getString("latitude");
        if (!json.isNull("longitude")) data.longitude = json.getString("longitude");


        return data;
    }

}