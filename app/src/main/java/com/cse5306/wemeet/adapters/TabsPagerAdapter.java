package com.cse5306.wemeet.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cse5306.wemeet.fragments.HostMeetingFragment;
import com.cse5306.wemeet.fragments.JoinedMeetingFragment;

/**
 * Created by Sathvik on 17/04/15.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0){
            return new HostMeetingFragment();
        }else if(position == 1){
            return new JoinedMeetingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position == 0){
            title = "Host meetings";
        }else if(position == 1){
            title = "Your meetings";
        }
        return title;
    }
}
