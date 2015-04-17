package com.cse5306.wemeet.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.cse5306.wemeet.objects.Restaurant;

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
 * Created by Sathvik on 17/04/15.
 */
public class GetRestaurantListTask extends AsyncTask<String,String,List<Restaurant>> {

    List<Restaurant> restaurantList;
    String group_id;

    public GetRestaurantListTask(String group_id) {
        this.group_id = group_id;
    }

    @Override
    protected List<Restaurant> doInBackground(String... params) {
        restaurantList = new ArrayList<Restaurant>();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://wemeet-sathvik1709.c9.io/find_places.php");
        HttpResponse response = null;
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("group_id", group_id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);

            String res = EntityUtils.toString(response.getEntity());

            JSONArray jsonArray = new JSONArray(res);
            for(int i=0;i<jsonArray.length();i++){
                Restaurant restaurant = new Restaurant();

                restaurant.setName(jsonArray.getJSONObject(i).getString("name"));
                restaurant.setAddress(jsonArray.getJSONObject(i).getString("address"));
                restaurant.setPrice_level(jsonArray.getJSONObject(i).getInt("price_level"));
                restaurant.setRating((float) jsonArray.getJSONObject(i).getDouble("rating"));
                restaurant.setImage(jsonArray.getJSONObject(i).getString("image"));
                restaurant.setLatitude(jsonArray.getJSONObject(i).getDouble("latitude"));
                restaurant.setLongitude(jsonArray.getJSONObject(i).getDouble("longitude"));

                Log.d("rest",jsonArray.getJSONObject(i).getString("name"));

                restaurantList.add(restaurant);
            }


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return restaurantList;
    }

    @Override
    protected void onPostExecute(List<Restaurant> restaurants) {
        super.onPostExecute(restaurants);
    }
}
