package com.cse5306.wemeet;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterActivity extends ActionBarActivity {

    EditText mRegEmail,mRegPassword,mRegPhoneNum,mRegUsername;
    Button regUserBtn;
    LinearLayout mRegErrorLinLayout;
    TextView mRegScreenErrorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegPassword = (EditText) findViewById(R.id.reg_password);
        mRegPhoneNum = (EditText) findViewById(R.id.reg_phone_number);
        mRegUsername = (EditText) findViewById(R.id.reg_username);
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
        if(validateInput()) {
            RegisterTask registerTask = new RegisterTask("suSafdasdassiSe", "sas", "gcm_id", "asdfsdf", "ss", "aa");
            registerTask.execute();
        }else{
            mRegErrorLinLayout.setVisibility(View.VISIBLE);
            mRegScreenErrorTv.setText("Something Wrong");
        }
    }

    private  boolean validateInput(){

        mRegUsername.setError(null);
        mRegPassword.setError(null);
        mRegEmail.setError(null);
        mRegPhoneNum.setError(null);

        if(mRegUsername.getText().toString().length() <= 7){
            mRegUsername.setError("Invalid Username");
            return false;
        }else if(mRegPassword.getText().toString().length() <=7){
            mRegPassword.setError("Invalid Password");
            return false;
        }else if(mRegPhoneNum.getText().toString().length() < 10  ||
                mRegPhoneNum.getText().toString().length() > 13){
            mRegPhoneNum.setError("Invalid Phone number");
            return false;
        }else if(!mRegEmail.getText().toString().contains("@") ||
                !mRegEmail.getText().toString().contains(".")){
            mRegEmail.setError("Invalid E-mail");
            return false;
        }

        return true;
    }
}
