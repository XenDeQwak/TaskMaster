package com.taskmaster.appui.Page;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taskmaster.appui.R;
import com.taskmaster.appui.Services.CosmeticItemTemplate.CosmeticAdapter;
import com.taskmaster.appui.Services.CosmeticItemTemplate.CosmeticItem;

import java.util.ArrayList;
import java.util.List;

public class CosmeticShop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cosmetic_shop);
        RecyclerView recycler = findViewById(R.id.recyclerView);
        List<CosmeticItem> data = new ArrayList<>();
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar1));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar2));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar3));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar4));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar5));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar1));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar2));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar3));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar4));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar5));

        CosmeticAdapter adapter = new CosmeticAdapter(data, pos -> {
            // handle click
            CosmeticItem clicked = data.get(pos);
            Toast.makeText(this, String.valueOf(clicked.price), Toast.LENGTH_SHORT).show();
        });

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

}
