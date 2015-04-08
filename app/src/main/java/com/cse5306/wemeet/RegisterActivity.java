package com.cse5306.wemeet;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends ActionBarActivity implements RegisterTaskResponse{

    EditText mRegEmail,mRegPassword,mRegPhoneNum,mRegUsername;
    Button regUserBtn;
    LinearLayout mRegErrorLinLayout;
    TextView mRegScreenErrorTv;
    ProgressBar mRegProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegPassword = (EditText) findViewById(R.id.reg_password);
        mRegPhoneNum = (EditText) findViewById(R.id.reg_phone_number);
        mRegUsername = (EditText) findViewById(R.id.reg_username);
        mRegProgressBar = (ProgressBar) findViewById(R.id.reg_progress);
        regUserBtn = (Button) findViewById(R.id.register_user_btn);
        mRegErrorLinLayout = (LinearLayout) findViewById(R.id.reg_error_lin_layout);
        mRegScreenErrorTv = (TextView) findViewById(R.id.reg_screen_error_tv);

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

        regUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

    }

    private  void attemptRegister(){
        mRegErrorLinLayout.setVisibility(View.GONE);
        if(validateInput()) {
            mRegProgressBar.setVisibility(View.VISIBLE);
            RegisterTask registerTask = new RegisterTask(mRegUsername.getText().toString(),
                                                            mRegPassword.getText().toString(),
                                                            "gcm_id",
                                                            "home_location",
                                                            mRegPhoneNum.getText().toString(),
                                                            mRegEmail.getText().toString());
            registerTask.response = this;
            registerTask.execute();
        }else{

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
            e.printStackTrace();
        }
        mRegProgressBar.setVisibility(View.GONE);
    }
}
