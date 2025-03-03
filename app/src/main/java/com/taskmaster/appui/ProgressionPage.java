package com.taskmaster.appui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Group;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProgressionPage extends AppCompatActivity {
    ImageButton dropDownGroupButton, imagebutton3, imagebutton4, imagebutton5, chartButton;
    AppCompatButton childAvatarName, childAvatarPresetName, childAvatarPresetNextButton, childAvatarPresetPrevButton, statFloorNum, statQuestDoneNum, popupLargerChartExitButton;
    ImageView statGraph, childAvatarImage;
    Group dropDownGroup, popupLargerChart;
    View rootLayout;
    BarChart barChart, barChartLarge;
    private List<Integer> avatarImages;
    private List<String> avatarNames;
    private int currentImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.progression_page);

        // hide status bar and nav bar
        hideSystemBars();

        avatarImages = new ArrayList<>();
        avatarImages.add(R.drawable.placeholderavatar1_framed);
        avatarImages.add(R.drawable.placeholderavatar2_framed);
        avatarImages.add(R.drawable.rectangle_rounded);

        avatarNames = new ArrayList<>();
        avatarNames.add("Preset 1");
        avatarNames.add("Preset 2");

        loadAvatarPreset();


        childAvatarPresetNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex++;
                if (currentImageIndex >= avatarImages.size()) {
                    currentImageIndex = 0;
                }
                updateAvatarUIAndFirebase();
            }
        });

        childAvatarPresetPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex--;
                if (currentImageIndex < 0) {
                    currentImageIndex = avatarImages.size() - 1;
                }
                updateAvatarUIAndFirebase();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // hooks
        dropDownGroupButton = findViewById(R.id.dropdownIconButton);
        imagebutton3 = findViewById(R.id.imageButton4);
        imagebutton4 = findViewById(R.id.imageButton5);
        imagebutton5 = findViewById(R.id.imageButton6);
        dropDownGroup = findViewById(R.id.dropdownGroup);
        rootLayout = findViewById(R.id.main);

        statGraph = findViewById(R.id.statGraph);
        childAvatarName = findViewById(R.id.childAvatarName);
        childAvatarPresetName = findViewById(R.id.childAvatarPresetName);
        childAvatarPresetNextButton = findViewById(R.id.childAvatarPresetNextButton);
        childAvatarPresetPrevButton = findViewById(R.id.childAvatarPresetPrevButton);
        childAvatarImage = findViewById(R.id.childAvatarImage);
        statFloorNum = findViewById(R.id.statFloorNum);
        statQuestDoneNum = findViewById(R.id.statQuestDoneNum);

        popupLargerChartExitButton = findViewById(R.id.popupLargerChartExitButton);
        popupLargerChart = findViewById(R.id.popupLargerChart);

        chartButton = findViewById(R.id.chartButton);

        // image list
        avatarImages = new ArrayList<>();
        avatarImages.add(R.drawable.rectangle_rounded);
        avatarImages.add(R.drawable.placeholderavatar1_framed);
        avatarImages.add(R.drawable.placeholderavatar2_framed);

        // name list
        avatarNames = new ArrayList<>();
        avatarNames.add("Avatar 1");
        avatarNames.add("Avatar 2");
        avatarNames.add("Avatar 3");

        // Set the initial image
        childAvatarImage.setImageResource(avatarImages.get(currentImageIndex));

        // change text based on child here
        childAvatarName.setText("Child Name");
        statFloorNum.setText("12");
        statQuestDoneNum.setText("12");


        barChart = findViewById(R.id.chart);
        barChartLarge = findViewById(R.id.chartLarge);

// Create entries for the bars
        List<BarEntry> entries1 = new ArrayList<>();
        entries1.add(new BarEntry(0f, 3f)); // Strength
        entries1.add(new BarEntry(1f, 2f)); // Intelligence

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

        barChartLarge.invalidate();


        // exclude elems within dropdown
        View[] dropDownElements = {
                findViewById(R.id.imageView24),
                findViewById(R.id.imageView25),
                findViewById(R.id.imageView26),
                findViewById(R.id.imageView27),
                findViewById(R.id.textView7),
                findViewById(R.id.textView8),
                findViewById(R.id.textView9)
        };

        // hide dropdown group
        dropDownGroup.setVisibility(View.GONE);

        // view dropdown group
        dropDownGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownGroup.getVisibility() == View.VISIBLE) {
                    dropDownGroup.setVisibility(View.GONE);
                } else {
                    dropDownGroup.setVisibility(View.VISIBLE);
                }
            }
        });

        imagebutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(ProgressionPage.this, "Move", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProgressionPage.this, WeeklyBoss.class);
                    startActivity(intent);
            }
        });

        imagebutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(ProgressionPage.this, "Move", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProgressionPage.this, QuestManagement.class);
                    startActivity(intent);
            }
        });

        imagebutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(ProgressionPage.this, "Log Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProgressionPage.this, Splash.class);
                    startActivity(intent);
            }
        });

        // exit dropdown & popup group
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    boolean isInsideDropdown = false;

                    for (View element : dropDownElements) {
                        if (isViewTouched(element, event)) {
                            isInsideDropdown = true;
                            break;
                        }
                    }

                    if (!isInsideDropdown) {
                        dropDownGroup.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });

        childAvatarPresetNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex++;
                if (currentImageIndex >= avatarImages.size()) {
                    currentImageIndex = 0;
                }
                childAvatarImage.setImageResource(avatarImages.get(currentImageIndex));
                childAvatarPresetName.setText(avatarNames.get(currentImageIndex));
            }
        });

        childAvatarPresetPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex--;
                if (currentImageIndex < 0) {
                    currentImageIndex = avatarImages.size() - 1;
                }
                childAvatarImage.setImageResource(avatarImages.get(currentImageIndex));
            }
        });

        popupLargerChartExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupLargerChart.setVisibility(View.GONE);
            }
        });

        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupLargerChart.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadAvatarPreset() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String presetStr = documentSnapshot.getString("presetAvatar");
                        if (presetStr != null) {
                            try {
                                currentImageIndex = Integer.parseInt(presetStr);
                            } catch (NumberFormatException e) {
                                currentImageIndex = 0;
                            }
                        }
                        childAvatarImage.setImageResource(avatarImages.get(currentImageIndex));
                        childAvatarPresetName.setText(avatarNames.get(currentImageIndex));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Avatar", "Error loading presetAvatar", e);
                });
    }

    private void updateAvatarUIAndFirebase() {
        childAvatarImage.setImageResource(avatarImages.get(currentImageIndex));
        childAvatarPresetName.setText(avatarNames.get(currentImageIndex));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(userId)
                .update("presetAvatar", String.valueOf(currentImageIndex))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Avatar", "presetAvatar updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Avatar", "Failed to update presetAvatar", e);
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

    private void hideSystemBars() {
        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide nav bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}