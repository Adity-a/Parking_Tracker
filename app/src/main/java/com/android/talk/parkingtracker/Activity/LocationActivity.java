package com.android.talk.parkingtracker.Activity;

import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.android.talk.parkingtracker.R;

import java.util.Objects;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final LatLng mDefaultLocation = new LatLng(30.2845299, 78.0486342);

    private static final int DEFAULT_ZOOM = 18;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            recreate();
            swipeRefreshLayout.setRefreshing(false);
        });

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setIndoorEnabled(false);

        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        //get the location permission
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            assert mLastKnownLocation != null;
                            LatLng currentLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));
                            //mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));


                            LatLng spotA = new LatLng(30.2845299, 78.0486342);
                            mMap.addMarker(new MarkerOptions().position(spotA).title("Spot A"));

                            LatLng spotB = new LatLng(30.2842456, 78.0484505);
                            mMap.addMarker(new MarkerOptions().position(spotB).title("Spot B"));

                            LatLng spotC = new LatLng(30.2843776, 78.0485350);
                            mMap.addMarker(new MarkerOptions().position(spotC).title("Spot C"));

                            Log.v("Location", String.valueOf(mLastKnownLocation.getLatitude()));
                            Log.v("Location", String.valueOf(mLastKnownLocation.getLongitude()));
                        } else {
                            Log.d("LocationActivity", "Current location is null. Using defaults.");
                            Log.e("LocationActivity", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_right);
    }
}