package com.example.ian.travelsafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterParentActivity extends AppCompatActivity {
    boolean flag = false;

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

    public static boolean isValidPassword(String s) {
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
        String pass = password.getText().toString();
        if (!isValidPassword(pass)) {
            password.setError("password must contain at least 6 characters and 1 number");
            return false;
        }
        String user_password = computeMD5Hash(pass);

        UserLocalStore stale = new UserLocalStore(this);
        stale.clearUserData();
        Users user = new Users(user_email, user_username, user_password);

        if (registerUser(user)){
            logUserIn(user);
            return true;
        }
        else
            return false;
    }

    private void logUserIn(Users returnedUser) {
        UserLocalStore userLocalStore = new UserLocalStore(this);
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        Intent i = new Intent(this, ParentHome.class);
        startActivity(i);
        finish();
    }

    private boolean registerUser(Users user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(Users returnedUser) {
            }

        }, this);
        return true;
    }

    public static String computeMD5Hash(String password)
    {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }
            return MD5Hash.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void showRegisteredMessage() {

        new android.support.v7.app.AlertDialog.Builder(RegisterParentActivity.this)
                .setMessage("You have successfully registered")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(RegisterParentActivity.this, ParentHome.class);
                        startActivity(i);
                        finish();
                    }
                })
                .show();
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterParentActivity.this);
        dialogBuilder.setMessage("Register was unsuccessful");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    public void SubmitNewRegisteredUser(View view) {
        // Load next activity
        if (verifyDetails()) {
            showRegisteredMessage();
        } else {
            showErrorMessage();
        }
    }

}
