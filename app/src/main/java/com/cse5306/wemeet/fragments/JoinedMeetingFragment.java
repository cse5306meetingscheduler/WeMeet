package com.cse5306.wemeet.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.activities.UserHomeScreenActivity;
import com.cse5306.wemeet.objects.MeetingDetails;

import java.util.List;

/**
 * Created by Sathvik on 12/04/15.
 */
public class JoinedMeetingFragment extends Fragment {

    List<MeetingDetails> meetingDetailsList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.your_meetings_fragment, container, false);

        UserHomeScreenActivity userHomeScreenActivity = (UserHomeScreenActivity)getActivity();
        meetingDetailsList = userHomeScreenActivity.getMeetingDetailsList();

        TextView textView = new TextView(getActivity());
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(40);
        textView.setText(String.valueOf(meetingDetailsList.size()));


        return rootView;
    }
}
