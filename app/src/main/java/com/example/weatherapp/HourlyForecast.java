package com.example.weatherapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HourlyForecast {
    private final String temperature;
    private final String description;
    private final String time;

    public HourlyForecast(String temperature, String description, long timestamp) {
        this.temperature = temperature;
        this.description = description;
        this.time = convertTimestampToTime(timestamp);
    }

    private String convertTimestampToTime(long timestamp) {
        // Chuyển đổi Unix timestamp thành thời gian
        Date date = new Date(timestamp * 1000L); // API trả về Unix timestamp (giây)
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault()); // Cài đặt múi giờ mặc định
        return sdf.format(date);
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }
}


