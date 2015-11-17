package com.example.ian.travelsafe;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    boolean flag = false;
    EditText username, password;
    UserLocalStore userLocalStore;

    private static final String TAG = "";

    @Override
    protected void onStart() {
        super.onStart();

        if (authenticate()) {
            displayUserDetails();
        }
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails() {
        Users user = userLocalStore.getLoggedInUser();

        username.setText(user._username);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);     // Turn off action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        userLocalStore = new UserLocalStore(this);
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

        String user_password = password.getText().toString();
        if (!RegisterParentActivity.isValidPassword(user_password)) {
            password.setError("password must contain at least 6 characters and 1 number");
            return false;
        }

        String _username = username.getText().toString();
        String _password = password.getText().toString();

        UserLocalStore stale = new UserLocalStore(this);
        stale.clearUserData();
        Users user = new Users(null, _username, _password);

        if (authenticate(user))
            return true;
        else
            return false;
    }

    private boolean authenticate(Users user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(Users returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                    flag = false;
                    Log.i("MyActivity", "Flag = false");
                } else {
                    logUserIn(returnedUser);
                    flag = true;
                    Log.i("MyActivity", "Flag = true");
                }
            }
        });
        return flag;
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void logUserIn(Users returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        Intent i = new Intent(this, ParentHome.class);
        startActivity(i);
        finish();
    }

    public void CheckLoginDetailsOnSubmit(View view) {
        //Check Login details and login to parent or child home screen.
        if (verifyDetails())
            Log.i("MyActivity", "Details verified OK");
        else
            Log.i("MyActivity", "Details not verified");

    }

}

