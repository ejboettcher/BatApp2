package com.didactex.www.batapp2;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
                          Toast.makeText(MainActivity.this, "Location: +"+location.getLatitude()+", "+ location.getLongitude(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageButton imageButton = (ImageButton) findViewById(R.id.hlpBtn);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnGPS();
                Intent intent2=new Intent("android.location.GPS_ENABLED_CHANGE");
                intent2.putExtra("enabled", true);
                sendBroadcast(intent2);

                Intent intent = new Intent (getBaseContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    //http://stackoverflow.com/questions/3470693/android-get-location-or-prompt-to-enable-location-service-if-disabled
    //https://examples.javacodegeeks.com/android/core/location/android-location-based-services-example/
    private void turnOnGPS() throws SecurityException {
        LocationManager locationManager = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(true);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);

        String providerName = locationManager.getBestProvider(locationCritera, true);

        if (providerName != null && locationManager.isProviderEnabled(providerName)) {
            // Provider is enabled
            locationManager.requestLocationUpdates(providerName, 20000, 100, MainActivity.this.locationListener);
        } else {
            // Provider not enabled, prompt user to enable it
            Toast.makeText(MainActivity.this, R.string.gps_request, Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            MainActivity.this.startActivity(myIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
