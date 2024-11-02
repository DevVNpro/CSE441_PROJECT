package com.example.weatherapp;

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

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private List<WeatherCity> cityList;
    private final CityForecastActivity activity; // Tham chiếu đến activity

    public CityAdapter(List<WeatherCity> cityList, CityForecastActivity activity) {
        this.cityList = cityList;
        this.activity = activity; // Lưu trữ tham chiếu đến activity
    }

    public void updateCities(List<WeatherCity> newCityList) {
        this.cityList = newCityList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_manager_forecast, parent, false);
        return new CityViewHolder(view, this, (CityForecastActivity) parent.getContext()); // Truyền activity
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        WeatherCity city = cityList.get(position);
        holder.bind(city);  // Gọi phương thức bind để thiết lập dữ liệu
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
        private final Button submitbButton;
        private final CityAdapter adapter;  // Tham chiếu đến adapter
        private final CityForecastActivity activity; // Tham chiếu đến activity

        public CityViewHolder(@NonNull View itemView, CityAdapter adapter, CityForecastActivity activity) {
            super(itemView);
            this.adapter = adapter;
            this.activity = activity; // Khởi tạo tham chiếu đến activity
            dayName = itemView.findViewById(R.id.day_name);
            currentTemp = itemView.findViewById(R.id.current_temp);
            minTemp = itemView.findViewById(R.id.Min_Temp);
            maxTemp = itemView.findViewById(R.id.Max_temp);
            weatherIcon = itemView.findViewById(R.id.weather_icon);
            deleteButton = itemView.findViewById(R.id.delete_city_Button);
            submitbButton = itemView.findViewById(R.id.submid_city_button);
        }

        public void bind(WeatherCity city) {
            dayName.setText(city.getCity());
            currentTemp.setText("Temp: " + city.getCurrentTemperature() + "°C");
            minTemp.setText("Min: " + city.getMinTemperature() + "°C");
            maxTemp.setText("Max: " + city.getMaxTemperature() + "°C");

            submitbButton.setOnClickListener(v -> {
                CityManager.instance.SetLastCityView(city.getCity());
                Intent intent = new Intent(itemView.getContext(), MainActivity.class);
                intent.putExtra("city_name", city.getCity());
                itemView.getContext().startActivity(intent);
            });

            deleteButton.setOnClickListener(v -> {
                activity.showDeleteConfirmationDialog(city); // Hiển thị hộp thoại xác nhận
            });
        }
    }
}
