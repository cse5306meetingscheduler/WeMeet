package com.cse5306.wemeet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.github.clans.fab.FloatingActionButton;


public class UserHomeScreenActivity extends ActionBarActivity {

    LinearLayout mLinLayoutProgress,mLinLayoutNoMeetings;
    ListView mMeetingsList;
    FloatingActionButton mFloatingActionCreateMeeting,mFloatingActionJoinMeeting;
    UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);

        userPreferences = new UserPreferences(getApplicationContext());
        Toast.makeText(getApplicationContext(),userPreferences.getSessionUserPrefUsername(),Toast.LENGTH_SHORT).show();

        mLinLayoutProgress = (LinearLayout) findViewById(R.id.home_lin_layout_progress);
        mLinLayoutNoMeetings = (LinearLayout) findViewById(R.id.home_lin_layout_no_meetings);
        mMeetingsList = (ListView) findViewById(R.id.home_list_view);
        mFloatingActionCreateMeeting = (FloatingActionButton) findViewById(R.id.home_create_meeting_button);
        mFloatingActionJoinMeeting = (FloatingActionButton) findViewById(R.id.home_join_meeting_button);

        mFloatingActionCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creatingMeetingIntent = new Intent(UserHomeScreenActivity.this,CreateMeetingActivity.class);
                startActivity(creatingMeetingIntent);
            }
        });

        mFloatingActionJoinMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // alert dailog
            }
        });



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
