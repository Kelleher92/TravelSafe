package com.example.ian.travelsafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

public class AppSplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_splash_screen);
        final UserLocalStore userLocalStore = new UserLocalStore(this);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(userLocalStore.getUserLoggedIn()) {
                    Log.i("Splash Screen", userLocalStore.getLoggedInUser()._flag);
                    if(userLocalStore.getLoggedInUser()._flag.equals("P")) {
                        Intent i = new Intent(AppSplashScreen.this, ParentHome.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Intent i = new Intent(AppSplashScreen.this, ChildHome.class);
                        startActivity(i);
                        finish();
                    }
                }
                else {
                    Intent i = new Intent(AppSplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}

