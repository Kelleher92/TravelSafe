package com.example.ian.travelsafe;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterNewChild extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_child);

    }

    ChildDetails registeredChild = new ChildDetails(null, null);

    public void ReturnToParentHome(View view) {
        Intent i = new Intent(this, ParentHome.class);
        startActivity(i);
        finish();
        Toast.makeText(this, "Return To ParentHome", Toast.LENGTH_SHORT);
    }

    public void CreateNewChild(View view) {

        // Get new child details entered by user
        EditText name = (EditText) this.findViewById(R.id.newChildName);
        String newChildName = name.getText().toString();
        EditText username = (EditText) this.findViewById(R.id.newChildUsername);
        String newChildUsername = username.getText().toString();
        EditText password = (EditText) this.findViewById(R.id.newChildPassword);
        String pass = password.getText().toString();
        String newChildPassword = RegisterParentActivity.computeMD5Hash(pass);


        if (verifyDetails()) {
            int currentUserId = new UserLocalStore(this).getLoggedInUser().get_id();
            String currentUserEmail = new UserLocalStore(this).getLoggedInUser().get_emailAddress();
            ChildDetails child = new ChildDetails(currentUserId, newChildName, newChildUsername, newChildPassword);

            Log.i("MyActivity", "1. user id = " + currentUserId);
            Log.i("MyActivity", "1. user email = " + currentUserEmail);

            registerChild(child);
        }
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterNewChild.this);
        dialogBuilder.setMessage("Register was unsuccessful, try another username");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private boolean registerChild(ChildDetails child) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeChildDataInBackground(child, new GetChildCallback() {
            @Override
            public void done(ChildDetails returnedChild) {
                if (returnedChild.get_id()==0){
                    showErrorMessage();
                }
                else {
                    Intent i = new Intent(RegisterNewChild.this, ParentHome.class);
                    startActivity(i);
                    finish();
                }
            }
        });
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
