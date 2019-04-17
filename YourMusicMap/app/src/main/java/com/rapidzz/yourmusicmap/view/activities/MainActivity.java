package com.rapidzz.yourmusicmap.view.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
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
import com.rapidzz.mymusicmap.datamodel.model.fan.Song;
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.mymusicmap.view.activities.LandingActivity;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.ActivityMainBinding;
import com.rapidzz.yourmusicmap.other.util.GeofenceErrorMessages;
import com.rapidzz.yourmusicmap.other.util.Permission;
import com.rapidzz.yourmusicmap.other.util.ReplaceFragmentManger;
import com.rapidzz.yourmusicmap.other.util.RxBus;
import com.rapidzz.yourmusicmap.view.broadcastReceivers.GeofenceBroadcastReceiver;
import com.rapidzz.yourmusicmap.view.fragments.ProfileFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnCompleteListener<Void> {

    public static final String TAG = MainActivity.class.getSimpleName();

    // For Geo-Fencing
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent = null;
    private ArrayList<Geofence> geofenceList = new ArrayList<>();

    private ArrayList<Song> mSongs = new ArrayList<>();

    LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationRequest mLocationRequest;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageView ivProfile;
    private TextView tvProfileName;
    private TextView tvProfileEmail;
    public ActivityMainBinding binding;

    private Fragment fragment;
    ReplaceFragmentManger replaceFragment = new ReplaceFragmentManger();

    SongViewModel viewModel;

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        ivProfile = headerView.findViewById(R.id.ivProfile);
        tvProfileName = headerView.findViewById(R.id.tvProfile);
        tvProfileEmail = headerView.findViewById(R.id.tvProfileEmail);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /*private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        viewModelCallbacks(viewModel);
        viewModel.getSongs(new SessionManager(this).getUserId(), false);
    }*/

    public void viewModelCallbacks(SongViewModel viewModel) {

        viewModel.mSongListingResponseMutableLiveData.observe(this, songs -> {

            mSongs.clear();
            mSongs.addAll(songs);

            addGeoFences(mSongs);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();

       // setupViewModel();

        setupNavigation();

        updateUI();

        addMapFragment();

        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        setupGeofence();
    }

    private void setupGeofence() {
        geofencingClient = LocationServices.getGeofencingClient(this);
    }

    private void addGeoFences(ArrayList<Song> songs) {

        // removing previously add geofences
        if (!removeGeofencesListener())
            return;

        geofenceList = new ArrayList<>();

        for (int i = 0; i < songs.size(); i++) {
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

        addGeofencesWithPermission();
    }

    @SuppressLint("MissingPermission")
    private void addGeofencesWithPermission() {
        if (!Permission.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Permission.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                     /*   Toast.makeText(
                                MainActivity.this, "Added Geofence Success", Toast.LENGTH_SHORT).show();*/
                    }
                })
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnCompleteListener(this);
    }

    private boolean removeGeofencesListener() {
        if (!Permission.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Permission.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            return false;
        }

        geofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        /*Toast.makeText(
                                MainActivity.this, "Remove Geofence Success", Toast.LENGTH_SHORT).show();*/
                    }
                })
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnCompleteListener(this);
        return true;
    }

    @Override
    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
        if (task.isSuccessful()) {

            String message = null;

            if (task.getResult() != null)
                message = task.getResult().toString();

            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "Geofenct Add/Remove onComplete called", Toast.LENGTH_SHORT).show();
            }

        } else {

            String errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
            Log.e(TAG, errorMessage);

        }
    }

    @NotNull
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
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    private void updateUI() {
        String firstName = new SessionManager(this).getFirstName();
        String lastName = new SessionManager(this).getLastName();
        String email = new SessionManager(this).getEmail();
        String image = new SessionManager(this).getProfileImage();
        tvProfileName.setText(firstName + " " + lastName);
        tvProfileEmail.setText(email);

        if (image.isEmpty()) {
            Glide.with(getApplicationContext()).load(R.drawable.dp_placeholder).apply(RequestOptions.circleCropTransform()).into(ivProfile);
        } else {
            Glide.with(getApplicationContext()).load(image).apply(RequestOptions.circleCropTransform()).into(ivProfile);
        }

        ivProfile.setOnClickListener(v -> {
            drawer.closeDrawers();

            fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
            if (fragment != null) {
                String fragmentName = fragment.getTag();
                String name = ProfileFragment.class.getSimpleName();

                if (fragmentName == null || !fragmentName.equals(name)) {
                    replaceFragment.replaceFragment(new ProfileFragment(), ProfileFragment.TAG, null, MainActivity.this);
                }
            }

        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.nav_songs:
                fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
                if (fragment != null) {
                    String fragmentName = fragment.getTag();
                    String name = SongsFragment.class.getSimpleName();

                    if (fragmentName == null || !fragmentName.equals(name)) {
                        replaceFragment.replaceFragment(new SongsFragment(), SongsFragment.TAG, null, this);
                    }
                }

                break;
            case R.id.nav_logout:
                new SessionManager(this).setUserLoggedIn(false);

                Intent intent = new Intent(MainActivity.this, LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addMapFragment() {

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
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                locationCallback, null /* Looper */);
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

                Log.e("LOCATION", "Its called: " + locationResult.getLastLocation());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    private void setupNavigation() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 50) {
            if (mSongs != null && mSongs.size() > 0)
                addGeoFences(mSongs);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra("notificationDetails")) {
            Log.e(TAG, "onNewIntent: " + intent.getStringExtra("notificationDetails"));

            String songId = intent.getStringExtra("notificationDetails").split(":")[1]
                    .trim().split(",")[0].split("song_")[1];

            Bundle args = new Bundle();
            args.putString("type", "playSongFromNotification");
            args.putString("songId", songId);

            RxBus.defaultInstance().send(args);

        }
    }
}
