package com.example.weatherapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherAPI {
    private static final String API_KEY = "ca0cc331f07186dbfb8156dbecaa91db";
    private final RequestQueue requestQueue;

    public WeatherAPI(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void fetchWeatherData(String city, final WeatherDataCallback callback) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city.replace(" ", "%20") + "&appid=" + API_KEY + "&units=metric";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        WeatherCity weatherCity = parseWeatherData(response, city);
                        callback.onSuccess(weatherCity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure("Error parsing weather data");
                    }
                },
                error -> callback.onFailure("Error fetching weather data")
        );

        requestQueue.add(request);
    }

    private WeatherCity parseWeatherData(JSONObject response, String city) throws JSONException {
        WeatherCity weatherCity = new WeatherCity(city);

        JSONArray forecastList = response.getJSONArray("list");

        // Get data for the first forecast
        JSONObject currentForecast = forecastList.getJSONObject(0);
        JSONObject mainData = currentForecast.getJSONObject("main");
        double currentTemp = mainData.getDouble("temp");
        int humidity = mainData.optInt("humidity", 0);
        double windSpeed = currentForecast.getJSONObject("wind").optDouble("speed", 0.0);
        String description = currentForecast.getJSONArray("weather").getJSONObject(0).getString("description");

        weatherCity.setCurrentTemperature(currentTemp);
        weatherCity.setHumidity(humidity);
        weatherCity.setWindSpeed(windSpeed);
        weatherCity.setWeatherDescription(description);

        List<HourlyForecast> hourlyForecasts = new ArrayList<>();
        for (int i = 0; i < Math.min(12, forecastList.length()); i++) {
            JSONObject forecast = forecastList.getJSONObject(i);
            double tempHourly = forecast.getJSONObject("main").getDouble("temp");
            String hourlyDescription = forecast.getJSONArray("weather").getJSONObject(0).getString("description");
            hourlyForecasts.add(new HourlyForecast(String.format("%.1f°", tempHourly), hourlyDescription, i * 3));
        }
        weatherCity.setHourlyForecasts(hourlyForecasts);

        List<SimpleForecast> dailyForecasts = new ArrayList<>();
        for (int i = 0; i < 5; i++) { // Get forecast for 5 days
            // For each day, take the first entry of the day's 8 entries
            JSONObject dailyForecast = forecastList.getJSONObject(i * 8);
            String day = "Day " + (i + 1);
            String dailyDescription = dailyForecast.getJSONArray("weather").getJSONObject(0).getString("description");

            // Get the current temperature and feels-like temperature for each day
            double dayTemp = dailyForecast.getJSONObject("main").getDouble("temp");
            double feelsLikeTemp = dailyForecast.getJSONObject("main").getDouble("feels_like");

            // Add daily forecast to the list
            dailyForecasts.add(new SimpleForecast(day, dailyDescription, (float) dayTemp, (float) feelsLikeTemp, (float) currentTemp));
        }
        weatherCity.setDailyForecasts(dailyForecasts);

        return weatherCity;
    }

    public interface WeatherDataCallback {
        void onSuccess(WeatherCity weatherCity);
        void onFailure(String errorMessage);
    }
}
