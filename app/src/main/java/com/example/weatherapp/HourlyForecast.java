package com.example.weatherapp;

public class HourlyForecast {
    private String temperature;
    private String time;
    private int iconResourceId;

    public HourlyForecast(String temperature, String time, int iconResourceId) {
        this.temperature = temperature;
        this.time = time;
        this.iconResourceId = iconResourceId;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getTime() {
        return time;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
}
