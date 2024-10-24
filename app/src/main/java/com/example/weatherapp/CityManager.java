package com.example.weatherapp;

import java.util.HashSet;
import java.util.Set;

public class CityManager {

    private SharedPreferencesControl sharedPreferencesControl;
    private HashSet<String> cities;  // Set để lưu trữ các thành phố

    // Constructor nhận SharedPreferencesControl
    public CityManager(SharedPreferencesControl sharedPreferencesControl) {
        this.sharedPreferencesControl = sharedPreferencesControl;
        this.cities = sharedPreferencesControl.getCities();  // Lấy danh sách thành phố từ SharedPreferences
    }

    // Thêm một thành phố
    public void addCity(String city) {
        cities.add(city);  // Thêm thành phố vào Set
        sharedPreferencesControl.saveCities(cities);  // Lưu danh sách thành phố vào SharedPreferences
    }

    // Xóa một thành phố
    public void removeCity(String city) {
        if (cities.contains(city)) {
            cities.remove(city);  // Xóa thành phố từ Set
            sharedPreferencesControl.saveCities(cities);  // Lưu lại danh sách thành phố
        }
    }

    // Lấy tất cả các thành phố
    public Set<String> getAllCities() {
        return cities;  // Trả về danh sách thành phố
    }
}
