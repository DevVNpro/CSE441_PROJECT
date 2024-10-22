package com.example.weatherapp;

//public class HourlyForecast {
//    private String temperature;
//    private String time;
//    private int iconResourceId;
//
//    public HourlyForecast(String temperature, String time, int iconResourceId) {
//        this.temperature = temperature;
//        this.time = time;
//        this.iconResourceId = iconResourceId;
//    }
//
//    public String getTemperature() {
//        return temperature;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public int getIconResourceId() {
//        return iconResourceId;
//    }
//}
public class HourlyForecast {
    private final String temperature;
    private final String description;
    private final String time; // Đảm bảo rằng kiểu dữ liệu là String

    public HourlyForecast(String temperature, String description, int time) {
        this.temperature = temperature;
        this.description = description;
        this.time = String.valueOf(time);  // Nếu là int, hãy sửa thành String
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
