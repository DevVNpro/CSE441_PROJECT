package com.example.weatherapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;

public class CitySearch extends AppCompatActivity {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager); // Đảm bảo bạn có layout này

        searchView = findViewById(R.id.search_view);
        setupSearchView();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Khi người dùng nhấn Enter
                Intent intent = new Intent(CitySearch.this, CitySearchAdapter.class);
                intent.putExtra("city_name", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Tùy chọn: Xử lý khi văn bản thay đổi
                return false;
            }
        });
    }
}
