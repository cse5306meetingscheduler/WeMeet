package com.cse5306.wemeet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.objects.Restaurant;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.cse5306.wemeet.tasks.CalculateDistanceTask;
import com.cse5306.wemeet.tasks.DownloadImageTask;

import java.util.List;

/**
 * Created by Sathvik on 19/04/15.
 */
public class RestaurantListRvAdapter extends RecyclerView.Adapter<RestaurantListRvAdapter.ResViewHolder> {

    List<Restaurant> restaurantList;
    Context context;
    public OnItemClickListener mItemClickListener;

    public class ResViewHolder extends RecyclerView.ViewHolder{
        ImageView restaurantImage;
        TextView restaurantName;
        TextView restaurantRating;
        TextView restaurantPrice;
        TextView restaurantAddress;
        TextView distAndTime;
        public ResViewHolder(View convertView) {
            super(convertView);
            restaurantImage = (ImageView) convertView.findViewById(R.id.restaurant_image);
             restaurantName = (TextView) convertView.findViewById(R.id.restaurant_name);
             restaurantRating = (TextView) convertView.findViewById(R.id.restaurant_rating);
             restaurantPrice = (TextView) convertView.findViewById(R.id.restaurant_price);
             restaurantAddress = (TextView) convertView.findViewById(R.id.restaurant_addr);
             distAndTime = (TextView) convertView.findViewById(R.id.distAndTime);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v,getPosition());
                }
            });

        }
    }

    public interface OnItemClickListener{
        public void onItemClick(View v,int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public RestaurantListRvAdapter(List<Restaurant> restaurantList,Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @Override
    public RestaurantListRvAdapter.ResViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_list_item, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters

        ResViewHolder vh = new ResViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RestaurantListRvAdapter.ResViewHolder resViewHolder, int position) {
        resViewHolder.restaurantName.setText(restaurantList.get(position).getName());
        resViewHolder.restaurantRating.setText(String.valueOf(restaurantList.get(position).getRating())+"/5.0");

        CalculateDistanceTask calculateDistanceTask =  new CalculateDistanceTask(resViewHolder.distAndTime);
        calculateDistanceTask.execute(new UserPreferences(context).getUserPrefHomeLocation(),
                restaurantList.get(position).getLatitude()+","+restaurantList.get(position).getLongitude());

        String priceLevel = "";
        for(int i=0;i<restaurantList.get(position).getPrice_level();i++){
            priceLevel += "$";
        }
        resViewHolder.restaurantPrice.setText(priceLevel);
        resViewHolder.restaurantAddress.setText(restaurantList.get(position).getAddress());

        if(!restaurantList.get(position).getImage().equalsIgnoreCase("null")){
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + restaurantList.get(position).getImage()
                    + "&key=AIzaSyA0-lltBKfC00Q-W0n04Zy46ha6QTDqtAc";
            DownloadImageTask downloadImageTask = new DownloadImageTask(resViewHolder.restaurantImage);
            downloadImageTask.execute(url);
        }

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}
