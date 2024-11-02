package com.example.weatherapp;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CitySearchAdapter extends RecyclerView.Adapter<CitySearchAdapter.ViewHolder> {
    private List<String> cityList;

    public CitySearchAdapter(List<String> cityList) {
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String city = cityList.get(position);
        holder.cityTextView.setText(city);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.city_name); // ID của TextView trong city_item.xml
        }
    }
}
