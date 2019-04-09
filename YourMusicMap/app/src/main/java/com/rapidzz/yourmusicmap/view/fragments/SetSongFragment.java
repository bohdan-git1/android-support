package com.rapidzz.yourmusicmap.view.fragments;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rapidzz.mymusicmap.other.extensions.OneShotEvent;
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentSetSongBinding;
import com.rapidzz.yourmusicmap.other.util.GPSTracker;
import com.rapidzz.yourmusicmap.other.util.Permission;
import com.rapidzz.yourmusicmap.view.Adapters.PlacesAutoCompleteAdapter;
import com.rapidzz.yourmusicmap.viewmodel.SongViewModel;

import java.io.File;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


public class SetSongFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener {
    public static final String TAG = SetSongFragment.class.getSimpleName();
    FragmentSetSongBinding binding;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    SongViewModel viewModel;
    protected GoogleApiClient mGoogleApiClient;
    private PlacesAutoCompleteAdapter mPlacesAdapter;
    SupportMapFragment mapFragment;
    Location mLocation;
    int REQUEST_CODE = 1001;

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
        mLocation = new Location("");
        if (getArguments().containsKey("lat")) {
            mLocation.setLatitude(getArguments().getDouble("lat"));
            mLocation.setLongitude(getArguments().getDouble("lng"));

        }
        initMap();
        init();
        if (!Permission.isPermissionGranted(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Permission.requestPermission(SetSongFragment.this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public void init() {
        viewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        viewModelCallbacks(viewModel);
        binding.btSaveSong.setOnClickListener(this);
        binding.ivMpsong.setOnClickListener(this);


        placesText();
    }


    public void placesText() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();

        Log.e("GoogleClientAPI", "Is Connected " + mGoogleApiClient.isConnected());

        // builder = new PlacePicker.IntentBuilder();

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("PK")
                .build();


        mPlacesAdapter = new PlacesAutoCompleteAdapter(context, android.R.layout.simple_list_item_1,
                mGoogleApiClient, typeFilter);
        binding.actvPlaces.setAdapter(mPlacesAdapter);
        binding.actvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Log.e("Tag", " OnItemClick pick");
                    view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    getPlaces(position);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    public void getPlaces(int position) {
        try {
            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlacesAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            try {
                if (!places.getStatus().isSuccess()) {
                    Log.e("TAG", "Place query did not complete. Error: " +
                            places.getStatus().toString());
                    return;
                }
                // Selecting the first object buffer.
                final Place place = places.get(0);

                Log.e("TAG", "Place query did not complete. Error: " +
                        places.getStatus().toString());

                if (place == null)
                    return;


                mMap.clear();
                Log.d("where to lat ", places.get(0).getLatLng() + "");
                //Move the camera on that zoom
                CameraPosition cameraPosition = new CameraPosition.Builder().target(places.get(0).getLatLng()).zoom(11).build();

                LatLng latLng = places.get(0).getLatLng();
                Log.d("where to lat ", latLng.latitude + "");
                Log.d("where to long ", latLng.longitude + "");


                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latLng.latitude, latLng.longitude));
                mMap.addMarker(marker).showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };


    public void viewModelCallbacks(SongViewModel viewModel) {
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

        viewModel.mSongMutableLiveData.observe(this, songResponse -> {

            getFragmentManager().popBackStack();
            //Log.e("Response",""+user.getFirstName());
            //getFragmentManager().popBackStack();
        });
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
        LatLng UCA = new LatLng(0.0, 0.0);
        if (mLocation != null) {
            UCA = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        }
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
        if (v == binding.btSaveSong) {
            String id = new SessionManager(getActivity()).getUserId();
            viewModel.doSaveSong(binding.etSongTitle.getText().toString()
                    , binding.etSongUrl.getText().toString()
                    , id
                    , mLocation.getLatitude() + ""
                    , mLocation.getLongitude() + "");
        }else if (v == binding.ivMpsong) {
           pickAudio();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null) {
            if (requestCode == REQUEST_CODE) {

                Uri uri = data.getData();
                try {
                    String uriString = uri.toString();
                    String songURl = null;
                    String path = getAudioPath(uri);
                    File myFile = new File(path);
                    songURl = myFile.getName();


                    Log.e("Name",songURl);
                    String songTitle = getArtistName(path);

                    if (songTitle == null) {
                        binding.etSongTitle.setText("unknown title");
                    }else {
                        binding.etSongTitle.setText(songTitle);
                    }

                    binding.etSongUrl.setText(path);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Unable to process,try again", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getArtistName(String myFile) {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(myFile);
        String artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);


//        Log.e("artist",artist);
//        Log.e("title",titlele);

        return title;
    }


    private String getAudioPath(Uri uri) {
        String[] data = {MediaStore.Audio.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void pickAudio(){
        Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(Intent.createChooser(audioIntent, "Select Audio"), REQUEST_CODE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

}
