package com.example.weatherapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Set;


public class CityActivity extends AppCompatActivity {

    private CityManager cityManager;
    private TextView txtCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_city);

        // Khởi tạo SharedPreferencesControl và CityManager
        SharedPreferencesControl sharedPreferencesControl = new SharedPreferencesControl(this);
        cityManager = new CityManager(sharedPreferencesControl);

        // Tìm TextView để hiển thị danh sách thành phố
     //   txtCities = findViewById(R.id.txt_cities);

        // Lấy danh sách các thành phố và hiển thị
        Set<String> cities = cityManager.getAllCities();
        txtCities.setText("Cities: " + cities.toString());
    }
}