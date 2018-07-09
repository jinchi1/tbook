package com.travelbooks3.android.activity.write;

import android.media.ExifInterface;
import android.widget.TextView;

/**
 * Created by system777 on 2017-07-31.
 */

//사진정보가져올코드로 쓰일예정

public class Exif {


    private void getExif(ExifInterface exif) {

        String myAttribute = "[Exif information] \n\n";

        myAttribute += getTagString(ExifInterface.TAG_DATETIME, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE,
                exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE,
                exif);
        myAttribute += getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_LENGTH,
                exif);
        myAttribute += getTagString(ExifInterface.TAG_IMAGE_WIDTH,
                exif);
        myAttribute += getTagString(ExifInterface.TAG_MAKE, exif);
        myAttribute += getTagString(ExifInterface.TAG_MODEL, exif);
        myAttribute += getTagString(ExifInterface.TAG_ORIENTATION,
                exif);
        myAttribute += getTagString(ExifInterface.TAG_WHITE_BALANCE,
                exif);

    }

    private String getTagString(String tag, ExifInterface exif) {
        return (tag + " : " + exif.getAttribute(tag) + "\n");
    }



}
