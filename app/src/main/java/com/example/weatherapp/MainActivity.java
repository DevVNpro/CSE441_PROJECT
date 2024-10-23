package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HourlyForecastAdapter hourlyForecastAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.current_temperature), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tìm RecyclerView trong bố cục
        recyclerView = findViewById(R.id.hourly_forecast_recycler_view);

        // Thiết lập LinearLayoutManager để hiển thị các mục theo chiều ngang
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Khởi tạo adapter rỗng ban đầu
        hourlyForecastAdapter = new HourlyForecastAdapter(new ArrayList<>());
        recyclerView.setAdapter(hourlyForecastAdapter);

    }

    public void GetData(View view) {
        String apiKey = "ca0cc331f07186dbfb8156dbecaa91db";
        String city = "Ha Noi"; // Thành phố
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city.replace(" ", "%20") + "&appid=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("WeatherResponse", response.toString());
                            // Lấy dữ liệu dự báo
                            JSONArray list = response.getJSONArray("list");
                            List<HourlyForecast> hourlyForecastList = new ArrayList<>();

                            // Lặp qua danh sách và chỉ lấy 8 mục (12 giờ, mỗi mục là 3 giờ)
                            for (int i = 0; i < 12; i++) {
                                JSONObject forecast = list.getJSONObject(i);
                                JSONObject main = forecast.getJSONObject("main");
                                JSONArray weatherArray = forecast.getJSONArray("weather");
                                JSONObject weather = weatherArray.getJSONObject(0);

                                // Lấy nhiệt độ và mô tả thời tiết
                                double temp = main.getDouble("temp") - 273.15; // Đổi từ Kelvin sang Celsius
                                String description = weather.getString("description");

                                // Thêm vào danh sách dự báo
                                @SuppressLint("DefaultLocale") String formattedTemp = String.format("%.1f", temp);
                                hourlyForecastList.add(new HourlyForecast(formattedTemp, description, 0));
                            }

                            // Cập nhật adapter với dữ liệu mới
                            hourlyForecastAdapter.updateForecastList(hourlyForecastList);
                            hourlyForecastAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing weather data", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace(); // General catch for other exceptions
                            Toast.makeText(MainActivity.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace(); // Print the stack trace for debugging
                        Toast.makeText(MainActivity.this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(request);
    }

}
