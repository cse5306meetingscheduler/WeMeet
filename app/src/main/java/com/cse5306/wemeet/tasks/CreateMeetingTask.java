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
 * Created by Sathvik on 10/04/15.
 */
public class CreateMeetingTask extends AsyncTask<String,String,String> {

    public CreateMeetingTaskResponse response = null;
    int num_of_ppl;
    String date,time,location,user_id;

    public CreateMeetingTask(String date, String time, String location, String user_id, int num_of_ppl) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.user_id = user_id;
        this.num_of_ppl = num_of_ppl;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://wemeet-sathvik1709.c9.io/create_meeting.php");
        HttpResponse response = null;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("max_ppl", String.valueOf(num_of_ppl)));
            nameValuePairs.add(new BasicNameValuePair("username", user_id));
            nameValuePairs.add(new BasicNameValuePair("meeting_time", time));
            nameValuePairs.add(new BasicNameValuePair("meeting_date", date));
            nameValuePairs.add(new BasicNameValuePair("location", location));
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
