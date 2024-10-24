package com.example.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

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

    public void saveCities(HashSet<String> cities) {
        editor.putStringSet(KEY_CITIES, cities);
        editor.apply();
    }

    public HashSet<String> getCities() {
        boolean isFirstLaunch = sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);

        HashSet<String> cities = (HashSet<String>) sharedPreferences.getStringSet(KEY_CITIES, new HashSet<>());

        if (isFirstLaunch) {
            cities.add("Ha Noi");
            saveCities(cities);

            editor.putBoolean(KEY_FIRST_LAUNCH, false);
            editor.apply();
        }

        return cities;
    }
}
