package com.taskmaster.appui.view.child;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Group;
import com.google.firebase.firestore.FieldValue;
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.entity.Boss;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.entity.RemainingTimer;
import com.taskmaster.appui.util.*;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.R;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class WeeklyBoss extends ChildPage {

    ChildData childData;
    RemainingTimer remainingTimer, remainingTimerDays;

    // Below arent mine -gab

    private AppCompatButton fightButton;
    private AppCompatButton statReqStr;
    private AppCompatButton statReqInt;
    private AppCompatButton monsterName;
    private AppCompatButton popupMonsterButton;
    private AppCompatButton childBarFloorCount;
    private ImageView popupMonsterImage,childBarAvatar,monsterImage;
    private Group popupMonsterMessage;
    private ProgressBar monsterHealthBar;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView childBarName,timerTxt,timerTxtDays,popupMonsterMessageText, monsterHealthBarText;
    private CurrentUser currentUser;
    private Boss currBoss;
    private int penalty;
    private List<Integer> avatarImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_boss);
        // hide status bar and nav bar
        NavUtil.hideSystemBars(this);
        initNavigationMenu(this, WeeklyBoss.class);

        timerTxtDays = findViewById(R.id.TimerTxtDays);
        timerTxt = findViewById(R.id.TimerTxt);
        AppCompatButton childBarStatsButton = findViewById(R.id.childBarStatsButton);
        childBarName = findViewById(R.id.childBarName);
        childBarAvatar = findViewById(R.id.childBarAvatar);
        fightButton = findViewById(R.id.fightButton);
        popupMonsterButton = findViewById(R.id.popupMonsterButton);
        popupMonsterMessage = findViewById(R.id.popupMonsterMessage);
        popupMonsterMessageText = findViewById(R.id.popupMonsterMessageText);
        popupMonsterImage = findViewById(R.id.popupMonsterImage);
        monsterHealthBar = findViewById(R.id.monsterHealthBar);
        monsterHealthBarText = findViewById(R.id.monsterHealthBarText);
        childBarFloorCount = findViewById(R.id.childBarFloorCount);
        monsterName = findViewById(R.id.monsterName);
        statReqStr = findViewById(R.id.statReqStr);
        statReqInt = findViewById(R.id.statReqInt);
        monsterImage = findViewById(R.id.monsterImage);

        NavUtil.setNavigation(this, childBarStatsButton, ProgressionPage.class);

        currentUser = CurrentUser.getInstance();
        childData = currentUser.getUserData().getUserSnapshot().toObject(ChildData.class);
        childData.updateData(cd -> {
            remainingTimer = new RemainingTimer(DateTimeUtil.getDateTimeFromEpochSecond(cd.getWeeklyBossRespawnDate()), "dd:hh:mm:ss");
            remainingTimer.setTextView(timerTxt);
            remainingTimerDays = new RemainingTimer(DateTimeUtil.getDateTimeFromEpochSecond(cd.getWeeklyBossRespawnDate()), "dd Days");
            remainingTimerDays.setTextView(timerTxtDays);
            DateTimeUtil.addTimer(remainingTimer);
            DateTimeUtil.addTimer(remainingTimerDays);

            setUpBoss(cd);
            setUpAvatar(cd);
        });

        popupMonsterButton.setOnClickListener(v -> popupMonsterMessage.setVisibility(View.GONE));
    }


    private void setUpAvatar(ChildData cd){
        avatarImages = new ArrayList<>();
        avatarImages.add(R.drawable.placeholderavatar5_framed_round);
        avatarImages.add(R.drawable.placeholderavatar1_framed_round);
        avatarImages.add(R.drawable.placeholderavatar2_framed_round);
        avatarImages.add(R.drawable.placeholderavatar3_framed_round);
        avatarImages.add(R.drawable.placeholderavatar4_framed_round);

        childBarName.setText(cd.getUsername());
        childBarAvatar.setImageResource(avatarImages.get(cd.getAvatar()));
    }

    private void setUpBoss(ChildData cd){
        int floor = cd.getFloor();
        childBarFloorCount.setText("Floor: " + floor);

        Boss[] bosses = new Boss[]{
                new Boss("Buckler", R.drawable.bucklerbossundamaged_sprite, R.drawable.bucklerbossdamaged_sprite),
                new Boss("Book", R.drawable.bookbossundamaged_sprite, R.drawable.bookbossdamaged_sprite),
                new Boss("Glove", R.drawable.gloveboss_sprite, R.drawable.glovebossdamaged_sprite),
                new Boss("Spirit Animal", R.drawable.spiritanimalboss, R.drawable.spiritanimalbossdamaged),
                new Boss("Witch Hat", R.drawable.witchhatboss_sprite, R.drawable.witchhatbossdamaged_sprite)
        };

        int index = (floor % bosses.length);
        currBoss = bosses[index];
        statReqStr.setText("STR: " + floor);
        statReqInt.setText("INT: " + floor);

        int Strength = cd.getStrength();
        int Intelligence = cd.getIntelligence();
        double childStats = (Strength+Intelligence)/2;
        penalty = (int) Math.max((childStats*0.1),1); //10% of stats

        if(!remainingTimer.isPastDue()){
            updateProgressBar(0);
            monsterImage.setImageResource(currBoss.getUndamagedImageResId());
            int childStr = cd.getStrength();
            int childInt = cd.getIntelligence();
            fightButton.setOnClickListener(e -> bossFight(childStr, childInt, floor));
        }else{
            updateProgressBar(100);
            monsterImage.setImageResource(currBoss.getDamagedImageResId());
            fightButton.setAlpha(0.70f);
            fightButton.setText("Defeated");
            fightButton.setOnClickListener(e-> showPopup("Fight the Next Boss\nNext Week","Okay"));
        }
        monsterName.setText(currBoss.getName());
    }

    private void updateProgressBar(int progress) {
        progress = 100-progress;
        monsterHealthBar.setProgress(progress);
        monsterHealthBarText.setText(progress + "/100");
    }

    private void showPopup(String message, String buttonText){
        if(remainingTimer.isPastDue()){
            popupMonsterImage.setImageResource(currBoss.getUndamagedImageResId());
        }else{
            popupMonsterImage.setImageResource(currBoss.getDamagedImageResId());
        }
        popupMonsterMessageText.setText(message);
        popupMonsterButton.setText(buttonText);
        popupMonsterMessage.setVisibility(View.VISIBLE);
    }

    private void bossFight(int strength, int intelligence, int floor) {
        int[] i = {5};
        int[] damage={0};
        if (strength >= floor && intelligence >= floor) {
            Runnable damageRunnable = new Runnable() {
                @Override
                public void run() {
                    if (i[0]>0) {
                        damage[0] +=20;
                        i[0]--;
                        updateProgressBar(damage[0]);
                        handler.postDelayed(this, 500);
                    } else {
                        ZonedDateTime zdt = DateTimeUtil.getDateTimeFromEpochSecond(childData.getWeeklyBossRespawnDate());
                        zdt.plusWeeks(1);
                        childData.setWeeklyBossRespawnDate(zdt.toEpochSecond());
                        childData.uploadData();
                        monsterImage.setImageResource(currBoss.getDamagedImageResId());
                        showPopup("You have defeated me!","Great!");
                        //attachRestartHandler();
                    }
                }
            };
            handler.post(damageRunnable);
        } else{
            Runnable damageRunnable = new Runnable() {
                @Override
                public void run() {
                    if (i[0]>0) {
                        damage[0] +=5;
                        i[0]--;
                        updateProgressBar(damage[0]);
                        handler.postDelayed(this, 500);
                    } else {
                        showPopup("Come Back When You Are Stronger","Okay");
                    }
                }
            };
            handler.post(damageRunnable);
        }
    }
}