package com.example.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SharedPreferencesControl {

    private static final String PREF_NAME = "WeatherAppPrefs";
    private static final String KEY_CITIES = "cities";
    private static final String KEY_FIRST_LAUNCH = "isFirstLaunch";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesControl(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveCities(List<CityForecast> cityForecast) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(cityForecast);
        editor.putStringSet(KEY_CITIES, Collections.singleton(json));
        editor.apply();
    }

    public  List<CityForecast>  getCities() {
        boolean isFirstLaunch = sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
        HashSet<String> citiesSet = (HashSet<String>) sharedPreferences.getStringSet(KEY_CITIES, new HashSet<>());

        // Nếu là lần đầu, thêm dữ liệu mẫu vào HashSet
        if (isFirstLaunch) {
            citiesSet.add("{\"cityName\":\"Ha Noi\",\"temperature\":\"25.0\",\"highTemp\":\"30.0\",\"lowTemp\":\"20.0\",\"description\":\"Clear sky\"}");

            editor.putBoolean(KEY_FIRST_LAUNCH, false);
            editor.apply();
        }

        List<CityForecast> cityForecasts = new ArrayList<>();
        Gson gson = new Gson();

        for (String cityJson : citiesSet) {
            // Chuyển từ chuỗi JSON sang đối tượng CityForecast
            CityForecast cityForecast = gson.fromJson(cityJson, CityForecast.class);
            cityForecasts.add(cityForecast);
        }

        return cityForecasts;
    }
}
