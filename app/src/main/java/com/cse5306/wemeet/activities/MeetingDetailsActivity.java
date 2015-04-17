package com.cse5306.wemeet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.preferences.UserPreferences;
import com.cse5306.wemeet.tasks.GetRestaurantListTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MeetingDetailsActivity extends ActionBarActivity implements OnMapReadyCallback {

    UserPreferences userPreferences;
    LinearLayout mSelectRestaurant,mMap;
    ListView selectRestaurantListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        userPreferences = new UserPreferences(this);
        mMap = (LinearLayout) findViewById(R.id.meeting_details_map_fragment);
        mSelectRestaurant = (LinearLayout) findViewById(R.id.select_restaurant_list_layout);
        selectRestaurantListView = (ListView) findViewById(R.id.select_restaurant_list);

        Intent intent = getIntent();
        String message = intent.getStringExtra("fragmentInflateType");
        String groupId = intent.getStringExtra("groupId");
        Log.d("asd", groupId);


        if(message.equalsIgnoreCase("suggest_list")) {
            mSelectRestaurant.setVisibility(View.VISIBLE);
            GetRestaurantListTask getRestaurantListTask = new GetRestaurantListTask(groupId);
            getRestaurantListTask.execute();
        }
        else if(message.equalsIgnoreCase("map")){
            mMap.setVisibility(View.VISIBLE);
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
