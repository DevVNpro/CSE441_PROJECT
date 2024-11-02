package com.example.weatherapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class BackgroundChangeActivity extends AppCompatActivity {
    private WeatherAPI weatherAPI;
    private LinearLayout layoutBackground;
    private HourlyForecastAdapter hourlyForecastAdapter;
    private TextView cityNameTextView, currentTemperatureTextView, highLowTempTextView, rainPercentageTextView, windSpeedTextView, humidityPercentageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutBackground = findViewById(R.id.layoutBackground);
        cityNameTextView = findViewById(R.id.city_name);
        currentTemperatureTextView = findViewById(R.id.current_temperature);
        highLowTempTextView = findViewById(R.id.high_low_temperatures);
        rainPercentageTextView = findViewById(R.id.rain_percentage);
        windSpeedTextView = findViewById(R.id.wind_speed);
        humidityPercentageTextView = findViewById(R.id.humidity_percentage);

        weatherAPI = new WeatherAPI(this);

        // Fetch weather data cho thành phố và cập nhật UI cùng hình nền
        weatherAPI.fetchWeatherData("Hanoi", new WeatherAPI.WeatherDataCallback() {
            @Override
            public void onSuccess(WeatherCity weatherCity) {
                updateUI(weatherCity);
            }

            @Override
            public void onFailure(String errorMessage) {
                layoutBackground.setBackgroundResource(R.drawable.bg_default);
            }
        });
    }

    private void UpdateBackground(List<HourlyForecast> hourlyForecasts) {
        int sunnyCount = 0, rainyCount = 0, cloudyCount = 0;

        for (HourlyForecast forecast : hourlyForecasts) {
            String description = forecast.getDescription().toLowerCase();
            if (description.contains("clear") || description.contains("sunny")) {
                sunnyCount++;
            } else if (description.contains("rain")) {
                rainyCount++;
            } else if (description.contains("cloud")) {
                cloudyCount++;
            }
        }

        int backgroundResource;
        if (sunnyCount > rainyCount && sunnyCount > cloudyCount) {
            backgroundResource = R.drawable.bg_sunny;
        } else if (rainyCount > sunnyCount && rainyCount > cloudyCount) {
            backgroundResource = R.drawable.bg_rainy;
        } else if (cloudyCount > sunnyCount && cloudyCount > rainyCount) {
            backgroundResource = R.drawable.bg_cloudy;
        } else {
            backgroundResource = R.drawable.bg_default;
        }

        layoutBackground.setBackgroundResource(backgroundResource);
    }

    private void updateUI(WeatherCity weatherCity) {
        UpdateBackground(weatherCity.getHourlyForecasts());

        cityNameTextView.setText(weatherCity.getCity());
        currentTemperatureTextView.setText(String.format("%.1f°C", weatherCity.getCurrentTemperature()));
        highLowTempTextView.setText(String.format("H: %.1f° / L: %.1f°", weatherCity.getMaxTemperature(), weatherCity.getMinTemperature()));
        rainPercentageTextView.setText(String.format("Rain: %.1f%%", weatherCity.getRainfall()));
        windSpeedTextView.setText(String.format("Wind: %.1f m/s", weatherCity.getWindSpeed()));
        humidityPercentageTextView.setText(String.format("Humidity: %d%%", weatherCity.getHumidity()));

        hourlyForecastAdapter.updateForecastList(weatherCity.getHourlyForecasts());
    }
}

