package com.taskmaster.appui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
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

public class ManageChild extends AppCompatActivity {
    ImageButton imagebutton1, imagebutton2, imagebutton3, imagebutton4, imagebutton5, openChildPage;
    ImageView childFrame, childAvatar;
    TextView statStr, statInt, floorView;
    Group dropDownGroup;
    GridLayout gridLayout;
    FrameLayout newGroup;
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
        openChildPage = findViewById(R.id.open_child_page);
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

            // quest count
            int groupCount = 1;

            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                // convert px to dp
                float density = context.getResources().getDisplayMetrics().density;
                int dpToPx = (int) (108 * density);

                // create a new FrameLayout
                newGroup = new FrameLayout(context);
                FrameLayout.LayoutParams groupParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        dpToPx
                );
                groupParams.setMargins(0, 0, 0, (int) (5 * density));
                newGroup.setLayoutParams(groupParams);
                newGroup.setId(View.generateViewId());

                // create child frame
                childFrame = new ImageView(context);
                FrameLayout.LayoutParams childFrameParams = new FrameLayout.LayoutParams(
                        (int) (316 * density), (int) (84 * density)
                );
                childFrameParams.setMargins((int) (27 * density), (int) (9 * density), (int) (18 * density), (int) (137 * density));
                childFrame.setLayoutParams(childFrameParams);
                childFrame.setImageResource(R.drawable.rectangle_rounded);

                // create child frame avatar
                childAvatar = new ImageView(context);
                FrameLayout.LayoutParams avatarParams = new FrameLayout.LayoutParams(
                        (int) (108 * density), (int) (107 * density)
                );
                avatarParams.setMargins((int) (11 * density), 0, 0, 0);
                childAvatar.setLayoutParams(avatarParams);
                childAvatar.setImageResource(R.drawable.circle_with_shadow);

                // create stat view STR text
                statStr = new TextView(context);
                FrameLayout.LayoutParams strParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                strParams.setMargins((int) (125 * density), (int) (20 * density), 0, 0);
                statStr.setLayoutParams(strParams);
                statStr.setText("STR 8");
                statStr.setTextSize(20);
                statStr.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));

                // create floor view text
                floorView = new TextView(context);
                FrameLayout.LayoutParams floorParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                floorParams.setMargins((int) (258 * density), (int) (30 * density), 0, 0);
                floorView.setLayoutParams(floorParams);
                floorView.setText("Floor: Y");
                floorView.setTextSize(20);
                floorView.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));

                // create stat view INT text
                statInt = new TextView(context);
                FrameLayout.LayoutParams intParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                intParams.setMargins((int) (125 * density), (int) (50 * density), 0, 0);
                statInt.setLayoutParams(intParams);
                statInt.setText("INT 10");
                statInt.setTextSize(20);
                statInt.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));

                // create open child page button
                openChildPage = new ImageButton(context);
                FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                        (int) (333 * density), (int) (104 * density)
                );
                buttonParams.setMargins((int) (10 * density), 0, 0, 0);
                openChildPage.setLayoutParams(buttonParams);
                openChildPage.setBackground(null);

                openChildPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ManageChild.this, "Open Child Page " + groupCount, Toast.LENGTH_SHORT).show();
                    }
                });

                // add views to the FrameLayout
                newGroup.addView(childFrame);
                newGroup.addView(childAvatar);
                newGroup.addView(statStr);
                newGroup.addView(floorView);
                newGroup.addView(statInt);
                newGroup.addView(openChildPage);

                // add to GridLayout
                gridLayout = findViewById(R.id.gridLayout1);
                gridLayout.addView(newGroup);

                // test message
                Toast.makeText(context, "Group " + groupCount + " added!", Toast.LENGTH_SHORT).show();

                groupCount++;
            }
        });

        openChildPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManageChild.this, "Open Child Page ", Toast.LENGTH_SHORT).show();
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