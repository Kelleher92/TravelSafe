package com.example.ian.travelsafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ChildListAdapter extends ArrayAdapter<ChildDetails> {

    public ChildListAdapter(Context context, List<ChildDetails> listSettings) {

        super(context, R.layout.custom_child_row, listSettings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater childInflater = LayoutInflater.from(getContext());
        View customView = childInflater.inflate(R.layout.custom_child_row, parent, false);

        ChildDetails cd = getItem(position);

        TextView childName = (TextView) customView.findViewById(R.id.child_name);
        TextView childRoute = (TextView) customView.findViewById(R.id.child_route);
        ImageView childImage = (ImageView) customView.findViewById(R.id.child_photo);

        childName.setText(cd._name);
        childRoute.setText("Name of route attached to child");
        childImage.setImageResource(R.drawable.child_placeholder);

        return customView;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }

}
