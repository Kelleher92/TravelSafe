package com.example.ian.travelsafe;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Thomas on 29/10/2015.
 */
public class RouteListAdapter extends ArrayAdapter<RouteDetails> {
    Integer selected_position = -1;
    Context contextPopUp;
    String activity;

    public RouteListAdapter(String callingActivity, Context context, List<RouteDetails> listSettings) {
        super(context, R.layout.custom_route_row, listSettings);
        contextPopUp = context;
        activity = callingActivity;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        LayoutInflater routeListInflater = LayoutInflater.from(getContext());
        View customView = routeListInflater.inflate(R.layout.custom_route_row, parent, false);

        final RouteDetails rd = getItem(position);

        TextView tvRTitle = (TextView) customView.findViewById(R.id.route_title);
        TextView tvRStart = (TextView) customView.findViewById(R.id.route_details_Start);
        TextView tvREnd = (TextView) customView.findViewById(R.id.route_details_End);

        tvRTitle.setText(rd.getmRouteName());
        try {
            tvRStart.setText(ChildHome.getAddressForLocation(contextPopUp, new LatLng(rd.getStart().latitude, rd.getStart().longitude)).getAddressLine(0));
            tvREnd.setText(ChildHome.getAddressForLocation(contextPopUp, new LatLng(rd.getEnd().latitude, rd.getEnd().longitude)).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        CheckBox chkbox = (CheckBox) customView.findViewById(R.id.checkboxSelectRoute);

        if (position == selected_position) {
            chkbox.setChecked(true);
        } else {
            chkbox.setChecked(false);
        }

        chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selected_position = position;
                } else {
                    selected_position = -1;
                }
                notifyDataSetChanged();

                ServerRequests serverRequests = new ServerRequests(contextPopUp);
                if (activity.equals(Pop.callingActivityAssign)) {
                    serverRequests.attachRoute(FragmentParentHomeChildren.childClicked.get_id(), ParentRouteList.getCurrentRouteList().get(position).getRouteID(), new GetRouteCallback() {
                        @Override
                        public void done(RouteDetails returnedRoute) {

                        }
                    });
                    ((Activity) contextPopUp).finish();

                } else {
                    if (activity.equals(Pop.callingActivityDelete)) {
                        serverRequests.deleteRoute(FragmentParentHomeChildren.childClicked.get_id(), ParentRouteList.getCurrentRouteList().get(position).getRouteID(), new GetRouteCallback() {
                            @Override
                            public void done(RouteDetails returnedRoute) {

                            }
                        });
                        ParentRouteList.removeFromRouteList(ParentRouteList.getCurrentRouteList().get(position));
                        ((Activity) contextPopUp).finish();

                    }
                }
            }
        });
        return customView;
    }
}