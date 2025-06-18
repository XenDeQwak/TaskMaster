package com.taskmaster.appui.view.child;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Group;

import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.util.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeklyBoss extends AppCompatActivity {
    AppCompatButton fightButton, statReqStr, statReqInt, monsterName, childBarStatsButton, popupMonsterButton, childBarFloorCount;
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
    TextView timerTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.weekly_boss);
        // hide status bar and nav bar
        NavUtil.hideSystemBars(this);

        // hooks
        {
            dropDownGroup = findViewById(R.id.dropdownGroup);
            childBarStatsButton = findViewById(R.id.childBarStatsButton);
            rootLayout = findViewById(R.id.statContainer);
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
            timerTxt = findViewById(R.id.TimerTxt);
        }
        DropdownUtil.dropdownSetup(this, rootLayout);
        NavUtil.setNavigation(this, childBarStatsButton, ProgressionPage.class);
        TimeUtil timeUtil = TimeUtil.getInstance();
        timeUtil.setupTimer();
        timeUtil.startTimer(new TimeUtil.TimerListener() {
            @Override
            public void onTick(long timeRemaining) {
                setTime(timeRemaining);
            }

            @Override
            public void onFinish() {
                //if(bossDefeated){
                //    bossDefeated = false; goodjob or something floor advance
                //}else{
                //    popupMonsterMessageText.setText("Time's up! You lose!");
                //        childRef.update("childStr", FieldValue.increment(-5));
                //        childRef.update("childInt", FieldValue.increment(-5));
                //}

                timeUtil.setupTimer();
            }
        });
        // hide popup
        popupMonsterMessage.setVisibility(View.GONE);


        //child data init
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Map<String, Object> bossData = new HashMap<>();

        User user = User.getInstance();
        DocumentSnapshot documentSnapshot = user.getDocumentSnapshot();

//        db.collection("users").document(userId).get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot childDocument = task.getResult();
//                        if (childDocument.exists()) {
//                            db.collection("boss").get()
//                                    .addOnCompleteListener(tasks -> {
//                                        if (tasks.isSuccessful()) {
//                                            if (tasks.getResult().isEmpty()) {
//                                                bossData.put("bossReq", bossReq);
//                                                bossData.put("bossAvatar", 0);
//                                                bossData.put("bossIsDead", false);
//                                                bossID = db.collection("boss").document().getId();
//                                                db.collection("boss").document(bossID).set(bossData);
//                                            } else {
//                                                QuerySnapshot bossDocs = tasks.getResult();
//                                                bossID = bossDocs.getDocuments().get(0).getId();
//                                                bossDoc = db.collection("boss").document(bossID);
//                                                bossAvatar = bossDocs.getDocuments().get(0).getLong("bossAvatar").intValue();
//                                                bossIsDead = bossDocs.getDocuments().get(0).getBoolean("bossIsDead");
//                                                List<Integer>bossImages = new ArrayList<>();
//                                                bossImages.add(R.drawable.bucklerbossundamaged);
//                                                bossImages.add(R.drawable.bookbossundamaged);
//
//                                                if (bossAvatar == 0) {
//                                                    monsterImage.setImageResource(bossImages.get(0));
//                                                    monsterName.setText("SHIELD");
//                                                    popupMonsterName.setText("SHIELD");
//                                                    popupMonsterImage.setImageResource(R.drawable.bucklerbossdamaged);
//                                                } else {
//                                                    monsterImage.setImageResource(bossImages.get(1));
//                                                    monsterName.setText("BOOK");
//                                                    popupMonsterName.setText("BOOK");
//                                                    popupMonsterImage.setImageResource(R.drawable.bookbossdamaged);
//                                                }
//
//                                                bossReq = bossDocs.getDocuments().get(0).getLong("bossReq").intValue();
//                                                statReqStr.setText("STR: " + bossReq);
//                                                statReqInt.setText("INT: " + bossReq);
//                                            }
//                                        }
//                                    });
//
//                            childStr = childDocument.getLong("childStr").intValue();
//                            childInt = childDocument.getLong("childInt").intValue();
//                            floorCount = childDocument.getLong("floor").intValue();
//                            childAvatar = childDocument.getLong("childAvatar").intValue();
//                            childBarGroup.setVisibility(View.VISIBLE);
//                            childBarName.setText(childDocument.getString("username"));
//                            childBarFloorCount.setText("Floor " + floorCount);
//                            //startBossTimer();
//
//
//                            List<Integer>avatarImages = new ArrayList<>();
//                            avatarImages.add(R.drawable.placeholderavatar5_framed_round);
//                            avatarImages.add(R.drawable.placeholderavatar1_framed_round);
//                            avatarImages.add(R.drawable.placeholderavatar2_framed_round);
//                            avatarImages.add(R.drawable.placeholderavatar3_framed_round);
//                            avatarImages.add(R.drawable.placeholderavatar4_framed_round);
//
//                            childBarAvatar.setImageResource(avatarImages.get(childAvatar));
//                            Log.d("FLOOR", "FLOOR: " + floorCount);
//                            DocumentReference docRef = db.collection("users").document(userId);
//                            fightButton.setOnClickListener(e -> {
//                                bossFight(docRef, bossID, bossDoc);
//                            });
//                        } else {
//                            Log.d("DEBUG", "PARENT DOCUMENT DOES NOT EXIST");
//                        }
//                    } else {
//                        Log.d("DEBUG", "Error getting parent document", task.getException());
//                    }
//                });


        updateProgressBar(currentProgress);
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
    public void setTime(long ms){
        Long hours = ms / 3600000;
        Long minutes = (ms % 3600000) / 60000;
        Long seconds = ((ms % 3600000) % 60000) / 1000;
        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        runOnUiThread(() -> timerTxt.setText(timeFormatted));
    }

}