package com.taskmaster.appui.view.child;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.R;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.uimodule.CosmeticItemTemplate.CosmeticItem;

import java.util.ArrayList;
import java.util.List;

public class ProgressionPage extends ChildPage {

    ChildData childData;
    private AppCompatButton childAvatarName;
    private AppCompatButton childArmorName;
    private ImageView childAvatarImage;
    private int avatarIndex;
    private List<CosmeticItem> ownedItems;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.progression_page);

        // hide status bar and nav bar
        NavUtil.hideSystemBars(this);
        initNavigationMenu(this, ProgressionPage.class);

        childAvatarImage = findViewById(R.id.childAvatarImage);
        childArmorName = findViewById(R.id.childArmorName);
        childAvatarName = findViewById(R.id.childAvatarName);
        AppCompatButton statFloorNum = findViewById(R.id.statFloorNum);
        AppCompatButton statQuestDoneNum = findViewById(R.id.statQuestDoneNum);
        Button nextButton = findViewById(R.id.nextButton);
        Button prevButton = findViewById(R.id.prevButton);

        // Setup
        CurrentUser.getInstance().getUserData().getUserSnapshot().getReference()
                .get()
                .addOnCompleteListener(ds -> {
                    childData = new ChildData(ds.getResult().toObject(ChildData.class));
                    statFloorNum.setText(Integer.toString(childData.getFloor()));
                    statQuestDoneNum.setText(Integer.toString(childData.getQuestsCompleted()));
                    avatarIndex = childData.getAvatar();
                    setUpAvatar(childData);
                    pieGraph(childData.getIntelligence(), childData.getStrength());
                });

        prevButton.setOnClickListener(e -> {
            avatarIndex--;
            if(avatarIndex<0){
                avatarIndex=ownedItems.size()-1;
            }
            updateAvatar();
        });
        nextButton.setOnClickListener(e->{
            avatarIndex++;
            if(avatarIndex==ownedItems.size()){
                avatarIndex=0;
            }
            updateAvatar();
        });

    }
    private void updateAvatar() {
        CosmeticItem currentArmor = ownedItems.get(avatarIndex);
        childAvatarImage.setImageResource(currentArmor.getImageResId());
        childArmorName.setText(currentArmor.getName());
        childData.setAvatar(avatarIndex);
        childData.uploadData();
    }
    private List<CosmeticItem> filterItems(List<CosmeticItem> allItems, List<CosmeticItem> OwnedItems){
        List<CosmeticItem> ownedItems = new ArrayList<>();
        for (CosmeticItem item : allItems) {
            if (OwnedItems.contains(item.getName())) {
                ownedItems.add(item);
            }
        }
        return ownedItems;
    }

    private void setUpAvatar (ChildData childData) {

        ownedItems = childData.getOwnedItems();

        childAvatarImage.setImageResource(ownedItems.get(avatarIndex).getImageResId());
        childArmorName.setText(ownedItems.get(avatarIndex).getName());
        childAvatarName.setText(childData.getUsername());
    }

    private void pieGraph(int childInt, int childStr) {
        PieChart pieChart = findViewById(R.id.chart);
        //Typeface font = ResourcesCompat.getFont(this, R.font.silkscreen); // tf is this?
        //pieChart.setEntryLabelTypeface(font);

        // Create entries for the pie chart
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(childStr, "Strength"));
        entries.add(new PieEntry(childInt, "Intelligence"));

        // Setup colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(this, R.color.Strength));  // Strength
        colors.add(ContextCompat.getColor(this, R.color.Intelligence));    // Intelligence

        // Create dataset
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        // Create PieData
        PieData data = new PieData(dataSet);
        data.setValueTextSize(20f);

        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        // Apply data to pieChart
        pieChart.setData(data);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate();
    }
}