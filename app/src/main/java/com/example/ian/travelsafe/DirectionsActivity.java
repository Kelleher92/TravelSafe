package com.example.ian.travelsafe;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;

public class DirectionsActivity extends AppCompatActivity implements RoutingListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    protected GoogleMap map;
    protected LatLng start;
    protected LatLng end;
    protected AbstractRouting.TravelMode modeTransport;
    protected MarkerOptions startOptions = new MarkerOptions();
    protected MarkerOptions endOptions = new MarkerOptions();
    protected Route chosenRoute;
    protected int routeNo;

    @InjectView(R.id.start)
    AutoCompleteTextView starting;
    @InjectView(R.id.destination)
    AutoCompleteTextView destination;
    @InjectView(R.id.send)
    ImageView send;

    //protected AutoCompleteTextView starting; //= new AutoCompleteTextView();
    //protected AutoCompleteTextView destination;// = new AutoCompleteTextView();
    // ImageView send;// = new ImageView();

    private String LOG_TAG = "DirectionsActivity";
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<Route> routes;

    private static final LatLngBounds BOUNDS_UCD = new LatLngBounds(new LatLng(53.298158, -6.246800),
            new LatLng(53.316057, -6.205602));

    /**
     * This activity loads a map and then displays the route and pushpins on it.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        ButterKnife.inject(this);
        //this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        MapsInitializer.initialize(this);
        routes = new ArrayList<>();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //AutoCompleteTextView starting = (AutoCompleteTextView) findViewById(R.id.start);
        //AutoCompleteTextView destination = (AutoCompleteTextView) findViewById(R.id.destination);
        //ImageView send = (ImageView) findViewById(R.id.send);

        mGoogleApiClient.connect();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        map = mapFragment.getMap();


        mAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_UCD, null);

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(53.306647, -6.221427));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

        map.moveCamera(center);
        map.animateCamera(zoom);

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
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        /* LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 5000, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                        map.moveCamera(center);
                        map.animateCamera(zoom);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                        map.moveCamera(center);
                        map.animateCamera(zoom);

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });



        /*
        * Adds auto complete adapter to both auto complete
        * text views.
        * */
        starting.setAdapter(mAdapter);
        destination.setAdapter(mAdapter);


        /*
        * Sets the start and destination points based on the values selected
        * from the autocomplete text views.
        * */

        starting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(LOG_TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);

                        start = place.getLatLng();
                    }
                });

            }
        });

        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(LOG_TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);

                        end = place.getLatLng();
                    }
                });

            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!routes.isEmpty()) {
                    for (Route iroute : routes) {
                        for (LatLng routeCoords : iroute.getPoints()) {
                            float[] results = new float[1];
                            Location.distanceBetween(latLng.latitude, latLng.longitude,
                                    routeCoords.latitude, routeCoords.longitude, results);

                            if (results[0] < 100) {
                                map.clear();
                                map.addMarker(startOptions);
                                map.addMarker(endOptions);

                                for (Route r : routes) {
                                    if (r != iroute) {
                                        map.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.colorInactiveRoute)).addAll(r.getPoints()).width(20));
                                    } else {
                                        chosenRoute = r;
                                        routeNo = routes.indexOf(r) + 1;
                                    }
                                }

                                map.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.colorPrimary)).addAll(chosenRoute.getPoints()).width(20));
                            }

                        }

                    }


                    Toast.makeText(getApplicationContext(), "Chosen route set to Route: " + routeNo, Toast.LENGTH_LONG).show();
                    Log.e("PolyLine ", routes.toString());
                }
            }

        });


        //send.setOnClickListener(new ImageView.OnClickListener() {
        //  @Override
        //  public void onClick(View v) {
        //    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //     inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        //if (Util.Operations.isOnline(DirectionsActivity.this)){
        //        route();//}
        //  else {
        // Toast.makeText(DirectionsActivity.this, "No internet connectivity", Toast.LENGTH_SHORT).show();
        //       }
        //}
        //});
        //@Override
        //  public void onClick(View view) {
        //  Pop.routeList.add(new RouteDetails("Route 1", "UCD", "Rathmines"));
        //  Pop.routeListView.invalidateViews();
        //    Toast.makeText(DirectionsActivity.this,"Save Route to Parent",Toast.LENGTH_SHORT).show();
        /*
        These text watchers set the start and end points to null because once there's
        * a change after a value has been selected from the dropdown
        * then the value has to reselected from dropdown to get
        * the correct location.
        * */
        starting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int startNum, int before, int count) {
                findViewById(R.id.fabSaveRoute).setVisibility(View.GONE);
                if (start != null) {
                    start = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findViewById(R.id.fabSaveRoute).setVisibility(View.GONE);

                if (end != null) {
                    end = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Floating Action Button
        // FloatingActionButton saveRoute = (FloatingActionButton) findViewById(R.id.fabSaveRoute);

        //saveRoute.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //     public void onClick(View view) {
        //Pop.routeListView.invalidateViews();
        //    Toast.makeText(DirectionsActivity.this, "Route " + routeNo + " saved to Parent", Toast.LENGTH_SHORT).show();
        // Toast.makeText(DirectionsActivity.this, "Route " + routeNo + " details: ", Toast.LENGTH_SHORT).show();
        //}
        //});

        //       }
        //   })
    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.button_walk:
                modeTransport = AbstractRouting.TravelMode.WALKING;
                break;
            case R.id.button_cycle:
                modeTransport = AbstractRouting.TravelMode.BIKING;
                break;
        }
    }

    public void SaveRoute(View view) {
        Log.i("MyActivity", "SaveRoute called");
        RouteDetails route = new RouteDetails(start, end, "MyRoute", modeTransport, routeNo, 0);
        UserLocalStore userLocalStore;
        userLocalStore = new UserLocalStore(this);
        Users returnedUser = userLocalStore.getLoggedInUser();
        Log.i("MyActivity", "User id = " + returnedUser.get_id());
        Log.i("MyActivity", "Route co-ordinates = " + route.getStart().latitude + " + " + route.getStart().longitude);
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.saveRouteInBackground(returnedUser.get_id(), route);
        Log.i("MyActivity", "route ID = " + route.getRouteID());
    }


    //send.setOnClickListener(new View.OnClickListener() {
    //   @Override
    //    public void onClick(View view) {
    //   Pop.routeList.add(new RouteDetails("Route 1", "UCD", "Rathmines"));
    //      Pop.routeListView.invalidateViews();
//                Toast.makeText(DirectionsActivity.this,"Save Route to Parent",Toast.LENGTH_SHORT).show();

    @OnClick(R.id.send)
    public void sendRequest() {
        if (Util.Operations.isOnline(this)) {
            route();
        } else {
            Toast.makeText(this, "No internet connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    public void route() {
        if (start == null || end == null || modeTransport == null) {
            if (start == null) {
                if (starting.getText().length() > 0) {
                    starting.setError("Choose location from dropdown.");
                } else {
                    Toast.makeText(this, "Please choose a starting point.", Toast.LENGTH_SHORT).show();
                }
            }
            if (end == null) {
                if (destination.getText().length() > 0) {
                    destination.setError("Choose location from dropdown.");
                } else {
                    Toast.makeText(this, "Please choose a destination.", Toast.LENGTH_SHORT).show();
                }
            }
            if (modeTransport == null) {
                Toast.makeText(this, "Please choose a mode of transport.", Toast.LENGTH_SHORT).show();
            }
        } else {
            progressDialog = ProgressDialog.show(this, "Please wait.",
                    "Fetching route information.", true);

            Log.e("Route ", routes.toString());
            Routing routing = new Routing.Builder()
                    .travelMode(modeTransport)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(start, end)
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRoutingFailure() {
        // The Routing request failed
        progressDialog.dismiss();
        Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        findViewById(R.id.fabSaveRoute).setVisibility(View.GONE);
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        map.clear();
        progressDialog.dismiss();

        CameraUpdate center = newLatLngBounds(route.get(0).getLatLgnBounds(), 100);
        map.moveCamera(center);

        routes = route;

        for (int i = 0; i < route.size(); i++) {
            if (i != 0) {
                map.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.colorInactiveRoute)).addAll(route.get(i).getPoints()).width(20));
            } else {
                chosenRoute = route.get(i);
                routeNo = i + 1;
            }
            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_LONG).show();
        }

        Toast.makeText(getApplicationContext(), "Chosen route set to Route: " + routeNo, Toast.LENGTH_LONG).show();
        map.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.colorPrimary)).addAll(chosenRoute.getPoints()).width(20));

        // Start marker
        startOptions = new MarkerOptions();
        startOptions.position(start);
        startOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
        map.addMarker(startOptions);

        // End marker
        endOptions = new MarkerOptions();
        endOptions.position(end);
        endOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));
        map.addMarker(endOptions);

        findViewById(R.id.fabSaveRoute).setVisibility(View.VISIBLE);

    }

    @Override
    public void onRoutingCancelled() {
        Log.i(LOG_TAG, "Routing was cancelled.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(LOG_TAG, connectionResult.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
