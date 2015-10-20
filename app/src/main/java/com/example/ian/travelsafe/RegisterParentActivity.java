package com.example.ian.travelsafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class RegisterParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);     // Turn off action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_parent);
    }
}
