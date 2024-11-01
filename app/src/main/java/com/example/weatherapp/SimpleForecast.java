package com.example.weatherapp;

public class SimpleForecast {
    private String day;
    private String description;
    private float minTemp;
    private float maxTemp;
    private float currenttemp;

    public SimpleForecast(String day, String description, float minTemp, float maxTemp,float currenttemp) {
        this.day = day;
        this.description = description;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.currenttemp = currenttemp;
    }

    public String getDay() {
        return day;
    }


    public String getDescription() {
        return description;
    }


    public float getMinTemp() {
        return minTemp;
    }


    public float getMaxTemp() {
        return maxTemp;
    }

    private float GetCurrentTemp(){return  currenttemp;}



}
