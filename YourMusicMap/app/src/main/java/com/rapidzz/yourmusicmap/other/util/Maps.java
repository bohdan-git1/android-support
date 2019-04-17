package com.rapidzz.yourmusicmap.other.util;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/**
 * Created by dev on 6/16/17.
 */

public class Maps {

    private static final String MAP_API_KEY = "AIzaSyBSWwxlm0LrAomCJpJURe2DP9z83MwgkC0";
    public static String AUTO_COMPLETE_BASE = "https://maps.googleapis.com/maps/api/place/autocomplete/json";

    @NonNull
    public static String getPlaceAutoCompleteUrl(Context context, String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append(AUTO_COMPLETE_BASE);
        urlString.append("?input=");
        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //urlString.append("&location=");
        //urlString.append(latitude + "," + longitude); // append lat long of current location to show nearby results.
      //  urlString.append("&components=country:" + context.getString(R.string.countries_for_places));
        urlString.append("&language=en");
        urlString.append("&key=" + MAP_API_KEY);
        return urlString.toString();
    }

    public static void displayLocationSettingsRequest(final Context context) {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("TAG", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("TAG", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult((Activity) context, 88);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("TAG", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
}
