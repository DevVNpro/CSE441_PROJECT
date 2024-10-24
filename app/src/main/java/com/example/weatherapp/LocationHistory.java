package com.example.weatherapp;

public class LocationHistory {
    private double latitude;
    private double longitude;
    private String city;
    private String timestamp;

    // Constructor mặc định cần thiết cho Firebase
    public LocationHistory() {}

    public LocationHistory(double latitude, double longitude, String city, String timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.timestamp = timestamp;
    }

    // Getters và Setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
