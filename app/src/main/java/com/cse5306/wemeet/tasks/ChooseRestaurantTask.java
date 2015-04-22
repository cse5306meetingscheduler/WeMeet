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
 * Created by Sathvik on 18/04/15.
 */
public class ChooseRestaurantTask extends AsyncTask<String,String,String> {

    String username,groupId;
    int restaurantId;
    public ChooseRestaurantTaskResponse response=null;

    public ChooseRestaurantTask(String username, String groupId, int restaurantId) {
        this.username = username;
        this.groupId = groupId;
        this.restaurantId = restaurantId;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://omega.uta.edu/~sxa6933/WeMeet/final_destination.php");
        HttpResponse response = null;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            Log.d("sel", String.valueOf(restaurantId)+"--"+username+"--"+groupId);
            nameValuePairs.add(new BasicNameValuePair("loc_id", String.valueOf(restaurantId)));
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("group_id", groupId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);

            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        return "error";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        response.processFinish(s);
    }
}
