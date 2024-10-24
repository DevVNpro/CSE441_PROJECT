package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

public class CurrentLocationWeatherActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView tvLocation, tvTemperature, tvStatus;
    private Button btnFetchWeather, btnNext;
    private final String API_KEY = "ca0cc331f07186dbfb8156dbecaa91db"; // OpenWeatherMap API key
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_location_weather_activity); // Đảm bảo bạn có file XML này

        tvLocation = findViewById(R.id.tvCurrentAddress);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvStatus = findViewById(R.id.tvStatus); // TextView hiển thị trạng thái thời tiết
        btnFetchWeather = findViewById(R.id.btnFetchWeather);
        btnNext = findViewById(R.id.btnNext);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnFetchWeather.setOnClickListener(view -> checkLocationSettings());

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentLocationWeatherActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            // All location settings are satisfied, continue
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
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    tvLocation.setText("Lat: " + lat + ", Lon: " + lon);
                    fetchWeatherData(lat, lon);
                } else {
                    Toast.makeText(CurrentLocationWeatherActivity.this, "Không lấy được vị trí", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchWeatherData(double lat, double lon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainObject = response.getJSONObject("main");
                    String temp = mainObject.getString("temp");

                    // Convert temperature from Kelvin to Celsius
                    double tempCelsius = Double.parseDouble(temp) - 273.15;
                    String formattedTemp = String.format("%.2f", tempCelsius) + " °C";

                    // Update TextView with the temperature
                    tvTemperature.setText("Nhiệt độ: " + formattedTemp);

                    JSONObject weatherObject = response.getJSONArray("weather").getJSONObject(0);
                    String status = weatherObject.getString("description");

                    // Cập nhật trạng thái thời tiết
                    tvStatus.setText("Trạng thái: " + status);
                } catch (JSONException e) {
                    Toast.makeText(CurrentLocationWeatherActivity.this, "Lỗi phân tích dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CurrentLocationWeatherActivity.this, "Không thể lấy thông tin thời tiết", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                getLocationAndWeather();
            } else {
                // Permission was denied
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // User enabled location settings
                getLocationAndWeather();
            } else {
                // User did not enable location settings
                Toast.makeText(this, "Cài đặt vị trí không được bật", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onFailure(Exception e) {
        if (e instanceof ResolvableApiException) {
            try {
                // Location settings are not satisfied, ask user to enable them
                ResolvableApiException resolvable = (ResolvableApiException) e;
                resolvable.startResolutionForResult(CurrentLocationWeatherActivity.this, REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException sendEx) {
                sendEx.printStackTrace();
            }
        }
    }

}
