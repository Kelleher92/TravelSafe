package com.example.ian.travelsafe;

import com.google.android.gms.maps.model.LatLng;

public class RouteDetails {

    LatLng start;
    LatLng end;
    String mRouteName;
    AbstractRouting.TravelMode modeTransport;
    int index, routeID;


    public RouteDetails(LatLng rstart, LatLng rend, String RouteName, AbstractRouting.TravelMode RmodeTransport, int Rindex, int routeID) {
        start = rstart;
        end = rend;
        mRouteName = RouteName;
        modeTransport = RmodeTransport;
        index = Rindex;
        routeID = routeID;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public AbstractRouting.TravelMode getModeTransport() {

        return modeTransport;
    }

    public void setModeTransport(AbstractRouting.TravelMode modeTransport) {
        this.modeTransport = modeTransport;
    }

    public String getmRouteName() {

        return mRouteName;
    }

    public void setmRouteName(String mRouteName) {
        this.mRouteName = mRouteName;
    }

    public LatLng getEnd() {

        return end;
    }

    public void setEnd(LatLng end) {
        this.end = end;
    }

    public LatLng getStart() {
        return start;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }
}
