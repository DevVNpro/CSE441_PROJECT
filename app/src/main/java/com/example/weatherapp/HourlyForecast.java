package com.example.weatherapp;
//Thuc update time
public class HourlyForecast {
    private final String temperature;
    private final String description;
    private final String time;

    public HourlyForecast(String temperature, String description, int time) {
        this.temperature = temperature;
        this.description = description;
        this.time = String.valueOf(time);

    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

}
