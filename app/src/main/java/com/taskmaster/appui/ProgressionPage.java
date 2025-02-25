package com.taskmaster.appui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.List;

public class ProgressionPage extends AppCompatActivity {
    ImageButton dropDownGroupButton, imagebutton3, imagebutton4, imagebutton5;
    AppCompatButton childAvatarName, childAvatarPresetName, childAvatarPresetNextButton, childAvatarPresetPrevButton, statFloorNum, statQuestDoneNum;
    ImageView statGraph, childAvatarImage;
    Group dropDownGroup;
    View rootLayout;
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


        RadarChart radarChart = findViewById(R.id.chart);

        List<RadarEntry> entries1 = new ArrayList<>();
        entries1.add(new RadarEntry(30f));
        entries1.add(new RadarEntry(20f));
        entries1.add(new RadarEntry(10f));
        entries1.add(new RadarEntry(0f));
        entries1.add(new RadarEntry(0f));

        RadarDataSet dataSet1 = new RadarDataSet(entries1, "");
        dataSet1.setColor(Color.BLUE);
        dataSet1.setFillAlpha(180);
        dataSet1.setLineWidth(1f);
        dataSet1.setDrawHighlightCircleEnabled(true);
        dataSet1.setDrawHighlightIndicators(false);

        List<IRadarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet1);

        RadarData data = new RadarData(dataSets);
        data.setValueTextSize(10f);
        data.setDrawValues(false);

        radarChart.setData(data);

        radarChart.getDescription().setEnabled(false);

        radarChart.setRotationEnabled(false);

        // Y-Axis (Numbers)
        YAxis yAxis = radarChart.getYAxis();
        yAxis.setDrawLabels(true); // Enable drawing the numbers
        yAxis.setAxisMinimum(0f); // Set the minimum value to 0

// Legend (Dataset Name)
        Legend legend = radarChart.getLegend();
        legend.setEnabled(false); // Disable the legend

// X-Axis (Labels)
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setDrawLabels(true); // Enable drawing the labels
        xAxis.setLabelCount(3, true); // Set the number of labels to 3

// Create labels for the X-Axis
        List<String> labels = new ArrayList<>();
        labels.add("Strength");
        labels.add("Intelligence");
        labels.add("Biking");

// Set the labels to the X-Axis
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        radarChart.invalidate();

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