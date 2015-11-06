package com.example.ian.travelsafe;

import android.content.Intent;
import android.hardware.usb.UsbRequest;
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

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isValidPassword(String s){
        if (s == null)
            return false;

        if (s.length() < 7)
            return false;

        int counter = 0;

        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i)))
                return false;
            if (Character.isDigit(s.charAt(i)))
                counter++;
        }
        if (counter >= 1)
            return true;
        else
            return false;
    }

    public boolean verifyDetails() {
        EditText email = (EditText) this.findViewById(R.id.email);
        String user_email = email.getText().toString();
        if (!isValidEmailAddress(user_email)) {
            email.setError("invalid email address");
            return false;
        }

        EditText username = (EditText) this.findViewById(R.id.username);
        String user_username = username.getText().toString();

        EditText password = (EditText) this.findViewById(R.id.password);
        String user_password = password.getText().toString();
        if (!isValidPassword(user_password)) {
            password.setError("password must contain at least 6 characters and 1 number");
            return false;
        }

        Users user = new Users(user_email,user_username,user_password);

        registerUser(user);

        return true;
    }

    private void registerUser(Users user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(Users returnedUser) {

            }
        });
    }

    public void SubmitNewRegisteredUser(View view) {
        // Load next activity
        if (verifyDetails()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
        else {}
    }
}
