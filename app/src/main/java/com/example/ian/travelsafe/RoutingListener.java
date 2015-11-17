package com.example.ian.travelsafe;

import java.util.ArrayList;

public interface RoutingListener {
    void onRoutingFailure();

    void onRoutingStart();

    void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex);

    void onRoutingCancelled();
}
