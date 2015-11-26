package com.example.ian.travelsafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.Marker;
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
    protected double routeDistance;
    private String newRouteName = "";
    protected int routeNo;

    @InjectView(R.id.start)
    AutoCompleteTextView starting;
    @InjectView(R.id.destination)
    AutoCompleteTextView destination;
    @InjectView(R.id.send)
    ImageView send;

    private String LOG_TAG = "DirectionsActivity";
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<Route> routes;
    private ArrayList<MarkerOptions> routeOptionsList;
    // ArrayList<PolylineOptions> polylineOptionsList;
    private Marker lastOpened = null;

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

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                // Check if there is an open info window
                if (lastOpened != null) {
                    // Close the info window
                    lastOpened.hideInfoWindow();

                    // Is the marker the same marker that was already open
                    if (lastOpened.equals(marker)) {
                        // Nullify the lastOpened object
                        lastOpened = null;
                        // Return so that the info window isn't opened again
                        return true;
                    }
                }

                // Open the info window for the marker
                marker.showInfoWindow();
                // Re-assign the last opened such that we can close it later
                lastOpened = marker;

                // Event was handled by our code do not launch default behaviour.
                return true;
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

                            if (results[0] < (routeDistance * 50)) {
                                map.clear();
                                map.addMarker(startOptions);
                                map.addMarker(endOptions);
                                //routeOptions.position(iroute.getPoints().get(iroute.getPoints().size()/ 2));
                                //routeOptions.title("Route " + routes.indexOf(iroute));
                                // map.addMarker(routeOptions).showInfoWindow();


                                for (Route r : routes) {
                                    //routeOptions(routes.indexOf(r)).position(r.getPoints().get(r.getPoints().size() / 2));
                                    //routeOptions.title("Route " + routes.indexOf(r));
                                    //map.addMarker(routeOptionsList.get(routes.indexOf(r))).showInfoWindow();

                                    if (r != iroute) {
                                        map.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.colorInactiveRoute)).addAll(r.getPoints()).width(20));
                                        //routeOptionsList.get(routes.indexOf(r)).visible(false);
                                    } else {
                                        //chosenRoute = r;
                                        routeNo = routes.indexOf(r);
                                        //routeOptionsList.get(routes.indexOf(r)).visible(true);
                                    }
                                }

                                map.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.colorPrimary)).addAll(routes.get(routeNo).getPoints()).width(20));
                                //map.addMarker(routeOptionsList.get(routeNo)).showInfoWindow();
                                lastOpened = map.addMarker(routeOptionsList.get(routeNo));
                                lastOpened.showInfoWindow();
                            }

                        }

                    }


//                    Toast.makeText(getApplicationContext(), "Chosen route set to Route: " + (routeNo + 1), Toast.LENGTH_SHORT).show();
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
        FloatingActionButton saveRoute = (FloatingActionButton) findViewById(R.id.fabSaveRoute);

        saveRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Pop.routeListView.invalidateViews();
                SaveRoute(view);
            }
        });

    }
//           })
//    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.button_walk:
                modeTransport = AbstractRouting.TravelMode.WALKING;
                findViewById(R.id.fabSaveRoute).setVisibility(View.GONE);
                break;
            case R.id.button_cycle:
                modeTransport = AbstractRouting.TravelMode.BIKING;
                findViewById(R.id.fabSaveRoute).setVisibility(View.GONE);
                break;
            default:
                modeTransport = AbstractRouting.TravelMode.WALKING;
                findViewById(R.id.fabSaveRoute).setVisibility(View.GONE);
        }
    }

    public void SaveRoute(View view) {
        Log.i("MyActivity", "SaveRoute called");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHeight(100);
        input.setWidth(240);
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(1);
        int maxLength = 10;
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        input.setHint("New Route");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newRouteName = input.getText().toString();
                RouteDetails route = new RouteDetails(start, end, newRouteName, modeTransport, routeNo, 0);
                UserLocalStore userLocalStore;
                userLocalStore = new UserLocalStore(DirectionsActivity.this);
                Users returnedUser = userLocalStore.getLoggedInUser();
                Log.i("Save route", "User id = " + returnedUser.get_id());
                Log.i("Save route", "Route co-ordinates = " + route.getStart().latitude + " + " + route.getStart().longitude);
                ServerRequests serverRequests = new ServerRequests(DirectionsActivity.this);
                serverRequests.saveRouteInBackground(returnedUser.get_id(), route, new GetRouteCallback() {
                    @Override
                    public void done(RouteDetails returnedRoute) {
//                        FragmentParentHomeChildren.routeList.add(returnedRoute);
//                        ParentRouteList.addToRouteList(returnedRoute);
                        Log.i("MyActivity", "************* route ID = " + returnedRoute.getRouteID());
                    }
                });
                Intent intent = new Intent(DirectionsActivity.this, ParentHome.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public static Bitmap flipBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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

        Bitmap bmWalkScaled = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.child_run_blue), 100, 100, true);
        Bitmap bmCycleScaled = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.child_cycle_blue), 100, 100, true);
        Bitmap bmStartScaled = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.start_blue), 100, 100, true);
        Bitmap bmEndScaled = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.end_green), 100, 100, true);

        Bitmap bmWalkscaleddisplay;
        Bitmap bmCyclescaleddisplay;

        CameraUpdate center = newLatLngBounds(route.get(0).getLatLgnBounds(), 200);
        map.moveCamera(center);

        routes = route;

        routeOptionsList = new ArrayList<>();

        for (int i = 0; i < route.size(); i++) {
            routeDistance = Math.round(((double) route.get(i).getDistanceValue() / 1000) * 1000.0) / 1000.0;
            double routeDuration = Math.round(((double) route.get(i).getDurationValue() / 60) * 1000.0) / 1000.0;
            int routeDurationHours = ((int) routeDuration) / 60;
            int routeDurationMins = ((int) routeDuration) % 60;


            MarkerOptions routeOptions = new MarkerOptions();
            routeOptionsList.add(i, routeOptions);
            routeOptionsList.get(i).position(route.get(i).getPoints().get((route.get(i).getPoints().size()) / 5));
            routeOptionsList.get(i).title("Route " + (i + 1));
            routeOptionsList.get(i).snippet("Distance: " + routeDistance + "km\nDuration: " + routeDurationHours + "hr " + routeDurationMins + "min");

            if ((start.longitude) > (end.longitude)) {
                bmWalkscaleddisplay = flipBitmap(bmWalkScaled);
                bmCyclescaleddisplay = flipBitmap(bmCycleScaled);
            } else {
                bmWalkscaleddisplay = bmWalkScaled;
                bmCyclescaleddisplay = bmCycleScaled;
            }

            if (modeTransport == AbstractRouting.TravelMode.WALKING) {
                routeOptionsList.get(i).icon(BitmapDescriptorFactory.fromBitmap(bmWalkscaleddisplay));
            } else {
                routeOptionsList.get(i).icon(BitmapDescriptorFactory.fromBitmap(bmCyclescaleddisplay));
            }

            if (i != 0) {
                //polylineOptionsList.get(i).color(getResources().getColor(R.color.colorInactiveRoute)).addAll(route.get(i).getPoints()).width(20);
                map.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.colorInactiveRoute)).addAll(route.get(i).getPoints()).width(20));
                //routeOptionsList.get(i).visible(false);
            } else {
                //polylineOptionsList.get(i).color(getResources().getColor(R.color.colorPrimary)).addAll(route.get(i).getPoints()).width(20);
                //chosenPoly = route.get(i);
                routeNo = i;

            }

            //Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_LONG).show();
        }

//        Toast.makeText(getApplicationContext(), "Chosen route set to Route: " + (routeNo + 1), Toast.LENGTH_SHORT).show();
        map.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.colorPrimary)).addAll(route.get(routeNo).getPoints()).width(20));
        lastOpened = map.addMarker(routeOptionsList.get(routeNo));
        lastOpened.showInfoWindow();

        // Start marker
        startOptions = new MarkerOptions();
        startOptions.position(start);
        startOptions.icon(BitmapDescriptorFactory.fromBitmap(bmStartScaled));
        map.addMarker(startOptions);

        // End marker
        endOptions = new MarkerOptions();
        endOptions.position(end);
        endOptions.icon(BitmapDescriptorFactory.fromBitmap(bmEndScaled));
        map.addMarker(endOptions);

        findViewById(R.id.fabSaveRoute).setVisibility(View.VISIBLE);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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
