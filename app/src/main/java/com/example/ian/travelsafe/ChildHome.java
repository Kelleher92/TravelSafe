package com.example.ian.travelsafe;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChildHome extends AppCompatActivity implements OnMapReadyCallback {

    private TextView mLocationView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    GetAddress getAdd = new GetAddress();

    private GoogleMap mMap;

    TextView startLocation;
    TextView childName;
    TextView endLocation;
    TextView currentLocation;
    Button btnShowLoc;
    private SwipeRefreshLayout swipeContainer;
    Handler handler;
    Location myLocation;
    Address myAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);

        startLocation = (TextView) findViewById(R.id.startLocation);
        endLocation = (TextView) findViewById(R.id.endLocation);
        currentLocation = (TextView) findViewById(R.id.currentLocation);
        currentLocation = (TextView) findViewById(R.id.currentLocation);
        btnShowLoc = (Button) findViewById(R.id.ButtonStartJourney);
        childName = (TextView) findViewById(R.id.ChildFirstName);


        UserLocalStore current = new UserLocalStore(this);
        Users currentUser = current.getLoggedInUser();

        childName.setText(currentUser.get_emailAddress());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.childMap);
        mapFragment.getMapAsync(this);

        // Button to start location tracking or journey.
//        btnShowLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        // Setup refresh listener which triggers new data loading
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_childHome);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                myLocation = mMap.getMyLocation();
                Locale loc = null;
                myAddress = new Address(loc);
                try {
                    myAddress = getAddressForLocation(ChildHome.this, myLocation);
                    currentLocation.setText(myAddress.getAddressLine(0) + ", " + myAddress.getAddressLine(1));
                    // Focusing camera on current location.
                    CameraUpdate center = CameraUpdateFactory
                            .newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }
        });


    }

    public Address getAddressForLocation(Context context, Location location) throws IOException {

        if (location == null) {
            return null;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        int maxResults = 1;

        Geocoder gc = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = gc.getFromLocation(latitude, longitude, maxResults);

        if (addresses.size() == 1) {
            return addresses.get(0);
        } else {
            return null;
        }
    }

    public void DisplayStartLocation() {

    }

    public void DisplayEndLocation() {

    }

    public void DisplayCurrentLocation() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng eng = new LatLng(53.306373, -6.218638);
        mMap.addMarker(new MarkerOptions().position(eng).title("Marker in Eng Building"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eng));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.setTrafficEnabled(true);

//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//
//                Location myLocation = mMap.getMyLocation();
//                Address myAddress = null;
//                try {
//                    myAddress = getAddressForLocation(ChildHome.this, myLocation);
//                    currentLocation.setText(myAddress.getAddressLine(0));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

    }

    public void StartJourney(View view) {

        Snackbar.make(view, "Button Click", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
