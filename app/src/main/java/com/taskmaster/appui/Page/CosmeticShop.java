package com.taskmaster.appui.Page;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
    View[] confirmationViews;
    CosmeticItem clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cosmetic_shop);
        ImageView confirmationBox = findViewById(R.id.confirmationTextContainer);
        TextView confirmationText = findViewById(R.id.confirmationText);
        ImageView yesBox = findViewById(R.id.confirmationYesContainer);
        TextView yesText = findViewById(R.id.confirmationYesText);
        ImageView noBox = findViewById(R.id.confirmationNoContainer);
        TextView noText = findViewById(R.id.confirmationNoText);
        View blurOverlay = findViewById(R.id.blurOverlay);

        RecyclerView recycler = findViewById(R.id.recyclerView);
        List<CosmeticItem> data = new ArrayList<>();
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar1));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 3, R.drawable.placeholderavatar2));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 4, R.drawable.placeholderavatar3));
        data.add(new CosmeticItem("Silver Armor", "High-level protection", 5, R.drawable.placeholderavatar4));
        CosmeticAdapter adapter = new CosmeticAdapter(data, pos -> {
            // handle click
            clicked = data.get(pos);
            setVisibility(View.VISIBLE);
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        //Popup
        confirmationViews = new View[]{confirmationBox, confirmationText, yesBox, yesText, noBox, noText,blurOverlay};
        setUpButtons(yesBox,noBox,yesText,noText);
    }
    void setUpButtons(ImageView yesBox, ImageView noBox, TextView yesText, TextView noText){
        yesBox.setOnClickListener(v -> {
            Toast.makeText(this, String.valueOf(clicked.price), Toast.LENGTH_SHORT).show();
            setVisibility(View.GONE);
        });
        yesText.setOnClickListener(v -> {
            Toast.makeText(this, String.valueOf(clicked.price), Toast.LENGTH_SHORT).show();
            setVisibility(View.GONE);
        });
        noBox.setOnClickListener(v -> {
            clicked=null;
            setVisibility(View.GONE);
        });
        noText.setOnClickListener(v -> {
            clicked=null;
            setVisibility(View.GONE);
        });
    }
    void setVisibility(int visibility) {
        for (View v : confirmationViews) {
            v.setVisibility(visibility);
        }
    }
}
