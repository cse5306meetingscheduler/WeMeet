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
public class JoinedMeetingFragment extends Fragment {

    List<MeetingDetails> meetingDetailsList,meetingsYouJoined;
    LinearLayout mJoinMeetingsProgress, mJoinMeetingsNoMeetings;
    ListView yourMeetingsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.your_meetings_fragment, container, false);

        UserHomeScreenActivity userHomeScreenActivity = (UserHomeScreenActivity)getActivity();
        meetingDetailsList = userHomeScreenActivity.getMeetingDetailsList();
        meetingsYouJoined = new ArrayList<MeetingDetails>();

        yourMeetingsListView = (ListView) rootView.findViewById(R.id.your_meetings_list);
        mJoinMeetingsProgress = (LinearLayout) rootView.findViewById(R.id.your_meetings_lin_layout_progress);
        mJoinMeetingsNoMeetings = (LinearLayout) rootView.findViewById(R.id.your_meetings_lin_layout_no_meetings);

        // get user meeting list
        for(int i=0;i<meetingDetailsList.size();i++){
            if(!meetingDetailsList.get(i).isHost()){
                meetingsYouJoined.add(meetingDetailsList.get(i));
            }
        }

        Toast.makeText(getActivity(), "your"+String.valueOf(meetingsYouJoined.size()), Toast.LENGTH_SHORT).show();

        MeetingListAdapter adapter = new MeetingListAdapter(meetingsYouJoined,getActivity());
        yourMeetingsListView.setAdapter(adapter);

        return rootView;
    }
}
