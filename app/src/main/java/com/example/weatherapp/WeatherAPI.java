package com.example.weatherapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        double minTemp = mainData.optDouble("temp_min", 0.0);  // Default to 0.0 if not available
        double maxTemp = mainData.optDouble("temp_max", 0.0);



       // weatherCity.SetDateTime(formattedDate);
        weatherCity.setCurrentTemperature(currentTemp);
        weatherCity.setHumidity(humidity);
        weatherCity.setWindSpeed(windSpeed);
        weatherCity.setWeatherDescription(description);
        weatherCity.setMaxTemperature(maxTemp);
        weatherCity.setMinTemperature(minTemp);

        List<HourlyForecast> hourlyForecasts = new ArrayList<>();
        long currentTime = System.currentTimeMillis();

        long currentHour = (currentTime / (3600 * 1000)) * (3600 * 1000);
        if (currentTime % (3600 * 1000) != 0) {
            currentHour += 3600 * 1000;
        }
        long nextHourTime = currentHour;

        for (int i = 0; i < 8; i++) {
            JSONObject forecast;

            if (i == 0) {
                forecast = forecastList.getJSONObject(0);
            } else {
                // Dự báo cho các giờ tiếp theo, cách nhau 3 giờ
                forecast = forecastList.getJSONObject(i - 1);
            }

            double tempHourly = forecast.getJSONObject("main").getDouble("temp");
            String hourlyDescription = forecast.getJSONArray("weather").getJSONObject(0).getString("description");

            hourlyForecasts.add(new HourlyForecast(String.format("%.1f°", tempHourly), hourlyDescription, nextHourTime));

            nextHourTime += 3 * 3600 * 1000;
        }



        weatherCity.setHourlyForecasts(hourlyForecasts);

        List<SimpleForecast> dailyForecasts = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 5; i++) {
            JSONObject dailyForecast = forecastList.getJSONObject(i * 8);
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            String day = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());

            String dailyDescription = dailyForecast.getJSONArray("weather").getJSONObject(0).getString("description");

            double dayTemp = dailyForecast.getJSONObject("main").getDouble("temp");
            double feelsLikeTemp = dailyForecast.getJSONObject("main").getDouble("feels_like");

            dailyForecasts.add(new SimpleForecast(day, dailyDescription, (float) dayTemp, (float) feelsLikeTemp, (float) currentTemp));
        }

        weatherCity.setDailyForecasts(dailyForecasts);
        //update data in shareReference
        if(CityManager.instance.cityExists(weatherCity.getCity())){
            CityManager.instance.updateCity(weatherCity);
        }
        else {
            CityManager.instance.addCity(weatherCity);
        }
        return weatherCity;
    }

    public interface WeatherDataCallback {
        void onSuccess(WeatherCity weatherCity);
        void onFailure(String errorMessage);
    }
}
