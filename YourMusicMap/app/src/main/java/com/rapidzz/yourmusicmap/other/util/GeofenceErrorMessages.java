package com.rapidzz.yourmusicmap.other.util;

import android.content.Context;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.rapidzz.yourmusicmap.R;

import org.jetbrains.annotations.NotNull;

public class GeofenceErrorMessages {

  //  public static final int GEO

    private GeofenceErrorMessages(){

    }

    public static String getErrorString(Context context, Exception e){
        if(e instanceof ApiException){
            return getErrorString(context, ((ApiException)e).getStatusCode());
        }else{
            return context.getString(R.string.geofence_error_unknown_not_available);
        }
    }

    @NotNull
    public static String getErrorString(Context context, int errorCode){

        switch (errorCode){
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return context.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return context.getString(R.string.geofence_too_many_geofences);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return context.getString(R.string.geofence_too_many_pending_intents);
            default:
                return context.getString(R.string.geofence_error_unknown_not_available);
                    
        }

    }

}
