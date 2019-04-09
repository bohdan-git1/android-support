package com.rapidzz.yourmusicmap.view.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rapidzz.mymusicmap.datamodel.model.fan.Song;
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.other.util.ReplaceFragmentManger;
import com.rapidzz.yourmusicmap.other.util.RxBus;
import com.rapidzz.yourmusicmap.view.fragments.MapFragment;
import com.rapidzz.yourmusicmap.view.fragments.ProfileFragment;
import com.rapidzz.yourmusicmap.view.fragments.SongsFragment;
import com.rapidzz.yourmusicmap.view.services.GeofenceTransitionsIntentService;
import com.rapidzz.yourmusicmap.viewmodel.SongViewModel;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationRequest mLocationRequest;
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private SongViewModel viewModel;
    private ArrayList<Geofence> geofenceList;
    private ImageView ivProfile;
    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private Fragment fragment;
    ReplaceFragmentManger replaceFragment = new ReplaceFragmentManger();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geofencingClient = LocationServices.getGeofencingClient(this);
        getGeofencePendingIntent();
        removeGeofencesListener();
        geofenceList = new ArrayList<>();
        viewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        viewModelCallbacks(viewModel);
        String id =  new SessionManager(this).getUserId();
        viewModel.getSongs(id,false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        ivProfile = (ImageView)headerView.findViewById(R.id.ivProfile);
        tvProfileName = (TextView) headerView.findViewById(R.id.tvProfile);
        tvProfileEmail = (TextView) headerView.findViewById(R.id.tvProfileEmail);
        String firstName =  new SessionManager(this).getFirstName();
        String lastName =  new SessionManager(this).getLastName();
        String email =  new SessionManager(this).getEmail();
        tvProfileName.setText(firstName + " " + lastName);
        tvProfileEmail.setText(email);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();

                fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
                if (fragment != null) {
                    String fragmentName = fragment.getTag();
                    String name = ProfileFragment.class.getSimpleName();

                    if (fragmentName == null || !fragmentName.equals(name)) {
                        replaceFragment.replaceFragment(new ProfileFragment(), ProfileFragment.TAG, null,MainActivity.this);
                    }
                }

            }
        });




        addHomeFragment();
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
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

        if (id == R.id.nav_home) {
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (id == R.id.nav_songs) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
            if (fragment != null) {
                String fragmentName = fragment.getTag();
                String name = SongsFragment.class.getSimpleName();

                if (fragmentName == null || !fragmentName.equals(name)) {
                    replaceFragment.replaceFragment(new SongsFragment(), SongsFragment.TAG, null, this);
                }
            }

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addHomeFragment() {

        /**
         * Adding HomeFragment
         */

        FragmentManager fmanager = getSupportFragmentManager();
        FragmentTransaction ft = fmanager.beginTransaction();
        ft.replace(R.id.main_container, new MapFragment(), MapFragment.TAG);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if (requestingLocationUpdates) {
        startLocationUpdates();
        //}
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                locationCallback,
                null /* Looper */);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(10000);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(10000);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Log.e("LOCATION","Its called: " + locationResult.getLastLocation());
                RxBus.defaultInstance().send(locationResult);
                //CurrentLocation = locationResult.getLastLocation();
               // mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
              //  updateLocationUI();
            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    private void addGeoFences(ArrayList<Song> songs){

        for(int i = 0; i < songs.size();i++) {
            geofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId("song_" + songs.get(i).getId())

                    .setCircularRegion(
                            Double.parseDouble(songs.get(i).getLat()),
                            Double.parseDouble(songs.get(i).getLng()),
                            100
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    private void removeGeofencesListener(){
        geofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences removed
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to remove geofences
                        // ...
                    }
                });
    }



    public void viewModelCallbacks(SongViewModel viewModel) {

        viewModel.mSongListingResponseMutableLiveData.observe(this, songArrayList -> {

            addGeoFences(songArrayList);

        });
    }
}
