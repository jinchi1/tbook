package com.travelbooks3.android.activity.write;

/**
 * Created by system777 on 2017-09-08.
 */
class Point {
    // 위도
    public double x;
    // 경도
    public double y;
    public String addr;
    // 포인트를 받았는지 여부
    public boolean havePoint;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("x : ");
        builder.append(x);
        builder.append(" y : ");
        builder.append(y);
        builder.append(" addr : ");
        builder.append(addr);

        return builder.toString();
    }
}

