package com.example.ian.travelsafe;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Pop extends Activity {

    ListAdapter routeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_choose_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));


        final List<RouteDetails> routeList = getRouteDetails();
        routeListAdapter = new RouteListAdapter(this, routeList);
        ListView routeListView = (ListView) findViewById(R.id.listViewRoutes);
        routeListView.setAdapter(routeListAdapter);
    }


    private List<RouteDetails> getRouteDetails() {

        List<RouteDetails> list = new ArrayList<>();
        list.add(new RouteDetails("Route 1", "UCD", "Rathmines"));
        list.add(new RouteDetails("Route 2", "UCD", "Rathmines"));
        list.add(new RouteDetails("Route 3", "UCD", "Rathmines"));
        list.add(new RouteDetails("Route 4", "UCD", "Rathmines"));
        list.add(new RouteDetails("Route 5", "UCD", "Rathmines"));
        list.add(new RouteDetails("Route 6", "UCD", "Rathmines"));
        list.add(new RouteDetails("Route 7", "UCD", "Rathmines"));

        return list;
    }


}
