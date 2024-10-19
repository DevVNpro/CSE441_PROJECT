package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText ed;
    TextView  txt,txtName,txtWeatherStatus, txtMinMaxTemp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ed = findViewById(R.id.editCity);
        txt = findViewById(R.id.txtTemp);
        txtName = findViewById(R.id.txtCityName);
        txtWeatherStatus = findViewById(R.id.txtWeatherStatus);
        txtMinMaxTemp =findViewById(R.id.txtMinMaxTemp);

    }

    public void GetData(View view) {
        String apiKey = "ca0cc331f07186dbfb8156dbecaa91db";
        String city = ed.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=ca0cc331f07186dbfb8156dbecaa91db";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = response.getJSONObject("main");
                    String temp = object.getString("temp");
                    Double tempdb= Double.parseDouble(temp) -273.15;
                    txt.setText(tempdb.toString().substring(0,4)+" °C");

                    String cityName = response.getString("name");
                    txtName.setText(cityName);

                    JSONObject weatherObject = response.getJSONArray("weather").getJSONObject(0);
                    String weatherDescription = weatherObject.getString("description");
                    txtWeatherStatus.setText(weatherDescription);

                    String tempMin = object.getString("temp_min");
                    String tempMax = object.getString("temp_max");
                    Double tempMinC = Double.parseDouble(tempMin) - 273.15;
                    Double tempMaxC = Double.parseDouble(tempMax) - 273.15;
                    txtMinMaxTemp.setText("Min: " + tempMinC.toString().substring(0, 4) + " °C | Max: " + tempMaxC.toString().substring(0, 4) + " °C");

                //    getHourlyForecast(city, apiKey);
               //     getDailyForecast(city, apiKey);
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}