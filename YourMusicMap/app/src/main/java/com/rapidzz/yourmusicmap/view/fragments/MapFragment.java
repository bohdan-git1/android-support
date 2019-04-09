package com.rapidzz.yourmusicmap.view.fragments;

import android.Manifest;
import android.content.Context;
import android.location.Location;
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

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentMapBinding;
import com.rapidzz.yourmusicmap.other.util.GPSTracker;
import com.rapidzz.yourmusicmap.other.util.Permission;
import com.rapidzz.yourmusicmap.other.util.ReplaceFragmentManger;
import com.rapidzz.yourmusicmap.other.util.RxBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener {
    public static final String TAG = MapFragment.class.getSimpleName();
    private Context context;
    FragmentMapBinding binding;
    ReplaceFragmentManger replaceFragment;
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    Location location;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceFragment = new ReplaceFragmentManger();
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
        init();
        registerWithBus();
        if(!Permission.isPermissionGranted(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            Permission.requestPermission(MapFragment.this,Manifest.permission.ACCESS_FINE_LOCATION);
        }
//        ((MainActivity)context).binding.contentMain.rlToolbarMain.setVisibility(View.VISIBLE);
//        ((MainActivity)context).binding.contentMain.etSearchSong.setVisibility(View.VISIBLE);
//        ((MainActivity)context).binding.contentMain.ivMenu.setVisibility(View.VISIBLE);
//        ((MainActivity)context).binding.contentMain.tvToolbarTitle.setVisibility(View.GONE);
    }

    public void init(){
        binding.btnSetSong.setOnClickListener(this);
        binding.btnSongListing.setOnClickListener(this);

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
                target(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())).zoom(14.2f).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
         mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }


    public void zoomMap(Location location) {
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                        location.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(location.getLatitude(), location.getLongitude()));
        mMap.addMarker(marker).showInfoWindow();
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 50) {

        }
    }

    private void registerWithBus() {
        RxBus.defaultInstance().toObservable().
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object event) {

                        if (event instanceof LocationResult) {
                            LocationResult locationResult = (LocationResult) event;
                             location = locationResult.getLastLocation();
                            if (mMap != null)
                                zoomMap(location);
                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == binding.btnSetSong){

            Bundle bundle = new Bundle();
            if(location != null) {
                bundle.putDouble("lat",location.getLatitude() );
                bundle.putDouble("lng",location.getLongitude() );
            }
            replaceFragment.replaceFragment(new SetSongFragment(),SetSongFragment.TAG,bundle,getActivity());
            //binding.etSongUrl.setVisibility(View.VISIBLE);
        }else  if(v == binding.btnSongListing){

            Bundle bundle = new Bundle();
            replaceFragment.replaceFragment(new SongsFragment(),SetSongFragment.TAG,bundle,getActivity());
            //binding.etSongUrl.setVisibility(View.VISIBLE);
        }

    }
}
