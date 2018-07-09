package com.travelbooks3.android.activity.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.news.adapter.NewsViewPagerAdapter;
import com.travelbooks3.android.activity.search.PlacePagerFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by system777 on 2017-06-20.
 */

public class NewsFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_news, container, false);

            ViewPager newsViewPager = (ViewPager) view.findViewById(R.id.news_viewPager);

            NewsViewPagerAdapter newPagerAdapter = new NewsViewPagerAdapter(getChildFragmentManager());
            newPagerAdapter.addFragment(new FollowingPagerFragment(), getString(R.string.txt_1012));
            newPagerAdapter.addFragment(new NewPagerFragment(), getString(R.string.txt_1013));
            newPagerAdapter.addFragment(new NewsEventPagerFragment(), getString(R.string.txt_1014));
            newsViewPager.setAdapter(newPagerAdapter);
            newsViewPager.setCurrentItem(0);
            newsViewPager.setOffscreenPageLimit(3);
            newPagerAdapter.notifyDataSetChanged();
            TabLayout tab_lay = (TabLayout) view.findViewById(R.id.news_tabLay);
            tab_lay.setupWithViewPager(newsViewPager);

        }
        return view;
    }
}
