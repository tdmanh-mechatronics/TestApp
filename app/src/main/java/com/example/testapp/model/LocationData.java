package com.example.testapp.model;

public class LocationData {
    private double latitude;
    private double longitude;
    private float speed;
    private int checkHole;
    private int count;

    public LocationData(double latitude, double longitude, float speed, int checkHole, int count) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.checkHole = checkHole;
        this.count = count;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getSpeed() {
        return speed;
    }

    public int getCheckHole() {
        return checkHole;
    }

    public int getCount() {
        return count;
    }
}
