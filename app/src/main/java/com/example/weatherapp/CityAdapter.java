package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<WeatherCity> cityList;

    public CityAdapter(List<WeatherCity> cityList) {
        this.cityList = cityList;
    }

    public void updateCities(List<WeatherCity> newCityList) {
        this.cityList = newCityList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_manager_forecast, parent, false);
        return new CityViewHolder(view, this);  // Pass the adapter instance to the ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        WeatherCity city = cityList.get(position);
        holder.bind(city);  // Call bind method to set data
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayName;
        private final TextView currentTemp;
        private final TextView minTemp;
        private final TextView maxTemp;
        private final ImageView weatherIcon;
        private final Button deleteButton;
        private final CityAdapter adapter;  // Reference to the adapter

        public CityViewHolder(@NonNull View itemView, CityAdapter adapter) {  // Accept adapter in constructor
            super(itemView);
            this.adapter = adapter;  // Initialize the adapter reference
            dayName = itemView.findViewById(R.id.day_name);
            currentTemp = itemView.findViewById(R.id.current_temp);
            minTemp = itemView.findViewById(R.id.Min_Temp);
            maxTemp = itemView.findViewById(R.id.Max_temp);
            weatherIcon = itemView.findViewById(R.id.weather_icon);
            deleteButton = itemView.findViewById(R.id.delete_city_Button);
        }

        public void bind(WeatherCity city) {
            dayName.setText(city.getCity());
            currentTemp.setText("Temp: " + city.getCurrentTemperature() + "°C");
            minTemp.setText("Min: " + city.getMinTemperature() + "°C");
            maxTemp.setText("Max: " + city.getMaxTemperature() + "°C");

            // Optionally, set weather icon
            // weatherIcon.setImageResource(R.drawable.ic_weather_icon);

            deleteButton.setOnClickListener(v -> {
                CityManager.instance.removeCity(city);  // Remove the city from CityManager
                adapter.updateCities(CityManager.instance.getCities());  // Update adapter data
            });
        }
    }
}
