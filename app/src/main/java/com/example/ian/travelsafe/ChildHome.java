package com.example.ian.travelsafe;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class ChildHome extends AppCompatActivity {

    private TextView mLocationView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    GetAddress getAdd = new GetAddress();

    TextView startLocation;
    TextView endLocation;
    TextView currentLocation;
    TextView textView10;
    Button  btnShowLoc;
    AppLocationService gps;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);

        startLocation = (TextView) findViewById(R.id.startLocation);
        endLocation = (TextView) findViewById(R.id.endLocation);
        currentLocation = (TextView) findViewById(R.id.currentLocation);
        textView10 = (TextView) findViewById(R.id.textView10);
        btnShowLoc = (Button) findViewById(R.id.ButtonStartJourney);
        btnShowLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new AppLocationService(ChildHome.this);

                if(gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Handler handler = new Handler() {
                        public void handleMessage (Message msg) {
                            textView10.setText(msg.toString());
                        }
                    };
                    LocationAddress.getAddressFromLocation(latitude, longitude, ChildHome.this, handler);

//                    Toast.makeText(getApplicationContext(),
//                            "Latitude: "+latitude + "\nLongitude: "+longitude, Toast.LENGTH_SHORT).show();

                }


            }
        });


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_childHome);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                gps = new AppLocationService(ChildHome.this);

                if(gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    startLocation.setText(Double.toString(latitude));
                    endLocation.setText(Double.toString(longitude));

                }
                swipeContainer.setRefreshing(false);
            }
        });

    }



    public void DisplayStartLocation(){

    }

    public void DisplayEndLocation(){

    }

    public void DisplayCurrentLocation(){

    }

    public void StartJourney(View view) {
    }
}
