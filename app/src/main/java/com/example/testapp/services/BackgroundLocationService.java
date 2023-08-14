package com.example.testapp.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.testapp.MainActivity;
import com.example.testapp.model.LocationData;
import com.example.testapp.viewmodel.LocationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class BackgroundLocationService extends Service {
    private final String myTag = "My_Location_Service";
    private int count = 0; // để theo dõi xem service có chay ko
    private static final long UPDATE_INTERVAL = 2000; // Cập nhật vị trí mỗi 2 giây
    private static final long FASTEST_INTERVAL = 1000; // Cập nhật vị trí nhanh nhất mỗi 1 giây

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LocationViewModel locationViewModel;

    public void onCreate() {
        super.onCreate();
        locationViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(LocationViewModel.class);
        Log.d(myTag, "Service created");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        createLocationCallback();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        startLocationUpdates();
        Log.d(myTag, "Service started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(myTag, "Service destroyed");
        stopLocationUpdates();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createLocationRequest() {
        // Deprecated API
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(UPDATE_INTERVAL);
//        locationRequest.setFastestInterval(FASTEST_INTERVAL);
//        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(FASTEST_INTERVAL)
                .setMaxUpdateDelayMillis(UPDATE_INTERVAL)
                .build();
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    float speed = location.getSpeed();
                    int checkHole = checkInHole(latitude, longitude);
                    count  = count + 1;

                    locationViewModel.setLocationData(new LocationData(latitude, longitude, speed, checkHole, count));

                    Log.d(myTag, String.valueOf(latitude));
                    Log.d(myTag, String.valueOf(count));
                }
            }
        };
    }

    private void startLocationUpdates() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private int checkInHole(double latitude, double longitude) {
        int hole = 0;
        boolean isInsidePolygon = false;

        for (int i = 0; i < MainActivity.list_polygon.size(); i++) {
            List<LatLng> polygon = MainActivity.list_polygon.get(i);
            isInsidePolygon = PolyUtil.containsLocation(latitude, longitude, polygon, false);
            if (isInsidePolygon) {
                hole = i+1;
                break;
            }
        }
        return hole;
    }
}
