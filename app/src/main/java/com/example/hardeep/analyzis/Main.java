package com.example.hardeep.analyzis;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        HashMap<String, Integer> sessions = new HashMap<String, Integer>();
        sessions.put("Main", 45);
        sessions.put("Second", 26);
        sessions.put("Third", 27);
        sessions.put("Fourth", 19);
        sessions.put("Fifth", 21);
        sessions.put("Sixth", 24);
        sessions.put("Seventh", 19);
        sessions.put("Eigth", 7);
        sessions.put("Ninth", 4);
        sessions.put("Tenth", 25);
        sessions.put("Eleventh", 32);


        HashMap<String, Integer> events = new HashMap<String, Integer>();
        events.put("EMainE", 75);
        events.put("ESecondE", 66);
        events.put("EThirdE", 57);
        events.put("EFourthE", 39);
        events.put("EFifthE", 41);
        events.put("ESixthE", 74);
        events.put("ESeventhE", 59);
        events.put("EEigthE", 47);
        events.put("ENinthE", 14);
        events.put("ETenthE", 50);
        events.put("EEleventhE", 90);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sessions) {
            SessionFragment sf = new SessionFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_layout_container, sf, sf.getTag()).commit();
        } else if (id == R.id.events) {
            EventFragment ef = new EventFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_layout_container, ef, ef.getTag()).commit();
        } else if (id == R.id.settings) {
            SettingsManager sm = new SettingsManager();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_layout_container, sm, sm.getTag()).commit();
        } else if (id == R.id.screenShot) {

        } else if (id == R.id.refresh) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
