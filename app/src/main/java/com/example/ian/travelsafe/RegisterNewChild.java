package com.example.ian.travelsafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        EditText email = (EditText) this.findViewById(R.id.newChildEmail);
        String newChildEmail = email.getText().toString();
        EditText username = (EditText) this.findViewById(R.id.newChildUsername);
        String newChildUsername = username.getText().toString();
        EditText password = (EditText) this.findViewById(R.id.newChildPassword);
        String newChildPassword = password.getText().toString();

        if(verifyDetails()){
            // Add to user list
            ParentChildList.addToChildList(new ChildDetails(newChildUsername, R.drawable.child_placeholder, "No route selected"));
            // Load parent home again.
            Intent i = new Intent(this, ParentHome.class);
            startActivity(i);
            finish();

        }
    }


    public boolean verifyDetails() {
        // Get new child details entered by user
        EditText email = (EditText) this.findViewById(R.id.newChildEmail);
        String newChildEmail = email.getText().toString();
        if (!RegisterParentActivity.isValidEmailAddress(newChildEmail)) {
            email.setError("invalid email address");
            return false;
        }

        EditText password = (EditText) this.findViewById(R.id.newChildPassword);
        String newChildPassword = password.getText().toString();
        if (!RegisterParentActivity.isValidPassword(newChildPassword)) {
            password.setError("password must contain at least 6 characters and 1 number");
            return false;
        }
        return true;
    }
}
