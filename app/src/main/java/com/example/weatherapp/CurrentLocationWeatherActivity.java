package com.example.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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

        // Khởi tạo và thiết lập OnClickListener cho nút
        btnGetWeather = findViewById(R.id.get_weather_button);
        btnGetWeather.setOnClickListener(v -> fetchWeather());
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

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Xử lý vị trí
                    Toast.makeText(CurrentLocationWeatherActivity.this, "Lấy vị trí thành công: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    // Gọi API thời tiết ở đây với tọa độ vị trí
                } else {
                    Toast.makeText(CurrentLocationWeatherActivity.this, "Không lấy được vị trí", Toast.LENGTH_SHORT).show();
                }
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
