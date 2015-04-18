package com.cse5306.wemeet.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.cse5306.wemeet.tasks.UserLoginTask;
import com.cse5306.wemeet.tasks.UserLoginTaskResponse;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements UserLoginTaskResponse {


    private UserLoginTask mAuthTask = null;

    // UI references.
    private CheckBox mKeepLoginCb;
    private EditText mUserNameView;
    private EditText mPasswordView;
    private View mLoginProgressView;
    Button mSignInButton;
    ViewGroup mLoginForm;
    UserPreferences userPreferences;
    Button mRegisterButton;
    LinearLayout mLoginScreenErrorLinLayout;
    TextView mLoginScreenErrorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideKeyboard();
        userPreferences = new UserPreferences(getApplicationContext());

        mLoginForm = (ViewGroup) findViewById(R.id.login_form);
        mLoginScreenErrorTv = (TextView) findViewById(R.id.login_screen_error_tv);
        mLoginScreenErrorLinLayout = (LinearLayout) findViewById(R.id.login_error_lin_layout);
        mKeepLoginCb = (CheckBox) findViewById(R.id.reg_check_box);
        mLoginProgressView = findViewById(R.id.login_progress);
        mSignInButton = (Button) findViewById(R.id.login_button);

        mUserNameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == R.id.ime_login || actionId == EditorInfo.IME_NULL){
                    Toast.makeText(getApplicationContext(), "Test Login", Toast.LENGTH_LONG).show();
                    return  true;
                }
                return false;
            }
        });

        if(userPreferences.getUserPrefKeepLogin()){
            mKeepLoginCb.setChecked(true);
            userPreferences.setSessionUserPrefUsername(userPreferences.getUserPrefUsername());
            mPasswordView.setText(userPreferences.getUserPrefPassword());
            mUserNameView.setText(userPreferences.getUserPrefUsername());
            attemptLogin();
        }


        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void attemptLogin() {
        hideKeyboard();
        mLoginProgressView.setVisibility(View.VISIBLE);
        mLoginScreenErrorLinLayout.setVisibility(View.GONE);
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_email));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (mUserNameView.getText().toString().length() <= 6) {
            mUserNameView.setError("Invalid username");
            focusView = mUserNameView;
            cancel = true;
        }


        if(!isNetworkConnected()){
            focusView = mSignInButton;
            showError(true,"No internet connection");
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            mLoginProgressView.setVisibility(View.GONE);
        } else {
            userPreferences.setSessionUserPrefUsername(username);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.response = this;
            mAuthTask.execute();
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    private void showError(boolean show,String message){
        mLoginScreenErrorLinLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginScreenErrorTv.setText(message);
    }

    @Override
    public void processFinish(String output) {
        mLoginProgressView.setVisibility(View.GONE);
        mAuthTask = null;
        try{
            JSONObject jsonObject = new JSONObject(output.toString());
            Log.d("res",output);
            if(jsonObject.getInt("success") == 0){

                userPreferences.setSessionUserPrefUsername(null);
                showError(true,jsonObject.getString("message"));
                mLoginForm.setVisibility(View.VISIBLE);
            }else if(jsonObject.getInt("success") == 1){
                userPreferences.setUserPrefHomeLocation(jsonObject.getString("user_location"));
                if(mKeepLoginCb.isChecked()){
                    userPreferences.setUserPrefKeepLogin(true);
                    userPreferences.setUserPrefUsername(mUserNameView.getText().toString());
                    userPreferences.setUserPrefPassword(mPasswordView.getText().toString());
                }
                Intent intent = new Intent(this,UserHomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        }catch(JSONException e){
            mLoginProgressView.setVisibility(View.GONE);
            e.printStackTrace();
        }

    }

    public void hideKeyboard(){
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }
}



