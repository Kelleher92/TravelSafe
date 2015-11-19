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

    public void fetchChildDataInBackground(int id, ChildDetails child, GetChildCallback childCallback) {
        progressDialog.show();
        new fetchChildDataAsyncTask(id, child, childCallback).execute();
    }

    public void removeChildInBackground(ChildDetails child) {
        new removeChildAsyncTask(child).execute();
    }

    public RouteDetails fetchChildAttachedRoute(int childID, GetRouteCallback routeCallback) {
        new fetchChildAttachedRouteAsyncTask(childID, routeCallback).execute();
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
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchRoute.php");

            RouteDetails returnedRoute = null;
            Log.i("MyActivity", "Fetch php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                Log.i("MyActivity", "Sent id " + id + " to server");

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() == 0)
                    Log.i("MyActivity", "No response");
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

                    returnedRoute = new RouteDetails(start, end, routeName, mode, index);
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

    public class fetchChildDataAsyncTask extends AsyncTask<Void, Void, ChildDetails> {
        ChildDetails child;
        GetChildCallback childCallback;
        int id;

        public fetchChildDataAsyncTask(int id, ChildDetails child, GetChildCallback childCallback) {
            this.child = child;
            this.childCallback = childCallback;
            this.id = id;
        }

        @Override
        protected ChildDetails doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("parentid", id + ""));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchChildData.php");

            ChildDetails returnedChild = null;
            Log.i("MyActivity", "Fetch php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                Log.i("MyActivity", "Sent id " + id + " to server");

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);

                if (jObject.length() == 0)
                    Log.i("MyActivity", "No response");
                else {
                    int id = jObject.getInt("id");
                    int parentid = jObject.getInt("parentid");
                    String name = jObject.getString("name");
                    String username = jObject.getString("username");
                    String password = jObject.getString("password");
                    String flag = jObject.getString("flag");

                    returnedChild = new ChildDetails(id, parentid, name, username, password);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MyActivity", "EXCEPTION");
            }

            return returnedChild;
        }

        @Override
        protected void onPostExecute(ChildDetails child) {
            progressDialog.dismiss();
            childCallback.done(child);
            super.onPostExecute(child);
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
}