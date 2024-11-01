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
        holder.minTemperatureTextView.setText(String.valueOf(dailyForecast.getMinTemp()));
        holder.maxTemperatureTextView.setText(String.valueOf(dailyForecast.getMaxTemp()));

        //holder.weatherIconImageView.setImageResource(dailyForecast.get());
    }

    @Override
    public int getItemCount() {
        return dailyForecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayNameTextView;
        TextView temperatureTextView;
        TextView minTemperatureTextView;
        TextView maxTemperatureTextView;
        ImageView weatherIconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayNameTextView = itemView.findViewById(R.id.day_name);
            minTemperatureTextView= itemView.findViewById(R.id.Min_Temp);
            maxTemperatureTextView= itemView.findViewById(R.id.Max_temp);
            temperatureTextView = itemView.findViewById(R.id.current_temp);
            weatherIconImageView = itemView.findViewById(R.id.weather_icon);
        }
    }
}
