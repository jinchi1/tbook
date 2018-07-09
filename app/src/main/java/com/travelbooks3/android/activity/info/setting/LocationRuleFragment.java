package com.travelbooks3.android.activity.info.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.travelbooks3.android.R;

/**
 * Created by system777 on 2017-11-17.
 */

public class LocationRuleFragment extends Fragment {
    View view;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_location_rule, container, false);
        textView = (TextView) view.findViewById(R.id.privacy_txt);


        return view;
    }
}
