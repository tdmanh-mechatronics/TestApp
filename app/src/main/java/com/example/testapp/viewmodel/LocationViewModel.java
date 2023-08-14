package com.example.testapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testapp.model.LocationData;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<LocationData> locationLiveData = new MutableLiveData<>();

    public void setLocationData(LocationData locationData) {
        locationLiveData.setValue(locationData);
    }

    public LiveData<LocationData> getLocationLiveData() {
        return locationLiveData;
    }
}
