package com.example.ian.travelsafe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class Notifications extends ActionBarActivity {

    NotificationCompat.Builder notification;
    private  static final  int uniqueID = 45678;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
    }

    public void createNote(View view) {
        notification.setSmallIcon(R.drawable.child_cycle_blue);
        notification.setTicker("New Route Update");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("New Update");
        notification.setContentText("Your child is making progress");

        Intent intent = new Intent(this, ParentHome.class);
        PendingIntent pI = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pI);

        // Build notification and issue it.
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());

    }
}
