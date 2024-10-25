package com.example.weatherapp;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CityForecastActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CityForecastAdapter cityForecastAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);

        recyclerView = findViewById(R.id.recycleview_city);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<CityForecast> cityForecasts = new ArrayList<>();
        cityForecasts.add(new CityForecast("Ha noi", "25", "30", "20", "Cloudy"));
        cityForecasts.add(new CityForecast("TPHCM", "28", "33", "24", "Sunny"));
        cityForecasts.add(new CityForecast("Đa nang", "27", "31", "23", "Rainy"));

        cityForecastAdapter = new CityForecastAdapter(cityForecasts);
        recyclerView.setAdapter(cityForecastAdapter);
    }
}