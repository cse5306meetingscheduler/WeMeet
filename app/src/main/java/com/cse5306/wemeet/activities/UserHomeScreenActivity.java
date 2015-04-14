package com.cse5306.wemeet.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.fragments.HostMeetingFragment;
import com.cse5306.wemeet.fragments.JoinedMeetingFragment;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.cse5306.wemeet.tasks.JoinMeetingTask;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;


public class UserHomeScreenActivity extends ActionBarActivity implements ActionBar.TabListener {

    String joinGrpId = null;
    ActionBar actionBar;
    ViewPager mViewPager;
    TabsPagerAdapter tabsPagerAdapter;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton mFloatingActionCreateMeeting,mFloatingActionJoinMeeting;
    UserPreferences userPreferences;
    private String[] tabs = { "Host meetings","Your Meetings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);

        userPreferences = new UserPreferences(getApplicationContext());
        Toast.makeText(getApplicationContext(),userPreferences.getUserPrefHomeLocation(),Toast.LENGTH_SHORT).show();

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_menu);
        mFloatingActionCreateMeeting = (FloatingActionButton) findViewById(R.id.home_create_meeting_button);
        mFloatingActionJoinMeeting = (FloatingActionButton) findViewById(R.id.home_join_meeting_button);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();

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

        // Initializing action bar tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(userPreferences.getSessionUserPrefUsername() + " - Your Meetings");
        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tabsPagerAdapter);
       mViewPager.setOnPageChangeListener(onPageChangeListener);
        actionBar.setHomeButtonEnabled(false);
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            actionBar.setSelectedNavigationItem(position);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void promptUserInput() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Group Id");
        alert.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_join_group, null);
        final EditText joinGrpIdEt = (EditText) dialogView.findViewById(R.id.join_grp_id);
        alert.setView(dialogView);

        alert.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                joinGrpId = joinGrpIdEt.getText().toString();
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
        JoinMeetingTask joinMeetingTask = new JoinMeetingTask(userPreferences.getSessionUserPrefUsername(),
                userPreferences.getUserPrefHomeLocation(),
                joinGrpId);
        joinMeetingTask.execute();
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Log.d("onTabUnSel",String.valueOf(tab.getPosition()));
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Log.d("onTabUnReSel",String.valueOf(tab.getPosition()));
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
        }

        return super.onOptionsItemSelected(item);
    }
}

class TabsPagerAdapter extends FragmentStatePagerAdapter {

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
