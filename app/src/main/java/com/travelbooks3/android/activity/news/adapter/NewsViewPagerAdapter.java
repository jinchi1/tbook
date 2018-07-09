package com.travelbooks3.android.activity.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by system777 on 2017-06-21.
 */

public class NewsViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mFragmentTitle = new ArrayList<>();



    public NewsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        mFragments.add(fragment);
        mFragmentTitle.add(title);
    }


    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);

    }

    @Override
    public void destroyItem(View container, int position, Object object)
    {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitle.get(position);
    }
}