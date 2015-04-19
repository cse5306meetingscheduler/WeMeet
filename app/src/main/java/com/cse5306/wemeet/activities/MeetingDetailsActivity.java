package com.cse5306.wemeet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.adapters.RestaurantListAdapter;
import com.cse5306.wemeet.objects.Restaurant;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.cse5306.wemeet.tasks.ChooseRestaurantTask;
import com.cse5306.wemeet.tasks.ChooseRestaurantTaskResponse;
import com.cse5306.wemeet.tasks.GetRestaurantListTask;
import com.cse5306.wemeet.tasks.GetRestaurantListTaskResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MeetingDetailsActivity extends ActionBarActivity implements OnMapReadyCallback,
        GetRestaurantListTaskResponse,ChooseRestaurantTaskResponse {

    UserPreferences userPreferences;
    LinearLayout mSelectRestaurant,mMap;
    ListView selectRestaurantListView;
    List<Restaurant> restaurantList;
    Toolbar meeting_details_toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        userPreferences = new UserPreferences(this);
        mMap = (LinearLayout) findViewById(R.id.meeting_details_map_fragment);
        mSelectRestaurant = (LinearLayout) findViewById(R.id.select_restaurant_list_layout);
        selectRestaurantListView = (ListView) findViewById(R.id.select_restaurant_list);
        meeting_details_toolbar = (Toolbar) findViewById(R.id.meeting_details_toolbar);
        restaurantList = new ArrayList<Restaurant>();

        setSupportActionBar(meeting_details_toolbar);

        Intent intent = getIntent();
        String message = intent.getStringExtra("fragmentInflateType");
        final String groupId = intent.getStringExtra("groupId");
        Log.d("asd", groupId);



        if(message.equalsIgnoreCase("suggest_list")) {
            mSelectRestaurant.setVisibility(View.VISIBLE);
            GetRestaurantListTask getRestaurantListTask = new GetRestaurantListTask(groupId);
            getRestaurantListTask.response = this;
            getRestaurantListTask.execute();
        }
        else if(message.equalsIgnoreCase("map")){
            mMap.setVisibility(View.VISIBLE);
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        selectRestaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sendRestaurant(restaurantList.get(position),groupId);
            }
        });

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

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(List<Restaurant> output) {
        restaurantList = output;
        RestaurantListAdapter adapter = new RestaurantListAdapter(output,getApplicationContext());
        selectRestaurantListView.setAdapter(adapter);
    }

    @Override
    public void processFinish(String res) {
        Log.d("choose rest res",res);
    }
}
