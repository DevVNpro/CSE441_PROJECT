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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HourlyForecastAdapter hourlyForecastAdapter;

    TextView cityNameTextView, currentTemperatureTextView, weatherDescriptionTextView, highLowTempTextView, rainPercentageTextView, windSpeedTextView, humidityPercentageTextView, currentTimeTextView;
    ImageView weatherIconImageView, rainIconImageView, windIconImageView, humidityIconImageView;

    private DailyForecastAdapter dailyForecastAdapter;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các thành phần giao diện
        cityNameTextView = findViewById(R.id.city_name);
        currentTemperatureTextView = findViewById(R.id.current_temperature);
        highLowTempTextView = findViewById(R.id.high_low_temperatures);
        rainPercentageTextView = findViewById(R.id.rain_percentage);
        windSpeedTextView = findViewById(R.id.wind_speed);
        humidityPercentageTextView = findViewById(R.id.humidity_percentage);
        currentTimeTextView = findViewById(R.id.current_time);

        // Thiết lập RecyclerView cho Today (dự báo theo giờ)
        recyclerView = findViewById(R.id.hourly_forecast_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        hourlyForecastAdapter = new HourlyForecastAdapter(new ArrayList<>());
        recyclerView.setAdapter(hourlyForecastAdapter);


        // Thiết lập RecyclerView cho 7 Day-Forecast (dự báo theo ngày)
//        RecyclerView dailyForecastRecyclerView = findViewById(R.id.daily_forecast_recycler_view);
//        LinearLayoutManager dailyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        dailyForecastRecyclerView.setLayoutManager(dailyLayoutManager);
//        dailyForecastAdapter = new DailyForecastAdapter(new ArrayList<>());
//        dailyForecastRecyclerView.setAdapter(dailyForecastAdapter);
        RecyclerView dailyForecastRecyclerView = findViewById(R.id.daily_forecast_recycler_view);
        dailyForecastRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dailyForecastAdapter = new DailyForecastAdapter(new ArrayList<>());
        dailyForecastRecyclerView.setAdapter(dailyForecastAdapter);




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
            default: return R.drawable.default_icon;
        }
    }

    public void GetData(View view) {
        String apiKey = "96b20cc6216333159a8e894ba26d0eea";
        String city = "Ha Noi"; // Thành phố
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city.replace(" ", "%20") + "&appid=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("WeatherResponse", response.toString());

                            // Lấy tên thành phố và cập nhật TextView
                            JSONObject cityObject = response.getJSONObject("city");
                            String cityName = cityObject.getString("name");
                            cityNameTextView.setText(cityName);

                            // Lấy dự báo thời tiết hiện tại
                            JSONArray list = response.getJSONArray("list");
                            JSONObject firstForecast = list.getJSONObject(0);
                            JSONObject main = firstForecast.getJSONObject("main");
                            JSONArray weatherArray = firstForecast.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);
                            JSONObject wind = firstForecast.getJSONObject("wind");

                            JSONObject jsonResponse = new JSONObject(String.valueOf(response)); // "response" là chuỗi JSON từ API
                            JSONArray dailyArray = jsonResponse.getJSONArray("list"); // Lấy danh sách "list" từ JSON

                            // Nhiệt độ hiện tại
                            double temp = main.getDouble("temp") - 273.15; // Chuyển đổi từ Kelvin sang Celsius
                            @SuppressLint("DefaultLocale") String formattedTemp = String.format("%.1f°", temp);
                            currentTemperatureTextView.setText(formattedTemp);


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


                            // Lấy giờ hiện tại (Unix timestamp tính theo giây)
                            long currentTimestamp = System.currentTimeMillis() / 1000L;

                            // Khởi tạo danh sách dự báo theo giờ
                            List<HourlyForecast> hourlyForecastList = new ArrayList<>();
                            for (int i = 0; i < 12; i++) { // Lấy liên tiếp 12 mục dự báo cách nhau 1 giờ
                                JSONObject forecast = list.getJSONObject(i);
                                double tempHourly = forecast.getJSONObject("main").getDouble("temp") - 273.15;
                                String hourlyDescription = forecast.getJSONArray("weather").getJSONObject(0).getString("description");
                                long timestamp = forecast.getLong("dt");
                                hourlyForecastList.add(new HourlyForecast(String.format("%.1f°", tempHourly), hourlyDescription, timestamp));
                            }
                            hourlyForecastAdapter.updateForecastList(hourlyForecastList);
                            hourlyForecastAdapter.notifyDataSetChanged();

                            //7dayforecast
                            List<DailyForecast> dailyForecastList = new ArrayList<>();
                            for (int i = 1; i < dailyArray.length(); i++) { // Bắt đầu từ i = 1 để lấy dữ liệu từ ngày hôm sau
                                JSONObject dailyData = dailyArray.getJSONObject(i);
                                long timestamp = dailyData.getLong("dt");

                                // Kiểm tra temp để tránh lỗi
                                if (dailyData.has("temp")) {
                                    JSONObject tempData = dailyData.getJSONObject("temp");
                                    double tempMax = tempData.getDouble("max") - 273.15;
                                    double tempMin = tempData.getDouble("min") - 273.15;

                                    String dayName = getDayOfWeek(timestamp);
                                    int iconResId = getIconResourceId(dailyData.getJSONArray("weather").getJSONObject(0).getString("icon"));

                                    dailyForecastList.add(new DailyForecast(dayName, iconResId, String.format("%.1f", tempMin), String.format("%.1f", tempMax)));
                                }
                            }
                            dailyForecastAdapter.updateForecastList(dailyForecastList);


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
