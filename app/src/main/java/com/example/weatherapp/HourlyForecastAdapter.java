package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.BreakIterator;
import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder> {
    private final List<HourlyForecast> hourlyForecastList;

    // Constructor
    public HourlyForecastAdapter(List<HourlyForecast> hourlyForecastList) {
        this.hourlyForecastList = hourlyForecastList;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateForecastList(List<HourlyForecast> newForecastList) {
        this.hourlyForecastList.clear();
        this.hourlyForecastList.addAll(newForecastList);
        notifyDataSetChanged();
    }


    @Override
    public HourlyForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout cho từng item trong RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_forecast_item, parent, false);
        return new HourlyForecastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastViewHolder holder, int position) {
        HourlyForecast forecast = hourlyForecastList.get(position);

        // Hiển thị nhiệt độ
        holder.tempTextView.setText(forecast.getTemperature());

        // Hiển thị mô tả thời tiết
        holder.descTextView.setText(forecast.getDescription());

        // Hiển thị giờ (sử dụng định dạng đã chuyển đổi từ timestamp)
        holder.timeTextView.setText(forecast.getTime());
    }


    @Override
    public int getItemCount() {
        return hourlyForecastList.size();
    }

    public static class HourlyForecastViewHolder extends RecyclerView.ViewHolder {
        TextView tempTextView;
        TextView descTextView;
        TextView timeTextView;

        public HourlyForecastViewHolder(View itemView) {
            super(itemView);
            tempTextView = itemView.findViewById(R.id.tempTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}

