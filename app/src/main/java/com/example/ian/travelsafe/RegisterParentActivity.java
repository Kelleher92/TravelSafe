package com.example.ian.travelsafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

public class RegisterParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);     // Turn off action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_parent);
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

        if (id == R.id.email) {
            EditText email = (EditText)this.findViewById(R.id.email);
        }

        if (id == R.id.username) {
            EditText username = (EditText)this.findViewById(R.id.username);
        }

        if (id == R.id.password) {
            EditText password = (EditText)this.findViewById(R.id.password);
        }

        return super.onOptionsItemSelected(item);
    }
}
