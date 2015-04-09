package com.cse5306.wemeet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

        mLoginScreenErrorTv = (TextView) findViewById(R.id.login_screen_error_tv);
        mLoginScreenErrorLinLayout = (LinearLayout) findViewById(R.id.login_error_lin_layout);
        mKeepLoginCb = (CheckBox) findViewById(R.id.reg_check_box);

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
            mAuthTask = new UserLoginTask(userPreferences.getUserPrefUsername(), userPreferences.getUserPrefPassword());
            mAuthTask.response = this;
            mAuthTask.execute();
        }

        mSignInButton = (Button) findViewById(R.id.login_button);
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

        mLoginProgressView = findViewById(R.id.login_progress);
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

        if (cancel) {
            focusView.requestFocus();
            mLoginProgressView.setVisibility(View.GONE);
        } else {
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.response = this;
            mAuthTask.execute();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void showError(boolean show,String message){
        mLoginScreenErrorLinLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginScreenErrorTv.setText(message);
    }

    @Override
    public void processFinish(String output) {
        mAuthTask = null;
        try{
            JSONObject jsonObject = new JSONObject(output.toString());
            if(jsonObject.getInt("success") == 0){
                showError(true,jsonObject.getString("message"));
                mLoginProgressView.setVisibility(View.GONE);
            }else if(jsonObject.getInt("success") == 1){
                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                if(mKeepLoginCb.isChecked()){
                    userPreferences.setUserPrefKeepLogin(true);
                    userPreferences.setUserPrefUsername(mUserNameView.getText().toString());
                    userPreferences.setUserPrefPassword(mPasswordView.getText().toString());

                }
                mLoginProgressView.setVisibility(View.GONE);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    public void hideKeyboard(){
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}


