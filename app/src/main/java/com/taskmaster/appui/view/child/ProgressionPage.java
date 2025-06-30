package com.taskmaster.appui.view.child;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Group;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.view.login.Splash;
import com.taskmaster.appui.view.parent.QuestManagement;
import com.taskmaster.appui.R;
import com.taskmaster.appui.util.NavUtil;

import java.util.ArrayList;
import java.util.List;

public class ProgressionPage extends ChildView {
    ImageButton chartButton;
    AppCompatButton childAvatarName, dropdownNavButton, navQuestPage, navManageAdv, navLogOut, childAvatarPresetName, childAvatarPresetNextButton, childAvatarPresetPrevButton, statFloorNum, statQuestDoneNum, popupLargerChartExitButton;
    ImageView statGraph, childAvatarImage;
    Group dropDownGroup, popupLargerChart;
    View rootLayout;
    BarChart barChart, barChartLarge;
    private List<Integer> avatarImages;
    private List<String> avatarNames;
    int questCount;
    FirebaseFirestore db;
    String parentID;
    String username;
    TextView strCount, intCount;
    int childAvatar;
    int currentImageIndex;
    int childInt, childStr;
    int floorCount;
    FirebaseAuth auth;
    User user;
    DocumentSnapshot childDocument;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.progression_page);

        // hide status bar and nav bar
        NavUtil.hideSystemBars(this);

        childAvatarImage = findViewById(R.id.childAvatarImage);
        childAvatarPresetName = findViewById(R.id.childAvatarPresetName);
        childAvatarName = findViewById(R.id.childAvatarName);
        intCount = findViewById(R.id.intCount);
        strCount = findViewById(R.id.strCount);
        statFloorNum = findViewById(R.id.statFloorNum);
        statQuestDoneNum = findViewById(R.id.statQuestDoneNum);


        initNavigationMenu(this, ProgressionPage.class);
        user = User.getInstance();
        childDocument = user.getDocumentSnapshot();
        setUpAvatar();
        List<String> ownedItems = (List<String>) childDocument.get("ownedItems");
        username = childDocument.getString("Username");
//        questCount = childDocument.getLong("questCount").intValue();
        childAvatar = childDocument.getLong("Avatar").intValue();
        childStr = childDocument.getDouble("Strength").intValue();
        childInt = childDocument.getDouble("Intelligence").intValue();
        floorCount = childDocument.getDouble("Floor").intValue();
        barGraph(childInt, childStr);
        statFloorNum.setText(Integer.toString(floorCount));
        statQuestDoneNum.setText(Integer.toString(questCount));
        //loadAvatarPreset();
    }

    private void setUpAvatar(){
        List<Integer> avatarImages = new ArrayList<>();
        avatarImages.add(R.drawable.placeholderavatar1_framed_round);
        avatarImages.add(R.drawable.placeholderavatar2_framed_round);
        avatarImages.add(R.drawable.placeholderavatar3_framed_round);
        avatarImages.add(R.drawable.placeholderavatar4_framed_round);
        avatarImages.add(R.drawable.placeholderavatar5_framed_round);
        childAvatarImage.setImageResource(avatarImages.get(childDocument.getDouble("Avatar").intValue()));
        childAvatarPresetName.setText(childDocument.getString("Username"));
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

//    private void loadAvatarPreset() {
//        db.collection("users").whereEqualTo("username", username)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        QuerySnapshot document = task.getResult();
//                        if (document != null && !document.isEmpty()) {
//                            DocumentReference childRef = document.getDocuments().get(0).getReference();
//                            DocumentSnapshot documents = childRef.get().getResult();
//                            String presetStr = documents.getString("childAvatar");
//
//                            if (presetStr != null) {
//                                try {
//                                    currentImageIndex = Integer.parseInt(presetStr);
//                                } catch (NumberFormatException e) {
//                                    currentImageIndex = 0;
//                                }
//                            }
//                            childAvatarImage.setImageResource(avatarImages.get(currentImageIndex));
//                            childAvatarPresetName.setText(avatarNames.get(currentImageIndex));
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("Avatar", "Error loading presetAvatar", e);
//                });
//    }

    private void updateAvatarUIAndFirebase() {
        childAvatarImage.setImageResource(avatarImages.get(currentImageIndex));
        childAvatarPresetName.setText(avatarNames.get(currentImageIndex));

        db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        if (document != null && !document.isEmpty()) {
                            DocumentReference childRef = document.getDocuments().get(0).getReference();
                            childRef.update("childAvatar", currentImageIndex);
                        }
                    }
                });
    }



    // exclude elems within dropdown and popup
    private boolean isViewTouched(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        return event.getRawX() >= x && event.getRawX() <= x + view.getWidth()
                && event.getRawY() >= y && event.getRawY() <= y + view.getHeight();
    }


}