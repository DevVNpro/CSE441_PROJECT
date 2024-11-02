package com.example.weatherapp;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    public DatabaseReference mReferenceStudents;

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceStudents = mDatabase.getReference("Weather");
    }

    public void addWeather(WeatherCity weatherCity) {
        HashMap<String, String> weatherCityHashMap = new HashMap<>();
        weatherCityHashMap.put("CurrentTemperature", String.valueOf(weatherCity.getCurrentTemperature()));
        weatherCityHashMap.put("WeatherDescription",weatherCity.getWeatherDescription());
        weatherCityHashMap.put("MinTemperature",String.valueOf(weatherCity.getMinTemperature()));
        weatherCityHashMap.put("MaxTemperature",String.valueOf(weatherCity.getMaxTemperature()));
        weatherCityHashMap.put("RainFall",String.valueOf(weatherCity.getRainfall()));
        weatherCityHashMap.put("Humidity",String.valueOf(weatherCity.getHumidity()));
        weatherCityHashMap.put("WindSpeed",String.valueOf(weatherCity.getWindSpeed()));

        //save listHourlyForecast
        List<HourlyForecast> listHourlyForecast = weatherCity.getHourlyForecasts();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(listHourlyForecast);
        weatherCityHashMap.put("HourlyForecasts",json);

        //save listDailyForecast



        String studentId = weatherCity.getCity();
        if (studentId != null) {
            mReferenceStudents.child(studentId).setValue(weatherCityHashMap);
        }
    }
/*
    // Edit an existing student (based on student ID)
    public void editStudent(String studentId, Student student) {
        mReferenceStudents.child(studentId).setValue(student);
    }

    // Delete a student from the database
    public void deleteStudent(String studentId) {
        mReferenceStudents.child(studentId).removeValue();
    }
*/
}
