package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder> {

    private List<HourlyForecast> hourlyForecastList;

    public HourlyForecastAdapter(List<HourlyForecast> hourlyForecastList) {
        this.hourlyForecastList = hourlyForecastList;
    }
    @Override
    public HourlyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        @NonNull
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_item, parent, false);
        return new HourlyForecastViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull HourlyForecastViewHolder holder, int position) {
        HourlyForecast forecast = hourlyForecastList.get(position);
        holder.tempTextView.setText(forecast.getTemperature());
        holder.descTextView.setText(forecast.getTime()); // Now this line works
    }


    @Override
    public int getItemCount() {
        return hourlyForecastList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateForecastList(List<HourlyForecast> newForecastList) {
        this.hourlyForecastList = newForecastList;
        notifyDataSetChanged();
    }

    static class HourlyForecastViewHolder extends RecyclerView.ViewHolder {
        TextView tempTextView, descTextView;

        public HourlyForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            tempTextView = itemView.findViewById(R.id.tempTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
        }
    }
}
