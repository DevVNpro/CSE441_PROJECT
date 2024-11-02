package com.example.weatherapp;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast; // Import Toast

public class DisasterWarningPopup {
    private Context context;

    public DisasterWarningPopup(Context context) {
        this.context = context;
    }

    // Phương thức showDisasterWarning để lấy dữ liệu cảnh báo thiên tai
    // Phương thức showDisasterWarning để lấy dữ liệu cảnh báo thiên tai
    public void showDisasterWarning(String city) {
        WeatherAPI weatherAPI = new WeatherAPI(context);
        weatherAPI.fetchDisasterWarning(city, new WeatherAPI.DisasterWarningCallback() {
            @Override
            public void onSuccess(String warningTitle, String warningDetails, String location, String severity, String duration, String instructions) {
                showWarning(warningTitle, warningDetails, location, severity, duration, instructions); // Gọi showWarning với đầy đủ thông tin
            }

            @Override
            public void onSuccess(String warningTitle, String warningDetails) {

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show(); // Hiển thị thông báo lỗi
            }
        });
    }

    public void showWarning(String warningTitle, String warningDetails, String location, String severity, String duration, String instructions) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.disaster_warning_popup);

        TextView titleTextView = dialog.findViewById(R.id.warning_title);
        TextView detailsTextView = dialog.findViewById(R.id.warning_details);
        TextView locationTextView = dialog.findViewById(R.id.warning_location);
        TextView severityTextView = dialog.findViewById(R.id.warning_severity);
        TextView durationTextView = dialog.findViewById(R.id.warning_duration);
        TextView instructionsTextView = dialog.findViewById(R.id.safety_instructions);
        Button closeButton = dialog.findViewById(R.id.button_dismiss);

        titleTextView.setText(warningTitle);
        detailsTextView.setText(warningDetails);
        locationTextView.setText(location);
        severityTextView.setText(severity);
        durationTextView.setText(duration);
        instructionsTextView.setText(instructions);

        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

}
