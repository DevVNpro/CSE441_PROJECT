package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CityForecastAdapter extends RecyclerView.Adapter<CityForecastAdapter.CityForecastViewHolder> {

    private final List<CityForecast> cityForecastList;

    public CityForecastAdapter(List<CityForecast> cityForecastList) {
        this.cityForecastList = cityForecastList;
    }

    @NonNull
    @Override
    public CityForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new CityForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityForecastViewHolder holder, int position) {
        CityForecast city = cityForecastList.get(position);
        holder.bind(city); // Call bind method to set data
    }

    @Override
    public int getItemCount() {
        return cityForecastList.size();
    }

    static class CityForecastViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtCityName;
        private final TextView txtTemp;
        private final TextView txtHigh;
        private final TextView txtLow;
        private final TextView txtDescription;
        private  Button button;
        private OnCityDeleteListener deleteListener;
        public interface OnCityDeleteListener {
            void onCityDelete(WeatherCity city);
        }
        public CityForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCityName = itemView.findViewById(R.id.txt_city_name);
            txtTemp = itemView.findViewById(R.id.txt_temp);
            txtHigh = itemView.findViewById(R.id.txt_high_temp);
            txtLow = itemView.findViewById(R.id.txt_low_temp);
            txtDescription = itemView.findViewById(R.id.txt_description);
            button = itemView.findViewById(R.id.btn_city);
            this.deleteListener = deleteListener;
        }

        public void bind(CityForecast city) {
            txtCityName.setText(city.getCityName());
            txtTemp.setText("Temp: " + city.getTemperature() + "°C");
            txtHigh.setText("H: " + city.getHighTemp() + "°C");
            txtLow.setText("L: " + city.getLowTemp() + "°C");
            txtDescription.setText(city.getDescription());
            // Set OnClickListener for the button

            button.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), MainActivity.class);
                intent.putExtra("city_name", city.getCityName());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}