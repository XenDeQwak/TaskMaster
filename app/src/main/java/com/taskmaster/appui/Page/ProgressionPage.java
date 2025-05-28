package com.taskmaster.appui.Page;

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
import com.taskmaster.appui.Page.Login.Splash;
import com.taskmaster.appui.Page.Main.QuestManagement;
import com.taskmaster.appui.R;
import com.taskmaster.appui.Services.NavUtil;

import java.util.ArrayList;
import java.util.List;

public class ProgressionPage extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.progression_page);

        // hide status bar and nav bar
        NavUtil.hideSystemBars(this);

        avatarImages = new ArrayList<>();
        avatarImages.add(R.drawable.placeholderavatar1_framed);
        avatarImages.add(R.drawable.placeholderavatar2_framed);
        avatarImages.add(R.drawable.rectangle_rounded);

        childAvatarPresetNextButton = findViewById(R.id.childAvatarPresetNextButton);
        childAvatarPresetPrevButton = findViewById(R.id.childAvatarPresetPrevButton);


        avatarNames = new ArrayList<>();
        avatarNames.add("None");
        avatarNames.add("Avatar 1");
        avatarNames.add("Avatar 2");
        avatarNames.add("Avatar 3");
        avatarNames.add("Avatar 4");

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot childDocument = task.getResult();
                        if (childDocument.exists()) {
                            username = childDocument.getString("username");
                            questCount = childDocument.getLong("questCount").intValue();
                            childAvatar = childDocument.getLong("childAvatar").intValue();

                            childInt = childDocument.getLong("childInt").intValue();
                            childStr = childDocument.getLong("childStr").intValue();
                            childAvatarName.setText(childDocument.getString("username"));
                            floorCount = childDocument.getLong("floor").intValue();


                            barGraph(childInt, childStr);
                            // Set the initial image
                            childAvatarImage.setImageResource(avatarImages.get(childAvatar));
                            currentImageIndex = childAvatar;

                            childAvatarPresetName.setText(avatarNames.get(currentImageIndex));

                            statQuestDoneNum.setText(String.valueOf(questCount));
                            statFloorNum.setText(String.valueOf(floorCount));

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
                        } else {
                            Log.d("DEBUG", "PARENT DOCUMENT DOES NOT EXIST");
                        }
                    } else {
                        Log.d("DEBUG", "Error getting parent document", task.getException());
                    }
                });

        loadAvatarPreset();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // hooks
        dropdownNavButton = findViewById(R.id.dropdownNavButton);
        navQuestPage = findViewById(R.id.navQuestPage);
        navManageAdv = findViewById(R.id.navManageAdv);
        navLogOut = findViewById(R.id.navLogOut);
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

        strCount = findViewById(R.id.strCount);
        intCount = findViewById(R.id.intCount);

        // image list
        avatarImages = new ArrayList<>();
        avatarImages.add(R.drawable.placeholderavatar5_framed_round);
        avatarImages.add(R.drawable.placeholderavatar1_framed_round);
        avatarImages.add(R.drawable.placeholderavatar2_framed_round);
        avatarImages.add(R.drawable.placeholderavatar3_framed_round);
        avatarImages.add(R.drawable.placeholderavatar4_framed_round);

        // name list
        avatarNames = new ArrayList<>();
        avatarNames.add("None");
        avatarNames.add("Avatar 1");
        avatarNames.add("Avatar 2");
        avatarNames.add("Avatar 3");
        avatarNames.add("Avatar 4");

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "");

        // change text based on child here
//        childAvatarName.setText(username);

        statQuestDoneNum.setText("11");


        // exclude elems within dropdown
        View[] dropDownElements = {
                findViewById(R.id.navFrame)
        };

        // hide dropdown group
        dropDownGroup.setVisibility(View.GONE);

        // view dropdown group
        dropdownNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownGroup.getVisibility() == View.VISIBLE) {
                    dropDownGroup.setVisibility(View.GONE);
                } else {
                    dropDownGroup.setVisibility(View.VISIBLE);
                }
            }
        });

        navManageAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProgressionPage.this, "Move", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProgressionPage.this, WeeklyBoss.class);
                startActivity(intent);
            }
        });

        navQuestPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProgressionPage.this, "Move", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProgressionPage.this, QuestManagement.class);
                startActivity(intent);
            }
        });

        navLogOut.setOnClickListener(new View.OnClickListener() {
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

    private void loadAvatarPreset() {
        db.collection("users").whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        if (document != null && !document.isEmpty()) {
                            DocumentReference childRef = document.getDocuments().get(0).getReference();
                            DocumentSnapshot documents = childRef.get().getResult();
                            String presetStr = documents.getString("childAvatar");

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
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Avatar", "Error loading presetAvatar", e);
                });
    }

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