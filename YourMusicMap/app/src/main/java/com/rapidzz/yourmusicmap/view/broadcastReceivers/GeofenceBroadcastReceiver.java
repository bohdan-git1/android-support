package com.rapidzz.yourmusicmap.view.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rapidzz.yourmusicmap.view.services.GeofenceTransitionsIntentService;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "broadcasting geofence intent");
        GeofenceTransitionsIntentService.enqueueWork(context, intent);
    }
}
