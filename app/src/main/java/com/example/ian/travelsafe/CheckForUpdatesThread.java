package com.example.ian.travelsafe;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by temp2015 on 20/11/2015.
 */
public class CheckForUpdatesThread extends Thread {

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://travelsafe.esy.es/";
    Context ctext;
    boolean running = false;
    final static String ACTION = "NotifyServiceAction";
    NotificationManager notificationManager;
    NotificationCompat.Builder notification;
    private static int uniqueID = 1;
    NotificationDetails notificationDetails;
    private final String myBlog = "http://android-er.blogspot.com/";
    private static final int MY_NOTIFICATION_ID = 1;
    List<ChildDetails> lcd = new ArrayList<ChildDetails>();

    void setRunning(boolean b) {
        running = b;
    }

    public synchronized void start(Context context) {
        // TODO Auto-generated method stub
        super.start();
        ctext = context;
        notificationManager = (NotificationManager) ctext.getSystemService(Context.NOTIFICATION_SERVICE);
        lcd = ParentChildList.getCurrentChildList();
        Log.i("CheckForUpdates", "1. current child list = " + lcd);
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
            Log.i("CheckForUpdates", "2. current child list = " + lcd);

            for (ChildDetails cd : lcd) {
                fetchEvents(currentUser.get_id(), cd.get_id(), cd.get_name(), ctext, new GetEventCallback() {
                    @Override
                    public void done(NotificationDetails returnedNotification) {
                        if (returnedNotification == null) {
                            Log.i("CheckForUpdates", "No notification");
                        } else {
                            Log.i("CheckForUpdates", "New notification ");
                            notificationDetails = returnedNotification;
//                            notificationDetails = new NotificationDetails("Child Name"," This is a notification.");
                            notification = new NotificationCompat.Builder(ctext);
                            notification.setAutoCancel(true);
                            createNotification(notificationDetails);
                        }
                    }
                });

            }
        }
    }

    public void createNotification(NotificationDetails details) {

        notification.setSmallIcon(R.drawable.child_cycle_blue);
        notification.setTicker("New Route Update");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(details.getChildName());     // Child name
        notification.setContentText(details.getNote());

        Intent intent = new Intent(ctext, ParentHome.class);
        PendingIntent pI = PendingIntent.getActivity(ctext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pI);
        notification.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        // Build notification and issue it.
        notificationManager.notify(uniqueID, notification.build());
        uniqueID++;
    }

    public static NotificationDetails fetchEvents(int parentid, int childid, String name, Context context, GetEventCallback eventCallback) {
        new fetchEventsAsyncTask(parentid, childid, name, context, eventCallback).execute();
        Log.i("FetchEvent", "************** " + parentid + childid + name);
        return null;
    }

    public static class fetchEventsAsyncTask extends AsyncTask<Void, Void, NotificationDetails> {
        GetEventCallback eventCallback;
        int parentid;
        int childid;
        String name;
        private Context mContext;

        public fetchEventsAsyncTask(int parentid, int cid, String cname, Context context, GetEventCallback eventCallback) {
            this.childid = cid;
            this.parentid = parentid;
            this.name = cname;
            this.eventCallback = eventCallback;
            mContext = context;
        }

        @Override
        protected NotificationDetails doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("parentid", parentid + ""));
            dataToSend.add(new BasicNameValuePair("childid", childid + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchEvents.php");

            Log.i("FetchEvent", "Sent to server " + dataToSend);
            NotificationDetails returnedNotification = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.i("FetchEvent", "result = " + result);
                JSONArray jObject = new JSONArray(result);

                if (jObject.length() == 0)
                    Log.i("FetchEvent", "No response");
                else {
                    JSONArray jArray = new JSONArray(result);
                    int eventid = jArray.getJSONObject(0).getInt("eventid");
                    String eventType = jArray.getJSONObject(0).getString("event_type");
                    String timeLogged = jArray.getJSONObject(0).getString("time_logged");
                    Log.i("FetchEvent", "Received from server " + eventid + eventType + timeLogged);

                    returnedNotification = new NotificationDetails(name, eventType);
                }

            } catch (Exception e) {
                Log.i("FetchEvent", "Catch  Exception");
                e.printStackTrace();
            }
            return returnedNotification;
        }

        @Override
        protected void onPostExecute(NotificationDetails note) {
            eventCallback.done(note);
            super.onPostExecute(note);
        }
    }
}
