package com.example.ian.travelsafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class RegisterNewChild extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_child);

    }


    public void RegisterNewChild() {
        Toast.makeText(RegisterNewChild.this, "New Child Registered", Toast.LENGTH_SHORT).show();
    }

    public void ReturnToParentHome(View view) {

        Intent i = new Intent(this, ParentHome.class);
        startActivity(i);
        Toast.makeText(this, "Return To ParentHome", Toast.LENGTH_SHORT);
    }

    public void RegisterNewChild(View view) {

        Toast.makeText(this, "Register New Child Here", Toast.LENGTH_SHORT);
    }
}
