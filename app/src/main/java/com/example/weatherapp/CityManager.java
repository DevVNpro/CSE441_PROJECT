package com.example.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CityManager {
    public static  CityManager instance;
    private static final String PREFS_NAME = "WeatherAppPrefs";
    private static final String CITIES_KEY = "cities";
    private static final String LAST_CITY_VIEW_KEY = "lastCityView";
    private static final String ASK_PERMISTION_KEY = "lastAskPermistion";
    private final SharedPreferences sharedPreferences;
    private List<WeatherCity> cityList;
    private String lastCityView ="";
    private String hasAskPermistion="";

    public CityManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        instance = this;
        loadCities();
    }

    public String GetPermistionKey(){
        return hasAskPermistion;
    }
    public void SetPermistionKey(){
        hasAskPermistion ="true";
        saveCities();
    }
    public  void SetLastCityView(String city){
        lastCityView = city;
        Log.d("CityManager", "SetLastCityView called. New lastCityView: " + lastCityView);
        saveCities();
    }
    public String GetLastCityView(){
        return  lastCityView;
    }

    private void loadCities() {
        String json = sharedPreferences.getString(CITIES_KEY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<WeatherCity>>() {}.getType();
            cityList = new Gson().fromJson(json, type);
        } else {
            cityList = new ArrayList<>();
        }
        String lastView = sharedPreferences.getString(LAST_CITY_VIEW_KEY,"");
        String hasPermistion = sharedPreferences.getString(ASK_PERMISTION_KEY,"");
        lastCityView = lastView;
        hasAskPermistion = hasPermistion;
    }

    private void saveCities() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CITIES_KEY, new Gson().toJson(cityList));
        editor.putString(LAST_CITY_VIEW_KEY, lastCityView); // Save lastCityView together
        editor.putString(ASK_PERMISTION_KEY,hasAskPermistion);
        editor.apply();
    }

    public List<WeatherCity> getCities() {
        return cityList;
    }
    public void updateCity(WeatherCity weatherCity) {
        cityList.stream()
                .filter(city -> city.getCity().equals(weatherCity.getCity()))
                .findAny()
                .ifPresent(city -> {
                   city.updateWeatherInfo(weatherCity);
                });
        saveCities();
    }

    public void addCity(WeatherCity city) {
        cityList.add(city);
        saveCities();
    }

    public void removeCity(WeatherCity city) {
        cityList.remove(city);
        saveCities();
    }
    public WeatherCity GetCity(String cityName) {
        return cityList.stream()
                .filter(city -> city.getCity().equals(cityName))
                .findFirst()
                .orElse(null); // Return null if no city matches
    }
    public boolean cityExists(String cityName) {
        return cityList.stream()
                .anyMatch(city -> city.getCity().equalsIgnoreCase(cityName));
    }

}