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
        // Parse current temperature and description
       // JSONObject cityInfo = response.getJSONObject("city");
        JSONArray forecastList = response.getJSONArray("list");

        JSONObject currentForecast = forecastList.getJSONObject(0);
        JSONObject mainData = currentForecast.getJSONObject("main");
        double currentTemp = mainData.getDouble("temp");
        double minTemp = mainData.getDouble("temp_min");
        double maxTemp = mainData.getDouble("temp_max");
        int humidity = mainData.getInt("humidity");
        double windSpeed = currentForecast.getJSONObject("wind").getDouble("speed");
        String description = currentForecast.getJSONArray("weather").getJSONObject(0).getString("description");

        WeatherCity weatherCity = new WeatherCity(city);
        weatherCity.setCity(String.valueOf(city));
        weatherCity.setCurrentTemperature(currentTemp);
        weatherCity.setMinTemperature(minTemp);
        weatherCity.setMaxTemperature(maxTemp);
        weatherCity.setHumidity(humidity);
        weatherCity.setWindSpeed(windSpeed);
        weatherCity.setWeatherDescription(description);


        // Parse hourly forecast data for 12 intervals (36 hours)
        List<HourlyForecast> hourlyForecasts = new ArrayList<>();
        for (int i = 0; i < Math.min(12, forecastList.length()); i++) {
            JSONObject forecast = forecastList.getJSONObject(i);
            double tempHourly = forecast.getJSONObject("main").getDouble("temp");
            String hourlyDescription = forecast.getJSONArray("weather").getJSONObject(0).getString("description");
            hourlyForecasts.add(new HourlyForecast(String.format("%.1f°", tempHourly), hourlyDescription, i * 3));
        }
        weatherCity.setHourlyForecasts(hourlyForecasts);

        return weatherCity;
    }

    public interface WeatherDataCallback {
        void onSuccess(WeatherCity weatherCity);
        void onFailure(String errorMessage);
    }
}
