package com.example.ian.travelsafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Thomas on 29/10/2015.
 */
public class RouteListAdapter extends ArrayAdapter<RouteDetails> {

    public RouteListAdapter(Context context, List<RouteDetails> listSettings) {

        super(context, R.layout.custom_route_row, listSettings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater routeListInflater = LayoutInflater.from(getContext());
        View customView = routeListInflater.inflate(R.layout.custom_route_row, parent, false);

        RouteDetails rd = getItem(position);

        TextView tvRTitle = (TextView) customView.findViewById(R.id.route_title);
        TextView tvRStart = (TextView) customView.findViewById(R.id.route_details_Start);
        TextView tvREnd = (TextView) customView.findViewById(R.id.route_details_End);

        tvRTitle.setText(rd.mRouteTitle);
        tvRStart.setText(rd.mRouteStart);
        tvREnd.setText(rd.mRouteEnd);

        return customView;
    }
}
