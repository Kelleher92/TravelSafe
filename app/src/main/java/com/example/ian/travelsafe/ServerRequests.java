package com.example.ian.travelsafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by ian on 06/11/2015.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://file-manager.hostinger.co.uk/6/index.php";

    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(Users user, GetUserCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchUserDataInBackground(Users user, GetUserCallback userCallback){
        progressDialog.show();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void>{
        Users user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(Users user, GetUserCallback userCallback){
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user._username));
            dataToSend.add(new BasicNameValuePair("email address", user._emailAddress));
            dataToSend.add(new BasicNameValuePair("password", user._password));


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid){
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

}
