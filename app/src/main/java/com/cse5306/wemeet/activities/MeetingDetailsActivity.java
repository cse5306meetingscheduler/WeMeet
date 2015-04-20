package com.cse5306.wemeet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.adapters.RestaurantListRvAdapter;
import com.cse5306.wemeet.objects.Restaurant;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.cse5306.wemeet.tasks.CalculateDistanceTask;
import com.cse5306.wemeet.tasks.ChooseRestaurantTask;
import com.cse5306.wemeet.tasks.ChooseRestaurantTaskResponse;
import com.cse5306.wemeet.tasks.DestinationDetailsTask;
import com.cse5306.wemeet.tasks.DestinationDetailsTaskResponse;
import com.cse5306.wemeet.tasks.DownloadImageTask;
import com.cse5306.wemeet.tasks.GetRestaurantListTask;
import com.cse5306.wemeet.tasks.GetRestaurantListTaskResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MeetingDetailsActivity extends ActionBarActivity implements OnMapReadyCallback,
        GetRestaurantListTaskResponse,ChooseRestaurantTaskResponse,DestinationDetailsTaskResponse {

    UserPreferences userPreferences;
    LinearLayout mSelectRestaurant,mMap;
    RecyclerView selectRestaurantRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RestaurantListRvAdapter restaurantListRvAdapter;
    List<Restaurant> restaurantList;
    Toolbar meeting_details_toolbar;
    String groupId;
    GoogleMap map;
    ImageView meetingDetailsImageView;
    TextView mRestaurantName,mRestaurantRating,mRestaurantPrice,mRestaurantAddress,mRestaurantDistance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        userPreferences = new UserPreferences(this);
        mMap = (LinearLayout) findViewById(R.id.meeting_details_map_fragment);
        mSelectRestaurant = (LinearLayout) findViewById(R.id.select_restaurant_list_layout);
        meetingDetailsImageView = (ImageView) findViewById(R.id.meeting_details_rest_image);
        mRestaurantName = (TextView) findViewById(R.id.meeting_details_restaurant_name);
        mRestaurantRating = (TextView) findViewById(R.id.meeting_details_rating);
        mRestaurantPrice = (TextView) findViewById(R.id.meeting_details_price);
        mRestaurantAddress= (TextView) findViewById(R.id.meeting_details_address);
        mRestaurantDistance = (TextView) findViewById(R.id.meeting_details_destination_distance);
        // recycler view
        selectRestaurantRecyclerView = (RecyclerView) findViewById(R.id.select_restaurant_list);
        mLayoutManager = new LinearLayoutManager(this);
        selectRestaurantRecyclerView.setLayoutManager(mLayoutManager);
        meeting_details_toolbar = (Toolbar) findViewById(R.id.meeting_details_toolbar);
        restaurantList = new ArrayList<Restaurant>();

        Intent intent = getIntent();
        String message = intent.getStringExtra("fragmentInflateType");
        groupId = intent.getStringExtra("groupId");
        Log.d("asd", groupId);

        setSupportActionBar(meeting_details_toolbar);


        if(message.equalsIgnoreCase("suggest_list")) {
            mSelectRestaurant.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Choose your choice of place for "+groupId);
            GetRestaurantListTask getRestaurantListTask = new GetRestaurantListTask(groupId);
            getRestaurantListTask.response = this;
            getRestaurantListTask.execute();
        }
        else if(message.equalsIgnoreCase("map")){
            getSupportActionBar().setTitle("Final meeting point for group: "+groupId);
            mMap.setVisibility(View.VISIBLE);
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            fetchDestinationDetails();
        }
    }

    private void fetchDestinationDetails(){
        DestinationDetailsTask destinationDetailsTask = new DestinationDetailsTask(groupId,userPreferences.getSessionUserPrefUsername());
        destinationDetailsTask.response = this;
        destinationDetailsTask.execute();

    }

    private void sendRestaurant(Restaurant restaurant,String group_id){
        ChooseRestaurantTask chooseRestaurantTask = new ChooseRestaurantTask(userPreferences.getSessionUserPrefUsername(),
                                                group_id,
                                                restaurant.getLocId());
        chooseRestaurantTask.response = this;
        chooseRestaurantTask.execute();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(Double.parseDouble(userPreferences.getUserPrefHomeLocation().split(",")[0]),
                Double.parseDouble(userPreferences.getUserPrefHomeLocation().split(",")[1]));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker"));


    }

    private void startNavigation(){
        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=" + userPreferences.getUserPrefHomeLocation().split(",")[0] + "," + userPreferences.getUserPrefHomeLocation().split(",")[1]));
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meeting_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(List<Restaurant> output) {
        // fetching list
        restaurantList = output;
        //rec adapter
        restaurantListRvAdapter = new RestaurantListRvAdapter(restaurantList,this);
        selectRestaurantRecyclerView.setAdapter(restaurantListRvAdapter);
        selectRestaurantRecyclerView.setItemViewCacheSize(output.size());
        selectRestaurantRecyclerView.setItemAnimator(new DefaultItemAnimator());
        restaurantListRvAdapter.setOnItemClickListener(new RestaurantListRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                sendRestaurant(restaurantList.get(position+1),groupId);
            }
        });
    }

    @Override
    public void processFinish(String res) {
        // choosing restautant
        Log.d("choose rest res",res);
    }

    @Override
    public void getDestinationDetails(String s) {
        Log.d("getDestinationDetails",s);
        try{
            JSONObject jsonObject = new JSONObject(s.toString());
            mRestaurantName.setText(jsonObject.getString("name"));
            mRestaurantRating.setText(String.valueOf(jsonObject.getDouble("rating"))+"/5");

            String priceLevel = "";
            for(int i=0;i<jsonObject.getInt("price_level");i++){
                priceLevel += "$";
            }
            mRestaurantPrice.setText(priceLevel);

            mRestaurantAddress.setText(jsonObject.getString("address"));
            CalculateDistanceTask calculateDistanceTask =  new CalculateDistanceTask(mRestaurantDistance);
            calculateDistanceTask.execute(userPreferences.getUserPrefHomeLocation(),
                    jsonObject.getString("latitude")+","+jsonObject.getString("longitude"));

            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                    + jsonObject.getString("image")
                    + "&key=AIzaSyA0-lltBKfC00Q-W0n04Zy46ha6QTDqtAc";
            DownloadImageTask downloadImageTask = new DownloadImageTask(meetingDetailsImageView);
            downloadImageTask.execute(url);
            buildMap(jsonObject.getString("latitude"),jsonObject.getString("longitude"));


        }catch (JSONException e){
            e.getMessage();
        }


    }

    private void buildMap(String destLatStr, String destLngStr){

        double srcLat = Double.parseDouble(userPreferences.getUserPrefHomeLocation().split(",")[0]);
        double srcLng = Double.parseDouble(userPreferences.getUserPrefHomeLocation().split(",")[1]);
        double destLat = Double.parseDouble(destLatStr);
        double destLng = Double.parseDouble(destLngStr);


        Marker srcMarker = map.addMarker(new MarkerOptions().position(new LatLng(srcLat,srcLng)));
        Marker destMarker = map.addMarker(new MarkerOptions().position(new LatLng(destLat,destLng)));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });
        List<Marker> markers = new ArrayList<Marker>();
        markers.add(srcMarker);
        markers.add(destMarker);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.moveCamera(cu);
        map.animateCamera(cu);
    }
}
