package com.didactex.www.batapp2;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;

import java.text.DecimalFormat;
import java.util.Locale;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.util.Log;
import android.location.LocationListener;

import android.location.Geocoder;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public  Geocoder   geocoder ;
    private GoogleMap mMap;
    private Marker mapMarker;
    public  String      pintitle;
    public  LatLng      ExifPos     = new LatLng(39.759444, -84.191667);
    public  double      Longitude   = 0;
    public  double      Latitude    = 0;
    public Integer      TFsave = 0;
    public String       MarkAddress;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected boolean gps_enabled,network_enabled;
    public String basetext = "Emergency at ";
    public TextView SMSFeed;
    public DecimalFormat df = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button TakePhoto =(Button)findViewById(R.id.CameraButton);
        geocoder                = new Geocoder(this, Locale.ENGLISH);

        SMSFeed = (TextView)findViewById(R.id.sample_view);
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }
    //http://stackoverflow.com/questions/3470693/android-get-location-or-prompt-to-enable-location-service-if-disabled
    //https://examples.javacodegeeks.com/android/core/location/android-location-based-services-example/
    private LatLng getLocation() throws SecurityException {
        //http://stackoverflow.com/questions/17591147/how-to-get-current-location-in-android
        Toast.makeText(MapsActivity.this,"in getLocation",Toast.LENGTH_LONG).show();
        Location location;
        Latitude    = 0;
        Longitude   = 0;
        // flag for GPS status
        boolean isGPSEnabled = false;


            Toast.makeText(this,"In try",Toast.LENGTH_LONG).show();
            locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);

            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    Toast.makeText(this,"Have Location",Toast.LENGTH_LONG).show();
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                }
            }


        // Remove the listener you previously added

        return new LatLng(Latitude, Longitude);
    }



    public class AsyncTaskgetMyLocationAddress extends AsyncTask< LatLng, String, String>{
    /// /(double Latitude, double Longitude) {
    //Get address in background
    @Override
            protected void onPreExecute(){}
    @Override
    protected String doInBackground(LatLng... arg0) {
        String mAddress=" ";
        Boolean geoTH = geocoder.isPresent();
        LatLng ll = arg0[0];
        mAddress = Boolean.toString(geoTH);
        if (geoTH) {
            try {
                //Place your latitude and longitude
                List<Address> addresses = geocoder.getFromLocation(Latitude, Longitude, 2);
                //if (addresses!=null)
                if (addresses.size() > 0) {
                    Address fetchedAddress = addresses.get(0);
                    StringBuilder strAddress = new StringBuilder();
                    for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                        if (i == 1) {
                            strAddress.append(fetchedAddress.getAddressLine(i)).append(" \n ");
                        } else {
                            strAddress.append(fetchedAddress.getAddressLine(i)).append("  ");
                        }
                    }
                    mAddress = strAddress.toString();
                } else {
                    mAddress = "No location found..!";
                }
            } catch (IOException e) {
                mAddress = "No Address.  Enter new one here.";

                Toast.makeText(getApplicationContext(), "Could not get address..!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return mAddress;
    }
        @Override
        protected void onPostExecute(String mAddress){
            mapMarker.setSnippet(mAddress);
            mapMarker.showInfoWindow();
            SMSFeed.setText(basetext+mAddress);


        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // ExifPos = getLocation();
        setMapMarker(ExifPos);
     }

   // private void setUpMap() {
    //    mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Target Marker"));
   // }

    public void setMapMarker(LatLng pos) {
        // map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

       mMap.clear();
        // Set the Marker up
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        TFsave = 0; //Boolean to keep the marker updating status without user input.
        if (TFsave == 0) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    pos).zoom(16).build();

            if (pos.latitude == 0) {
                cameraPosition = new CameraPosition.Builder().target(
                        pos).zoom(1).build();
                pintitle = "No Location Information.";

            } else {
                pintitle = "You are Here.";
            }
            markerOptions.title(pintitle);
            markerOptions.snippet("Drag to change location.");
            mapMarker = mMap.addMarker(markerOptions);
            mapMarker.setDraggable(true);
            mapMarker.showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker ImMark) {
                    mapMarker.setTitle(pintitle);
                    mapMarker.setSnippet("Drag Market to location");
                    mapMarker.showInfoWindow();
                }

                @Override
                public void onMarkerDrag(Marker ImMark) {
                    mapMarker.setSnippet("Target location may not be your location");
                    mapMarker.setTitle("Target location");
                    mapMarker.showInfoWindow();
                }

                @Override
                public void onMarkerDragEnd(Marker ImMark) {
                    LatLng newpos   = ImMark.getPosition();
                    Latitude        = newpos.latitude;
                    Longitude       = newpos.longitude;
                    mapMarker.setTitle("Target Location" );
                    ExifPos         = new LatLng(Latitude, Longitude);
                    TFsave = 1;
                    new AsyncTaskgetMyLocationAddress().execute(ExifPos);
                    //getMyLocationAddress(Latitude, Longitude);
                    //MarkAddress = "Latitude: " + df.format(Latitude) + " Longitude: " +df.format(Longitude);
                    //mapMarker.setSnippet(MarkAddress);
                    mapMarker.showInfoWindow();
                }
            });

        } else {
            markerOptions.title("Target Location");
            markerOptions.snippet("Drag to change.");
            final Marker ImMarker = mMap.addMarker(markerOptions);
            mapMarker.setDraggable(true);
            mapMarker.showInfoWindow();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    pos).zoom(12).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            TFsave = 0;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    protected void onPause() {
        super.onPause();
        // mImageFetcher.setExitTasksEarly(true);
        // mImageFetcher.flushCache();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    protected void onStart() {

        super.onStart();
    }

    protected void onStop() {

        super.onStop();
    }
    // Create an instance of GoogleAPIClient.


}
