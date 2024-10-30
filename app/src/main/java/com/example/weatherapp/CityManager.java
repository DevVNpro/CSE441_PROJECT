package com.example.weatherapp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CityManager {

    private SharedPreferencesControl sharedPreferencesControl;
    private List<CityForecast> listCityForecast;  // Set để lưu trữ các thành phố

    // Constructor nhận SharedPreferencesControl
    public CityManager(SharedPreferencesControl sharedPreferencesControl) {
        this.sharedPreferencesControl = sharedPreferencesControl;
        this.listCityForecast = sharedPreferencesControl.getCities();  // Lấy danh sách thành phố từ SharedPreferences
    }

    // Thêm một thành phố
    public void addCity(CityForecast cityForecast) {
        listCityForecast.add(cityForecast);  // Thêm thành phố vào Set
        sharedPreferencesControl.saveCities(listCityForecast);  // Lưu danh sách thành phố vào SharedPreferences
    }

    // Xóa một thành phố
    public void removeCity(String city) {
        if (listCityForecast.contains(city)) {
            listCityForecast.remove(city);  // Xóa thành phố từ Set
            sharedPreferencesControl.saveCities(listCityForecast);  // Lưu lại danh sách thành phố
        }
    }

    // Lấy tất cả các thành phố
    public List<CityForecast> getAllCities() {
        return listCityForecast;  // Trả về danh sách thành phố
    }
}
