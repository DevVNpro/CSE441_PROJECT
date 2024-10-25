package com.example.weatherapp;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CityForecast  {
    private final String cityName;
    private final String temperature;
    private final String highTemp;
    private final String lowTemp;
    private final String description;

    public CityForecast(String cityName, String temperature, String highTemp, String lowTemp, String description) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.description = description;
    }

    public String getCityName() {
        return cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHighTemp() {
        return highTemp;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public String getDescription() {
        return description;
    }
}
