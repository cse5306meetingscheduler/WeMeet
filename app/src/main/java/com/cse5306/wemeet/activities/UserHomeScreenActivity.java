package com.cse5306.wemeet.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cse5306.wemeet.R;


public class UserHomeScreenActivity extends ActionBarActivity {

    LinearLayout mLinLayoutProgress,mLinLayoutNoMeetings;
    ListView mMeetingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);

        mLinLayoutProgress = (LinearLayout) findViewById(R.id.home_lin_layout_progress);
        mLinLayoutNoMeetings = (LinearLayout) findViewById(R.id.home_lin_layout_no_meetings);
        mMeetingsList = (ListView) findViewById(R.id.home_list_view);



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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
