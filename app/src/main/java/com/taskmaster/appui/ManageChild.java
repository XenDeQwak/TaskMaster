package com.taskmaster.appui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ManageChild extends AppCompatActivity {
    ImageButton imagebutton1, imagebutton2, imagebutton3, imagebutton4, imagebutton5, openChildPage, copyButton, exitButton;
    TextView codeText;
    Group dropDownGroup, popUpGroup;
    GridLayout gridLayout;
    String tavernCode;
    View rootLayout;
    FirebaseFirestore db;

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
        popUpGroup = findViewById(R.id.pop_up_tavern_code);
        copyButton = findViewById(R.id.copy_button);
        exitButton = findViewById(R.id.exit_button);
        codeText = findViewById(R.id.code_text);
        gridLayout = findViewById(R.id.gridLayout);
        rootLayout = findViewById(R.id.main);

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

        // exclude elems within popup
        View[] popupElements = {
                findViewById(R.id.pop_up_frame),
                findViewById(R.id.copy_frame),
                findViewById(R.id.exit_frame),
                findViewById(R.id.copy_text),
                findViewById(R.id.exit_text),
                findViewById(R.id.notice_text),
                findViewById(R.id.notice_shadow_overlay),
                copyButton,
                exitButton,
                codeText
        };

        // hide popup group
        popUpGroup.setVisibility(View.GONE);

        // view popup group
        imagebutton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popUpGroup.getVisibility() == View.VISIBLE) {
                    popUpGroup.setVisibility(View.GONE);
                } else {
                    popUpGroup.setVisibility(View.VISIBLE);
                }
            }
        });

        db = FirebaseFirestore.getInstance();

        // add tavern code
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "");

        if (username != null) {
            db.collection("users")
                    .whereEqualTo("username", username) // Query based on username
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tavernCode = document.getString("code");
                                codeText.setText("Tavern Code: " + tavernCode);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ManageChild.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        openChildPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popUpGroup.getVisibility() == View.GONE) {
                    Toast.makeText(ManageChild.this, "Open Child Page ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imagebutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popUpGroup.getVisibility() == View.GONE) {
                    Toast.makeText(ManageChild.this, "ur here", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imagebutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popUpGroup.getVisibility() == View.GONE) {
                    Toast.makeText(ManageChild.this, "Move", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManageChild.this, QuestManagement.class);
                    startActivity(intent);
                }
            }
        });

        imagebutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popUpGroup.getVisibility() == View.GONE) {
                    Toast.makeText(ManageChild.this, "Log Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManageChild.this, Splash.class);
                    startActivity(intent);
                }
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

                    for (View element : popupElements) {
                        if (isViewTouched(element, event)) {
                            isInsideDropdown = true;
                            break;
                        }
                    }

                    if (!isInsideDropdown) {
                        dropDownGroup.setVisibility(View.GONE);
                        popUpGroup.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeToCopy = tavernCode;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Tavern Code", codeToCopy);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ManageChild.this, "Copied Tavern Code", Toast.LENGTH_SHORT).show();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpGroup.setVisibility(View.GONE);
                Toast.makeText(ManageChild.this, "Exit", Toast.LENGTH_SHORT).show();
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