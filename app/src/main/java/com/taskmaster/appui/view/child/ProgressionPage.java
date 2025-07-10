package com.taskmaster.appui.view.child;

import static java.security.AccessController.getContext;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.R;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.uimodule.CosmeticItemTemplate.CosmeticItem;
import java.util.ArrayList;
import java.util.List;

public class ProgressionPage extends ChildView {
    private AppCompatButton childAvatarName;
    private AppCompatButton childArmorName;
    private ImageView childAvatarImage;
    private int[] currIndex = new int[1];
    private User user;
    private DocumentSnapshot childDocument;
    private List<CosmeticItem> ownedItems,items;

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

        user = User.getInstance();
        childDocument = user.getDocumentSnapshot();
        statFloorNum.setText(Integer.toString(childDocument.getDouble("Floor").intValue()));
//        statQuestDoneNum.setText(Integer.toString(childDocument.getDouble("QuestCount").intValue()));
        int childInt = childDocument.getDouble("Intelligence").intValue();
        int childStr = childDocument.getDouble("Strength").intValue();
        currIndex[0] = childDocument.getDouble("Avatar").intValue();
        setUpAvatar();
        pieGraph(childInt, childStr);

        prevButton.setOnClickListener(e -> {
            currIndex[0]--;
            if(currIndex[0]<0){
                currIndex[0]=ownedItems.size()-1;
            }
            updateAvatar();
        });
        nextButton.setOnClickListener(e->{
            currIndex[0]++;
            if(currIndex[0]==ownedItems.size()){
                currIndex[0]=0;
            }
            updateAvatar();
        });

    }
    private void updateAvatar(){
        CosmeticItem currentArmor = items.get(currIndex[0]);
        childAvatarImage.setImageResource(currentArmor.getImageResId());
        childArmorName.setText(currentArmor.getName());
        childDocument.getReference().update("Avatar", currentArmor.getId());
        user.loadDocumentSnapshot(ee-> childDocument = user.getDocumentSnapshot());
    }
    private List<CosmeticItem> filterItems(List<CosmeticItem> allItems, List<String> OwnedItems){
        List<CosmeticItem> ownedItems = new ArrayList<>();
        for (CosmeticItem item : allItems) {
            if (OwnedItems.contains(item.getName())) {
                ownedItems.add(item);
            }
        }
        return ownedItems;
    }

    private void setUpAvatar(){
        items = new ArrayList<>();
        items.add(new CosmeticItem(0,"Armorless", "High-level protection", 5, R.drawable.placeholderavatar5_framed_round));
        items.add(new CosmeticItem(1,"Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar1_framed_round));
        items.add(new CosmeticItem(2,"Red Armor", "High-level protection", 3, R.drawable.placeholderavatar2_framed_round));
        items.add(new CosmeticItem(3,"Crusader Armor", "High-level protection", 4, R.drawable.placeholderavatar3_framed_round));
        items.add(new CosmeticItem(4,"Copper Crusader Armor", "High-level protection", 5, R.drawable.placeholderavatar4_framed_round));

        List<String> ownedItemsString = (List<String>) childDocument.get("OwnedItems");
        ownedItems = filterItems(items,ownedItemsString); //transform list of strings to list of items

        childAvatarImage.setImageResource(items.get(currIndex[0]).getImageResId());
        childArmorName.setText(items.get(currIndex[0]).getName());
        childAvatarName.setText(childDocument.getString("Username"));
    }

    private void pieGraph(int childInt, int childStr) {
        PieChart pieChart = findViewById(R.id.chart);
        Typeface font = ResourcesCompat.getFont(this, R.font.silkscreen);
        pieChart.setEntryLabelTypeface(font);

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
        data.setValueTypeface(font);

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