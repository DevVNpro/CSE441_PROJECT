package com.example.weatherapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final String API_KEY = "ca0cc331f07186dbfb8156dbecaa91db"; // Thay YOUR_OPENWEATHER_API_KEY bằng API Key của bạn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Thiết lập Toolbar với nút Back
        Toolbar toolbar = findViewById(R.id.map_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Khởi tạo bản đồ
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Lấy vị trí hiện tại (ví dụ: 10.762622, 106.660172 - TP Hồ Chí Minh)
        LatLng location = new LatLng(10.762622, 106.660172);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5));

        // Tạo và thêm lớp phủ thời tiết từ OpenWeather
        addWeatherOverlay("clouds_new"); // Ví dụ: Lớp phủ clouds_new
    }

    private void addWeatherOverlay(String layer) {
        UrlTileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                // Tạo URL từ OpenWeather Map để lấy lớp phủ thời tiết
                String url = String.format(
                        "https://tile.openweathermap.org/map/%s/%d/%d/%d.png?appid=%s",
                        layer, zoom, x, y, API_KEY
                );
                try {
                    return new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        // Thêm lớp phủ vào bản đồ
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }
}
