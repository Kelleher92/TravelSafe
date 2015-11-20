package com.example.ian.travelsafe;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by temp2015 on 20/11/2015.
 */
public class CheckForUpdatesThread extends Thread {

    Context ctext;
    boolean running = false;
    final static String ACTION = "NotifyServiceAction";
    NotificationManager notificationManager;
    Notification myNotification;
    NotificationDetails notificationDetails = new NotificationDetails("", "");
    private final String myBlog = "http://android-er.blogspot.com/";
    private static final int MY_NOTIFICATION_ID = 1;
    List<ChildDetails> lcd = new ArrayList<ChildDetails>();
    ServerRequests sreq;

    void setRunning(boolean b) {
        running = b;
    }

    public synchronized void start(Context context) {
        // TODO Auto-generated method stub
        super.start();
        ctext = context;
        notificationManager = (NotificationManager) ctext.getSystemService(Context.NOTIFICATION_SERVICE);
        lcd = ParentChildList.getCurrentChildList();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (running) {
            try {
                sleep(10000);           //Check every 10 seconds.
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.i("CheckForUpdates", "new updatecheck");
            // Server request to check for notifications from child.
            UserLocalStore current = new UserLocalStore(ctext);
            Users currentUser = current.getLoggedInUser();

            for (ChildDetails cd : lcd) {
                sreq = new ServerRequests(ctext);
                NotificationDetails noteDetails = sreq.fetchEventsAsyncTask(currentUser.get_id(), cd, ctext, new GetEventCallback() {
                    @Override
                    public void done(NotificationDetails returnedNotification) {
                        if (returnedNotification == null) {
                            Log.i("ChildHome", "No route returned");
                        } else {
                            notificationDetails = returnedNotification;
                        }
                    }
                });

                if(notificationDetails != null) {
                    // Send Notification
                    //            myNotification = new Notification(R.drawable.icon, "Notification!", System.currentTimeMillis());
                    //            Context context = getApplicationContext();
                    //            String notificationTitle = "Exercise of Notification!";
                    //            String notificationText = "http://android-er.blogspot.com/";
                    //            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
                    //            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
                    //            myNotification.defaults |= Notification.DEFAULT_SOUND;
                    //            myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                    //            myNotification.setLatestEventInfo(context,
                    //                    notificationTitle,
                    //                    notificationText,
                    //                    pendingIntent);
                    //            notificationManager.notify(MY_NOTIFICATION_ID, myNotification);

                }

            }
        }
    }
}
