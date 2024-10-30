package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.ViewHolder> {
    private List<SimpleForecast> dailyForecastList;

    public DailyForecastAdapter(List<SimpleForecast> dailyForecastList) {
        this.dailyForecastList = dailyForecastList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateForecastList(List<SimpleForecast> newForecastList) {
        this.dailyForecastList.clear();
        this.dailyForecastList.addAll(newForecastList);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleForecast dailyForecast = dailyForecastList.get(position);

        holder.dayNameTextView.setText(dailyForecast.getDay());
        holder.temperatureTextView.setText(dailyForecast.getDescription());
        //holder.weatherIconImageView.setImageResource(dailyForecast.get());
    }

    @Override
    public int getItemCount() {
        return dailyForecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayNameTextView;
        TextView temperatureTextView;
        ImageView weatherIconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayNameTextView = itemView.findViewById(R.id.day_name);
            temperatureTextView = itemView.findViewById(R.id.min_max_temperature);
            weatherIconImageView = itemView.findViewById(R.id.weather_icon);
        }
    }
}
