package com.example.ian.travelsafe;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ian on 06/11/2015.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(Users user) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("username", user._username);
        spEditor.putString("emailaddress", user._emailAddress);
        spEditor.putString("password", user._password);
        spEditor.putInt("id", user._id);

        spEditor.commit();
    }

    public Users getLoggedInUser() {
        int id = userLocalDatabase.getInt("id", -1);
        String username = userLocalDatabase.getString("username", "");
        String emailAddress = userLocalDatabase.getString("emailaddress", "");
        String password = userLocalDatabase.getString("password", "");

        Users storedUser = new Users(id, emailAddress, username, password);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == true)
            return true;
        else
            return false;
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

}
