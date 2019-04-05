package com.rapidzz.yourmusicmap.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rapidzz.yourmusicmap.MainActivity;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentMapBinding;
import com.rapidzz.yourmusicmap.other.util.GPSTracker;

public class MapFragment extends BaseFragment implements OnMapReadyCallback {
    public static final String TAG = MapFragment.class.getSimpleName();
    private Context context;
    FragmentMapBinding binding;

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GPSTracker gpsTracker;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();
//        ((MainActivity)context).binding.contentMain.rlToolbarMain.setVisibility(View.VISIBLE);
//        ((MainActivity)context).binding.contentMain.etSearchSong.setVisibility(View.VISIBLE);
//        ((MainActivity)context).binding.contentMain.ivMenu.setVisibility(View.VISIBLE);
//        ((MainActivity)context).binding.contentMain.tvToolbarTitle.setVisibility(View.GONE);
    }

    private void initMap() {
        try {
            FragmentManager fmanager = ((AppCompatActivity) context).getSupportFragmentManager();
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction ft = fmanager.beginTransaction();
            ft.replace(R.id.flMap, mapFragment);
            ft.commit();
            this.mapFragment.getMapAsync(this);

        } catch (IllegalStateException ex) {
            Log.e("Exception", "" + ex.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gpsTracker = new GPSTracker(context);
        mMap = googleMap;
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        CameraPosition camPos = new CameraPosition.Builder().
                target(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude())).zoom(14.2f).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()));

        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.addMarker(marker).showInfoWindow();
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }
}
