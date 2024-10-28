package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

    public class MainActivity extends AppCompatActivity {

        private HourlyForecastAdapter hourlyForecastAdapter;
        private DailyForecastAdapter dailyForecastAdapter;
        private TextView cityNameTextView, currentTemperatureTextView, weatherDescriptionTextView, highLowTempTextView, rainPercentageTextView, windSpeedTextView, humidityPercentageTextView, currentTimeTextView;
        private ImageView weatherIconImageView, rainIconImageView, windIconImageView, humidityIconImageView;

        private static final int REQUEST_LOCATION_PERMISSION = 1;
        private static final int REQUEST_CHECK_SETTINGS = 2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Initialize UI components
            cityNameTextView = findViewById(R.id.city_name);
            currentTemperatureTextView = findViewById(R.id.current_temperature);
            highLowTempTextView = findViewById(R.id.high_low_temperatures);
            rainPercentageTextView = findViewById(R.id.rain_percentage);
            windSpeedTextView = findViewById(R.id.wind_speed);
            humidityPercentageTextView = findViewById(R.id.humidity_percentage);
            currentTimeTextView = findViewById(R.id.current_time);

            // Thiết lập RecyclerView cho Today (dự báo theo giờ)
            RecyclerView recyclerView = findViewById(R.id.hourly_forecast_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            hourlyForecastAdapter = new HourlyForecastAdapter(new ArrayList<>());
            recyclerView.setAdapter(hourlyForecastAdapter);


/*            // Thiết lập RecyclerView cho 7 Day-Forecast (dự báo theo ngày)
            RecyclerView dailyForecastRecyclerView = findViewById(R.id.daily_forecast_recycler_view);
            dailyForecastRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
             dailyForecastAdapter = new DailyForecastAdapter(new ArrayList<>());
            dailyForecastRecyclerView.setAdapter(dailyForecastAdapter);*/


            // Retrieve the city name from the Intent
            String cityName = getIntent().getStringExtra("city_name");
            if (cityName != null && !cityName.isEmpty()) {
                CallApi(cityName);
            } else {
                // Default to a city if none is provided
                CallApi("tokyo");
            }
        }



        private String getDayOfWeek(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
            Date date = new Date(timestamp * 1000); // Chuyển đổi timestamp từ giây sang mili giây
            return sdf.format(date);
        }

        private int getIconResourceId(String iconCode) {
            switch (iconCode) {
                case "01d": return R.drawable.sunny;
                case "02d": return R.drawable.windy;
                case "03d": return R.drawable.rainy;
                case "04d": return R.drawable.storm;
                default: return R.drawable.sunny; // update icon after 2 days
            }
        }
        public void CallApi(String city){

            // Fetch weather data
            WeatherAPI weatherAPI = new WeatherAPI(this);
            weatherAPI.fetchWeatherData(city, new WeatherAPI.WeatherDataCallback() {
                @Override
                public void onSuccess(WeatherCity weatherCity) {
                    updateUI(weatherCity);
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Handle error (e.g., show a Toast or Log error)
                }
            });

        }

        private void updateUI(WeatherCity weatherCity) {
            cityNameTextView.setText(weatherCity.getCity());
            currentTemperatureTextView.setText(String.format("%.1f°C", weatherCity.getCurrentTemperature()));
      //      weatherDescriptionTextView.setText(weatherCity.getWeatherDescription());
            highLowTempTextView.setText(String.format("H: %.1f° / L: %.1f°", weatherCity.getMaxTemperature(), weatherCity.getMinTemperature()));
            rainPercentageTextView.setText(String.format("Rain: %.1f%%", weatherCity.getRainfall()));
            windSpeedTextView.setText(String.format("Wind: %.1f m/s", weatherCity.getWindSpeed()));
            humidityPercentageTextView.setText(String.format("Humidity: %d%%", weatherCity.getHumidity()));
            hourlyForecastAdapter.updateForecastList(weatherCity.getHourlyForecasts());
        }
        public void OnClickCityActivity(View view) {
            Intent intent = new Intent(this, CityForecastActivity.class);
            startActivity(intent);
        }
    }



