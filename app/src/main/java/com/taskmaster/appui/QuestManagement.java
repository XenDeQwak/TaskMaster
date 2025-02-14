package com.taskmaster.appui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuestManagement extends AppCompatActivity {

    ImageButton imagebutton1, imagebutton2, imagebutton3, imagebutton4, imagebutton5, openQuestButton;

    ImageView questFrame, questNameFrame, questImage;

    TextView questNameText;

    Group dropDownGroup;
    GridLayout gridLayout;
    LinearLayout newGroup;
    ConstraintLayout newQuest;
    ConstraintSet constraintSet;

    //quest count
    int groupCount = 0;

    int questWidth, questHeight, imageWidth, imageHeight, nameFrameWidth, nameFrameHeight, topMarginImage, bottomMarginImage, topMarginNameFrame, bottomMarginNameFrame;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.quest_management);

        // hide status bar and nav bar
        hideSystemBars();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // hooks
        imagebutton1 = findViewById(R.id.imageButton2);
        imagebutton2 = findViewById(R.id.imageButton3);
        imagebutton3 = findViewById(R.id.imageButton4);
        imagebutton4 = findViewById(R.id.imageButton5);
        imagebutton5 = findViewById(R.id.imageButton6);
        dropDownGroup = findViewById(R.id.dropdownGroup);
        gridLayout = findViewById(R.id.gridLayout);

        // hide dropdown group
        dropDownGroup.setVisibility(View.GONE);

        // view dropdown group
        imagebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownGroup.getVisibility() == View.VISIBLE) {
                    dropDownGroup.setVisibility(View.GONE);
                } else {
                    dropDownGroup.setVisibility(View.VISIBLE);
                }
            }
        });

        imagebutton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (groupCount >= 4) { // limit quests
                    Toast.makeText(QuestManagement.this, "Max quests reached!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // convert px to dp
                questWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 172, getResources().getDisplayMetrics());
                questHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 285, getResources().getDisplayMetrics());
                imageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 161, getResources().getDisplayMetrics());
                imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 227, getResources().getDisplayMetrics());
                nameFrameWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 162, getResources().getDisplayMetrics());
                nameFrameHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 39, getResources().getDisplayMetrics());
                topMarginImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());
                bottomMarginImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                topMarginNameFrame = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 241, getResources().getDisplayMetrics());
                bottomMarginNameFrame = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());

                // create a new LinearLayout
                newGroup = new LinearLayout(context);
                newGroup.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                newGroup.setOrientation(LinearLayout.HORIZONTAL);
                newGroup.setGravity(Gravity.CENTER);

                // create a new ConstraintLayout
                newQuest = new ConstraintLayout(context);
                ConstraintLayout.LayoutParams questParams = new ConstraintLayout.LayoutParams(questWidth, questHeight);
                if (groupCount % 2 == 0) {
                    questParams.setMarginEnd(4);
                } else {
                    questParams.setMarginStart(4);
                }
                newQuest.setLayoutParams(questParams);

                // create quest frame
                questFrame = new ImageView(context);
                questFrame.setId(View.generateViewId());
                questFrame.setLayoutParams(new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                ));
                questFrame.setAlpha(0.5f);
                questFrame.setImageResource(R.drawable.rectangle_rounded);

                // create quest name frame
                questNameFrame = new ImageView(context);
                questNameFrame.setId(View.generateViewId());
                ConstraintLayout.LayoutParams nameFrameParams = new ConstraintLayout.LayoutParams(nameFrameWidth, nameFrameHeight);
                nameFrameParams.topMargin = topMarginNameFrame;
                nameFrameParams.bottomMargin = bottomMarginNameFrame;
                questNameFrame.setLayoutParams(nameFrameParams);
                questNameFrame.setAlpha(0.5f);
                questNameFrame.setImageResource(R.drawable.rectangle_rounded);

                // create quest image
                questImage = new ImageView(context);
                questImage.setId(View.generateViewId());
                ConstraintLayout.LayoutParams imageParams = new ConstraintLayout.LayoutParams(imageWidth, imageHeight);
                imageParams.topMargin = topMarginImage;
                imageParams.bottomMargin = bottomMarginImage;
                questImage.setLayoutParams(imageParams);
                questImage.setAlpha(0.5f);
                questImage.setImageResource(R.drawable.rectangle_rounded);

                // create quest name text
                questNameText = new TextView(context);
                questNameText.setId(View.generateViewId());
                questNameText.setLayoutParams(new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                ));
                questNameText.setText("Quest Name " + (groupCount + 1));
                questNameText.setTextSize(20);
                questNameText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
                questNameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // create open quest button
                openQuestButton = new ImageButton(context);
                openQuestButton.setId(View.generateViewId());
                openQuestButton.setLayoutParams(new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                ));
                openQuestButton.setBackground(null);

                openQuestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(QuestManagement.this, "Edit Quest " + (groupCount + 1), Toast.LENGTH_SHORT).show();
                    }
                });

                // add views to ConstraintLayout
                newQuest.addView(questFrame);
                newQuest.addView(questNameFrame);
                newQuest.addView(questImage);
                newQuest.addView(questNameText);
                newQuest.addView(openQuestButton);

                // set constraints programmatically
                constraintSet = new ConstraintSet();
                constraintSet.clone(newQuest);

                // align questNameFrame inside questFrame
                constraintSet.connect(questNameFrame.getId(), ConstraintSet.TOP, questFrame.getId(), ConstraintSet.TOP, topMarginNameFrame);
                constraintSet.connect(questNameFrame.getId(), ConstraintSet.BOTTOM, questFrame.getId(), ConstraintSet.BOTTOM, bottomMarginNameFrame);
                constraintSet.connect(questNameFrame.getId(), ConstraintSet.START, questFrame.getId(), ConstraintSet.START, 0);
                constraintSet.connect(questNameFrame.getId(), ConstraintSet.END, questFrame.getId(), ConstraintSet.END, 0);

                // align questImage inside questFrame
                constraintSet.connect(questImage.getId(), ConstraintSet.TOP, questFrame.getId(), ConstraintSet.TOP, topMarginImage);
                constraintSet.connect(questImage.getId(), ConstraintSet.BOTTOM, questFrame.getId(), ConstraintSet.BOTTOM, bottomMarginImage);
                constraintSet.connect(questImage.getId(), ConstraintSet.START, questFrame.getId(), ConstraintSet.START, 0);
                constraintSet.connect(questImage.getId(), ConstraintSet.END, questFrame.getId(), ConstraintSet.END, 0);

                // align questNameText inside questNameFrame
                constraintSet.connect(questNameText.getId(), ConstraintSet.TOP, questNameFrame.getId(), ConstraintSet.TOP, 0);
                constraintSet.connect(questNameText.getId(), ConstraintSet.BOTTOM, questNameFrame.getId(), ConstraintSet.BOTTOM, 0);
                constraintSet.connect(questNameText.getId(), ConstraintSet.START, questNameFrame.getId(), ConstraintSet.START, 0);
                constraintSet.connect(questNameText.getId(), ConstraintSet.END, questNameFrame.getId(), ConstraintSet.END, 0);

                constraintSet.applyTo(newQuest);

                // add to LinearLayout
                newGroup.addView(newQuest);

                // add to GridLayout
                gridLayout.addView(newGroup);

                // test message
                Toast.makeText(QuestManagement.this, "New Quest Added: Quest " + (groupCount + 1), Toast.LENGTH_SHORT).show();

                groupCount++;
            }
        });

        imagebutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "ur here", Toast.LENGTH_SHORT).show();
            }
        });

        imagebutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "Move", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuestManagement.this, ManageChild.class);
                startActivity(intent);
            }
        });

        imagebutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "Log Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuestManagement.this, Splash.class);
                startActivity(intent);
            }
        });
    }

    private void hideSystemBars() {
        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide the nav bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}