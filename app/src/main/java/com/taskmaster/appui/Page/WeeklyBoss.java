package com.taskmaster.appui.Page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.CountDownTimer;

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.taskmaster.appui.Page.Login.Splash;
import com.taskmaster.appui.Page.Main.QuestManagement;
import com.taskmaster.appui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeklyBoss extends AppCompatActivity {
    AppCompatButton fightButton, dropdownNavButton, navQuestPage, navManageAdv, navLogOut, statReqStr, statReqInt, monsterName, childBarStatsButton, popupMonsterButton, childBarFloorCount;
    ImageView statGraph, childAvatarImage, popupMonsterImage;
    TextView popupMonsterMessageText, monsterHealthBarText, popupMonsterName;
    Group dropDownGroup, popupMonsterMessage;
    ProgressBar monsterHealthBar;
    View rootLayout;
    int bossReq = 10;
    int childStr;
    int childInt;
    int bossAvatar;
    String bossName;
    private final Handler handler = new Handler(Looper.getMainLooper());
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
    private boolean bossIsDead;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.weekly_boss);
        // hide status bar and nav bar
        NavUtil.hideSystemBars(this);

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
        childBarStatsButton = findViewById(R.id.childBarStatsButton);
        rootLayout = findViewById(R.id.main);
        childBarName = findViewById(R.id.childBarName);
        childBarGroup = findViewById(R.id.childBarGroup);
        childBarAvatar = findViewById(R.id.childBarAvatar);
        popupMonsterName = findViewById(R.id.popupMonsterName);

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
        popupMonsterImage = findViewById(R.id.popupMonsterImage);

        childBarFloorCount = findViewById(R.id.childBarFloorCount);
        // ... inside onCreate() after initializing views:
        fightButton = findViewById(R.id.fightButton);

        // exclude elems within dropdown
        View[] dropDownElements = {
                findViewById(R.id.navFrame)
        };

        // hide popup
        popupMonsterMessage.setVisibility(View.GONE);

        // hide dropdown group
        dropDownGroup.setVisibility(View.GONE);


        //child data init
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

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
                                                bossData.put("bossAvatar", 0);
                                                bossData.put("bossIsDead", false);
                                                bossID = db.collection("boss").document().getId();
                                                db.collection("boss").document(bossID).set(bossData);
                                            } else {
                                                QuerySnapshot bossDocs = tasks.getResult();
                                                bossID = bossDocs.getDocuments().get(0).getId();
                                                bossDoc = db.collection("boss").document(bossID);
                                                bossAvatar = bossDocs.getDocuments().get(0).getLong("bossAvatar").intValue();
                                                bossIsDead = bossDocs.getDocuments().get(0).getBoolean("bossIsDead");
                                                List<Integer>bossImages = new ArrayList<>();
                                                bossImages.add(R.drawable.bucklerbossundamaged);
                                                bossImages.add(R.drawable.bookbossundamaged);

                                                if (bossAvatar == 0) {
                                                    monsterImage.setImageResource(bossImages.get(0));
                                                    monsterName.setText("SHIELD");
                                                    popupMonsterName.setText("SHIELD");
                                                    popupMonsterImage.setImageResource(R.drawable.bucklerbossdamaged);
                                                } else {
                                                    monsterImage.setImageResource(bossImages.get(1));
                                                    monsterName.setText("BOOK");
                                                    popupMonsterName.setText("BOOK");
                                                    popupMonsterImage.setImageResource(R.drawable.bookbossdamaged);
                                                }

                                                bossReq = bossDocs.getDocuments().get(0).getLong("bossReq").intValue();
                                                statReqStr.setText("STR: " + bossReq);
                                                statReqInt.setText("INT: " + bossReq);
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
                            //startBossTimer();


                            List<Integer>avatarImages = new ArrayList<>();
                            avatarImages.add(R.drawable.placeholderavatar5_framed_round);
                            avatarImages.add(R.drawable.placeholderavatar1_framed_round);
                            avatarImages.add(R.drawable.placeholderavatar2_framed_round);
                            avatarImages.add(R.drawable.placeholderavatar3_framed_round);
                            avatarImages.add(R.drawable.placeholderavatar4_framed_round);

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
        NavUtil.setNavigation(this, childBarStatsButton,ProgressionPage.class);
        //DropDowns
        NavUtil.setNavigation(this, navLogOut, Splash.class);
        NavUtil.setNavigation(this,navQuestPage, QuestManagement.class);
        navManageAdv.setEnabled(false); //Frontend make this greyed out or smth

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

        updateProgressBar(currentProgress);

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
                                final int[] damageSteps = {20, 40, 60, 80, 100};
                                final int[] currentStepIndex = {0};
                                final int delayMillis = 500;

                                Runnable damageRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (currentStepIndex[0] < damageSteps.length) {
                                            int damage = damageSteps[currentStepIndex[0]];
                                            currentProgress -= damage;
                                            updateProgressBar(currentProgress);
                                            currentStepIndex[0]++;
                                            handler.postDelayed(this, delayMillis);
                                        } else {
                                            DocumentReference bossRef = db.collection("boss").document(bossID);
                                            bossRef.update("bossIsDead", true);
                                            double prevChildStats = ((childStr + childInt) / 2.0);
                                            bossReq = (int) Math.round(prevChildStats + Math.pow(10, 1.3) * Math.log10(prevChildStats));
                                            docRef.update("floor", floorCount + 1);

                                            popupMonsterMessageText.setText("You have defeated me!");
                                            popupMonsterMessage.setVisibility(View.VISIBLE);
                                            bossAvatar = (bossAvatar == 0) ? 1 : 0;

                                            bossDoc.update("bossReq", bossReq, "bossAvatar", bossAvatar)
                                                    .addOnSuccessListener(aVoid -> {
                                                        if (bossAvatar == 0) {
                                                            monsterImage.setImageResource(R.drawable.bucklerbossundamaged);
                                                        } else {
                                                            monsterImage.setImageResource(R.drawable.bookbossundamaged);
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("BossAvatarUpdate", "Error updating bossAvatar", e);
                                                    });
                                        }

                                    }
                                };
                                handler.post(damageRunnable);
                            } else if ((childStr < bossReq && childInt < bossReq) || (childStr == bossReq && childInt < bossReq) || (childStr < bossReq && childInt == bossReq) || (childStr > bossReq && childInt < bossReq) || (childStr < bossReq && childInt > bossReq)) {
                                final double dmgIncrement = 0.2;
                                final int[] currentSteps = {0};
                                final int delayMillis = 500;
                                final double[] prevTotalDmg = {100.0}; // Initialize with 100.0

                                Runnable damageRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (currentSteps[0] < 5) { // Run for 5 steps
                                            double strDmg = ((double) childStr / bossReq);
                                            double intDmg = ((double) childInt / bossReq);
                                            double baseDmg = (strDmg * intDmg * prevTotalDmg[0]);
                                            double totalDmg = baseDmg * dmgIncrement;
                                            prevTotalDmg[0] = baseDmg; // Update for the next step
                                            currentProgress -= totalDmg;
                                            updateProgressBar(currentProgress);
                                            currentSteps[0]++;
                                            handler.postDelayed(this, delayMillis);
                                        } else {
                                            popupMonsterMessageText.setText("Come back when you're stronger!");
                                            popupMonsterButton.setText("Exit");
                                            NavUtil.setNavigation(WeeklyBoss.this, popupMonsterButton, ProgressionPage.class);
                                            popupMonsterMessage.setVisibility(View.VISIBLE);
                                        }
                                    }
                                };
                                handler.post(damageRunnable);
                            }
                        }
                    }
                });
        // Check if the child's stats are sufficient to damage the boss

    }
    private void startBossTimer() {
        // to milliseconds
        long ms = 7L * 24 * 3600000; // 7 days

        new CountDownTimer(ms, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSeconds = millisUntilFinished / 1000;
                long days = totalSeconds / (24 * 3600);
                long remainder = totalSeconds % (24 * 3600);
                long hours = remainder / 3600;
                remainder %= 3600;
                long minutes = remainder / 60;
                long seconds = remainder % 60;

                String timeLeftFormatted = String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
                popupMonsterMessageText.setText("Boss Timer: " + timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                if (!bossIsDead) {  // Timer expired and the boss hasn't been defeated
                    // Reduce the child's stats by 5
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String childId = user.getUid();
                        DocumentReference childRef = db.collection("users").document(childId);
                        childRef.update("childStr", FieldValue.increment(-5));
                        childRef.update("childInt", FieldValue.increment(-5));
                    }
                    popupMonsterMessageText.setText("Time's up! You lose!");
                }
            }


        }.start();
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

}