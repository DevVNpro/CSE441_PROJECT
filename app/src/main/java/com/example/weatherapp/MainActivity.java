package com.example.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
//import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.location.Geocoder;
import android.location.Address;

    public class MainActivity extends AppCompatActivity {
        private LinearLayout layoutBackground;
        private HourlyForecastAdapter hourlyForecastAdapter;
        private DailyForecastAdapter dailyForecastAdapter;
        private TextView cityNameTextView, currentTemperatureTextView, weatherDescriptionTextView, highLowTempTextView, rainPercentageTextView, windSpeedTextView, humidityPercentageTextView, currentTimeTextView;
        private ImageView weatherIconImageView, rainIconImageView, windIconImageView, humidityIconImageView;

        private FusedLocationProviderClient fusedLocationProviderClient;
        private static final int REQUEST_LOCATION_PERMISSION = 1;
        private static final int REQUEST_CHECK_SETTINGS = 2;

        private CityManager cityManager;

        private FirebaseDatabaseHelper firebaseDatabaseHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            cityManager = new CityManager(this);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            firebaseDatabaseHelper = new FirebaseDatabaseHelper();
            // Initialize UI components
            cityNameTextView = findViewById(R.id.city_name);
            currentTemperatureTextView = findViewById(R.id.current_temperature);
            highLowTempTextView = findViewById(R.id.high_low_temperatures);
            rainPercentageTextView = findViewById(R.id.rain_percentage);
            windSpeedTextView = findViewById(R.id.wind_speed);
            humidityPercentageTextView = findViewById(R.id.humidity_percentage);
            currentTimeTextView = findViewById(R.id.current_day_time);
            layoutBackground = findViewById(R.id.layoutBackground);


            RecyclerView recyclerView = findViewById(R.id.hourly_forecast_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            hourlyForecastAdapter = new HourlyForecastAdapter(new ArrayList<>());
            recyclerView.setAdapter(hourlyForecastAdapter);


            RecyclerView recyclerViewDaily = findViewById(R.id.daily_forecast_recycler_view);
            LinearLayoutManager layoutManagerDaily = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerViewDaily.setLayoutManager(layoutManagerDaily);
            dailyForecastAdapter = new DailyForecastAdapter(new ArrayList<>());
            recyclerViewDaily.setAdapter(dailyForecastAdapter);

            ImageView bellIcon = findViewById(R.id.icon_bell);
            bellIcon.setOnClickListener(view -> {
                DisasterWarningPopup warningPopup = new DisasterWarningPopup(this);
                warningPopup.showWarning("Danger Warning", "Warning: Storm Level 8, Flood \nLocation: tokyo\n");
            });



            SetUpMain();

        }
        private void SetUpMain()
        {
            String cityName = getIntent().getStringExtra("city_name");
            if (cityName != null && !cityName.isEmpty()) {
                if(isNetworkAvailable()){
                    CallApi(cityName);
                }
                else{
                    if(CityManager.instance.cityExists(cityName)){
                        Toast.makeText(this, "Tải data từ thiết bị " + cityName, Toast.LENGTH_SHORT).show();
                        updateUI(CityManager.instance.GetCity(cityName));
                    }
                }
            }
            else {
                if(CityManager.instance.GetLastCityView() != "" && CityManager.instance.cityExists(CityManager.instance.GetLastCityView())){
                    if(isNetworkAvailable()){
                        CallApi(CityManager.instance.GetLastCityView());
                    }
                    else{
                        updateUI(CityManager.instance.GetCity(CityManager.instance.GetLastCityView()));
                        Toast.makeText(this, "Tải data từ thiết bị" + CityManager.instance.GetLastCityView(), Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    if(CityManager.instance.GetPermistionKey() == ""){
                        requestWeather();
                    }
                    else{
                        Intent intent = new Intent(this, CityForecastActivity.class);
                        startActivity(intent);
                    }

                }
            }
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
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
                default: return R.drawable.sunny;
            }
        }


        public void CallApi(String city){

            // Fetch weather data
            WeatherAPI weatherAPI = new WeatherAPI(this);
            weatherAPI.fetchWeatherData(city, new WeatherAPI.WeatherDataCallback() {
                @Override
                public void onSuccess(WeatherCity weatherCity) {
                    firebaseDatabaseHelper.addWeather(weatherCity);
                    updateUI(weatherCity);
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Handle error (e.g., show a Toast or Log error)
                }
            });

        }


        private void updateUI(WeatherCity weatherCity) {
            cityNameTextView.setText(weatherCity.getCity());
            currentTemperatureTextView.setText(String.format("%.1f°C", weatherCity.getCurrentTemperature()));
            //weatherDescriptionTextView.setText(weatherCity.getWeatherDescription());
          //  currentTimeTextView.setText(weatherCity.GetDateTime());
            highLowTempTextView.setText(String.format("H: %.1f° / L: %.1f°", weatherCity.getMaxTemperature(), weatherCity.getMinTemperature()));
            rainPercentageTextView.setText(String.format("Rain: %.1f%%", weatherCity.getRainfall()));
            windSpeedTextView.setText(String.format("Wind: %.1f m/s", weatherCity.getWindSpeed()));
            humidityPercentageTextView.setText(String.format("Humidity: %d%%", weatherCity.getHumidity()));
            hourlyForecastAdapter.updateForecastList(weatherCity.getHourlyForecasts());
            dailyForecastAdapter.updateForecastList(weatherCity.getDailyForecasts());
        }
        public void OnClickCityActivity(View view) {
            Intent intent = new Intent(this, CityForecastActivity.class);
            startActivity(intent);
        }


        private void requestWeather() {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            } else {
                Toast.makeText(this, "Đã cấp quyền", Toast.LENGTH_SHORT).show();
            }
            CityManager.instance.SetPermistionKey();
        }


        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_LOCATION_PERMISSION) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Quyền truy cập vị trí thành công", Toast.LENGTH_SHORT).show();
                    checkLocationSettings();
                } else {
                    Intent intent = new Intent(this, CityForecastActivity.class);
                    startActivity(intent);
                }
            }
        }

        private void checkLocationSettings() {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .setAlwaysShow(true);

            SettingsClient client = LocationServices.getSettingsClient(this);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnSuccessListener(locationSettingsResponse -> {
                Toast.makeText(this, "cài đặt vị trí đạt yêu cầu", Toast.LENGTH_SHORT).show();
            });

            task.addOnFailureListener(this::onFailure);
        }

        private void onFailure(Exception e) {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    sendEx.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CHECK_SETTINGS) {
                if (resultCode == RESULT_OK) {


                    getLocationAndWeather();
                } else {
                    Toast.makeText(this, "Cài đặt vị trí không được bật", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, CityForecastActivity.class);
                    startActivity(intent);
                }
            }
        }

        private void getLocationAndWeather() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                return;
            }
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000); // 10 giây
            locationRequest.setFastestInterval(5000); // 5 giây

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    Toast.makeText(this, "Lấy vị trí thành công: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();


                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            String cityName = addresses.get(0).getLocality();
                            CallApi(cityName);
                            if (cityName != null) {
                                Toast.makeText(this, "Thành phố: " + cityName, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Không tìm thấy tên thành phố", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Không tìm thấy địa chỉ nào", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Intent intent = new Intent(this, CityForecastActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(this, CityForecastActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(e -> {
                Intent intent = new Intent(this, CityForecastActivity.class);
                startActivity(intent);
            });
        }




    }



