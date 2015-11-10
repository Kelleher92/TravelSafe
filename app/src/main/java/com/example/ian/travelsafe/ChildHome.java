package com.example.ian.travelsafe;

import android.location.Location;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChildHome extends AppCompatActivity implements OnMapReadyCallback {

    private TextView mLocationView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;

    TextView startLocation;
    TextView endLocation;
    TextView currentLocation;
    Button  btnShowLoc;
    AppLocationService gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapChild);
        mapFragment.getMapAsync(this);

        startLocation = (TextView) findViewById(R.id.startLocation);
        endLocation = (TextView) findViewById(R.id.endLocation);
        currentLocation = (TextView) findViewById(R.id.currentLocation);

        btnShowLoc = (Button) findViewById(R.id.ButtonStartJourney);
        btnShowLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new AppLocationService(ChildHome.this);

                if(gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(),
                            "Latitude: "+latitude + "\nLongitude: "+longitude, Toast.LENGTH_SHORT).show();
                }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng eng = new LatLng(53.306373, -6.218638);
        mMap.addMarker(new MarkerOptions().position(eng).title("Marker in Eng Building"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eng));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}
