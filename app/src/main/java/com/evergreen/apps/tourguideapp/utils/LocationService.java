package com.evergreen.apps.tourguideapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService {

    private static final String TAG = LocationService.class.getSimpleName();

    private static final int MY_LOCATION_REQUEST_CODE = 1;

    private static Location mLocation;

    public static void setLastKnownLocation(final Context context, FusedLocationProviderClient fusedLocationProviderClient) {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
            }
        }


        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            LocationPreferences.setLocationDetails(context, location.getLatitude(), location.getLongitude());
                            Log.i(TAG, "onSuccess: "+ location.getLatitude() + " "+ location.getLongitude());
                        }
                    }
                });
    }


    public static double getDistanceInKm(Context context, Location endLocation){

        double distance = 0.00;

        if (LocationPreferences.isLocationLatLonAvailable(context)) {

            double latitude = LocationPreferences.getLocationCoordinates(context)[0];
            double longitude = LocationPreferences.getLocationCoordinates(context)[1];

            Location startLocation = new Location("Location");
            startLocation.setLatitude(latitude);
            startLocation.setLongitude(longitude);

            distance = convertToKm(startLocation, endLocation);
        }

        return distance;
    }

    private static List<Address> getAddresses(Location location, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address>  addresses = null;

        try {
            addresses = geocoder.getFromLocation( location.getLatitude(),
                    location.getLongitude(), 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public static Location getCoordinates(final Context context, FusedLocationProviderClient fusedLocationProviderClient, Activity activity) {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
            }
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            mLocation = location;
                        }
                    }
                });

        return mLocation;
    }

    private static float convertToKm(Location location, Location placeLocation) {
        return location.distanceTo(placeLocation)/1000;
    }

    private static float convertToMiles(Location location, Location placeLocation) {
        return location.distanceTo(placeLocation)/1609;
    }
}
