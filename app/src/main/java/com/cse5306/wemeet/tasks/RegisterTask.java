package com.cse5306.wemeet.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sathvik on 07/04/15.
 */
public class RegisterTask extends AsyncTask<String,String,String> {

    public RegisterTaskResponse response;
    String username,password,gcm_id,home_location,phone_number,email_id;

    public RegisterTask(String username, String password, String gcm_id, String home_location, String phone_number, String email_id) {
        this.username = username;
        this.password = password;
        this.gcm_id = gcm_id;
        this.home_location = home_location;
        this.phone_number = phone_number;
        this.email_id = email_id;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://omega.uta.edu/~sxa6933/WeMeet/register.php");
        HttpResponse response = null;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("gcm_id", gcm_id));
            nameValuePairs.add(new BasicNameValuePair("home_location", home_location));
            nameValuePairs.add(new BasicNameValuePair("phone_number", phone_number));
            nameValuePairs.add(new BasicNameValuePair("email_id", email_id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            response = httpclient.execute(httppost);

            return EntityUtils.toString(response.getEntity());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "error";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        response.processFinish(s);
        Log.d("res",s);
    }
}
