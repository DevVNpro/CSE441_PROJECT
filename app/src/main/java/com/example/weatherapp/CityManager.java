package com.example.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CityManager {
    public static  CityManager instance;
    private static final String PREFS_NAME = "WeatherAppPrefs";
    private static final String CITIES_KEY = "cities";
    private final SharedPreferences sharedPreferences;
    private List<WeatherCity> cityList;

    public CityManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        instance = this;
        loadCities();
    }

    private void loadCities() {
        String json = sharedPreferences.getString(CITIES_KEY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<WeatherCity>>() {}.getType();
            cityList = new Gson().fromJson(json, type);
        } else {
            cityList = new ArrayList<>();
        }
    }

    private void saveCities() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CITIES_KEY, new Gson().toJson(cityList));
        editor.apply();
    }

    public List<WeatherCity> getCities() {
        return cityList;
    }

    public void addCity(WeatherCity city) {
        cityList.add(city);
        saveCities();
    }

    public void removeCity(WeatherCity city) {
        cityList.remove(city);
        saveCities();
    }
}