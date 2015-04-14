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
 * Created by Sathvik on 14/04/15.
 */
public class JoinMeetingTask extends AsyncTask<String, String, String>  {

    public JoinMeetingTaskResponse response = null;

    String username;
    String home_loaction;
    String group_id;

    public JoinMeetingTask(String username, String home_loaction, String group_id) {
        this.username = username;
        this.home_loaction = home_loaction;
        this.group_id = group_id;
        Log.d("ff",username+home_loaction+group_id);
    }

    @Override
    protected String doInBackground(String... params) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://wemeet-sathvik1709.c9.io/participant_join.php");
        HttpResponse response = null;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("location", home_loaction));
            nameValuePairs.add(new BasicNameValuePair("group_id", group_id));
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
