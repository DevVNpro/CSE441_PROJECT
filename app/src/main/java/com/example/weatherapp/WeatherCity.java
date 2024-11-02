package com.example.weatherapp;

import java.util.List;

public class WeatherCity {
    private String city;
    private WeatherInfo weatherInfo;

    public WeatherCity(String city) {
        this.weatherInfo = new WeatherInfo();
        this.city = city;
    }


    public double getCurrentTemperature() {
        return weatherInfo.currentTemperature;
    }

    public void setCurrentTemperature(double currentTemperature) {
        weatherInfo.currentTemperature = currentTemperature;
    }

    public String getWeatherDescription() {
        return weatherInfo.weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        weatherInfo.weatherDescription = weatherDescription;
    }

    public double getMaxTemperature() {
        return weatherInfo.maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        weatherInfo.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return weatherInfo.minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        weatherInfo.minTemperature = minTemperature;
    }

    public double getRainfall() {
        return weatherInfo.rainfall;
    }

    public void setRainfall(double rainfall) {
        weatherInfo.rainfall = rainfall;
    }

    public int getHumidity() {
        return weatherInfo.humidity;
    }

    public void setHumidity(int humidity) {
        weatherInfo.humidity = humidity;
    }

    public double getWindSpeed() {
        return weatherInfo.windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        weatherInfo.windSpeed = windSpeed;
    }

    public List<HourlyForecast> getHourlyForecasts() {
        return weatherInfo.hourlyForecasts;
    }

    public void setHourlyForecasts(List<HourlyForecast> hourlyForecasts) {
        weatherInfo.hourlyForecasts = hourlyForecasts;
    }

    public List<SimpleForecast> getDailyForecasts() {
        return weatherInfo.simpleForecasts;
    }

    public void setDailyForecasts(List<SimpleForecast> simpleForecasts) {
        weatherInfo.simpleForecasts = simpleForecasts;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public void updateWeatherInfo(WeatherCity newCityInfo) {
        if (newCityInfo != null) {
            this.weatherInfo.currentTemperature = newCityInfo.getCurrentTemperature();
            this.weatherInfo.weatherDescription = newCityInfo.getWeatherDescription();
            this.weatherInfo.maxTemperature = newCityInfo.getMaxTemperature();
            this.weatherInfo.minTemperature = newCityInfo.getMinTemperature();
            this.weatherInfo.rainfall = newCityInfo.getRainfall();
            this.weatherInfo.humidity = newCityInfo.getHumidity();
            this.weatherInfo.windSpeed = newCityInfo.getWindSpeed();
            this.weatherInfo.hourlyForecasts = newCityInfo.getHourlyForecasts();
            this.weatherInfo.simpleForecasts = newCityInfo.getDailyForecasts();
        }
    }
}
