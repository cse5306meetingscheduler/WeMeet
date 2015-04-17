package com.cse5306.wemeet.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.adapters.TabsPagerAdapter;
import com.cse5306.wemeet.fragments.ZoomOutPageTransformer;
import com.cse5306.wemeet.objects.MeetingDetails;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.cse5306.wemeet.tasks.GetCurrentLocationTask;
import com.cse5306.wemeet.tasks.JoinMeetingTask;
import com.cse5306.wemeet.tasks.JoinMeetingTaskResponse;
import com.cse5306.wemeet.tasks.MeetingListTask;
import com.cse5306.wemeet.tasks.MeetingListTaskResponse;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserHomeScreenActivity extends ActionBarActivity implements JoinMeetingTaskResponse,MeetingListTaskResponse {

    String joinGrpId = null,locationStr = null;
    LinearLayout mHomeScreenLinLayout;
    TextView mHomeScreenResTv;
    ActionBar actionBar;
    ViewPager mViewPager;

    List<MeetingDetails> meetingDetailsList;
    TabsPagerAdapter tabsPagerAdapter;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton mFloatingActionCreateMeeting,mFloatingActionJoinMeeting;
    UserPreferences userPreferences;
    private String[] tabs = { "Host meetings","Your Meetings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_menu);
        mFloatingActionCreateMeeting = (FloatingActionButton) findViewById(R.id.home_create_meeting_button);
        mFloatingActionJoinMeeting = (FloatingActionButton) findViewById(R.id.home_join_meeting_button);
        mHomeScreenLinLayout = (LinearLayout) findViewById(R.id.home_screen_result_ll);
        mHomeScreenResTv = (TextView) findViewById(R.id.home_screen_result_tv);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        meetingDetailsList = new ArrayList<MeetingDetails>();
        userPreferences = new UserPreferences(getApplicationContext());
        //Toast.makeText(getApplicationContext(), userPreferences.getUserPrefHomeLocation(), Toast.LENGTH_SHORT).show();

        actionBar = getSupportActionBar();
        actionBar.setTitle(userPreferences.getSessionUserPrefUsername() + " - Your Meetings");

        mFloatingActionCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
                Intent creatingMeetingIntent = new Intent(UserHomeScreenActivity.this,CreateMeetingActivity.class);
                startActivity(creatingMeetingIntent);
            }
        });

        mFloatingActionJoinMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // alert dailog
                floatingActionMenu.close(true);
                promptUserInput();
            }
        });

        MeetingListTask meetingListTask = new MeetingListTask(userPreferences.getSessionUserPrefUsername());
        meetingListTask.response=this;
        meetingListTask.execute();

    }

    private void promptUserInput() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Group Id");
        alert.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_join_group, null);
        final EditText joinGrpIdEt = (EditText) dialogView.findViewById(R.id.join_grp_id);
        RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.join_meet_radio_grp);
        RadioButton joinHomeLoc = (RadioButton) dialogView.findViewById(R.id.join_meet_radio_home_loc);
        RadioButton joinCurrLoc = (RadioButton) dialogView.findViewById(R.id.join_meet_radio_curr_loc);
        final TextView joinMeetLocSetTv = (TextView) dialogView.findViewById(R.id.join_meet_location_set);
        joinMeetLocSetTv.setText(userPreferences.getUserPrefHomeLocation());
        locationStr = userPreferences.getUserPrefHomeLocation();

        alert.setView(dialogView);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.join_meet_radio_home_loc){
                    locationStr = userPreferences.getUserPrefHomeLocation();
                    joinMeetLocSetTv.setText(userPreferences.getUserPrefHomeLocation());
                }else if(checkedId == R.id.join_meet_radio_curr_loc){
                    joinMeetLocSetTv.setText(getUserLocation());
                }
            }
        });

        alert.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                joinGrpId = joinGrpIdEt.getText().toString();
                Toast.makeText(getApplication(),locationStr,Toast.LENGTH_SHORT).show();
                callJoinMeetingTask();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                joinGrpId = null;
            }
        });
        alert.show();
    }

    private void callJoinMeetingTask() {
        if(joinGrpId != null && locationStr != null){
            JoinMeetingTask joinMeetingTask = new JoinMeetingTask(userPreferences.getSessionUserPrefUsername(),
                    locationStr,
                    joinGrpId);
            joinMeetingTask.execute();
            joinMeetingTask.response = this;
        }else if(locationStr == null){
            showResponse(true, "Location not set");
        }else if(joinGrpId == null){
            showResponse(true, "Group Id not set");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            userPreferences.setUserPrefKeepLogin(false);
            userPreferences.setUserPrefUsername(null);
            userPreferences.setUserPrefPassword(null);
            userPreferences.setSessionUserPrefUsername(null);
            Intent intentLoginIntent = new Intent(this,LoginActivity.class);
            startActivity(intentLoginIntent);
            finish();
            return true;
        }else if(id == R.id.details){
            Intent intent = new Intent(this,MeetingDetailsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String output) {
        locationStr = null;
        joinGrpId = null;
        Log.d("join",output);
        try{
            JSONObject jsonObject = new JSONObject(output.toString());
            if(jsonObject.getInt("success") == 0){
                showResponse(true,jsonObject.getString("message"));
            }else if(jsonObject.getInt("success")==1){
                showResponse(true,jsonObject.getString("message"));
            }
        }catch (Exception e){
            showResponse(true,e.getMessage());
        }finally {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showResponse(false,"");
                }
            }, 4000);
        }

    }

    private void showResponse(final boolean show,final String res){
        mHomeScreenLinLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mHomeScreenResTv.setText(res);
    }

    private String getUserLocation(){
        locationStr = null;
        GetCurrentLocationTask gps;
        gps = new GetCurrentLocationTask(UserHomeScreenActivity.this);
        if(gps.canGetLocation() && gps.getLocation() != null){
            locationStr = String.valueOf(gps.getLocation().getLatitude())+","+String.valueOf(gps.getLocation().getLongitude());
            return String.valueOf(gps.getLocation().getLatitude())+","+String.valueOf(gps.getLocation().getLongitude());
        }else if(!gps.canGetLocation()){
            return "Turn on Location services";
        }else if(gps.getLocation() == null){
            return "Turn on Internet and Location services";
        }
        return "location not set";
    }

    @Override
    public void processFinish(List<MeetingDetails> output) {
        setMeetingDetailsList(output);
        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        mViewPager.setAdapter(tabsPagerAdapter);

    }

    public List<MeetingDetails> getMeetingDetailsList() {
        return meetingDetailsList;
    }

    public void setMeetingDetailsList(List<MeetingDetails> meetingDetailsList) {
        this.meetingDetailsList = meetingDetailsList;
    }
}

