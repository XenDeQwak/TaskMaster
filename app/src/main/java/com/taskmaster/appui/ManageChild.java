package com.taskmaster.appui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManageChild extends AppCompatActivity {

    ImageButton imagebutton1, imagebutton2, imagebutton3, imagebutton4, imagebutton5, openQuestButton;

    ImageView questFrame, questNameFrame, questImage;

    TextView questNameText;

    Group dropDownGroup;
    GridLayout gridLayout;
    LinearLayout newGroup;
    ConstraintLayout newQuest;
    ConstraintSet constraintSet;

    int questWidth, questHeight, imageWidth, imageHeight, nameFrameWidth, nameFrameHeight, topMarginImage, bottomMarginImage, topMarginNameFrame, bottomMarginNameFrame;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.manage_child);

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

        imagebutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManageChild.this, "ur here", Toast.LENGTH_SHORT).show();
            }
        });

        imagebutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManageChild.this, "Move", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ManageChild.this, QuestManagement.class);
                startActivity(intent);
            }
        });

        imagebutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManageChild.this, "Log Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ManageChild.this, Splash.class);
                startActivity(intent);
            }
        });
    }

    private void hideSystemBars() {
        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the nav bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}