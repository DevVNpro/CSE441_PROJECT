package com.example.weatherapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityForecastActivity extends AppCompatActivity {

    private CityAdapter cityAdapter;
    private RecyclerView recyclerViewCity;

    private SearchView searchView;
    private RecyclerView recyclerViewSearch;
    private SearchAdapter searchAdapter;
    private List<String> allCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);

        List<WeatherCity> cities = CityManager.instance.getCities();

        recyclerViewCity = findViewById(R.id.recycleview_city);
        LinearLayoutManager layoutManagerDaily = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCity.setLayoutManager(layoutManagerDaily);
        cityAdapter = new CityAdapter(cities,this);
        recyclerViewCity.setAdapter(cityAdapter);

        if (cities.isEmpty()) {
            if (isNetworkAvailable()) {
                // fetchTempData(); // Gọi hàm lấy aadữ liệu tạm thời nếu có kết nối mạng
            } else {
                Toast.makeText(this, "Không kết nối mạng và không có data", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (isNetworkAvailable()) {
                FetchNewData();
            } else {
                Toast.makeText(this, "Tải data có trong thiết bị", Toast.LENGTH_SHORT).show();
                cityAdapter.updateCities(cities);
            }
        }


        initSearch();

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    private void FetchNewData(){
        List<WeatherCity> cities = CityManager.instance.getCities();
        WeatherAPI weatherAPI = new WeatherAPI(this);
        for(int i = 0 ;i< cities.stream().count();i++){
            weatherAPI.fetchWeatherData(cities.get(i).getCity(), new WeatherAPI.WeatherDataCallback() {
                @Override
                public void onSuccess(WeatherCity weatherCity) {
                    Toast.makeText(CityForecastActivity.this, "Cập nhập dữ liệu  " + weatherCity.getCity(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(CityForecastActivity.this, "Lỗi cập nhập: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
        cityAdapter.updateCities(CityManager.instance.getCities());

    }

    private void initSearch() {
        searchView = findViewById(R.id.search_view);
        recyclerViewSearch = findViewById(R.id.recycleview_Search);

        allCities =  loadCities(getApplicationContext());

        searchAdapter = new SearchAdapter();
        recyclerViewSearch.setAdapter(searchAdapter);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCities(newText);
                return true;
            }
        });
    }

    private List<String> filteredCities = new ArrayList<>();

    private void filterCities(String query) {
        filteredCities.clear();

        if (query == null || query.trim().isEmpty()) {
            searchAdapter.updateCities(filteredCities);
            recyclerViewSearch.setVisibility(View.GONE);
            return;
        }

        // Lọc thành phố
        for (String city : allCities) {
            if (city.toLowerCase().contains(query.toLowerCase())) {
                filteredCities.add(city);
            }
        }
        recyclerViewSearch.setVisibility(View.VISIBLE);
        searchAdapter.updateCities(filteredCities);
    }


    public static List<String> loadCities(Context context) {
        List<String> cities = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("cities.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                cities.add(jsonArray.getString(i));
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return cities;
    }

    public void showDeleteConfirmationDialog(WeatherCity city) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa thành phố " + city.getCity() + " không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    deleteCity(city);
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteCity(WeatherCity city) {
        CityManager.instance.removeCity(city);
        cityAdapter.updateCities(CityManager.instance.getCities());
        Toast.makeText(this, "Đã xóa thành phố " + city.getCity(), Toast.LENGTH_SHORT).show();
    }

}
