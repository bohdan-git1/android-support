package com.rapidzz.yourmusicmap.view.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.other.util.GeofenceErrorMessages;
import com.rapidzz.yourmusicmap.view.activities.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionsIntentService extends JobIntentService {
    private static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();

    private static final int JOB_ID = 573;

    private static final String CHANNEL_ID = "channel_01";

    public static void enqueueWork(Context context, Intent intent){
        enqueueWork(context, GeofenceTransitionsIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition,
                    triggeringGeofences);

            sendNotification(geofenceTransitionDetails);
            Log.i(TAG, geofenceTransitionDetails);

        }else {
            Log.e(TAG,"GeoFence Transition: Error");
        }
    }

    @NotNull
    private String getGeofenceTransitionDetails(int geofenceTransition,
                                                @NotNull List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        ArrayList<String> triggerGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence: triggeringGeofences){
            triggerGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",triggerGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    @NotNull
    private String getTransitionString(int transitionType) {
        switch (transitionType){
             case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
             case Geofence.GEOFENCE_TRANSITION_EXIT:
                 return getString(R.string.geofence_transition_exited);
             default:
                 return getString(R.string.unknown_geofence_transition);
        }
    }


    private void sendNotification(String notificationDetails) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID,name,NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);

        notificationIntent.putExtra("notificationDetails" , notificationDetails);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setChannelId(CHANNEL_ID);
        }

        builder.setAutoCancel(true);

        notificationManager.notify(0, builder.build());

    }

//    protected void onHandleIntent(Intent intent) {
//        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
//        if (geofencingEvent.hasError()) {
//           /* String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    geofencingEvent.getErrorCode());*/
//            Log.e(TAG, geofencingEvent.getErrorCode() + "");
//            return;
//        }
//
//        // Get the transition type.
//        int geofenceTransition = geofencingEvent.getGeofenceTransition();
//
//        // Test that the reported transition was of interest.
//        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
//                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
//
//            // Get the geofences that were triggered. A single event can trigger
//            // multiple geofences.
//            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
//
//            // Get the transition details as a String.
//            /*String geofenceTransitionDetails = getGeofenceTransitionDetails(
//                    this,
//                    geofenceTransition,
//                    triggeringGeofences
//            );*/
//
//            // Send notification and log the transition details.
//            //    sendNotification(geofenceTransitionDetails);
//            Log.i(TAG, triggeringGeofences.get(0).getRequestId());
//        } else {
//            // Log the error.
//           /* Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
//                    geofenceTransition));*/
//        }
//    }

}
