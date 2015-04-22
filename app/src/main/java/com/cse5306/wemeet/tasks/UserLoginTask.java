package com.cse5306.wemeet.tasks;

import android.os.AsyncTask;

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
 * Created by Sathvik on 08/04/15.
 */
public class UserLoginTask extends AsyncTask<String,String,String> {

    public UserLoginTaskResponse response=null;
    private final String uname;
    private final String mPassword;

    public UserLoginTask(String email, String password) {
        this.uname = email;
        this.mPassword = password;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://omega.uta.edu/~sxa6933/WeMeet/login.php");
        HttpResponse response = null;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", uname));
            nameValuePairs.add(new BasicNameValuePair("password", mPassword));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);

            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return "error";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        response.processFinish(s);
    }
}
