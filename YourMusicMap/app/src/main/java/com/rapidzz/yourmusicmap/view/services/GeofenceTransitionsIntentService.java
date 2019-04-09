package com.rapidzz.yourmusicmap.view.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.rapidzz.yourmusicmap.R;

import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {


    private static final String TAG = "LOGLOG";

    public GeofenceTransitionsIntentService() {
        super("");

    }


    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
           /* String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());*/
            Log.e(TAG, geofencingEvent.getErrorCode() + "");
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            /*String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );*/

            // Send notification and log the transition details.
            //    sendNotification(geofenceTransitionDetails);
            Log.i(TAG, triggeringGeofences.get(0).getRequestId());
        } else {
            // Log the error.
           /* Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
                    geofenceTransition));*/
        }
    }
}
