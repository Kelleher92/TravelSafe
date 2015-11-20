package com.example.ian.travelsafe;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class FragmentParentHomeAccount extends Fragment {

    UserLocalStore userLocalStore;

    NotificationCompat.Builder notification;
    NotificationDetails nD = new NotificationDetails("Thomas", "Reached halfway");
    private  static int uniqueID = 1;

    public static FragmentParentHomeAccount newInstance() {
        return new FragmentParentHomeAccount();
    }

    public FragmentParentHomeAccount() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_home_account, container, false);

        // Initialise notifications
        notification = new NotificationCompat.Builder(this.getContext());
        notification.setAutoCancel(true);

        final List<SettingDetails> settingList = getSettingsDetails();
        ListAdapter settingsAdapter = new SettingsAdapter(this.getContext(), settingList);
        ListView settingsListView = (ListView) view.findViewById(R.id.listOfSettings);
        settingsListView.setAdapter(settingsAdapter);

        settingsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SettingDetails sd = (SettingDetails) parent.getItemAtPosition(position);

                        String settingTitle = sd.mSettingTitle;
                        switch (position) {
                            // New Route
                            case 0:
                               Intent i = new Intent(view.getContext(), DirectionsActivity.class);
                                startActivity(i);
                                break;
                            // Log Out
                            case 1:
                                userLocalStore.clearUserData();
                                userLocalStore.setUserLoggedIn(false);
                                ParentChildList.clearChildList();
                                returnToLoginPage();
                                break;
                            // Test child home
                            case 2:
                                Intent in = new Intent(view.getContext(), ChildHome.class);
                                startActivity(in);
                                break;
                            // Test notifications
                            case 3:
//                                startActivity(new Intent(FragmentParentHomeAccount.this.getContext(), Pop.class));
                                createNotification(nD);
                                break;
                        }
                    }
                }
        );
        return view;
    }

    public void returnToLoginPage() {
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private List<SettingDetails> getSettingsDetails() {

        List<SettingDetails> list= new ArrayList<>();
        list.add(new SettingDetails("Create new route", R.drawable.ic_place_black_24dp));
        list.add(new SettingDetails("Log Out", R.drawable.ic_power_settings_new_black_24dp));
        list.add(new SettingDetails("View ChildHome", R.drawable.child_placeholder));
        list.add(new SettingDetails("Test notifications", R.drawable.ic_notifications_black_24dp));

        return list;
    }


    public void createNotification(NotificationDetails details) {
        notification.setSmallIcon(R.drawable.child_cycle_blue);
        notification.setTicker("New Route Update");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(details.getChildName());     // Child name
        notification.setContentText(details.getNote());

        Intent intent = new Intent(this.getContext(), ParentHome.class);
        PendingIntent pI = PendingIntent.getActivity(this.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pI);

        // Build notification and issue it.
        NotificationManager nm = (NotificationManager) this.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());
        uniqueID++;
    }
}