package com.example.weatherapp;

import java.util.List;

public class WeatherInfo {
    public double currentTemperature;    // Nhiệt độ hiện tại
    public String weatherDescription;    // Mô tả thời tiết hiện tại
    public double maxTemperature;        // Nhiệt độ cao nhất
    public double minTemperature;        // Nhiệt độ thấp nhất
    public double rainfall;              // Lượng mưa
    public int humidity;                 // Độ ẩm
    public double windSpeed;             // Tốc độ gió
    public List<HourlyForecast> hourlyForecasts;   // Danh sách dự báo theo giờ
    public List<SimpleForecast> simpleForecasts;     // Danh sách dự báo theo ngày

}
