package com.example.weatherapp;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisasterWarningPopup {
    private Context context;

    public DisasterWarningPopup(Context context) {
        this.context = context;
    }

    public void showWarning(String warningTitle, String warningDetails) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.disaster_warning_popup);

        TextView titleTextView = dialog.findViewById(R.id.warning_title);
        TextView detailsTextView = dialog.findViewById(R.id.warning_details);
        Button closeButton = dialog.findViewById(R.id.close_button);

        titleTextView.setText(warningTitle);
        detailsTextView.setText(warningDetails);

        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}

