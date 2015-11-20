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

import java.io.IOException;
import java.util.List;
import android.location.Address;


/**
 * Created by Thomas on 29/10/2015.
 */
public class RouteListAdapter extends ArrayAdapter<RouteDetails> {
    Integer selected_position = -1;
    Context contextPopUp;
    public RouteListAdapter(Context context, List<RouteDetails> listSettings) {

        super(context, R.layout.custom_route_row, listSettings);
        contextPopUp = context;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        LayoutInflater routeListInflater = LayoutInflater.from(getContext());
        View customView = routeListInflater.inflate(R.layout.custom_route_row, parent, false);

        final RouteDetails rd = getItem(position);

        TextView tvRTitle = (TextView) customView.findViewById(R.id.route_title);
        TextView tvRStart = (TextView) customView.findViewById(R.id.route_details_Start);
        TextView tvREnd = (TextView) customView.findViewById(R.id.route_details_End);

//        try {
//            Address startLoc = new ChildHome().getAddressForLocation(contextPopUp, rd.getStart());
//            tvRStart.setText(startLoc.getAddressLine(0));
//            Address endLoc = new ChildHome().getAddressForLocation(contextPopUp, rd.getEnd());
//            tvREnd.setText(endLoc.getAddressLine(0));

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        tvRTitle.setText(rd.getmRouteName());

        CheckBox chkbox = (CheckBox) customView.findViewById(R.id.checkboxSelectRoute);

        if(position==selected_position) {
            chkbox.setChecked(true);
        }
        else {
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
                ((Activity)contextPopUp).finish();

            }
        });

        return customView;
    }
}
