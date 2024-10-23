package com.example.weatherapp;

import java.util.List;

public class WeatherInfo {
    private double currentTemperature;    // Nhiệt độ hiện tại
    private String weatherDescription;    // Mô tả thời tiết hiện tại
    private double maxTemperature;        // Nhiệt độ cao nhất
    private double minTemperature;        // Nhiệt độ thấp nhất
    private double rainfall;              // Lượng mưa
    private int humidity;                 // Độ ẩm
    private double windSpeed;             // Tốc độ gió
    private List<HourlyForecast> hourlyForecasts;   // Danh sách dự báo theo giờ
    private List<DailyForecast> dailyForecasts;     // Danh sách dự báo theo ngày

    // Constructor
    public WeatherInfo(double currentTemperature, String weatherDescription,
                       double maxTemperature, double minTemperature,
                       double rainfall, int humidity, double windSpeed,
                       List<HourlyForecast> hourlyForecasts, List<DailyForecast> dailyForecasts) {
        this.currentTemperature = currentTemperature;
        this.weatherDescription = weatherDescription;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.rainfall = rainfall;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.hourlyForecasts = hourlyForecasts;
        this.dailyForecasts = dailyForecasts;
    }

    // Getters and Setters
    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getRainfall() {
        return rainfall;
    }

    public void setRainfall(double rainfall) {
        this.rainfall = rainfall;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public List<HourlyForecast> getHourlyForecasts() {
        return hourlyForecasts;
    }

    public void setHourlyForecasts(List<HourlyForecast> hourlyForecasts) {
        this.hourlyForecasts = hourlyForecasts;
    }

    public List<DailyForecast> getDailyForecasts() {
        return dailyForecasts;
    }

    public void setDailyForecasts(List<DailyForecast> dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }
}
