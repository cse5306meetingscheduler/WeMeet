package com.cse5306.wemeet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cse5306.wemeet.R;

/**
 * Created by Sathvik on 12/04/15.
 */
public class HostMeetingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.host_meetings_fragment, container, false);

        return rootView;
    }
}
