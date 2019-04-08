package com.rapidzz.yourmusicmap.other.util;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.concurrent.Callable;

import bolts.Task;

public class LocationUpdateService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    private boolean locationUpdatePriority;
    private String slug = "";

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {


        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception{
                connectWithClient();
                return null;
            }
        });


        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }


    private void connectWithClient() throws Exception {
        stopClient();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(LocationUpdateService.this)
                .addOnConnectionFailedListener(LocationUpdateService.this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(Constants.INTERVAL);
            mLocationRequest.setFastestInterval(Constants.INTERVAL); //5 secs
            mLocationRequest.setSmallestDisplacement(Constants.DISTANCE);

        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        mLocationRequest.setPriority(priority);
        mGoogleApiClient.connect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d("== Error", "On onConnected() Permission not granted");

            return;
        }
        try {

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("Connected","to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Connection", "suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {
         mCurrentLocation = location;
    }


    private void stopClient(){

        if(mGoogleApiClient!=null &&mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();

        }
        if(mLocationRequest!= null ){
            mLocationRequest = null;
        }
    }
}
