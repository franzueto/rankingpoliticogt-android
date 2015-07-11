package com.hackatoncivico.rankingpolitico;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        setUpDrawer(toolbar);

    }

    private void setUpDrawer(Toolbar toolbar){
        // Configure Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_left);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        };

        drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        //Navigation Listener
        NavigationView main_drawer_navigation = (NavigationView) findViewById(R.id.main_drawer_navigation);
        main_drawer_navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                /*TODO FIX WITH RIGHT ACTIONS*/
                switch (menuItem.getItemId()){
                    case R.id.ranking:
                        //Intent mainIntent = new Intent(MainActivity.this, Profile.class);
                        //startActivity(mainIntent);
                        break;
                    case R.id.partidos_politicos:
                        //Intent faqIntent = new Intent(MainActivity.this, FAQActivity.class);
                        //startActivity(faqIntent);
                        break;
                    default:
                        Log.w("TAG", "Do nothing");
                        break;
                }

                return true;
            }
        });
    }
}
