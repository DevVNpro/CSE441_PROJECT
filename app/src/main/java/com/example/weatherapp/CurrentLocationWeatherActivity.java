package com.example.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

public class CurrentLocationWeatherActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView cityNameTextView, currentTempTextView, highLowTempTextView, humidityTextView, windSpeedTextView, weatherDescTextView;
    private WeatherAPI weatherAPI;
    private static final String DEFAULT_CITY = "Hanoi";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Sử dụng activity_main.xml

        // Khởi tạo WeatherAPI và gọi hàm fetchWeatherData
        WeatherAPI weatherAPI = new WeatherAPI(this);
        weatherAPI.fetchWeatherData("YourCity", new WeatherAPI.WeatherDataCallback() {
            @Override
            public void onSuccess(WeatherCity weatherCity) {
                // Cập nhật UI với dữ liệu từ weatherCity
                TextView cityName = findViewById(R.id.city_name);
                TextView currentTemp = findViewById(R.id.current_temperature);
                TextView highLowTemp = findViewById(R.id.high_low_temperatures);
                TextView humidity = findViewById(R.id.humidity_percentage);
                TextView windSpeed = findViewById(R.id.wind_speed);

                cityName.setText(weatherCity.getCity());
                currentTemp.setText(String.format("%.1f°", weatherCity.getCurrentTemperature()));
                highLowTemp.setText(String.format("H: %.1f° L: %.1f°", weatherCity.getMaxTemperature(), weatherCity.getMinTemperature()));
                humidity.setText(String.format("%d%%", weatherCity.getHumidity()));
                windSpeed.setText(String.format("%.1f km/h", weatherCity.getWindSpeed()));
            }

            @Override
            public void onFailure(String errorMessage) {
                // Hiển thị thông báo lỗi khi không lấy được dữ liệu thời tiết
                Toast.makeText(CurrentLocationWeatherActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchWeather(String city) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            checkLocationSettings();
        }
        weatherAPI.fetchWeatherData(city, new WeatherAPI.WeatherDataCallback() {
            @Override
            public void onSuccess(WeatherCity weatherCity) {
                // Cập nhật UI với dữ liệu nhận được
                cityNameTextView.setText(weatherCity.getCity());
                currentTempTextView.setText(String.format("%.1f°", weatherCity.getCurrentTemperature()));
                highLowTempTextView.setText(String.format("H:%.1f° L:%.1f°", weatherCity.getMaxTemperature(), weatherCity.getMinTemperature()));
                humidityTextView.setText(String.format("%d%%", weatherCity.getHumidity()));
                windSpeedTextView.setText(String.format("%.1f km/h", weatherCity.getWindSpeed()));
                weatherDescTextView.setText(weatherCity.getWeatherDescription());
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(CurrentLocationWeatherActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // 10 giây cho mỗi lần cập nhật
        locationRequest.setFastestInterval(5000); // Cập nhật nhanh nhất là 5 giây

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true); // Đảm bảo luôn hiển thị yêu cầu bật vị trí

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            // Nếu cài đặt vị trí đạt yêu cầu
            getLocationAndWeather();
        });

        task.addOnFailureListener(this::onFailure); // Khi thất bại, gọi hàm xử lý lỗi
    }

    // Khi người dùng nhấn vào nút "Get Weather"
    public void GetData() {
        // Ở đây có thể lấy tên thành phố từ input hoặc sử dụng vị trí hiện tại
        fetchWeather(DEFAULT_CITY);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                getLocationAndWeather();
            } else {
                Toast.makeText(this, "Cài đặt vị trí không được bật", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getLocationAndWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                // Xử lý vị trí
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                Toast.makeText(CurrentLocationWeatherActivity.this, "Lấy vị trí thành công: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
                fetchWeather("Hanoi");
            } else {
                Toast.makeText(CurrentLocationWeatherActivity.this, "Không lấy được vị trí", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onFailure(Exception e) {
        if (e instanceof ResolvableApiException) {
            try {
                ResolvableApiException resolvable = (ResolvableApiException) e;
                resolvable.startResolutionForResult(CurrentLocationWeatherActivity.this, REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException sendEx) {
                sendEx.printStackTrace();
            }
        }
    }
}
