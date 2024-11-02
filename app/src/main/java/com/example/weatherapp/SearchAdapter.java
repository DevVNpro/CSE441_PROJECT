package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CityViewHolder> {
    private List<String> filteredCityList;
    private static final int MAX_ITEMS = 7;

    public SearchAdapter() {
        this.filteredCityList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_search, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        String city = filteredCityList.get(position);
        holder.bind(city); // Gọi phương thức bind để cấu hình view
    }

    @Override
    public int getItemCount() {
        return filteredCityList.size();
    }

    public void updateCities(List<String> cities) {
        this.filteredCityList.clear();

        int limit = Math.min(cities.size(), MAX_ITEMS);
        for (int i = 0; i < limit; i++) {
            filteredCityList.add(cities.get(i));
        }

        notifyDataSetChanged();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        Button cityButton;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityButton = itemView.findViewById(R.id.btn_GoMain); // Liên kết với Button trong item_search.xml
        }

        public void bind(String city) {
            cityButton.setText(city); // Đặt văn bản cho nút

            cityButton.setOnClickListener(v -> {
                // Xử lý click nút
                if (!isNetworkAvailable(v.getContext()) && !CityManager.instance.cityExists(city)) {
                    Toast.makeText(v.getContext(), "Kết nối mạng lỗi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Selected: " + city, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("city_name", city);
                    v.getContext().startActivity(intent);
                }
            });
        }

        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }
}
