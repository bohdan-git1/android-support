package com.rapidzz.yourmusicmap.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
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
import com.rapidzz.mymusicmap.datamodel.source.UserDataSource;
import com.rapidzz.mymusicmap.other.util.SessionManager;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.databinding.FragmentSetSongBinding;
import com.rapidzz.yourmusicmap.other.util.GPSTracker;
import com.rapidzz.yourmusicmap.other.util.Permission;
import com.rapidzz.yourmusicmap.view.Adapters.PlacesAutoCompleteAdapter;
import com.rapidzz.yourmusicmap.view.activities.MainActivity;
import com.rapidzz.yourmusicmap.view.activities.PaypalWebView;
import com.rapidzz.yourmusicmap.viewmodel.SongViewModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;
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
    final int BRAIN_CODE = 1101;
    List<Address> addresses;
    Address address;
    private LatLng mCenterLatLong;
    boolean addressChecker;
    String strAddress;
    Geocoder geocoder;
    String clientToken = "";

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCenterLatLong = null;
        addressChecker = true;
    }

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

        ((MainActivity) context).binding.appbar.tvEdit.setVisibility(View.GONE);

        mLocation = new Location("");
        if (getArguments().containsKey("lat")) {
            mLocation.setLatitude(getArguments().getDouble("lat"));
            mLocation.setLongitude(getArguments().getDouble("lng"));

        }
        initMap();
        init();

        geocoder = new Geocoder(context, Locale.getDefault());
//        ViewModelFactory factory =
//                ViewModelFactory.Companion.getInstance(getActivity().getApplication());
//
//        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
//        viewModelCallbacks(viewModel);

       // mMarkers = new ArrayList<>();
        gpsTracker = new GPSTracker(context);
        if (!Permission.isPermissionGranted(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Permission.requestPermission(SetSongFragment.this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public void init() {
        viewModel = ViewModelProviders.of(this).get(SongViewModel.class);
        viewModelCallbacks(viewModel);
        binding.btSaveSong.setOnClickListener(this);
        binding.btSaveSong.setEnabled(false);
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

                mLocation.setLongitude(latLng.longitude);
                mLocation.setLatitude(latLng.latitude);

                isTrackAvailable(latLng);

                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latLng.latitude, latLng.longitude));

                mMap.addMarker(marker).showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private void isTrackAvailable(LatLng latLng) {
        viewModel.isTrackAvailable("" + latLng.latitude, "" + latLng.longitude,
                new UserDataSource.IsTrackAvailableCallback() {
                    @Override
                    public void onResponse(boolean trackAvailable, String message) {
                        binding.btSaveSong.setEnabled(true);


                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        if (trackAvailable) {
                            binding.btSaveSong.setText("Pay USD 1.0");
                            binding.txtTrack.setVisibility(View.VISIBLE);
                        } else {
                            binding.btSaveSong.setText("ADD SONG");
                            binding.txtTrack.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(@NotNull String error) {
                        Toast.makeText(context, "Song already exists at this location", Toast.LENGTH_SHORT).show();
                        binding.btSaveSong.setEnabled(false);
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void viewModelCallbacks(SongViewModel viewModel) {
        viewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), stringOneShotEvent -> {
            String msg = stringOneShotEvent.getContentIfNotHandled();
            if (msg != null)
                showAlertDialog(msg);

        });

        viewModel.getProgressBar().observe(getViewLifecycleOwner(), booleanOneShotEvent -> showProgressDialog(booleanOneShotEvent.getContentIfNotHandled()));

        viewModel.mSongMutableLiveData.observe(getViewLifecycleOwner(), songResponse -> {

            showSuccessDialog(songResponse.getMessage(), "addsong");
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

//        mMap.getUiSettings().setTiltGesturesEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.addMarker(marker).showInfoWindow();
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnCameraMoveStartedListener(i -> binding.btSaveSong.setEnabled(false));
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);

//        mMap.setOnCameraIdleListener(() -> isTrackAvailable(mMap.getCameraPosition().target));

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (!gpsTracker.canGetLocation())
                    gpsTracker = new GPSTracker(context);
                if (addressChecker && gpsTracker.isGPSEnabled) {
                    mCenterLatLong = mMap.getCameraPosition().target;
                    takingAddress();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btSaveSong) {

            if (binding.btSaveSong.getText().toString().equalsIgnoreCase("Pay USD 1.0")) {
                //showAlertDialog("Payment is under development");
                setupPaypal();
               // Intent intent = new Intent(getActivity(), PaypalWebView.class);
               // startActivity(intent);
            } else {
                String id = new SessionManager(getActivity()).getUserId();
                viewModel.doSaveSong(binding.etSongTitle.getText().toString()/*"online song"*/
                        , binding.etSongUrl.getText().toString()/*"https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"*/
                        , id
                        , mLocation.getLatitude() + ""
                        , mLocation.getLongitude() + "");
            }
        } else if (v == binding.ivMpsong) {
            pickAudio();
        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {

                    final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        gpsTracker.showSettingsAlert();
                    } else {
                        addressChecker = true;
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        CameraPosition camPos = new CameraPosition.Builder().
                                target(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())).zoom(14.2f).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
                    }
                    return false;
                }
            };

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


                    Log.e("Name", songURl);
                    String songTitle = getArtistName(path);

                    if (songTitle == null) {
                        binding.etSongTitle.setText("unknown title");
                    } else {
                        binding.etSongTitle.setText(songTitle);
                    }

                    binding.etSongUrl.setText(path);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Unable to process,try again", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);

                Toast.makeText(getActivity(),"Thank you for your payment, You will be able to change songs in next build",Toast.LENGTH_LONG).show();
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        }
    }

    private String getArtistName(String myFile) {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(myFile);
        String artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
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


    private void pickAudio() {

        if (!Permission.isPermissionGranted(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Permission.requestPermission(this, REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions,
                                           @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            pickAudio();
        }

    }


    private void setupPaypal() {

        new Connection().execute();

     /*   final PYPLCheckoutEnvironment pyplEnvironment = PYPLCheckoutEnvironment.getInstance();

        pyplEnvironment.setkPYPLEnvironment(Environment.SANDBOX);

        pyplEnvironment.setkPYPLUrlScheme("musicmap");

//set the redurect uri, that has the assetLinks.json.
//        pyplEnvironment.setkPYPLRedirectURL("https://paypalmerchant.herokuapp.com/thankyou");

//set the client ID for the merchant
        pyplEnvironment.setClientId("AUMMTnE_YQVy07B6NFrINKGnrv5puWQjsFT_swji9n4-3uES17q_3KcMfh9edzhVdC_MlN1YpBe6p1ig");

//set the user context. 'this' should be the activity from which the experience is being called.
        pyplEnvironment.setkPYPLUserContext(getActivity());


        //MainActivity.class

//in your activity where you want to call the experience.
        WebView webView = new WebView(getActivity());

//SampleWebViewIntercept is your webViewClient.
        webView.setWebViewClient(new SampleWebViewIntercept());

//SampleWebViewIntercept.class

        pyplEnvironment.setkCheckoutDelegate(new PYPLCheckoutDelegate(){

            @Override
            public void completeCheckout(HashMap<String,String> returnParams) {

                Log.e("CheckoutFinishedWith>>", returnParams.toString());

            }

            // in addition to the checkoutComplete delegate you can also provide a canceled delegate that is called when the user exits CCT amidst checkout
            @Override
            public void checkoutCanceled() {

                Log.e("Checkout Canceled>>", "Checkout Canceled");

            }

        });

        pyplEnvironment.setkPYPLWebBrowserOnly(true);

        PYPLCheckout.getInstance().startCheckoutWithECToken(getActivity(), "A21AAG11eY0sZxJ5KXvJUbjfAmbeKDY7h_codl7TAuhZxZY0mLSxaJIWFFxFZffrQqL-3qPFTlCX51gbIZbzHY44EguFVpmhQ");

    }

    public class SampleWebViewIntercept extends WebViewClient {

        //include this for integrating with Checkout.js
        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

            //this will load a script to handle the Checkout.js integration
            PYPLCheckout.getInstance().loadScript(view);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {

            return PYPLCheckout.getInstance().shouldOverrideUrlLoading(view, url);

        }*/

    }

    private class Connection extends AsyncTask<String, Void, String> {

        protected void onPostExecute(String result) {
            onBraintreeSubmit(result);
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.mashghol.com/alsadaqah/public/api/generatetoken");

            try {

                HttpResponse response = httpclient.execute(httppost);
                String responseContent = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(responseContent);
                clientToken = jsonObject.getString("client_token");
                Log.e("Response", responseContent );

            } catch (ClientProtocolException e) {
// TODO Auto-generated catch block
            } catch (IOException e) {
// TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return clientToken;
        }

        @Override
        protected void onPreExecute() {
        }

     }

  //  https://www.paypal.com/cgi-bin/webscr?&cmd=_xclick&business=gol@gmail.com&currency_code=USD&amount=1&item_name=Change song

   /* public String getToken(){

    }*/

    private void takingAddress() {
        new LongOperation().execute("");
    }


    public void onBraintreeSubmit(String clientToken) {
        DropInRequest dropInRequest = new DropInRequest()
                .amount("1.0")
                .clientToken(clientToken);
        startActivityForResult(dropInRequest.getIntent(getActivity()), BRAIN_CODE);
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            strAddress = "";
            Log.e("Text","ok");
            try {
                if (mCenterLatLong != null)
                    addresses = geocoder.getFromLocation(mCenterLatLong.latitude, mCenterLatLong.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!addresses.isEmpty() && addresses != null && mCenterLatLong != null) {
                address = addresses.get(0);

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    if (strAddress.isEmpty())
                        strAddress = strAddress + address.getAddressLine(i);
                    else
                        strAddress = strAddress + ", " + address.getAddressLine(i);
                }
            }
            return strAddress;
        }

        @Override
        protected void onPostExecute(String result) {
            if (getActivity() != null) {
                binding.locationMarkertext.setText(result);
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}

