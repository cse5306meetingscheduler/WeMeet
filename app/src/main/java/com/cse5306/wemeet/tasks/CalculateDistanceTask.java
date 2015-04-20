package com.cse5306.wemeet.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

/**
 * Created by Sathvik on 19/04/15.
 */
public class CalculateDistanceTask extends AsyncTask<String,String,String> {

    private final WeakReference<TextView> distanceAndTimeTv;

    String result;

    public CalculateDistanceTask(TextView tv) {
        distanceAndTimeTv = new WeakReference<TextView>(tv);

    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String url="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+params[0]+"&destinations="+params[1]+"&key=AIzaSyA0-lltBKfC00Q-W0n04Zy46ha6QTDqtAc";
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                JSONObject jsonObject= new JSONObject(builder.toString());

                result = "From your home location: \n";
                result +="Distance: "+ jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements")
                            .getJSONObject(0).getJSONObject("distance").getString("text");

                result += "\n"+"Approx time: "+jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements")
                            .getJSONObject(0).getJSONObject("duration").getString("text");

                Log.d("sath",result);
            } else {
                result = "Failed to download data!";
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.getMessage();
        }finally {
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        TextView tv = distanceAndTimeTv.get();
        if(tv != null){
             tv.setText(s);
        }
    }
}
