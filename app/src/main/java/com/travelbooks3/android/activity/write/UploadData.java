package com.travelbooks3.android.activity.write;

import java.util.ArrayList;

/**
 * Created by system777 on 2017-09-05.
 */

public class UploadData {

    public String trip_uid;
    public String comment;
    public ArrayList<photos> photosArrayList;
    public ArrayList<route> routeArrayList;

    public static class photos
    {
        public String path;
        public String idx;
        public String latitude;
        public String longitude;
        public String addr;
        public String country;
        public String m_color;

    }

    public static class route{

        public String trans_idx;
        public String start_photo_idx;
        public String end_photo_idx;
        public String hour;
        public String min;
        public String comment;
    }
}
