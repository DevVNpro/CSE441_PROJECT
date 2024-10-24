package com.example.weatherapp;

import android.annotation.SuppressLint;
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

    RecyclerView recyclerView;
    HourlyForecastAdapter hourlyForecastAdapter;

    TextView cityNameTextView, currentTemperatureTextView, weatherDescriptionTextView, highLowTempTextView, rainPercentageTextView, windSpeedTextView, humidityPercentageTextView, currentTimeTextView;
    ImageView weatherIconImageView, rainIconImageView, windIconImageView, humidityIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các thành phần giao diện
        cityNameTextView = findViewById(R.id.city_name);
        currentTemperatureTextView = findViewById(R.id.current_temperature);
        weatherDescriptionTextView = findViewById(R.id.weather_description);
        highLowTempTextView = findViewById(R.id.high_low_temperatures);
        rainPercentageTextView = findViewById(R.id.rain_percentage);
        windSpeedTextView = findViewById(R.id.wind_speed);
        humidityPercentageTextView = findViewById(R.id.humidity_percentage);
        currentTimeTextView = findViewById(R.id.current_time);
        weatherIconImageView = findViewById(R.id.weather_icon);

        // Thiết lập RecyclerView
        recyclerView = findViewById(R.id.hourly_forecast_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        hourlyForecastAdapter = new HourlyForecastAdapter(new ArrayList<>());
        recyclerView.setAdapter(hourlyForecastAdapter);
    }

    public void GetData(View view) {
        String apiKey = "96b20cc6216333159a8e894ba26d0eea";
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

                            // Lấy tên thành phố và cập nhật TextView
                            JSONObject cityObject = response.getJSONObject("city");
                            String cityName = cityObject.getString("name");
                            cityNameTextView.setText(cityName);

                            // Lấy thời gian hiện tại
//                            long timestamp = cityObject.getLong("sunrise") * 1000; // Thời gian theo mili giây
//                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd | hh:mm a", Locale.getDefault());
//                            String currentTime = sdf.format(new Date(timestamp));
//                            currentTimeTextView.setText(currentTime);

                            // Lấy dự báo thời tiết hiện tại
                            JSONArray list = response.getJSONArray("list");
                            JSONObject firstForecast = list.getJSONObject(0);
                            JSONObject main = firstForecast.getJSONObject("main");
                            JSONArray weatherArray = firstForecast.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);
                            JSONObject wind = firstForecast.getJSONObject("wind");

                            // Lấy múi giờ
                            int timezoneOffset = cityObject.getInt("timezone"); // Múi giờ tính bằng giây
                            Log.d("TimezoneOffset", "Timezone offset: " + timezoneOffset); // Kiểm tra giá trị múi giờ
                            // Lấy thời gian hiện tại
                            long timestamp = firstForecast.getLong("dt") * 1000; // Thời gian theo mili giây
                            // Chuyển đổi thời gian theo múi giờ
                            long localTimestamp = timestamp + timezoneOffset * 1000L; // Thêm múi giờ vào timestamp
                            // Định dạng thời gian
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd | hh:mm a", Locale.getDefault());
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // Đặt múi giờ về GMT để định dạng chính xác
                            String currentTime = sdf.format(new Date(localTimestamp));
                            currentTimeTextView.setText(currentTime);



                            // Nhiệt độ hiện tại
                            double temp = main.getDouble("temp") - 273.15; // Chuyển đổi từ Kelvin sang Celsius
                            String formattedTemp = String.format("%.1f°", temp);
                            currentTemperatureTextView.setText(formattedTemp);

                            // Mô tả thời tiết
                            String description = weather.getString("description");
                            weatherDescriptionTextView.setText(description);

                            // Cập nhật nhiệt độ cao và thấp
                            double tempMin = main.getDouble("temp_min") - 273.15;
                            double tempMax = main.getDouble("temp_max") - 273.15;
                            String highLowTemp = "H:" + String.format("%.1f", tempMax) + "° L:" + String.format("%.1f", tempMin) + "°";
                            highLowTempTextView.setText(highLowTemp);

                            // Lượng mưa và độ ẩm
                            if (firstForecast.has("rain")) {
                                JSONObject rain = firstForecast.getJSONObject("rain");
                                String rainPercentage = rain.has("3h") ? rain.getString("3h") : "0";
                                rainPercentageTextView.setText(rainPercentage + "%");
                            } else {
                                rainPercentageTextView.setText("0%");
                            }

                            // Tốc độ gió
                            String windSpeed = wind.getString("speed");
                            windSpeedTextView.setText(windSpeed + " km/h");

                            // Độ ẩm
                            int humidity = main.getInt("humidity");
                            humidityPercentageTextView.setText(humidity + "%");

//                            // Cập nhật biểu tượng thời tiết (icon)
//                            String iconCode = weather.getString("icon");
//                            int resourceId = getResources().getIdentifier("icon_" + iconCode, "drawable", getPackageName());
//                            weatherIconImageView.setImageResource(resourceId);
                            String iconCode = weather.getString("icon");
                            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                            //                        Picasso.get().load(iconUrl).into(weatherIconImageView);


                            // Cập nhật dự báo theo giờ
                            List<HourlyForecast> hourlyForecastList = new ArrayList<>();
                            for (int i = 0; i < 12; i++) { // Chỉ lấy 12 mục dự báo (36 giờ)
                                JSONObject forecast = list.getJSONObject(i);
                                double tempHourly = forecast.getJSONObject("main").getDouble("temp") - 273.15;
                                String hourlyDescription = forecast.getJSONArray("weather").getJSONObject(0).getString("description");
                                hourlyForecastList.add(new HourlyForecast(String.format("%.1f°", tempHourly), hourlyDescription, 0));


                            }

                            // Cập nhật adapter
                            hourlyForecastAdapter.updateForecastList(hourlyForecastList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing weather data", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(request);
    }

}