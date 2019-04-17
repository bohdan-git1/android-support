package com.rapidzz.yourmusicmap.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rapidzz.mymusicmap.datamodel.model.fan.Song;
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentMapBinding;
import com.rapidzz.yourmusicmap.other.util.GPSTracker;
import com.rapidzz.yourmusicmap.other.util.Permission;
import com.rapidzz.yourmusicmap.other.util.ReplaceFragmentManger;
import com.rapidzz.yourmusicmap.other.util.RxBus;
import com.rapidzz.yourmusicmap.view.Adapters.SongsListAdapter;
import com.rapidzz.yourmusicmap.view.activities.MainActivity;
import com.rapidzz.yourmusicmap.viewmodel.SongViewModel;

import java.util.ArrayList;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
    SongViewModel viewModel;
    ArrayList<MarkerOptions> mMarkerArrayList;
    private SongsListAdapter adapter;
    private ArrayList<Song> songs;
    private String songIdToPlay = null;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) context).binding.appbar.tvEdit.setVisibility(View.GONE);
        initMap();
        init();
        registerWithBus();
        getInfo();
    }

    public void init() {
        songs = new ArrayList<>();
        mMarkerArrayList = new ArrayList<>();
        viewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        viewModelCallbacks(viewModel);
        binding.btnSetSong.setOnClickListener(this);
        binding.btnSongListing.setOnClickListener(this);
        binding.ivCurrentLoc.setOnClickListener(this);

    }

    private void getInfo() {
        String id = new SessionManager(context).getUserId();
        viewModel.getSongs(id, true);
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
        if(mMarkerArrayList.size() == 0){
            drawSongsMarker(songs);
        }
    }


    public void zoomMap(Location location) {

        if(location != null) {
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                            location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            mMap.clear();
            if(songs.size() > 0){
                drawSongsMarker(songs);
            }
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(location.getLatitude(), location.getLongitude()));
            mMap.addMarker(marker).showInfoWindow();
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }
    }

    @SuppressLint("CheckResult")
    private void registerWithBus() {
        RxBus.defaultInstance().toObservable().
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {

                    if (event instanceof LocationResult) {
                        LocationResult locationResult = (LocationResult) event;
                        if(location == null){
                            if(mMap != null)
                            zoomMap(locationResult.getLastLocation());
                        }
                        location = locationResult.getLastLocation();

                    } else if (event instanceof Bundle) {

                        Bundle args = (Bundle) event;

                        String type = args.getString("type");

                        if (type != null && type.equalsIgnoreCase("playSongFromNotification")) {
                            String songId = args.getString("songId");

                            Bundle bundle = new Bundle();
                            replaceFragment.replaceFragment(SongsFragment.newInstance(songId),
                                    SetSongFragment.TAG, bundle, getActivity());
                        }

                    }

                });
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSetSong) {

            Bundle bundle = new Bundle();
            if (location != null) {
                bundle.putDouble("lat", location.getLatitude());
                bundle.putDouble("lng", location.getLongitude());
            }
            replaceFragment.replaceFragment(new SetSongFragment(), SetSongFragment.TAG, bundle, getActivity());
            //binding.etSongUrl.setVisibility(View.VISIBLE);
        } else if (v == binding.btnSongListing) {

            Bundle bundle = new Bundle();
            replaceFragment.replaceFragment(new SongsFragment(), SetSongFragment.TAG, bundle, getActivity());
            //binding.etSongUrl.setVisibility(View.VISIBLE);
        } else if(v == binding.ivCurrentLoc){
            if (mMap != null)
                zoomMap(location);
        }

    }

    public void viewModelCallbacks(SongViewModel viewModel) {

        viewModel.mSongListingResponseMutableLiveData.observe(getViewLifecycleOwner(), songArrayList -> {


            songs.clear();
            songs.addAll(songArrayList);
            if(mMap != null) {

                drawSongsMarker(songs);
            }

            if (adapter != null)
                adapter.notifyDataSetChanged();

        });

    }


    private void drawSongsMarker(ArrayList<Song> songArrayList){
        if (songArrayList.size() > 0) {
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_music_marker);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 120, 120, false);
            for(int i = 0;i < songArrayList.size();i++) {
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(Double.parseDouble(songArrayList.get(i).getLat()), Double.parseDouble(songArrayList.get(i).getLng())));
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                marker.title(songArrayList.get(i).getTitle());
                mMarkerArrayList.add(marker);
                mMap.addMarker(marker).showInfoWindow();
            }
        }
    }
}
