package com.example.schoolpa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.schoolpa.fragment.HomeFragment;
import com.example.schoolpa.fragment.ScheduleFragment;

/**
 * 标签适配器
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        //初始化公文通界面
        {

            return HomeFragment.newInstance();
        }
        if (position == 1)
            //初始化课表界面
            return ScheduleFragment.newInstance(6);
        else
            return ScheduleFragment.newInstance(6);
    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "来聊";
            case 1:
                return "课表";
            case 2:
                return "公文";
        }
        return null;
    }
}

