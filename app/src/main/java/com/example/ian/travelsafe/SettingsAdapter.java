package com.example.ian.travelsafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Thomas on 29/10/2015.
 */
public class SettingsAdapter extends ArrayAdapter<SettingDetails> {

    public SettingsAdapter(Context context, List<SettingDetails> listSettings) {

        super(context, R.layout.custom_setting_row, listSettings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater settingsInflater = LayoutInflater.from(getContext());
        View customView = settingsInflater.inflate(R.layout.custom_setting_row, parent, false);

        SettingDetails sd = getItem(position);

        TextView tv = (TextView) customView.findViewById(R.id.setting_title);
        ImageView iv = (ImageView) customView.findViewById(R.id.setting_icon);

        tv.setText(sd.mSettingTitle);

        iv.setImageResource(sd.mSettingImage);

        return customView;
    }
}
