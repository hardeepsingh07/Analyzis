package com.example.hardeep.analyzis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;

import me.itangqi.waveloadingview.WaveLoadingView;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    WaveLoadingView mWaveLoadingView;

    HashMap<String, Integer> mapSessions;
    HashMap<String, Integer> mapEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        mWaveLoadingView.setCenterTitle("Analyzis");
        mWaveLoadingView.setProgressValue(0);
        mWaveLoadingView.setBorderWidth(2);
        mWaveLoadingView.setAmplitudeRatio(0);
        mWaveLoadingView.setBorderColor(getResources().getColor(R.color.colorPrimaryDark));

        //Intiate HashMaps
        mapSessions = new HashMap<>();
        mapEvents = new HashMap<>();

        mWaveLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the load and animation
               new MyAsyncTask().execute();
            }
        });




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
            mWaveLoadingView.setVisibility(View.GONE);
            DataFragment df = new DataFragment();
            df.setData(mapSessions);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_layout_container, df, df.getTag()).commit();
        } else if (id == R.id.events) {
            mWaveLoadingView.setVisibility(View.GONE);
            DataFragment df = new DataFragment();
            df.setData(mapEvents);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_layout_container, df, df.getTag()).commit();
        } else if (id == R.id.settings) {
            SettingsFragment sm = new SettingsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_layout_container, sm, sm.getTag()).commit();
        } else if (id == R.id.screenShot) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                drawer.closeDrawer(GravityCompat.START);
                takeScreenShot();
                drawer.setDrawerListener(null);
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
        drawer.setDrawerListener(toggle2);
    }

    private class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... integers) {
            try {
                Server server = new Server();

                //Update Wave
                for(int i = 0; i < 49; i++) {
                    Thread.sleep(10);
                    publishProgress(i);
                }

                //Get Session Data and Parse into HashMap
                String jsonSessions = server.getSessions();
                JSONObject jsonSessionObject = new JSONObject(jsonSessions);
                JSONArray jsonSessionArray = jsonSessionObject.getJSONArray("Sessions");
                for (int i = 0; i < jsonSessionArray.length(); i++) {
                    JSONObject jsonobject = jsonSessionArray.getJSONObject(i);
                    String name = jsonobject.getString("Name");
                    int value = jsonobject.getInt("count");
                    mapSessions.put(name,value);
                }

                //Stall for animations
                Thread.sleep(1500);
                publishProgress(50);

                //Get Event Data and Parse with HashMap
                String jsonEvents = server.getEvents();
                JSONObject jsonEventObject = new JSONObject(jsonEvents);
                JSONArray jsonEventArray = jsonEventObject.getJSONArray("Events");
                for (int i = 0; i < jsonEventArray.length(); i++) {
                    JSONObject jsonobject2 = jsonEventArray.getJSONObject(i);
                    String name = jsonobject2.getString("Name");
                    int value = jsonobject2.getInt("count");
                    mapEvents.put(name,value);
                }

                //Update Wave
                for(int i = 49; i <= 99; i++) {
                    Thread.sleep(10);
                    publishProgress(i);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mWaveLoadingView.setCenterTitle("Retrieving...");
            mWaveLoadingView.setAmplitudeRatio(60);
            mWaveLoadingView.setBorderColor(getResources().getColor(R.color.colorPrimaryDark));
            mWaveLoadingView.setWaveColor(getResources().getColor(R.color.colorAccent));
        }

        @Override
        protected void onPostExecute(Void integer) {
            super.onPostExecute(integer);
            mWaveLoadingView.setCenterTitle("Done!");
            mWaveLoadingView.setAmplitudeRatio(1);
            mWaveLoadingView.setCenterTitleColor(getResources().getColor(R.color.colorAccent));
            mWaveLoadingView.setWaveColor(getResources().getColor(R.color.colorPrimaryDark));
            mWaveLoadingView.setBorderWidth(2f);
            mWaveLoadingView.setBorderColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mWaveLoadingView.setProgressValue((int) values[0]);
        }
    }
}
