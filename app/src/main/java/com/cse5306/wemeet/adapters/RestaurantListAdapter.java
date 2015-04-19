package com.cse5306.wemeet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.objects.Restaurant;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.cse5306.wemeet.tasks.CalculateDistanceTask;
import com.cse5306.wemeet.tasks.DownloadImageTask;

import java.util.List;

/**
 * Created by Sathvik on 17/04/15.
 */
public class RestaurantListAdapter extends BaseAdapter {

    List<Restaurant> restaurantList;
    Context context;

    public RestaurantListAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return restaurantList.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.restaurant_list_item,null);

        ImageView restaurantImage = (ImageView) convertView.findViewById(R.id.restaurant_image);
        TextView restaurantName = (TextView) convertView.findViewById(R.id.restaurant_name);
        TextView restaurantRating = (TextView) convertView.findViewById(R.id.restaurant_rating);
        TextView restaurantPrice = (TextView) convertView.findViewById(R.id.restaurant_price);
        TextView restaurantAddress = (TextView) convertView.findViewById(R.id.restaurant_addr);
        TextView distAndTime = (TextView) convertView.findViewById(R.id.distAndTime);


        restaurantName.setText(restaurantList.get(position).getName());
        restaurantRating.setText(String.valueOf(restaurantList.get(position).getRating())+"/5.0");

        CalculateDistanceTask calculateDistanceTask =  new CalculateDistanceTask(distAndTime);
        calculateDistanceTask.execute(new UserPreferences(context).getUserPrefHomeLocation(),
                restaurantList.get(position).getLatitude()+","+restaurantList.get(position).getLongitude());

        String priceLevel = "";
        for(int i=0;i<restaurantList.get(position).getPrice_level();i++){
            priceLevel += "$";
        }
        restaurantPrice.setText(priceLevel);
        restaurantAddress.setText(restaurantList.get(position).getAddress());

        if(!restaurantList.get(position).getImage().equalsIgnoreCase("null")){
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + restaurantList.get(position).getImage()
                    + "&key=AIzaSyCrjUsBQoM6zXPdhKzpXANDZ7tisHHHO3o";
            DownloadImageTask downloadImageTask = new DownloadImageTask(restaurantImage);
            downloadImageTask.execute(url);
        }


        return convertView;
    }
}
