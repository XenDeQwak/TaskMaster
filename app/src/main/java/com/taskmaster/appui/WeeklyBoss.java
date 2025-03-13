package com.taskmaster.appui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeklyBoss extends AppCompatActivity {
    ImageButton dropDownGroupButton, imagebutton3, imagebutton4, imagebutton5;
    AppCompatButton fightButton, statReqStr, statReqInt, monsterName, childBarStatsButton, popupMonsterButton, childBarFloorCount;
    ImageView statGraph, childAvatarImage;
    TextView popupMonsterMessageText, monsterHealthBarText;
    Group dropDownGroup, popupMonsterMessage;
    ProgressBar monsterHealthBar;
    View rootLayout;
    int bossReq = 10;
    int childStr;
    int childInt;
    String bossName;
    private int currentProgress = 100;
    private List<Integer> avatarImages;
    private List<String> avatarNames;
    private int currentImageIndex = 0;
    int childAvatar;
    DocumentReference bossDoc;
    FirebaseFirestore db;
    TextView childBarName;
    Group childBarGroup;
    ImageView childBarAvatar;
    String bossID;
    int floorCount;
    ImageView monsterImage;
    FirebaseAuth auth;



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
        childBarName = findViewById(R.id.childBarName);
        childBarGroup = findViewById(R.id.childBarGroup);
        childBarAvatar = findViewById(R.id.childBarAvatar);

        fightButton = findViewById(R.id.fightButton);
        popupMonsterButton = findViewById(R.id.popupMonsterButton);
        popupMonsterMessage = findViewById(R.id.popupMonsterMessage);
        popupMonsterMessageText = findViewById(R.id.popupMonsterMessageText);
        monsterHealthBar = findViewById(R.id.monsterHealthBar);
        monsterHealthBarText = findViewById(R.id.monsterHealthBarText);
        childBarFloorCount = findViewById(R.id.childBarFloorCount);
        monsterName = findViewById(R.id.monsterName);
        statReqStr = findViewById(R.id.statReqStr);
        statReqInt = findViewById(R.id.statReqInt);
        monsterImage = findViewById(R.id.monsterImage);

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


        //child data init
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

        Map<String, Object> bossData = new HashMap<>();

        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot childDocument = task.getResult();
                        if (childDocument.exists()) {
                            db.collection("boss").get()
                                    .addOnCompleteListener(tasks -> {
                                        if (tasks.isSuccessful()) {
                                            if (tasks.getResult().isEmpty()) {
                                                bossData.put("bossReq", bossReq);
                                                bossData.put("bossName", "Sancho");
                                                bossData.put("bossAvatar", 0);
                                                bossID = db.collection("boss").document().getId();
                                                db.collection("boss").document(bossID).set(bossData);
                                            } else {
                                                QuerySnapshot bossDocs = tasks.getResult();
                                                bossID = bossDocs.getDocuments().get(0).getId();
                                                bossDoc = db.collection("boss").document(bossID);
                                                bossName = bossDocs.getDocuments().get(0).getString("bossName");
                                                monsterName.setText(bossName);
                                                bossReq = bossDocs.getDocuments().get(0).getLong("bossReq").intValue();
                                                statReqStr.setText("STR: " + bossReq);
                                                statReqInt.setText("INT: " + bossReq);
                                                monsterImage.setImageResource(R.drawable.bucklerbossundamaged);
                                            }
                                        }
                                    });

                            childStr = childDocument.getLong("childStr").intValue();
                            childInt = childDocument.getLong("childInt").intValue();
                            floorCount = childDocument.getLong("floor").intValue();
                            childAvatar = childDocument.getLong("childAvatar").intValue();
                            childBarGroup.setVisibility(View.VISIBLE);
                            childBarName.setText(childDocument.getString("username"));
                            childBarFloorCount.setText("Floor " + floorCount);


                            List<Integer>avatarImages = new ArrayList<>();
                            avatarImages.add(R.drawable.rectangle_rounded);
                            avatarImages.add(R.drawable.placeholderavatar1_framed);
                            avatarImages.add(R.drawable.placeholderavatar2_framed);
                            avatarImages.add(R.drawable.placeholderavatar3_framed);
                            avatarImages.add(R.drawable.placeholderavatar4_framed);

                            childBarAvatar.setImageResource(avatarImages.get(childAvatar));
                            Log.d("FLOOR", "FLOOR: " + floorCount);
                            DocumentReference docRef = db.collection("users").document(userId);
                            fightButton.setOnClickListener(e -> {
                                bossFight(docRef, bossID, bossDoc);
                            });
                        } else {
                            Log.d("DEBUG", "PARENT DOCUMENT DOES NOT EXIST");
                        }
                    } else {
                        Log.d("DEBUG", "Error getting parent document", task.getException());
                    }
                });


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



        popupMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMonsterMessage.setVisibility(View.GONE);
            }
        });
    }

    private void bossFight(DocumentReference docRef, String bossID, DocumentReference bossDoc) {
        //boss data init
        db.collection("boss").document(bossID).get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       DocumentSnapshot bossDocument = task.getResult();
                       if (bossDocument.exists()) {
                           bossReq = bossDocument.getLong("bossReq").intValue();

                           if (childStr >= bossReq && childInt >= bossReq) {
                               int totalDmg = 100;
                               currentProgress -= totalDmg;
                               updateProgressBar(currentProgress);
                               double prevChildStats = ((childStr + childInt) / 2);
                               bossReq = (int)Math.round(prevChildStats + Math.pow(10, 1.3) * Math.log10(prevChildStats));
                               docRef.update("floor", floorCount + 1);
                               bossDoc.update("bossReq", bossReq);

                               popupMonsterMessageText.setText("im defeated :(");
                               popupMonsterMessage.setVisibility(View.VISIBLE);


                           } else if ((childStr < bossReq && childInt < bossReq) || (childStr == bossReq && childInt < bossReq) || (childStr < bossReq && childInt == bossReq) || (childStr > bossReq && childInt < bossReq) || (childStr < bossReq && childInt > bossReq)) {
                               double strDmg = ((double) childStr / bossReq);
                               double intDmg = ((double) childInt / bossReq);
                               double totalDmg = (intDmg * strDmg * 100);
                               currentProgress -= totalDmg;
                               currentProgress = Math.round(currentProgress);
                               updateProgressBar(currentProgress);

                               popupMonsterMessageText.setText("im :)");
                               popupMonsterButton.setText("Exit");
                               popupMonsterButton.setOnClickListener(e -> {
                                   Intent intent = new Intent(WeeklyBoss.this, QuestManagement.class);
                                   startActivity(intent);
                               });
                               popupMonsterMessage.setVisibility(View.VISIBLE);
                           }
                       }
                   }
                });
        // Check if the child's stats are sufficient to damage the boss

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