package com.example.ian.travelsafe;

import com.google.android.gms.maps.model.LatLng;

public class RouteDetails {

    LatLng start;
    LatLng end;
    String mRouteName;
    AbstractRouting.TravelMode modeTransport;
    int index;


    public RouteDetails(LatLng rstart, LatLng rend, String RouteName, AbstractRouting.TravelMode RmodeTransport, int Rindex) {

        start = rstart;
        end = rend;
        mRouteName = RouteName;
        modeTransport = RmodeTransport;
        index = Rindex;
    }

}
