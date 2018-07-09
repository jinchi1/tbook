package com.travelbooks3.android.activity.write;

/**
 * Created by system777 on 2017-09-06.
 */

public class Marker {

    private String _name;
    private int _flatId;

    public Marker(String name, int flatId)
    {
        _name = name;
        _flatId = flatId;
    }

    public String get_name() {
        return _name;
    }

    public  int get_flatId() {
        return _flatId;
    }

    public void set_flatId(int _flatId) {
        this._flatId = _flatId;
    }

}
