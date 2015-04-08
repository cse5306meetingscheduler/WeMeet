package com.cse5306.wemeet.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.tasks.GetCurrentLocationTask;
import com.cse5306.wemeet.tasks.RegisterTask;
import com.cse5306.wemeet.tasks.RegisterTaskResponse;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class RegisterActivity extends ActionBarActivity implements RegisterTaskResponse {

    EditText mRegEmail,mRegPassword,mRegPhoneNum,mRegUsername;
    Button regUserBtn;
    LinearLayout mRegErrorLinLayout;
    TextView mRegScreenErrorTv;
    ProgressBar mRegProgressBar;
    TextView mHomeLocationTv;
    Button mFetchLocationBtn;
    boolean dev_id_set = false;
    GoogleCloudMessaging gcm;
    String rId = null;
    String regid;
    String PROJECT_NUMBER = "928493204730";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegPassword = (EditText) findViewById(R.id.reg_password);
        mRegPhoneNum = (EditText) findViewById(R.id.reg_phone_number);
        mRegUsername = (EditText) findViewById(R.id.reg_username);
        mRegProgressBar = (ProgressBar) findViewById(R.id.reg_progress);
        regUserBtn = (Button) findViewById(R.id.register_user_btn);

        //error handling
        mRegErrorLinLayout = (LinearLayout) findViewById(R.id.reg_error_lin_layout);
        mRegScreenErrorTv = (TextView) findViewById(R.id.reg_screen_error_tv);
        mHomeLocationTv = (TextView) findViewById(R.id.reg_home_location);
        mFetchLocationBtn = (Button) findViewById(R.id.reg_fetch_home_location_btn);

        mRegEmail = (EditText) findViewById(R.id.reg_email);
        mRegEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == R.id.ime_register || actionId == EditorInfo.IME_NULL){
                    Toast.makeText(getApplicationContext(), "Test register", Toast.LENGTH_LONG).show();
                    return  true;
                }
                return false;
            }
        });
        // Get GCM id for this device.
        regUserBtn.setText("Retrieving your GCM id...");
        regUserBtn.setEnabled(false);
        getRegId();

        GetCurrentLocationTask gps;
        gps = new GetCurrentLocationTask(RegisterActivity.this);

        if(gps.canGetLocation()){
            Log.d("Reg", "loc Started");
            mHomeLocationTv.setVisibility(View.VISIBLE);
            mHomeLocationTv.setText(gps.getLocation().toString());
        }else{
            showError(true,"Turn on Location services");
            mFetchLocationBtn.setVisibility(View.VISIBLE);
        }

        mFetchLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCurrentLocationTask gps1 = new GetCurrentLocationTask(RegisterActivity.this);
                if(gps1.canGetLocation()){
                    showError(false,"");
                    mFetchLocationBtn.setVisibility(View.GONE);
                    mHomeLocationTv.setVisibility(View.VISIBLE);
                    mHomeLocationTv.setText(gps1.getLocation().toString());
                }else{
                    showError(true,"Turn on Location services");
                    mFetchLocationBtn.setVisibility(View.VISIBLE);
                }

            }
        });

        regUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

    private  void attemptRegister(){
        mRegErrorLinLayout.setVisibility(View.GONE);
        if(validateInput() && dev_id_set) {
            mRegProgressBar.setVisibility(View.VISIBLE);
            RegisterTask registerTask = new RegisterTask(mRegUsername.getText().toString(),
                                                            mRegPassword.getText().toString(),
                                                            regid,
                                                            "home_location",
                                                            mRegPhoneNum.getText().toString(),
                                                            mRegEmail.getText().toString());
            registerTask.response = this;
            registerTask.execute();
        }else if(!dev_id_set){
            showError(true,"Could not connect to GCM Server");
        }
    }

    private  boolean validateInput(){

        mRegUsername.setError(null);
        mRegPassword.setError(null);
        mRegEmail.setError(null);
        mRegPhoneNum.setError(null);

        if(mRegUsername.getText().toString().length() <= 7){
            mRegUsername.setError("Invalid Username");
            mRegUsername.requestFocus();
            return false;
        }else if(mRegPassword.getText().toString().length() <=7){
            mRegPassword.setError("Invalid Password");
            mRegPassword.requestFocus();
            return false;
        }else if(mRegPhoneNum.getText().toString().length() < 10  ||
                mRegPhoneNum.getText().toString().length() > 13){
            mRegPhoneNum.setError("Invalid Phone number");
            mRegPhoneNum.requestFocus();
            return false;
        }else if(!mRegEmail.getText().toString().contains("@") ||
                !mRegEmail.getText().toString().contains(".")){
            mRegEmail.setError("Invalid E-mail");
            mRegEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void showError(boolean show,String message){
        mRegErrorLinLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mRegScreenErrorTv.setText(message);
    }

    @Override
    public void processFinish(String output) {
        try{
            JSONObject jsonObject = new JSONObject(output.toString());
            if(jsonObject.getInt("success") == 0){
               showError(true,jsonObject.getString("message"));
            }else{
                //go to login screen
            }
        }catch(JSONException e){
            showError(true, e.getMessage());
        }
        mRegProgressBar.setVisibility(View.GONE);
    }

    public void getRegId(){
        mRegErrorLinLayout.setVisibility(View.GONE);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = null;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = regid;
                    Log.i("GCM", msg);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
            @Override
            protected void onPostExecute(String msg) {
                rId = msg;
                if(msg != null){
                    dev_id_set = true;
                    regUserBtn.setText(getResources().getString(R.string.register_text));
                    regUserBtn.setEnabled(true);
                }

            }
        }.execute(null, null, null);
   }
}
