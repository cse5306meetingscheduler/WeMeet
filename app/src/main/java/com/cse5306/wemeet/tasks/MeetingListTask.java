package com.cse5306.wemeet.tasks;

import android.os.AsyncTask;

import com.cse5306.wemeet.objects.MeetingDetails;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sathvik on 16/04/15.
 */
public class MeetingListTask extends AsyncTask<String, String,List<MeetingDetails>> {

    String username;
    public MeetingListTaskResponse response = null;

    public MeetingListTask(String username) {
        this.username = username;
    }

    @Override
    protected List<MeetingDetails> doInBackground(String... params) {

        List<MeetingDetails> meetingDetailsList = new ArrayList<MeetingDetails>();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://omega.uta.edu/~sxa6933/WeMeet/view_meetings.php");
        HttpResponse response = null;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);

            String res = EntityUtils.toString(response.getEntity());



            JSONArray jsonArray = new JSONArray(res);
            for(int i=0;i<jsonArray.length();i++){
                MeetingDetails meetingDetails = new MeetingDetails();
                meetingDetails.setGroupId(jsonArray.getJSONObject(i).getInt("group_id"));
                meetingDetails.setMeetingDate(jsonArray.getJSONObject(i).getString("meeting_time"));
                meetingDetails.setMeetingTime(jsonArray.getJSONObject(i).getString("meeting_date"));
                meetingDetails.setFinalDestination(jsonArray.getJSONObject(i).getString("final_dest"));
                meetingDetails.setMidpoint(jsonArray.getJSONObject(i).getString("feasible_midpoint"));
                if(jsonArray.getJSONObject(i).getInt("host") == 1){
                    meetingDetails.setHost(true);
                }else{
                    meetingDetails.setHost(false);
                }
                meetingDetails.setMaxPpl(jsonArray.getJSONObject(i).getInt("max_ppl"));
                meetingDetailsList.add(meetingDetails);
            }

        } catch (ClientProtocolException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        } catch (JSONException e) {
            e.getMessage();
        }


        return meetingDetailsList;
    }

    @Override
    protected void onPostExecute(List<MeetingDetails> s) {
        super.onPostExecute(s);
        response.processFinish(s);
    }
}
