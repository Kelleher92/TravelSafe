package com.example.ian.travelsafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;

public class ChildHome extends AppCompatActivity implements RoutingListener, OnConnectionFailedListener, ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;

    private GoogleMap map;
//    protected LatLng start;
//    protected LatLng end;

    TextView startLocation;
    TextView childName;
    TextView endLocation;
    TextView currentLocation;
    Button  btnShowLoc;
    private SwipeRefreshLayout swipeContainer;
    Location myLocation;
    Location startLoc;
    Location endLoc;
    Address myAddress;
    Address startAddress;
    Address endAddress;
    AbstractRouting.TravelMode travelMode;

    // Information for graphing route.
    RouteDetails route = new RouteDetails(null, null, null, null, 0, 0);
    private int[] colors = new int[]{R.color.colorPrimaryLighter, R.color.primary_dark_material_light};
    private ProgressDialog progressDialog;
    private PlaceAutoCompleteAdapter mAdapter;
    private ArrayList<Polyline> polylines;
    private static final LatLngBounds BOUNDS_UCD= new LatLngBounds(
            new LatLng(53.298158, -6.246800),
            new LatLng(53.316057, -6.205602));
    LatLng startExample =  new LatLng(53.316057, -6.205602);         // UCD
    LatLng destinationExample =  new LatLng( 53.323842, -6.26519);   // Rathmines
    int routeIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);

        // Get information that might be changed
        startLocation = (TextView) findViewById(R.id.startLocation);
        endLocation = (TextView) findViewById(R.id.endLocation);
        currentLocation = (TextView) findViewById(R.id.currentLocation);
        currentLocation = (TextView) findViewById(R.id.currentLocation);
        btnShowLoc = (Button) findViewById(R.id.ButtonStartJourney);
        childName = (TextView) findViewById(R.id.ChildFirstName);

        // Get login in Child so there name can be displayed.
        UserLocalStore current = new UserLocalStore(this);
        Users currentUser = current.getLoggedInUser();
        childName.setText(currentUser.get_emailAddress());

        // Server request for child route information.
        ServerRequests serverRequests = new ServerRequests(this);
        Log.i("FetchChildRoute", "id......" + currentUser.get_id());
        serverRequests.fetchChildAttachedRoute(currentUser.get_id(), new GetRouteCallback() {
            @Override
            public void done(RouteDetails returnedRoute) {
                if (returnedRoute == null) {
                    Log.i("ChildHome", "No route returned");
                } else {
//                    Log.i("ChildHome", "!!!!..." + route.getStart().toString());
                    route = returnedRoute;
                    startExample = route.getStart();         // Getting actual assigned location start.
                    destinationExample = route.getEnd();             // Getting actual start location end.
                    routeIndex = route.getIndex();
                    travelMode = route.getModeTransport();
                }
            }
        });

        // Get Map fragment from view and implement.
        MapsInitializer.initialize(this);
        polylines = new ArrayList<>();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        // Get map fragment from view and place one there if it does not exist.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.childMap);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.childMap, mapFragment).commit();
        }
        map = mapFragment.getMap();

//        mAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
//                mGoogleApiClient, BOUNDS_UCD, null);

//        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(53.306647, -6.221427));
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

//        map.moveCamera(center);
//        map.animateCamera(zoom);

                /*
        * Updates the bounds being used by the auto complete adapter based on the position of the
        * map.
        * */
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                mAdapter.setBounds(bounds);
            }
        });
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

//        start =  startExample;
//        end = destinationExample;

        if(Util.Operations.isOnline(this)) {
            route();
        }
        else {
            Toast.makeText(this,"No internet connectivity",Toast.LENGTH_SHORT).show();
        }
        // Swipe Refresh Listener
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_childHome);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                myLocation = map.getMyLocation();
                startLoc = new Location("");
                startLoc.setLatitude(startExample.latitude);//your coords of course
                startLoc.setLongitude(startExample.longitude);

                endLoc = new Location("");
                endLoc.setLatitude(destinationExample.latitude);//your coords of course
                endLoc.setLongitude(destinationExample.longitude);

                Locale loc = null;
                myAddress = new Address(loc);
                startAddress = new Address(loc);
                endAddress = new Address(loc);
                try {
                    myAddress = getAddressForLocation(ChildHome.this, new LatLng(myLocation.getLatitude(),myLocation.getLongitude()));
                    startAddress = getAddressForLocation(ChildHome.this, new LatLng(startLoc.getLatitude(),startLoc.getLongitude()));
                    endAddress = getAddressForLocation(ChildHome.this, new LatLng(endLoc.getLatitude(),endLoc.getLongitude()));
                    currentLocation.setText(myAddress.getAddressLine(0));
                    startLocation.setText(startAddress.getAddressLine(0));
                    endLocation.setText(endAddress.getAddressLine(0));
                    // Focusing camera on current location.
                    CameraUpdate center = CameraUpdateFactory.newLatLng(
                            new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
//                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
//                    mMap.moveCamera(center);
//                    mMap.animateCamera(zoom);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }
        });




    }

    public void route() {
        if(startExample==null || destinationExample==null){
            Toast.makeText(this,"Please choose a destination.",Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog = ProgressDialog.show(this, "Please wait.", "Fetching route information.", true);
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(startExample, destinationExample)
                    .build();
            routing.execute();
        }
    }

    public Address getAddressForLocation(Context context, LatLng location) throws IOException {

        if (location == null) {
            return null;
        }
        double latitude = location.latitude;
        double longitude = location.longitude;
        int maxResults = 1;

        Geocoder gc = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = gc.getFromLocation(latitude, longitude, maxResults);

        if (addresses.size() == 1) {
            return addresses.get(0);
        } else {
            return null;
        }
    }

    public void StartJourney(View view) {
        Snackbar.make(view, "Button Click", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        // New thread that logs location.

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("DirectionsActivity", connectionResult.toString());
    }

    @Override
    public void onRoutingFailure() {
        // The Routing request failed
        progressDialog.dismiss();
        Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingCancelled() {
        Log.i("DirectionsActivity", "Routing was cancelled.");
    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        map.clear();
        progressDialog.dismiss();

        CameraUpdate center = newLatLngBounds(route.get(0).getLatLgnBounds(), 100);
        map.moveCamera(center);


        polylines = new ArrayList<>();
        //add route(s) to the map.
        for(int i = 0; i <route.size(); i++) {

            if(i == routeIndex) {

                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(colors[0]);
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(route.get(i).getPoints());
                Polyline polyline = map.addPolyline(polyOptions);
                polylines.add(polyline);

                // Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_LONG).show();
            }
        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(startExample);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
        map.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(destinationExample);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));
        map.addMarker(options);

    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

}
