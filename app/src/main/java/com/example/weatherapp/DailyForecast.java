package com.example.weatherapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyForecast {
    private final String dayName;
    private final int iconResId;
    private final String minTemp;
    private final String maxTemp;

    public DailyForecast(String dayName, int iconResId, String minTemp, String maxTemp) {
        this.dayName = dayName;
        this.iconResId = iconResId;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

public String getDayOfWeek(long timestamp) {
    SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
    Date date = new Date(timestamp * 1000); // Chuyển timestamp từ giây sang mili giây
    return sdf.format(date);
}

    public int getIconResourceId(String iconCode) {
        switch (iconCode) {
            case "01d": return R.drawable.sunny;
            case "02d": return R.drawable.windy;
            case "03d": return R.drawable.rainy;
            case "04d": return R.drawable.storm;
            default: return R.drawable.default_icon;
        }
    }


    public String getDayName() {
        return dayName;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    // Phương thức mới để trả về chuỗi nhiệt độ min-max
    public String getMinMaxTemperature() {
        return minTemp + " / " + maxTemp;
    }
}
