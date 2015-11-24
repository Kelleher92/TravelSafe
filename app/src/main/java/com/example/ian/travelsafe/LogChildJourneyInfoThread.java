package com.example.ian.travelsafe;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

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
public class LogChildJourneyInfoThread extends Thread {

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://travelsafe.esy.es/";
    Context ctext;
    boolean running = false;
    final static String ACTION = "NotifyServiceAction";
    NotificationManager notificationManager;
    NotificationCompat.Builder notification;
    private static int uniqueID = 1;
    NotificationDetails notificationDetails;
    private List<LatLng> polyList;
    private GoogleMap map;

    void setRunning(boolean b, List<LatLng> inPolyList) {
        running = b;
        polyList = inPolyList;
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        map = mapFragment.getMap();
    }

    public synchronized void start(Context context) {
        // TODO Auto-generated method stub
        super.start();
        ctext = context;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (running) {
                    try {
                        Thread.sleep(10000);           //Check every 10 seconds.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Check if still on path
                    LatLng myLoc = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
                    boolean onpath = PolyUtil.isLocationOnEdge(myLoc, polyList, true, 50);
                    if(!onpath) {
                        Log.i("StartJourney", "On Path = " + onpath);
                    }
        }
    }


    // Logging information.
    public static NotificationDetails fetchEvents(int parentid, ChildDetails cd, Context context, GetEventCallback eventCallback) {
        new fetchEventsAsyncTask(parentid, cd, context, eventCallback).execute();
        return null;
    }

    public static class fetchEventsAsyncTask extends AsyncTask<Void, Void, NotificationDetails> {
        GetEventCallback eventCallback;
        int parentid;
        ChildDetails cd;
        private Context mContext;

        public fetchEventsAsyncTask(int parentid, ChildDetails cd, Context context, GetEventCallback eventCallback) {
            this.cd = cd;
            this.parentid = parentid;
            this.eventCallback = eventCallback;
            mContext = context;
        }

        @Override
        protected NotificationDetails doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("parentid", parentid + ""));
            dataToSend.add(new BasicNameValuePair("childid", cd.get_id() + ""));

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

                    returnedNotification = new NotificationDetails(cd.get_name(), eventType);
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
