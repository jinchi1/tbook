package com.travelbooks3.android.activity.write;

import android.content.Context;
import android.database.DataSetObserver;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.travelbooks3.android.R;

import java.util.ArrayList;

/**
 * Created by system777 on 2017-09-05.
 */

public class MarkerAdapter extends BaseAdapter {

    Context _context;
    ArrayList<Marker> _list;

    public MarkerAdapter(Context context, ArrayList<Marker> list)
    {
        _context = context;
        _list = list;
    }

    @Override
    public int getCount() {
        return (_list == null)?0:   _list.size();
    }

    @Override
    public Object getItem(int position) {
        return _list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.marker_spinner_dropdown,parent,false);

        }
        Marker marker = _list.get(position);

        TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
        ImageView imgView = (ImageView)view.findViewById(R.id.imageView);
        txtTitle.setText(marker.get_name());
        imgView.setImageResource(marker.get_flatId());

        return view;

    }
}
