package com.rapidzz.yourmusicmap.other.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;

/*
 * Created by regbits on 12/4/17.
 */

public class ReverseGeoCoding {

    public static Task<List<Address>> convertLatLngToAddress(final Context context, final LatLng latLng) {
        return Task.callInBackground(new Callable<List<Address>>() {
            @Override
            public List<Address> call() throws Exception {
                Geocoder geocoder =
                        new Geocoder(context);
                List<Address> list;
                try {
                    list = geocoder.getFromLocation(latLng.latitude,
                            latLng.longitude, 1);
                } catch (IOException e) {
                    return null;
                }
                return list;
            }
        });

    }


    public static Task<LatLng> getLatLngFromAdress(final Context context, final String strAddress) {
        return Task.callInBackground(new Callable<LatLng>() {
            @Override
            public LatLng call() throws Exception {
                Geocoder coder = new Geocoder(context);
                List<Address> address;
                LatLng p1 = null;

                try {
                    // May throw an IOException
                    address = coder.getFromLocationName(strAddress, 5);
                    if (address == null) {
                        return null;
                    }
                    Address location = address.get(0);
                    location.getLatitude();
                    location.getLongitude();

                    p1 = new LatLng(location.getLatitude(), location.getLongitude());

                } catch (IOException ex) {

                    ex.printStackTrace();
                }

                return p1;
            }
        });

    }

}
