package com.example.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class CurrentLocationWeatherActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView tvLocation, tvTemperature, tvStatus;
    private Button btnGetWeather; // Khai báo nút
    private final String API_KEY = "ca0cc331f07186dbfb8156dbecaa91db"; // Thay thế bằng API key thực tế
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Thực hiện kiểm tra và bật vị trí ngay khi Activity khởi tạo
        fetchWeather();
    }

    private void fetchWeather() {
        // Kiểm tra quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            checkLocationSettings();
        }
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            // Tất cả cài đặt vị trí đều được thoả mãn, tiếp tục lấy vị trí
            getLocationAndWeather();
        });

        task.addOnFailureListener(this::onFailure);
    }

    private void getLocationAndWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Toast.makeText(this, "Vị trí: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
                // Chuyển đổi tọa độ thành tên thành phố và gọi API thời tiết
                getCityNameFromLocation(latitude, longitude);
            } else {
                Toast.makeText(this, "Không lấy được vị trí", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCityNameFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String cityName = addresses.get(0).getLocality();
                if (cityName != null) {
                    Toast.makeText(this, "Thành phố: " + cityName, Toast.LENGTH_SHORT).show();
                    // Gọi API thời tiết với tên thành phố
                    fetchWeatherForCity(cityName);
                } else {
                    Toast.makeText(this, "Không tìm thấy tên thành phố", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lấy tên thành phố", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchWeatherForCity(String city) {
        WeatherAPI weatherAPI = new WeatherAPI(this);
        weatherAPI.fetchWeatherData(city, new WeatherAPI.WeatherDataCallback() {
            @Override
            public void onSuccess(WeatherCity weatherCity) {
                // Xử lý dữ liệu thời tiết nhận được
                Toast.makeText(getApplicationContext(), weatherCity.getCity(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationSettings();
            } else {
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
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
