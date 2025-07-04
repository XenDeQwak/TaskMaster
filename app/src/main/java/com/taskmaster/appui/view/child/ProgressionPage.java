package com.taskmaster.appui.view.child;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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
    private BarChart barChart, barChartLarge;
    private TextView strCount, intCount;
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
        intCount = findViewById(R.id.intCount);
        strCount = findViewById(R.id.strCount);
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
        barGraph(childInt, childStr);

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

    private void barGraph(int childInt, int childStr) {
        barChart = findViewById(R.id.chart);
        barChartLarge = findViewById(R.id.chartLarge);

// Create entries for the bars
        List<BarEntry> entries1 = new ArrayList<>();
        entries1.add(new BarEntry(0f, (float) childStr)); // Strength
        entries1.add(new BarEntry(1f, (float) childInt)); // Intelligence

        BarDataSet dataSet1 = new BarDataSet(entries1, "");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED); // Strength
        colors.add(Color.GREEN); // Intelligence

        dataSet1.setColors(colors);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setDrawValues(false);

        barChart.setData(data);

        barChart.getDescription().setEnabled(false);

        YAxis yAxis = barChart.getAxisLeft(); // Use getAxisLeft() for BarChart
        yAxis.setDrawLabels(true);
        yAxis.setAxisMinimum(0f); // Set the minimum value to 0
        barChart.getAxisRight().setEnabled(false); // Disable the right Y-axis

        Legend legend = barChart.getLegend();
        legend.setEnabled(false); // Disable the legend

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(false); // Disable drawing the labels
//xAxis.setCenterAxisLabels(true); // Remove this line
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set the position of the labels to the bottom
        xAxis.setGranularity(1f); // Set the granularity to 1
        xAxis.setXOffset(-25f); // Adjust the X offset to move labels to the left
        xAxis.setAxisMinimum(-0.5f); // Adjust the minimum value of the X-axis

        List<String> labels = new ArrayList<>();
        labels.add("Strength");
        labels.add("Intelligence`");

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.setTouchEnabled(false);

        barChart.invalidate();

// Copy the chart to the large chart
// Create a new BarDataSet for the large chart
        BarDataSet dataSetLarge = new BarDataSet(entries1, "");
        dataSetLarge.setColors(colors);

        List<IBarDataSet> dataSetsLarge = new ArrayList<>();
        dataSetsLarge.add(dataSetLarge);

        BarData dataLarge = new BarData(dataSetsLarge);
        dataLarge.setValueTextSize(10f);
        dataLarge.setDrawValues(true); // Enable values for the large chart

        barChartLarge.setData(dataLarge);
        barChartLarge.getDescription().setEnabled(false);

        YAxis yAxisLarge = barChartLarge.getAxisLeft();
        yAxisLarge.setDrawLabels(true); // Enable drawing the numbers
        yAxisLarge.setAxisMinimum(0f);
        barChartLarge.getAxisRight().setEnabled(false);

        Legend legendLarge = barChartLarge.getLegend();
        legendLarge.setEnabled(false);

        XAxis xAxisLarge = barChartLarge.getXAxis();
        xAxisLarge.setDrawLabels(true); // Enable drawing the labels
//xAxisLarge.setCenterAxisLabels(true); // Remove this line
        xAxisLarge.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisLarge.setGranularity(1f); // Set the granularity to 1
        xAxisLarge.setXOffset(-25f); // Adjust the X offset to move labels to the left
        xAxisLarge.setAxisMinimum(-0.5f); // Adjust the minimum value of the X-axis
        xAxisLarge.setValueFormatter(new IndexAxisValueFormatter(labels));
        barChartLarge.setTouchEnabled(false);

        if (childInt == 0) {
            intCount.setVisibility(View.GONE);
        }
        if (childStr == 0) {
            strCount.setVisibility(View.GONE);
        }
        strCount.setText(String.valueOf(childStr));
        intCount.setText(String.valueOf(childInt));

        barChartLarge.invalidate();
    }






}