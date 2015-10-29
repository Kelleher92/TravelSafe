package com.example.ian.travelsafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.common.SignInButton;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);     // Turn off action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void LoadRegisterScreen(View view) {
        // Load next activity
        Intent i = new Intent(this, RegisterParentActivity.class);
        startActivity(i);
    }

    public boolean verifyDetails() {
        EditText username = (EditText) this.findViewById(R.id.username);
        String user_username = username.getText().toString();
        /*if (!isRegisteredUser(user_username)) {
            username.setError("invalid username");
            return false;
        }*/

        EditText password = (EditText) this.findViewById(R.id.password);
        String user_password = password.getText().toString();
        if (!RegisterParentActivity.isValidPassword(user_password)) {
            password.setError("password must contain at least 6 characters and 1 number");
            return false;
        }

        return true;
    }

    public void CheckLoginDetailsOnSubmit(View view) {
        //Check Login details and login to parent or child home screen.
        if (verifyDetails()) {
            Intent i = new Intent(this, ParentHome.class);
            startActivity(i);
        }
        else {}

    }
}

