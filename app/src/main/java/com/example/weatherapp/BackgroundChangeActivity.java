package com.example.weatherapp;

import android.os.Bundle;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;



public class BackgroundChangeActivity extends AppCompatActivity {
    private RelativeLayout layoutBackground;
    private WeatherAPI weatherAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutBackground = findViewById(R.id.layoutBackground);

        // Khởi tạo WeatherAPI với context
        weatherAPI = new WeatherAPI(this);

        // Gọi phương thức để lấy dữ liệu thời tiết cho một thành phố cụ thể (ví dụ: "Hanoi")
        weatherAPI.fetchWeatherData("Hanoi", new WeatherAPI.WeatherDataCallback() {
            @Override
            public void onSuccess(WeatherCity weatherCity) {
                // Khi lấy dữ liệu thành công, cập nhật background dựa trên mô tả thời tiết
                setWeatherBackground(weatherCity.getWeatherDescription());
            }

            @Override
            public void onFailure(String errorMessage) {
                // Xử lý lỗi và đặt background mặc định
                layoutBackground.setBackgroundResource(R.drawable.bg_default);
            }
        });
    }

    private void setWeatherBackground(String weatherDescription) {
        int backgroundResource;

        // Kiểm tra mô tả thời tiết và đặt hình nền tương ứng
        if (weatherDescription.contains("clear")) {
            backgroundResource = R.drawable.bg_sunny;
        } else if (weatherDescription.contains("rain")) {
            backgroundResource = R.drawable.bg_rainy;
        } else if (weatherDescription.contains("cloud")) {
            backgroundResource = R.drawable.bg_cloudy;
        } else {
            backgroundResource = R.drawable.bg_default;
        }

        // Đặt hình nền cho layout
        layoutBackground.setBackgroundResource(backgroundResource);
    }
}
