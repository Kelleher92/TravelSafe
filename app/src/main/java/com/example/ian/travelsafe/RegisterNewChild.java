package com.example.ian.travelsafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterNewChild extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_child);

    }


    public void RegisterNewChild() {
//        Toast.makeText(RegisterNewChild.this, "New Child Registered", Toast.LENGTH_SHORT).show();
    }

    public void ReturnToParentHome(View view) {

        Intent i = new Intent(this, ParentHome.class);
        startActivity(i);
        Toast.makeText(this, "Return To ParentHome", Toast.LENGTH_SHORT);
    }

    public void CreateNewChild(View view) {

        // Get new child details entered by user
        EditText name = (EditText) this.findViewById(R.id.newChildName);
        String newChildName = name.getText().toString();
        EditText username = (EditText) this.findViewById(R.id.newChildUsername);
        String newChildUsername = username.getText().toString();
        EditText password = (EditText) this.findViewById(R.id.newChildPassword);
        String newChildPassword = password.getText().toString();

        if(verifyDetails()){
            int currentUserId = new UserLocalStore(this).getLoggedInUser().get_id();
            String currentUserEmail = new UserLocalStore(this).getLoggedInUser().get_emailAddress();
            ChildDetails child = new ChildDetails(currentUserId, newChildName,newChildUsername,newChildPassword);

            Log.i("MyActivity", "1. user id = " + currentUserId);
            Log.i("MyActivity", "1. user email = " + currentUserEmail);

            // Add to user list
            // ParentChildList.addToChildList(child);
            //////////////////////////
            registerChild(child);
            // Load parent home again.
            Intent i = new Intent(this, ParentHome.class);
            startActivity(i);
            finish();

        }
    }

    private boolean registerChild(ChildDetails child) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeChildDataInBackground(child, new GetChildCallback() {
            @Override
            public void done(ChildDetails returnedChild) {
            }

        }, this);
        return true;
    }

    public boolean verifyDetails() {
        // Get new child details entered by user

        EditText password = (EditText) this.findViewById(R.id.newChildPassword);
        String newChildPassword = password.getText().toString();
        if (!RegisterParentActivity.isValidPassword(newChildPassword)) {
            password.setError("password must contain at least 6 characters and 1 number");
            return false;
        }
        return true;
    }
}
