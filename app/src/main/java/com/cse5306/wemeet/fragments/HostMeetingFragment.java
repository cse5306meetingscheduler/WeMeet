package com.cse5306.wemeet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.activities.UserHomeScreenActivity;
import com.cse5306.wemeet.adapters.MeetingListAdapter;
import com.cse5306.wemeet.objects.MeetingDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sathvik on 12/04/15.
 */
public class HostMeetingFragment extends Fragment {

    List<MeetingDetails> meetingDetailsList,meetingsYouHost;
    LinearLayout mHostMeetingsProgress, mHostMeetingsNoMeetings;
    ListView hostMeetingListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.host_meetings_fragment, container, false);

        UserHomeScreenActivity userHomeScreenActivity = (UserHomeScreenActivity)getActivity();
        meetingDetailsList = userHomeScreenActivity.getMeetingDetailsList();
        meetingsYouHost = new ArrayList<MeetingDetails>();

        hostMeetingListView = (ListView) rootView.findViewById(R.id.host_meetings_list);
        mHostMeetingsProgress = (LinearLayout) rootView.findViewById(R.id.host_meetings_lin_layout_progress);
        mHostMeetingsNoMeetings= (LinearLayout) rootView.findViewById(R.id.host_meetings_lin_layout_no_meetings);

        // get user host meeting list
        for(int i=0;i<meetingDetailsList.size();i++){
            if(meetingDetailsList.get(i).isHost()){
                meetingsYouHost.add(meetingDetailsList.get(i));
            }
        }
        Toast.makeText(getActivity(),"Host"+String.valueOf(meetingsYouHost.size()),Toast.LENGTH_SHORT).show();

        MeetingListAdapter adapter = new MeetingListAdapter(meetingsYouHost,getActivity());
        hostMeetingListView.setAdapter(adapter);

        return rootView;
    }
}
