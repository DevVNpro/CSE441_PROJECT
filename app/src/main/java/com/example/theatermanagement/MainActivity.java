package com.example.theatermanagement;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.newlayout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewRecycle), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int countContent = 1;
        Language[] listLanguage = Language.class.getEnumConstants();
        List<Content> contents= new ArrayList<>();
        for(int i = 0 ; i< countContent; i++){
            Content clone = new Content();
            clone.name = listLanguage[i].toString();
            contents.add(clone);
        }
        RecyclerView recyclerView = findViewById(R.id.Recycler);
       // recyclerView.layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter((RecyclerView.Adapter) contents);


    }

}
