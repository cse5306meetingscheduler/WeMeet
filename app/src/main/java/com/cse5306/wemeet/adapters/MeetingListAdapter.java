package com.cse5306.wemeet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.objects.MeetingDetails;
import com.cse5306.wemeet.views.CircleText;

import java.util.List;

/**
 * Created by Sathvik on 17/04/15.
 */
public class MeetingListAdapter extends BaseAdapter {

    List<MeetingDetails> meetingDetailsList;
    Context context;

    public MeetingListAdapter(List<MeetingDetails> meetingDetailsList, Context context) {
        this.context = context;
        this.meetingDetailsList = meetingDetailsList;
    }

    @Override
    public int getCount() {
        return meetingDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.meeting_list_item,null);

        CircleText meetingGroupId = (CircleText) convertView.findViewById(R.id.meet_list_item_group_id);
        TextView meetingDate = (TextView) convertView.findViewById(R.id.meet_list_item_date);
        TextView meetingTime = (TextView) convertView.findViewById(R.id.meet_list_item_time);
        TextView meetingPoint = (TextView) convertView.findViewById(R.id.meet_list_item_meeting_point);
        TextView meetingPlace = (TextView) convertView.findViewById(R.id.meet_list_item_meeting_place);


        meetingGroupId.setmText(String.valueOf(meetingDetailsList.get(position).getGroupId()));
        meetingGroupId.setmColor(getBgColor(meetingDetailsList.get(position).getGroupId()));

        meetingDate.setText(meetingDetailsList.get(position).getMeetingDate());
        meetingTime.setText(meetingDetailsList.get(position).getMeetingTime());

        if(meetingDetailsList.get(position).getMidpoint().equalsIgnoreCase("null")){
            meetingPoint.setText("Status: Waiting for users to join");
            meetingPlace.setVisibility(View.GONE);
        }else if(!meetingDetailsList.get(position).getMidpoint().equalsIgnoreCase("null") &&
                meetingDetailsList.get(position).getFinalDestination().equalsIgnoreCase("null")){
            meetingPoint.setText("Status: Click to choose your choice location");
            meetingPlace.setVisibility(View.GONE);
        }else if(!meetingDetailsList.get(position).getFinalDestination().equalsIgnoreCase("null")){
            meetingPoint.setVisibility(View.GONE);
            meetingPlace.setVisibility(View.VISIBLE);
            meetingPlace.setText("Click to see final meeting point");
        }



        return convertView;
    }

    private  int getBgColor(int mod){
        mod = mod % 5;
        switch (mod){
            case 0:
                return context.getResources().getColor(R.color.mod1);
            case 1:
                return context.getResources().getColor(R.color.mod2);
            case 2:
                return context.getResources().getColor(R.color.mod3);
            case 3:
                return context.getResources().getColor(R.color.mod4);
            case 4:
                return context.getResources().getColor(R.color.mod5);
            default:
                return context.getResources().getColor(R.color.mod4);
        }
    }
}
