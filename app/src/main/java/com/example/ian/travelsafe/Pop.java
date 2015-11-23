package com.example.ian.travelsafe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Pop extends Activity {

    ListAdapter routeListAdapter;
    public static ListView routeListView;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_choose_popup);
        context = this;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        routeListAdapter = new RouteListAdapter(this, FragmentParentHomeChildren.routeList);
        routeListView = (ListView) findViewById(R.id.listViewRoutes);
        routeListView.setAdapter(routeListAdapter);
    }


//    private List<RouteDetails> getRouteDetails() {
//
//        List<RouteDetails> list = new ArrayList<>();
//        list.add(new RouteDetails("Route 1", "UCD", "Rathmines"));
//        list.add(new RouteDetails("Route 2", "UCD", "Rathmines"));
//        list.add(new RouteDetails("Route 3", "UCD", "Rathmines"));
//        list.add(new RouteDetails("Route 4", "UCD", "Rathmines"));
//        list.add(new RouteDetails("Route 5", "UCD", "Rathmines"));
//        list.add(new RouteDetails("Route 6", "UCD", "Rathmines"));
//        list.add(new RouteDetails("Route 7", "UCD", "Rathmines"));
//
//        return list;
//    }


}
