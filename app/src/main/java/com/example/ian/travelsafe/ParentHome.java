package com.example.ian.travelsafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class ParentHome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        // Set a toolbar which will replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup the viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Setup the Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        // By using this method the tabs will be populated according to viewPager's count and
        // with the name from the pagerAdapter getPageTitle()
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        // This method ensures that tab selection events update the ViewPager and page changes update the selected tab.
        tabLayout.setupWithViewPager(viewPager);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FragmentParentHomeChildren.newInstance();
                case 1:
                    return FragmentParentHomeAccount.newInstance();
                default:
                    return FragmentParentHomeChildren.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.ParentHomeTabTitleChildren).toUpperCase(l);
                case 1:
                    return getString(R.string.ParentHomeTabTitleAccount).toUpperCase(l);
                default:
                    return "Tab " + position;
            }
        }

    }

    /**
     * Method to call the assign route activity
     *
     * @param view
     */
    public void AssignRoute(View view) {
//        Snackbar.make(view, "Assign Route" , Snackbar.LENGTH_LONG).setAction("Action", null).show();
        if(ParentRouteList.getCurrentRouteList().isEmpty()) {
            Toast.makeText(view.getContext(), "No routes have been created.", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(ParentHome.this, Pop.class));
        }
    }

    /**
     * Method to delete a child account.
     *
     * @param view
     */

    public void DeleteChild(final View view) {

        new AlertDialog.Builder(view.getContext())
                .setTitle("Delete ")
                .setMessage("Are you sure you want to delete this child account?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ParentChildList.removeFromChildList(FragmentParentHomeChildren.childClicked);
                        // Remove child from database
                        FragmentParentHomeChildren.deRegisterChild(FragmentParentHomeChildren.childClicked);
                        Log.i("MyActivity", "sending to deRegister - " + FragmentParentHomeChildren.childClicked.get_id());

                        FragmentParentHomeChildren.childrenListView.invalidateViews();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = Toast.makeText(view.getContext(), "Cancelled Delete", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
