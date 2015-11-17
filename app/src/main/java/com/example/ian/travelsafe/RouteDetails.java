package com.example.ian.travelsafe;

public class RouteDetails {

    String mRouteTitle;
    String mRouteStart;
    String mRouteEnd;

    public RouteDetails(String routeTitle, String routeStart, String routeEnd) {

        mRouteTitle = routeTitle;
        mRouteStart = routeStart;
        mRouteEnd = routeEnd;
    }


    public String getmSettingTitle() {
        return mRouteTitle;
    }

    public String getRouteStart() {
        return mRouteStart;
    }

    public String getmRouteEnd() {
        return mRouteEnd;
    }
}
