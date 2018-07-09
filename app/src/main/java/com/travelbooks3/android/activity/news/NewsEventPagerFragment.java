package com.travelbooks3.android.activity.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travelbooks3.android.R;

/**
 * Created by system777 on 2017-06-24.
 */

public class NewsEventPagerFragment extends Fragment{
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_news_pager_event, container, false);

        }
        return view;
    }
}
