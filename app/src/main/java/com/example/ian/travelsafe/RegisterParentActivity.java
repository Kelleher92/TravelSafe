package com.example.ian.travelsafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class RegisterParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);     // Turn off action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_parent);
    }

    public void ReturnToLoginActivity(View view) {
        // Load next activity
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void SubmitNewRegisteredUser(View view) {
        // Load next activity
        EditText email = (EditText)this.findViewById(R.id.email);
        String user_email = email.getText().toString();

        EditText username = (EditText)this.findViewById(R.id.username);
        String user_username = username.getText().toString();

        EditText password = (EditText)this.findViewById(R.id.password);
        String user_password = password.getText().toString();

        Intent i = new Intent(this, ParentHome.class);
        startActivity(i);
    }
}
