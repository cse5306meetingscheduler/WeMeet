package com.cse5306.wemeet.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.cse5306.wemeet.tasks.CreateMeetingTask;
import com.cse5306.wemeet.tasks.CreateMeetingTaskResponse;
import com.cse5306.wemeet.tasks.GetCurrentLocationTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
* This activity lets user create a new meeting
* */

public class CreateMeetingActivity extends ActionBarActivity implements CreateMeetingTaskResponse{

    ProgressBar mCMProgressBar;
    ScrollView mCMForm;
    String checkList="";
    Toolbar create_meeting_toolbar;
    List<String> mCheckPref;
    LinearLayout mCMErrorLinLayout;
    TextView mCMErrorTv,mCMLocatinSelectedTv;
    NumberPicker mCMMaxPpl;
    public static EditText mCMDate,mCMTime;
    Button mCMBtn;
    ImageButton mCMTimePicker,mCMDatePicker;
    String mlocationString = null;
    public static String  datePicked = null, timePicked = null;
    RadioGroup mCMRadioGrp;
    UserPreferences userPreferences;
    CheckBox mCheckRestaurant, mCheckBar, mCheckLibrary,mCheckCafe,mCheckMall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        create_meeting_toolbar = (Toolbar) findViewById(R.id.create_meeting_toolbar);
        setSupportActionBar(create_meeting_toolbar);

        mCMProgressBar =(ProgressBar) findViewById(R.id.create_meet_progress);
        mCMForm = (ScrollView) findViewById(R.id.create_meeting_form);
        mCMErrorLinLayout = (LinearLayout) findViewById(R.id.create_meet_error_lin_layout);
        mCMErrorTv = (TextView) findViewById(R.id.create_meet_screen_error_tv);
        mCMMaxPpl = (NumberPicker) findViewById(R.id.create_meet_max_ppl);
        mCMMaxPpl.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mCMDate = (EditText) findViewById(R.id.create_meet_date);
        mCMTime= (EditText) findViewById(R.id.create_meet_time);
        mCMBtn = (Button) findViewById(R.id.create_meet_btn);
        mCMDatePicker = (ImageButton) findViewById(R.id.create_meet_date_picker_btn);
        mCMTimePicker  = (ImageButton) findViewById(R.id.create_meet_time_picker_btn);
        mCMRadioGrp = (RadioGroup) findViewById(R.id.create_meet_radio_grp);
        mCMLocatinSelectedTv = (TextView) findViewById(R.id.create_meet_location_set);
        mCheckRestaurant = (CheckBox) findViewById(R.id.check_restaurant);
        mCheckBar = (CheckBox) findViewById(R.id.check_bar);
        mCheckMall= (CheckBox) findViewById(R.id.check_shopping_mall);
        mCheckLibrary = (CheckBox) findViewById(R.id.check_library);
        mCheckCafe = (CheckBox) findViewById(R.id.check_cafe);
        mCheckPref = new ArrayList<String>();

        hideKeyboard();
        mCMMaxPpl.setMinValue(2);
        mCMMaxPpl.setMaxValue(10);

        // get user home location
        userPreferences = new UserPreferences(getApplicationContext());
        if(userPreferences.getUserPrefHomeLocation() != null){
            mlocationString = userPreferences.getUserPrefHomeLocation();
            mCMLocatinSelectedTv.setText(userPreferences.getUserPrefHomeLocation());
        }

        // date picker on click listener
        mCMDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        // time picker on click listener
        mCMTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        // validate input and send request to create meeting
        mCMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCreateMeeting();
            }
        });

        // listener for radio button change for home button and current location button
        mCMRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.create_meet_radio_home_loc){
                    mlocationString = userPreferences.getUserPrefHomeLocation();
                    mCMLocatinSelectedTv.setText(mlocationString);
                }else if(checkedId == R.id.create_meet_radio_curr_loc){
                    mlocationString = null;
                    mCMLocatinSelectedTv.setText("Retrieving location..");
                    // get user location
                    getUserLocation();
                }
            }
        });
    }

    // listeners for check box,
    public   void onCheckChangeListen(View view){
        checkList = "";
        switch (view.getId()){
            case R.id.check_restaurant:
                if(mCheckRestaurant.isChecked()){
                    mCheckPref.add("restaurant");
                }else{
                    mCheckPref.remove("restaurant");
                }
                break;
            case R.id.check_bar:
                if(mCheckBar.isChecked()){
                    mCheckPref.add("bar");
                }else{
                    mCheckPref.remove("bar");
                }
                break;
            case R.id.check_cafe:
                if(mCheckCafe.isChecked()){
                    mCheckPref.add("cafe");
                }else{
                    mCheckPref.remove("cafe");
                }
                break;
            case R.id.check_library:
                if(mCheckLibrary.isChecked()){
                    mCheckPref.add("library");
                }else{
                    mCheckPref.remove("library");
                }
                break;
            case R.id.check_shopping_mall:
                if(mCheckMall.isChecked()){
                    mCheckPref.add("shopping_mall");
                }else{
                    mCheckPref.remove("shopping_mall");
                }
                break;
            default:
                break;
        }
        for(int i=0;i<mCheckPref.size();i++){
            if(mCheckPref.size() == 1){
                checkList = mCheckPref.get(0);
                break;
            }
            checkList += mCheckPref.get(i);
            if(i+1 != mCheckPref.size()){
                checkList += "|";
            }

        }
    }

    // once create meeting task is completed, show the response
    public void attemptCreateMeeting(){
        showError(false,"");
        if(validateInput()){
            mCMProgressBar.setVisibility(View.VISIBLE);
            CreateMeetingTask createMeetingTask = new CreateMeetingTask(datePicked,
                    timePicked,
                    mlocationString,
                    userPreferences.getSessionUserPrefUsername(),
                   mCMMaxPpl.getValue(),
                    checkList);
            createMeetingTask.response = this;
            createMeetingTask.execute();
        }

    };

    // hiding keyboard
    public void hideKeyboard(){
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    // method to validate input
    private boolean validateInput(){

        if(datePicked == null){
            showError(true, "Date not set");
            return false;
        }else if(timePicked == null){
            showError(true, "Time not set");
            return false;
        }else if(mlocationString == null){
            showError(true,"Location not set");
            return false;
        }else if(checkList.length() == 0 || mCheckPref.size()==0){
            showError(true,"Select at-least one meeting place");
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

    // methos to display all errors
    private void showError(boolean show,String message){
        mCMErrorLinLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mCMErrorTv.setText(message);
    }

    // receive result of Creating meeting task
    @Override
    public void processFinish(String output) {
        mCMProgressBar.setVisibility(View.GONE);
        Log.d("Create meeting output",output);
        try{
           JSONObject jsonObject = new JSONObject(output.toString());
           if(jsonObject.getInt("success") == 1){
               datePicked = null;
               timePicked = null;
               showGroupId(jsonObject.getString("message"));
           }
        }catch (Exception e){
            e.printStackTrace();
            showError(true, e.getMessage());
        }


    }

    // Date picker dialog fragment
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

    // Time picker dialog fragment
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

    // method to get user current location
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

    // Alert dialog to display groupId from server
    private void showGroupId(final String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CreateMeetingActivity.this);
        alertDialogBuilder.setTitle("Your group ID");
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Share",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WeMeet");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Hey join my group:"+msg);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }
                })
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

