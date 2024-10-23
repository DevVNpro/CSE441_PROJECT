package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder> {

    private final List<HourlyForecast> hourlyForecastList;

    public HourlyForecastAdapter(List<HourlyForecast> hourlyForecastList) {
        this.hourlyForecastList = hourlyForecastList;
    }
    @NonNull
    @Override
    public HourlyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        @NonNull
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_item, parent, false);
        return new HourlyForecastViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastViewHolder holder, int position) {
        HourlyForecast forecast = hourlyForecastList.get(position);

        // Hiển thị nhiệt độ
        holder.tempTextView.setText(forecast.getTemperature() + "°C");

        // Nếu bạn muốn hiển thị mô tả thời tiết thay vì thời gian, sửa lại dòng này
        holder.descTextView.setText(forecast.getDescription()); // Thay vì forecast.getTime()

    }

    @Override
    public int getItemCount() {
        return hourlyForecastList.size(); // Return the size of the list
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateForecastList(List<HourlyForecast> newForecastList) {
        this.hourlyForecastList.clear(); // Xóa danh sách cũ
        this.hourlyForecastList.addAll(newForecastList); // Thêm danh sách mới
        notifyDataSetChanged(); // Thông báo adapter làm mới dữ liệu
    }



    static class HourlyForecastViewHolder extends RecyclerView.ViewHolder {
        public ImageView weatherIconImageView;
        TextView tempTextView, descTextView;

        public HourlyForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            tempTextView = itemView.findViewById(R.id.tempTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
        }
    }
}
