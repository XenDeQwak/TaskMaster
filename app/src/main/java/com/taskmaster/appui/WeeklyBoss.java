package com.taskmaster.appui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Group;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class WeeklyBoss extends AppCompatActivity {
    ImageButton dropDownGroupButton, imagebutton3, imagebutton4, imagebutton5;
    AppCompatButton fightButton, childBarStatsButton, popupMonsterButton;
    ImageView statGraph, childAvatarImage;
    TextView popupMonsterMessageText, monsterHealthBarText;
    Group dropDownGroup, popupMonsterMessage;
    ProgressBar monsterHealthBar;
    View rootLayout;
    private int currentProgress = 100;
    private List<Integer> avatarImages;
    private List<String> avatarNames;
    private int currentImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.weekly_boss);

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
        childBarStatsButton = findViewById(R.id.childBarStatsButton);
        rootLayout = findViewById(R.id.main);

        fightButton = findViewById(R.id.fightButton);
        popupMonsterButton = findViewById(R.id.popupMonsterButton);
        popupMonsterMessage = findViewById(R.id.popupMonsterMessage);
        popupMonsterMessageText = findViewById(R.id.popupMonsterMessageText);
        monsterHealthBar = findViewById(R.id.monsterHealthBar);
        monsterHealthBarText = findViewById(R.id.monsterHealthBarText);

        TextView childBarFloorCount;

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

        // hide popup
        popupMonsterMessage.setVisibility(View.GONE);

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
                Toast.makeText(WeeklyBoss.this, "ur here", Toast.LENGTH_SHORT).show();
            }
        });

        imagebutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeeklyBoss.this, "Move", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WeeklyBoss.this, QuestManagement.class);
                startActivity(intent);
            }
        });

        imagebutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeeklyBoss.this, "Log Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WeeklyBoss.this, Splash.class);
                startActivity(intent);
            }
        });

        childBarStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WeeklyBoss.this, "stats", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WeeklyBoss.this, ProgressionPage.class);
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
        childBarFloorCount = findViewById(R.id.childBarFloorCount);

        updateProgressBar(currentProgress);

        // ... inside onCreate() after initializing views:
        fightButton = findViewById(R.id.fightButton);

        int childStrength = 3;
        int childIntelligence = 2;
        int bossStrengthReq = 4;
        int bossIntelligenceReq = 3;

        fightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the child's stats are sufficient to damage the boss
                if (childStrength >= bossStrengthReq && childIntelligence >= bossIntelligenceReq) {
                    // Calculate damage as the sum of the child's stats
                    int damage = childStrength + childIntelligence;
                    currentProgress -= damage;
                    updateProgressBar(currentProgress);
                    Toast.makeText(WeeklyBoss.this, "You inflicted " + damage + " damage!", Toast.LENGTH_SHORT).show();

                    // Check if boss HP is zero or below
                    if (currentProgress <= 0) {
                        Toast.makeText(WeeklyBoss.this, "Boss defeated! Floor progressed!", Toast.LENGTH_SHORT).show();

                        // Increase the floor count;
                        try {
                            int currentFloor = Integer.parseInt(childBarFloorCount.getText().toString());
                            currentFloor++;
                            childBarFloorCount.setText(String.valueOf(currentFloor));
                        } catch (NumberFormatException e) {
                            // Default to floor 1 if parsing fails
                            childBarFloorCount.setText("1");
                        }

                        // Reset boss HP for the next week
                        currentProgress = 100; //dont mind this
                        updateProgressBar(currentProgress);
                    }
                } else {
                    // Child's stats are insufficient; no damage is dealt
                    Toast.makeText(WeeklyBoss.this, "Your stats are too low to damage the boss!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        popupMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMonsterMessage.setVisibility(View.GONE);
            }
        });
    }

    private void updateProgressBar(int progress) {
        int clampedProgress = Math.max(0, Math.min(progress, 100));
        monsterHealthBar.setProgress(clampedProgress);
        monsterHealthBarText.setText(clampedProgress + "/100");
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

/*
        fightButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(WeeklyBoss.this, "pressed", Toast.LENGTH_SHORT).show();

        currentProgress -= 10;
        updateProgressBar(currentProgress);
        // if meet stat req
//                if (currentProgress <= 0) {
//                    popupMonsterMessageText.setText("im defeated :(");
//                    popupMonsterMessage.setVisibility(View.VISIBLE);
//                    currentProgress = 0;
//                    updateProgressBar(currentProgress);
//                    // add floorcount, lock this till next week
//                }

        // if not meet stat req
        if (currentProgress <= 10) {
            popupMonsterMessageText.setText("im :)");
            popupMonsterButton.setText("Exit");
            popupMonsterMessage.setVisibility(View.VISIBLE);
            currentProgress = 0;
            updateProgressBar(currentProgress);
            // keep floorcount, lock this till next week
        }
    }
});
*/