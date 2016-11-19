package com.example.hardeep.analyzis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
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
            SettingsFragment sm = new SettingsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_layout_container, sm, sm.getTag()).commit();
        } else if (id == R.id.screenShot) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                drawer.closeDrawer(GravityCompat.START);
                takeScreenShot();
                drawer.setDrawerListener(toggle);
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else if (id == R.id.refresh) {

        }

        //Close Drawer
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeScreenShot();
                } else {
                    Toast.makeText(Main.this, "Permission are required to save Screenshots", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void takeScreenShot() {
        ActionBarDrawerToggle toggle2 = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_open) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Date now = new Date();
                DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                try {
                    File folder = new File(Environment.getExternalStorageDirectory().toString() + "/AnalzisScreenShots");
                    boolean success = false;
                    if (!folder.exists()) {
                        success = folder.mkdirs();
                    }

                    String mPath = folder.getAbsolutePath();
                    System.out.println(mPath + ": " + folder.exists() + ": " + success);


                    // create bitmap screen capture
                    View v1 = getWindow().getDecorView().getRootView();
                    v1.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                    v1.setDrawingCacheEnabled(false);

                    File imageFile = new File(mPath, now + ".jpg");
                    System.out.println(mPath + ": " + imageFile.exists());


                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Toast.makeText(Main.this, "ScreenShot Saved : " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (Throwable e) {
                    e.printStackTrace();
                }            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle2);
    }
}
