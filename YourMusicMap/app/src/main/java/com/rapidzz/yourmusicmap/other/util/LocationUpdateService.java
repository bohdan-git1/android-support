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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.concurrent.Callable;

import bolts.Task;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import qa.qserv.providers.MyApplication;
import qa.qserv.providers.model.LocationHistory;
import qa.qserv.providers.model.ProviderLocationHistory;
import qa.qserv.providers.networking.ApiService;
import qa.qserv.providers.networking.socket.QberSocket;
import qa.qserv.providers.utils.Config;
import qa.qserv.providers.utils.Constants;
import qa.qserv.providers.utils.SessionManager;
import timber.log.Timber;

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
                locationUpdatePriority = intent.getBooleanExtra(Constants.LOCATION_TYPE,false);
                long jobId = intent.getLongExtra(Constants.JOB_ID, 0);
                slug = intent.getStringExtra(Constants.SLUG);
                connectWithClient(locationUpdatePriority, jobId);
                return null;
            }
        });


        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }


    private void connectWithClient(boolean type, long jobId) throws Exception {
        stopClient();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(LocationUpdateService.this)
                .addOnConnectionFailedListener(LocationUpdateService.this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = new LocationRequest();
        if (type) {
            long providerId = SessionManager.getProviderId(this);
            if(slug != null && slug.equals(Constants.BUY_AND_DELIVER)){
                QberSocket.getDefaultSocket().openLocationUpdateChannel(providerId);
            }else {
                QberSocket.getDefaultSocket().openLocationUpdateChannel(jobId);
            }
            mLocationRequest.setInterval(Config.INTERVAL);
            mLocationRequest.setFastestInterval(Config.INTERVAL); //5 secs
            mLocationRequest.setSmallestDisplacement(Config.DISTANCE);
        } else{
            mLocationRequest.setInterval(Config.NOT_HEADING_TO_JOB_INTERVAL);
            mLocationRequest.setFastestInterval(Config.NOT_HEADING_TO_JOB_INTERVAL); //5 secs
            mLocationRequest.setSmallestDisplacement(Config.NOT_HEADING_TO_JOB_DISTANCE);
        }
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

            Timber.d("== Error On onConnected() Permission not granted");

            return;
        }
        try {

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch (Exception e){
            e.printStackTrace();
        }
        Timber.d("Connected to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Timber.d("Failed to connect to Google API");
    }

    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        Timber.d("Location changed");

        mCurrentLocation = location;
        //if(locationUpdatePriority != null && locationUpdatePriority.equals(Job.Status.STARTED_HEADING)) {
        if(locationUpdatePriority) {
            QberSocket.getDefaultSocket().push(location.getLatitude(), location.getLongitude());
        }else{
            if (location != null) {
                updateProviderLocationHistory(location);
            }
        }


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
    @SuppressLint("CheckResult")
    private void updateProviderLocationHistory(Location location) {
        LocationHistory history1 = new LocationHistory();
        ProviderLocationHistory history = new ProviderLocationHistory();
        ProviderLocationHistory.Location currentLocation = new ProviderLocationHistory.Location();
        currentLocation.setLatitude(location.getLatitude());
        currentLocation.setLongitude(location.getLongitude());
        history.setLocation(currentLocation);
        history.setInsertedBy(SessionManager.getProviderId(MyApplication.getAppContext()));
        history1.setProviderLocationHistory(history);
        Timber.d("Timber location history service %s", new Gson().toJson(history1));
        ApiService.get().updateLocationHistory(history1)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<LocationHistory>() {
                    @Override
                    public void accept(LocationHistory history) {
                        Timber.d("Timber Successfully updated");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Timber.d(throwable);
                    }
                });
    }
}
