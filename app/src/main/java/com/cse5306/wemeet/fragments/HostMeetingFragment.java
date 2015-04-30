package com.cse5306.wemeet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.activities.MeetingDetailsActivity;
import com.cse5306.wemeet.activities.UserHomeScreenActivity;
import com.cse5306.wemeet.adapters.MeetingListAdapter;
import com.cse5306.wemeet.objects.MeetingDetails;
import com.cse5306.wemeet.tasks.DeleteGroupTask;
import com.cse5306.wemeet.tasks.DeleteGroupTaskResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that  displays user hosted meeting list
 */
public class HostMeetingFragment extends Fragment implements DeleteGroupTaskResponse {

    List<MeetingDetails> meetingDetailsList,meetingsYouHost;
    LinearLayout mHostMeetingsProgress, mHostMeetingsNoMeetings;
    ListView hostMeetingListView;
    MeetingListAdapter adapter;
    MeetingDetails tempMeetingDetailsObj;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.long_press_action,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.delete_action){
            tempMeetingDetailsObj = meetingsYouHost.get(info.position);
            DeleteGroupTask deleteGroupTask = new DeleteGroupTask(String.valueOf(meetingsYouHost.get(info.position).getGroupId()));
            deleteGroupTask.response = this;
            deleteGroupTask.execute();
        }

        return true;
    }

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
        registerForContextMenu(hostMeetingListView);

        // get user host meeting list
        for(int i=0;i<meetingDetailsList.size();i++){
            if(meetingDetailsList.get(i).isHost()){
                meetingsYouHost.add(meetingDetailsList.get(i));
            }
        }

        if(meetingsYouHost.size() == 0){
            mHostMeetingsNoMeetings.setVisibility(View.VISIBLE);
        }else{
            adapter = new MeetingListAdapter(meetingsYouHost,getActivity());
            hostMeetingListView.setAdapter(adapter);

            hostMeetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(meetingsYouHost.get(position).getMidpoint().equalsIgnoreCase("null")){
                        Toast.makeText(getActivity(),"Other group members have not yet joined",Toast.LENGTH_LONG).show();
                    }else if(!meetingsYouHost.get(position).getMidpoint().equalsIgnoreCase("null") &&
                            meetingsYouHost.get(position).getFinalDestination().equalsIgnoreCase("null")) {
                        Intent meetingDetails = new Intent(getActivity(), MeetingDetailsActivity.class);
                        meetingDetails.putExtra("fragmentInflateType","suggest_list");
                        meetingDetails.putExtra("groupId",String.valueOf(meetingsYouHost.get(position).getGroupId()));
                        startActivity(meetingDetails);
                    }else if(!meetingsYouHost.get(position).getMidpoint().equalsIgnoreCase("null") &&
                            !meetingsYouHost.get(position).getFinalDestination().equalsIgnoreCase("null")){
                        Intent meetingDetails = new Intent(getActivity(), MeetingDetailsActivity.class);
                        meetingDetails.putExtra("fragmentInflateType","map");
                        meetingDetails.putExtra("groupId",String.valueOf(meetingsYouHost.get(position).getGroupId()));
                        startActivity(meetingDetails);
                    }

                }
            });
        }


        return rootView;
    }

    @Override
    public void processFinish(String output) {
        try{
           JSONObject jsonObject = new JSONObject(output.toString());
            Log.d("res", output);
           if(jsonObject.getInt("success") == 0){
                Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
           }else if(jsonObject.getInt("success") == 1){
               meetingsYouHost.remove(tempMeetingDetailsObj);
               if(meetingsYouHost.size() == 0){
                   mHostMeetingsNoMeetings.setVisibility(View.VISIBLE);
               }
               adapter.notifyDataSetChanged();
               Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
           }
        }catch(JSONException e) {
            e.printStackTrace();
        }
    }
}
