package com.example.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView tvLocation, tvTemperature;
    private Button btnFetchWeather;
    private final String API_KEY = "ca0cc331f07186dbfb8156dbecaa91db"; // OpenWeatherMap API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvLocation = findViewById(R.id.tvLocation);
        tvTemperature = findViewById(R.id.tvTemperature);
        btnFetchWeather = findViewById(R.id.btnFetchWeather);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnFetchWeather.setOnClickListener(view -> {
            getLocationAndWeather();
        });
    }

    private void getLocationAndWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                    Toast.makeText(WeatherActivity.this, "Không lấy được vị trí", Toast.LENGTH_SHORT).show();
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
                } catch (JSONException e) {
                    Toast.makeText(WeatherActivity.this, "Lỗi phân tích dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherActivity.this, "Không thể lấy thông tin thời tiết", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndWeather();
            } else {
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

