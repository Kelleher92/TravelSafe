package com.example.ian.travelsafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ian on 06/11/2015.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://travelsafe.esy.es/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(Users user, GetUserCallback userCallback, Context context) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback, context).execute();
    }

    public void storeChildDataInBackground(ChildDetails child, GetChildCallback childCallback, Context context) {
        progressDialog.show();
        new StoreChildDataAsyncTask(child, childCallback, context).execute();
    }

    public void fetchUserDataInBackground(Users user, GetUserCallback userCallback) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchChildDataInBackground(int id, List<ChildDetails> children, GetChildrenCallback childrenCallback) {
        progressDialog.show();
        new fetchChildDataAsyncTask(id, children, childrenCallback).execute();
    }

    public void fetchRouteDataInBackground(int id, List<RouteDetails> routes, GetRoutesCallback routesCallback) {
        progressDialog.show();
        new fetchRouteDataAsyncTask(id, routes, routesCallback).execute();
    }

    public void removeChildInBackground(ChildDetails child) {
        new removeChildAsyncTask(child).execute();
    }

    public void saveRouteInBackground(int userid, RouteDetails route, GetRouteCallback routeCallback) {
        new saveRouteAsyncTask(userid, route, routeCallback).execute();
    }

    public RouteDetails fetchChildAttachedRoute(int childID, GetRouteCallback routeCallback) {
        new fetchChildAttachedRouteAsyncTask(childID, routeCallback).execute();
        return null;
    }

    public RouteDetails attachRoute(int childid, int routeID, GetRouteCallback routeCallback) {
        new attachRouteAsyncTask(childid, routeID, routeCallback).execute();
        return null;
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        Users user;
        GetUserCallback userCallback;
        private Context mContext;

        public StoreUserDataAsyncTask(Users user, GetUserCallback userCallback, Context context) {
            mContext = context;
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user._username));
            dataToSend.add(new BasicNameValuePair("emailaddress", user._emailAddress));
            dataToSend.add(new BasicNameValuePair("password", user._password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            Log.i("MyActivity", "Sent to server");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                Log.i("MyActivity", "Sent to server");

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() == 0)
                    Log.i("MyActivity", "No response");
                else {
                    int parentid = jObject.getInt("id");
                    Log.i("MyActivity", "parent id = " + parentid);
                    user.set_id(parentid);
                    UserLocalStore userLocalStore = new UserLocalStore(mContext);
                    userLocalStore.storeUserData(user);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class StoreChildDataAsyncTask extends AsyncTask<Void, Void, Void> {
        ChildDetails child;
        GetChildCallback childCallback;
        private Context mContext;

        public StoreChildDataAsyncTask(ChildDetails child, GetChildCallback childCallback, Context context) {
            this.child = child;
            this.childCallback = childCallback;
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("parentid", child._parentid + ""));
            dataToSend.add(new BasicNameValuePair("name", child._name));
            dataToSend.add(new BasicNameValuePair("username", child._username));
            dataToSend.add(new BasicNameValuePair("password", child._password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "RegisterChild.php");

            Log.i("MyActivity", "Sent to server");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() == 0)
                    Log.i("MyActivity", "No response");
                else {
                    int childId = jObject.getInt("id");
                    Log.i("MyActivity", "parent id = " + childId);
                    child.set_id(childId);
                    UserLocalStore userLocalStore = new UserLocalStore(mContext);
                    userLocalStore.storeUserData(child);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            childCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class fetchChildAttachedRouteAsyncTask extends AsyncTask<Void, Void, RouteDetails> {
        GetRouteCallback routeCallback;
        int id;

        public fetchChildAttachedRouteAsyncTask(int id, GetRouteCallback routeCallback) {
            this.routeCallback = routeCallback;
            this.id = id;
        }

        @Override
        protected RouteDetails doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("parentid", id + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchChildRoute.php");

            RouteDetails returnedRoute = null;
            Log.i("FetchChildRoute", "Fetch php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                Log.i("FetchChildRoute", "Sent id " + id + " to server");

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                Log.i("","")
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() == 0)
                    Log.i("FetchChildRoute", "No response");
                else {
                    LatLng start = new LatLng(jObject.getDouble("start_lat"), jObject.getDouble("start_long"));
                    LatLng end = new LatLng(jObject.getDouble("end_lat"), jObject.getDouble("end_long"));
                    int id = jObject.getInt("childid");
                    int parentid = jObject.getInt("parentid");
                    String routeName = jObject.getString("route_name");
                    int routeID = jObject.getInt("routeid");
                    int index = jObject.getInt("index");
                    String mod = jObject.getString("mode");
                    AbstractRouting.TravelMode mode;
                    switch(mod){
                        case "BIKING": mode = AbstractRouting.TravelMode.BIKING;
                            break;
                        case "WALKING": mode = AbstractRouting.TravelMode.WALKING;
                            break;
                        default: mode = AbstractRouting.TravelMode.WALKING;
                            break;
                    }

                    returnedRoute = new RouteDetails(start, end, routeName, mode, index, routeID);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyActivity", "Could not fetch route with server request");
            }

            return returnedRoute;
        }

        @Override
        protected void onPostExecute(RouteDetails route) {
            progressDialog.dismiss();
            routeCallback.done(route);
            super.onPostExecute(route);
        }
    }

    public class attachRouteAsyncTask extends AsyncTask<Void, Void, RouteDetails> {
        GetRouteCallback routeCallback;
        int childid;
        int routeID;

        public attachRouteAsyncTask(int childid, int routeID, GetRouteCallback routeCallback) {
            this.routeCallback = routeCallback;
            this.childid = childid;
            this.routeID = routeID;
        }

        @Override
        protected RouteDetails doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("childid", childid + ""));
            dataToSend.add(new BasicNameValuePair("routeid", routeID + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "AssignRoute.php");

            RouteDetails returnedRoute = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                Log.i("MyActivity", "Sent " + childid + " " + routeID + " to server");

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyActivity", "EXCEPTION");
            }

            return returnedRoute;
        }

        @Override
        protected void onPostExecute(RouteDetails route) {
            progressDialog.dismiss();
            routeCallback.done(route);
            super.onPostExecute(route);
        }
    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, Users> {
        Users user;
        GetUserCallback userCallback;

        public fetchUserDataAsyncTask(Users user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Users doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user._username));
            dataToSend.add(new BasicNameValuePair("password", user._password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            Users returnedUser = null;
            Log.i("MyActivity", "Fetch php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                Log.i("MyActivity", "Sent to server - " + dataToSend + "    " + post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() == 0)
                    Log.i("MyActivity", "No response");
                else {
                    int id = jObject.getInt("id");
                    String emailaddress = jObject.getString("emailaddress");
                    String username = jObject.getString("username");
                    String password = jObject.getString("password");
                    String flag = jObject.getString("flag");
                    Log.i("MyActivity", id + emailaddress + username + password + flag);

                    returnedUser = new Users(id, emailaddress, username, password, flag);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyActivity", "EXCEPTION");
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(Users user) {
            progressDialog.dismiss();
            userCallback.done(user);
            super.onPostExecute(user);
        }
    }

    public class fetchRouteDataAsyncTask extends AsyncTask<Void, Void, List<RouteDetails>> {
        List<RouteDetails> routes;
        GetRoutesCallback routesCallback;
        int id;

        public fetchRouteDataAsyncTask(int id, List<RouteDetails> routes, GetRoutesCallback routesCallback) {
            this.routes = routes;
            this.routesCallback = routesCallback;
            this.id = id;
        }

        @Override
        protected List<RouteDetails> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("parentid", id + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchRoutes.php");

            List<RouteDetails> returnedRoutes = new ArrayList<RouteDetails>();

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                Log.i("MyActivity", "Sent id " + id + " to server");

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONArray jArray = new JSONArray(result);

                if (jArray.length() == 0)
                    Log.i("MyActivity", "No response from FetchRoutes");
                else {
                    Log.i("MyActivity", "jObject length = " + jArray.length());

                    for (int x = 0; x < jArray.length(); x++) {
                        int routeid = jArray.getJSONObject(x).getInt("routeid");
                        int parentid = jArray.getJSONObject(x).getInt("parentid");
                        int index = jArray.getJSONObject(x).getInt("route_index");
                        String route_name = jArray.getJSONObject(x).getString("route_name");
                        String travel_mode = jArray.getJSONObject(x).getString("travel_mode");
                        LatLng start = new LatLng(jArray.getJSONObject(x).getDouble("start_lat"), jArray.getJSONObject(x).getDouble("start_long"));
                        LatLng end = new LatLng(jArray.getJSONObject(x).getDouble("end_lat"), jArray.getJSONObject(x).getDouble("end_long"));
                        AbstractRouting.TravelMode mode;

                        switch (travel_mode){
                            case "BIKING" : mode = AbstractRouting.TravelMode.BIKING;
                                break;
                            case "WALKING" : mode = AbstractRouting.TravelMode.WALKING;
                                break;
                            default: mode = AbstractRouting.TravelMode.WALKING;

                        }

                        Log.i("MyActivity", "Returned route " + x + " = " + routeid + parentid + index + route_name + travel_mode + start + end);

                        returnedRoutes.add(new RouteDetails(start, end, route_name, mode, index, routeid));

                    }
                    Log.i("MyActivity", "returnedRoutes size = " + returnedRoutes.size());

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyActivity", "EXCEPTION");
            }

            return returnedRoutes;
        }

        @Override
        protected void onPostExecute(List<RouteDetails> routes) {
            progressDialog.dismiss();
            routesCallback.done(routes);
            super.onPostExecute(routes);
        }
    }

    public class fetchChildDataAsyncTask extends AsyncTask<Void, Void, List<ChildDetails>> {
        List<ChildDetails> children;
        GetChildrenCallback childrenCallback;
        int id;

        public fetchChildDataAsyncTask(int id, List<ChildDetails> children, GetChildrenCallback childrenCallback) {
            this.children = children;
            this.childrenCallback = childrenCallback;
            this.id = id;
        }

        @Override
        protected List<ChildDetails> doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("parentid", id + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchChildData.php");

            List<ChildDetails> returnedChildren = new ArrayList<ChildDetails>();
            Log.i("MyActivity", "Fetch php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                Log.i("MyActivity", "Sent id " + id + " to server");

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONArray jArray = new JSONArray(result);

                if (jArray.length() == 0)
                    Log.i("MyActivity", "No response from FetchChildData");
                else {
                    Log.i("MyActivity", "jObject length = " + jArray.length());

                    for (int x = 0; x < jArray.length(); x++) {
                        int id = jArray.getJSONObject(x).getInt("id");
                        int parentid = jArray.getJSONObject(x).getInt("parentid");
                        String name = jArray.getJSONObject(x).getString("name");
                        String username = jArray.getJSONObject(x).getString("username");
                        String password = jArray.getJSONObject(x).getString("password");
                        String flag = jArray.getJSONObject(x).getString("flag");

                        Log.i("MyActivity", "Child " + x + " = " + id + parentid + name + username + password + flag);

                        returnedChildren.add(new ChildDetails(id, parentid, name, username, password));

                    }
                    Log.i("MyActivity", "returnedChildren size = " + returnedChildren.size());

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyActivity", "EXCEPTION");
            }

            return returnedChildren;
        }

        @Override
        protected void onPostExecute(List<ChildDetails> children) {
            progressDialog.dismiss();
            childrenCallback.done(children);
            super.onPostExecute(children);
        }
    }

    public class removeChildAsyncTask extends AsyncTask<Void, Void, ChildDetails> {
        ChildDetails child;

        public removeChildAsyncTask(ChildDetails child) {
            this.child = child;
        }

        @Override
        protected ChildDetails doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("id", child.get_id() + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "DeregisterChild.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                Log.i("MyActivity", "Sent id " + child.get_id() + " to server to remove it");

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyActivity", "EXCEPTION");
            }
            return null;
        }

        @Override
        protected void onPostExecute(ChildDetails child) {
            progressDialog.dismiss();
            super.onPostExecute(child);
        }
    }

    public class saveRouteAsyncTask extends AsyncTask<Void, Void, RouteDetails> {
        int userid;
        RouteDetails route;
        GetRouteCallback routeCallback;

        public saveRouteAsyncTask(int userid, RouteDetails route, GetRouteCallback routeCallback) {
            this.userid = userid;
            this.route = route;
            this.routeCallback = routeCallback;
        }

        @Override
        protected RouteDetails doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("parentid", userid + ""));
            dataToSend.add(new BasicNameValuePair("route_name", route.getmRouteName() + ""));
            dataToSend.add(new BasicNameValuePair("start_lat", route.getStart().latitude + ""));
            dataToSend.add(new BasicNameValuePair("start_long", route.getStart().longitude + ""));
            dataToSend.add(new BasicNameValuePair("end_lat", route.getEnd().latitude + ""));
            dataToSend.add(new BasicNameValuePair("end_long", route.getEnd().longitude + ""));
            dataToSend.add(new BasicNameValuePair("travel_mode", route.getModeTransport() + ""));
            dataToSend.add(new BasicNameValuePair("index", route.getIndex() + ""));

            Log.i("MyActivity", "dataToSend (1) = " + dataToSend);

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "StoreRoute.php");
            RouteDetails returnedRoute = new RouteDetails(null, null, null, null, 0 ,0);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() == 0)
                    Log.i("MyActivity", "No response");
                else {
                    int routeid = jObject.getInt("routeid");
                    route.setRouteID(routeid);
                    Log.i("MyActivity", "Route ID = " + routeid);
                    returnedRoute = new RouteDetails(route.getStart(),route.getEnd(),route.getmRouteName(), route.getModeTransport(), route.getIndex(), routeid);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyActivity", "EXCEPTION");
            }
            return returnedRoute;
        }

        @Override
        protected void onPostExecute(RouteDetails route) {
            progressDialog.dismiss();
            routeCallback.done(route);
            super.onPostExecute(route);
        }
    }

}