package com.travelbooks3.android.activity.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.travelbooks3.android.R;
import com.travelbooks3.android.util.LogUtil;

/**
 * Created by system777 on 2017-08-10.
 */

public class MapPagerFragment extends Fragment {
    private View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

                LogUtil.d("지도페이지");
        if(view == null){
            view = inflater.inflate(R.layout.fragment_search2, container, false);


        }
        return view;
    }
}
