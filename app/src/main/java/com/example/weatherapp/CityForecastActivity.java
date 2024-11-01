package com.example.weatherapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CityForecastActivity extends AppCompatActivity {

    private CityManager cityManager;
    private CityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);

        cityManager = new CityManager(this);
        List<WeatherCity> cities = cityManager.getCities();

        RecyclerView recyclerView = findViewById(R.id.recycleview_city);
        LinearLayoutManager layoutManagerDaily = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerDaily);

        cityAdapter = new CityAdapter(cities); // Pass onDeleteClick to adapter
        recyclerView.setAdapter(cityAdapter);

        if (cities.isEmpty()) {
            WeatherAPI weatherAPI = new WeatherAPI(this);
            weatherAPI.fetchWeatherData("Ha Noi", new WeatherAPI.WeatherDataCallback() {
                @Override
                public void onSuccess(WeatherCity weatherCity) {
                    cityManager.addCity(weatherCity);
                    cityAdapter.updateCities(cities); // Update the adapter with the new list
                    Toast.makeText(CityForecastActivity.this, "Weather data fetched successfully for " + weatherCity.getCity(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(CityForecastActivity.this, "Failed to fetch weather data: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(CityForecastActivity.this, "Data Not Empty " , Toast.LENGTH_SHORT).show();

            cityAdapter.updateCities(cities); // Update adapter initially with existing cities
        }


        recyclerView.setAdapter(cityAdapter);
        cityAdapter.updateCities(cities);
    }

}