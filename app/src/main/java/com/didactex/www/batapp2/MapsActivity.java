package com.didactex.www.batapp2;

import android.content.Context;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import com.google.android.gms.maps.model.Marker;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker mapMarker;
    public  String      pintitle;
    public  LatLng      ExifPos     = new LatLng(0,0);
    public  double      Longitude   = 0;
    public  double      Latitude    = 0;
    public Integer      TFsave = 0;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected boolean gps_enabled,network_enabled;
    protected Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button TakePhoto =(Button)findViewById(R.id.CameraButton);

      // locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

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
     }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Target Marker"));
    }

    public void setMapMarker(LatLng pos) {
        // map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        mMap.clear();
        // Set the Marker up
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        if (TFsave == 0) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    pos).zoom(12).build();

            if (pos.latitude == 0) {
                cameraPosition = new CameraPosition.Builder().target(
                        pos).zoom(1).build();
                pintitle = "No Location Information.";

            } else {
                pintitle = "Photo was taken here.";
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
                    mapMarker.setTitle("Target location");
                    mapMarker.setSnippet("Drag marker to location");
                    mapMarker.showInfoWindow();
                    ExifPos         = new LatLng(Latitude, Longitude);
                    TFsave = 1;
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
}
