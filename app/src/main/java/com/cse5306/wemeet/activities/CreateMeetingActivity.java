package com.cse5306.wemeet.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.cse5306.wemeet.tasks.GetCurrentLocationTask;

import java.util.Calendar;

public class CreateMeetingActivity extends ActionBarActivity {

 /*   create_meet_radio_curr_loc
            create_meet_radio_home_loc
    create_meet_location_set*/
    ProgressBar mCMProgressBar;
    ScrollView mCMForm;
    LinearLayout mCMErrorLinLayout;
    TextView mCMErrorTv,mCMLocatinSelectedTv;
    EditText mCMMaxPpl;
    public static EditText mCMDate,mCMTime;
    Button mCMBtn;
    ImageButton mCMTimePicker,mCMDatePicker;
    String mlocationString = null;
    public static String  datePicked = null, timePicked = null;
    RadioGroup mCMRadioGrp;
    UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        mCMProgressBar =(ProgressBar) findViewById(R.id.create_meet_progress);
        mCMForm = (ScrollView) findViewById(R.id.create_meeting_form);
        mCMErrorLinLayout = (LinearLayout) findViewById(R.id.create_meet_error_lin_layout);
        mCMErrorTv = (TextView) findViewById(R.id.create_meet_screen_error_tv);
        mCMMaxPpl = (EditText) findViewById(R.id.create_meet_max_ppl);
        mCMDate = (EditText) findViewById(R.id.create_meet_date);
        mCMTime= (EditText) findViewById(R.id.create_meet_time);
        mCMBtn = (Button) findViewById(R.id.create_meet_btn);
        mCMDatePicker = (ImageButton) findViewById(R.id.create_meet_date_picker_btn);
        mCMTimePicker  = (ImageButton) findViewById(R.id.create_meet_time_picker_btn);
        mCMRadioGrp = (RadioGroup) findViewById(R.id.create_meet_radio_grp);
        mCMLocatinSelectedTv = (TextView) findViewById(R.id.create_meet_location_set);

        userPreferences = new UserPreferences(getApplicationContext());
        mCMLocatinSelectedTv.setText(userPreferences.getUserPrefHomeLocation());


        mCMDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        mCMTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        mCMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCreateMeeting();
            }
        });

        mCMRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.create_meet_radio_home_loc){
                    mCMLocatinSelectedTv.setText(userPreferences.getUserPrefHomeLocation());
                }else if(checkedId == R.id.create_meet_radio_curr_loc){
                    mCMLocatinSelectedTv.setText("Retrieving location..");
                    getUserLocation();
                }

            }
        });
    }

    public void attemptCreateMeeting(){
        if(valifdateInput()){

        }

    };


    private boolean valifdateInput(){

        if(Integer.parseInt(mCMMaxPpl.getText().toString()) < 2){
            mCMMaxPpl.setError("Max number of people should be greater than 1");
            return false;
        }else if(datePicked == null && datePicked.equals(mCMDate.getText().toString())){
            mCMDate.setError("Date not set");
            return false;
        }else if(timePicked == null && timePicked.equalsIgnoreCase(mCMTime.getText().toString())){
            mCMTime.setError("Time not set");
            return false;
        }else if(mlocationString == null){
            showError(true,"Location not set");
            return false;
        }

        return  true;
    }

    private void showDatePickerDialog(View v){
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");

    }

    private void showTimePickerDialog(View v){
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("sa",data.getStringExtra("DATE_PICKED"));

    }

    private void showError(boolean show,String message){
        mCMErrorLinLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mCMErrorTv.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_meeting, menu);
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            datePicked = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
            mCMDate.setText(datePicked);
        }

    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            timePicked = String.valueOf(hourOfDay)+":"+String.valueOf(minute);
            mCMTime.setText(timePicked);
        }

    }

    private void getUserLocation(){
        GetCurrentLocationTask gps;
        gps = new GetCurrentLocationTask(CreateMeetingActivity.this);
        if(gps.canGetLocation() && gps.getLocation() != null){
            //Log.d("Reg", "loc Started");
            showError(false, "");
            mCMLocatinSelectedTv.setVisibility(View.VISIBLE);
            mlocationString = String.valueOf(gps.getLocation().getLatitude())+","+String.valueOf(gps.getLocation().getLongitude());
            mCMLocatinSelectedTv.setText("Your current location co-ordinates: "+mlocationString);
        }else if(!gps.canGetLocation()){
            showError(true,"Turn on Location services");
            mCMLocatinSelectedTv.setText("Error");
            mlocationString = null;
        }else if(gps.getLocation() == null){
            showError(true,"Turn on Internet and Location services");
            mCMLocatinSelectedTv.setText("Error");
            mlocationString = null;
        }
    }


}

