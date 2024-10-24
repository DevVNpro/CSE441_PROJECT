package com.example.weatherapp;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CityViewHolder {
    private TextView txtCityName, txtTemp, txtHigh, txtLow, txtDescription;

    public CityViewHolder(@NonNull View itemView) {
        super();
        txtCityName = itemView.findViewById(R.id.txt_city_name);
        txtTemp = itemView.findViewById(R.id.txt_temp);
        txtHigh = itemView.findViewById(R.id.txt_high_temp);
        txtLow = itemView.findViewById(R.id.txt_low_temp);
        txtDescription = itemView.findViewById(R.id.txt_description);
    }

    public void bind(final DailyForecast city, final AdapterView.OnItemClickListener listener) {
        // Gán dữ liệu thành phố vào các TextView
    /*    txtCityName.setText(city.getName());
        txtTemp.setText("Nhiệt độ: " + city.getTemperature() + "°C");
        txtHigh.setText("Cao nhất: " + city.getHighTemp() + "°C");
        txtLow.setText("Thấp nhất: " + city.getLowTemp() + "°C");
        txtDescription.setText(city.getDescription());

        // Sự kiện khi nhấn vào button
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(city);
            }
        });*/
    }
}
