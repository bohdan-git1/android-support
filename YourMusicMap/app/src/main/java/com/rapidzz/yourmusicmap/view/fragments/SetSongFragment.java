package com.rapidzz.yourmusicmap.view.fragments;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentSetSongBinding;
import com.rapidzz.yourmusicmap.other.util.GPSTracker;
import com.rapidzz.yourmusicmap.other.util.Permission;
import com.rapidzz.yourmusicmap.other.util.ReverseGeoCoding;
import com.rapidzz.yourmusicmap.other.util.StringUtils;
import com.rapidzz.yourmusicmap.viewmodel.SetSongViewModel;

import bolts.Task;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


public class SetSongFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener {
    public static final String TAG = SetSongFragment.class.getSimpleName();
    FragmentSetSongBinding binding;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    SetSongViewModel viewModel;
    protected GoogleApiClient mGoogleApiClient;
    private LatLng latLng;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSetSongBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();
        init();
        if(!Permission.isPermissionGranted(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
            Permission.requestPermission(SetSongFragment.this,Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public void init(){
        viewModel = ViewModelProviders.of(this).get(SetSongViewModel.class);
        viewModelCallbacks(viewModel);
        binding.btSaveSong.setOnClickListener(this);


    }



    public void viewModelCallbacks(SetSongViewModel viewModel){
        viewModel.getSnackbarMessage().observe(this, new Observer<OneShotEvent<String>>() {
            @Override
            public void onChanged(@Nullable OneShotEvent<String> stringOneShotEvent) {
                String msg = stringOneShotEvent.getContentIfNotHandled();
                showAlertDialog(msg);

            }
        });

        viewModel.getProgressBar().observe(this, new Observer<OneShotEvent<Boolean>>() {
            @Override
            public void onChanged(@Nullable OneShotEvent<Boolean> booleanOneShotEvent) {
                showProgressDialog(booleanOneShotEvent.getContentIfNotHandled());
            }
        });

        viewModel.mSongMutableLiveData.observe(this, user -> {

            //Log.e("Response",""+user.getFirstName());
            //getFragmentManager().popBackStack();
        });
    }


    private void initMap() {
        try {
            if(getActivity()!=null) {
                SupportMapFragment mapFragment = (SupportMapFragment)
                        getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(this);
                }
            }
        } catch (IllegalStateException ex) {
            Log.e("Exception", "" + ex.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gpsTracker = new GPSTracker(context);
        mMap = googleMap;
        LatLng UCA = new LatLng(-34, 151);
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        CameraPosition camPos = new CameraPosition.Builder().
                target(UCA/*new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())*/).zoom(14.2f).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));


        MarkerOptions marker = new MarkerOptions().position(
                UCA/*new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())*/);

        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.addMarker(marker).showInfoWindow();
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if(v == binding.btSaveSong){
            /*String id = new SessionManager(getActivity()).getUser().getId();
            viewModel.doSaveSong(binding.etSongTitle.getText().toString()
                    ,binding.etSongUrl.getText().toString()
                    ,
                    ,binding.etPassword.getText().toString());*/
        }
    }

}
