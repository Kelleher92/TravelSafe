package com.example.ian.travelsafe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Pop extends Activity {

    ListAdapter routeListAdapter;
    public static ListView routeListView;
    public Context context;
    public static String callingActivity = "AssignRoute";
    public static String callingActivityAssign = "AssignRoute";
    public static String callingActivityDelete = "DeleteRoute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        FragmentParentHomeChildren.getCurrentRouteList();
        Log.i("!!!Routes","5 " + ParentRouteList.getCurrentRouteList().get(0).mRouteName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_choose_popup);
        context = this;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));
        routeListAdapter = new RouteListAdapter(callingActivity, this, ParentRouteList.getCurrentRouteList());
        routeListView = (ListView) findViewById(R.id.listViewRoutes);
        routeListView.setAdapter(routeListAdapter);
    }

}
