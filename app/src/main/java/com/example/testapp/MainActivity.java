package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.testapp.classes.JSONReader;
import com.example.testapp.services.BackgroundLocationService;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    public final static List<List<LatLng>> list_polygon = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Yêu cầu quyền truy cập
        requestLocationPermission();

        // Lấy dữ liệu từ Json file assets/SK_Holes.json
        getDataFromJson();

        // Khởi chạy service Location
        Intent serviceIntent = new Intent(this, BackgroundLocationService.class);
        startService(serviceIntent);

        // Chuyển sang menu activity
        Button btnStartMain = findViewById(R.id.btn_start_main);
        btnStartMain.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        });
    }

    private void requestLocationPermission() {
        // Check SDK version >= 29
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 29) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    // Xử lý kết quả của yêu cầu quyền truy cập vị trí
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Quyền truy cập vị trí đã được cấp, có thể tiếp tục xử lý
            } else {
                // Xử lý khi người dùng từ chối cấp quyền truy cập vị trí
                Toast.makeText(this, "Bạn đã từ chối quền truy cập vị trí, điều này sẽ gây ra các hạn chế", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getDataFromJson() {
        JSONReader jsonReader = new JSONReader(MainActivity.this);
        JSONObject jsonObject = jsonReader.readJSONFromAsset("Holes.json");
        if (jsonObject != null) {
            try {
                JSONArray data = jsonObject.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject holeData = data.getJSONObject(i);
                    JSONArray coordinatesArray = holeData.getJSONArray("coordinates");
                    List<LatLng> polygon = new ArrayList<>();

                    for (int j = 0; j < coordinatesArray.length(); j++) {
                        JSONArray coordinate = coordinatesArray.getJSONArray(j);
                        double longitude = coordinate.getDouble(0);
                        double latitude = coordinate.getDouble(1);

                        LatLng point = new LatLng(latitude, longitude);
                        polygon.add(point);
                    }
                    list_polygon.add(polygon);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}