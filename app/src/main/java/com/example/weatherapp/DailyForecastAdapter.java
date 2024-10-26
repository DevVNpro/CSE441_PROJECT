package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.ViewHolder> {
    private final List<DailyForecast> dailyForecastList;

    public DailyForecastAdapter(List<DailyForecast> dailyForecastList) {
        this.dailyForecastList = dailyForecastList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DailyForecast forecast = dailyForecastList.get(position);
        holder.dayName.setText(forecast.getDayName());
        holder.weatherIcon.setImageResource(forecast.getIconResId());
        holder.minMaxTemperature.setText(forecast.getMinMaxTemperature());
        holder.weatherIcon.setImageResource(forecast.getIconResId());
    }

    @Override
    public int getItemCount() {
        return dailyForecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayName;
        ImageView weatherIcon;
        TextView minMaxTemperature;

        public ViewHolder(View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.day_name);
            weatherIcon = itemView.findViewById(R.id.weather_icon);
            minMaxTemperature = itemView.findViewById(R.id.min_max_temperature);
        }
    }
    public void updateForecastList(List<DailyForecast> newForecastList) {
        dailyForecastList.clear();
        dailyForecastList.addAll(newForecastList);
        notifyDataSetChanged();
    }

}
