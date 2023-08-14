package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.testapp.model.LocationData;
import com.example.testapp.viewmodel.LocationViewModel;

public class MessageActivity extends AppCompatActivity {

    private final String myTag = "My_Message_Activity";
    private TextView textView_latC;
    private TextView textView_longC;
    private TextView textView_speedC;
    private TextView textView_checkHoleC;
    private TextView textView_countC;
    private LocationViewModel locationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        textView_latC = findViewById(R.id.textView_latC);
        textView_longC = findViewById(R.id.textView_longC);
        textView_speedC = findViewById(R.id.textView_speedC);
        textView_checkHoleC = findViewById(R.id.textView_checkHoleC);
        textView_countC = findViewById(R.id.textView_countC);

        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        locationViewModel.getLocationLiveData().observe(this, new Observer<LocationData>() {
            @Override
            public void onChanged(LocationData locationData) {
                updateUI(locationData);
            }
        });
    }

    private void updateUI(LocationData locationData) {
        textView_latC.setText(String.valueOf(locationData.getLatitude()));
        textView_longC.setText(String.valueOf(locationData.getLongitude()));
        textView_speedC.setText(String.valueOf(locationData.getSpeed()));
        textView_checkHoleC.setText(String.valueOf(locationData.getCheckHole()));
        textView_countC.setText(String.valueOf(locationData.getCount()));

        Log.d(myTag, String.valueOf(locationData.getLatitude()));
        Log.d(myTag, String.valueOf(locationData.getCount()));
    }
}