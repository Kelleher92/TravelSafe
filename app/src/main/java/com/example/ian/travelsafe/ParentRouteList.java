package com.example.ian.travelsafe;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by temp2015 on 13/11/2015.
 */
public class ParentRouteList extends ArrayList<RouteDetails> {

    public static List<RouteDetails> routeList = new ArrayList<>();
    public void ParentRouteList() {

    }


    public static void clearRouteList(){
        routeList.clear();
    }

    public static void addToRouteList(RouteDetails cd){

        if(cd == null) {
        }
        else {
            routeList.add(cd);     // Add new Route to list
            Log.i("MyActivity", "attempting to add to list");
        }
    }

    public static void removeFromRouteList(RouteDetails cd) {
        // Remove from local list.
        routeList.remove(cd);
    }

}
