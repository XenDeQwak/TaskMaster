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

import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.data.WeeklyBossData;
import com.taskmaster.appui.entity.BossAvatar;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.entity.RemainingTimer;
import com.taskmaster.appui.entity.WeeklyBoss;
import com.taskmaster.appui.util.*;
import com.taskmaster.appui.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ChildPageWeeklyBoss extends ChildPage {

    private final Integer[] avatarImages = {
            R.drawable.placeholderavatar5_framed_round,
            R.drawable.placeholderavatar1_framed_round,
            R.drawable.placeholderavatar2_framed_round,
            R.drawable.placeholderavatar3_framed_round,
            R.drawable.placeholderavatar4_framed_round
    };

    private ChildData childData;
    private RemainingTimer remainingTimer, remainingTimerDays;
    private WeeklyBoss weeklyBoss;

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
    private BossAvatar currBossAvatar;
    private int penalty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_boss);
        // hide status bar and nav bar
        NavUtil.hideSystemBars(this);
        initNavigationMenu(this, ChildPageWeeklyBoss.class);

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

        // Set up
        currentUser = CurrentUser.getInstance();
        childData = currentUser.getUserData().getUserSnapshot().toObject(ChildData.class);
        childData.updateData(cd -> {
            // Boss Setup
            weeklyBoss = new WeeklyBoss();
            weeklyBoss.setUpWeeklyBoss(cd, wbd -> {
                wbd.updateData(weeklyBossData -> {
                    currBossAvatar = new BossAvatar("Buckler", R.drawable.bucklerbossundamaged_sprite, R.drawable.bucklerbossdamaged_sprite);
                    monsterName.setText(currBossAvatar.getName());
                    updateBossHealthBar(weeklyBossData.getHealth());

                    if (weeklyBossData.isAlive()) {
                        monsterImage.setImageResource(currBossAvatar.getUndamagedImageResId());
                        fightButton.setOnClickListener(e -> bossFight(cd, weeklyBossData));
                    } else {
                        monsterImage.setImageResource(currBossAvatar.getDamagedImageResId());
                        fightButton.setAlpha(0.70f);
                        fightButton.setText("Defeated");
                        fightButton.setOnClickListener(e-> showPopup("Fight the Next BossAvatar\nNext Week","Okay"));
                    }

                    remainingTimer = new RemainingTimer(DateTimeUtil.getDateTimeFromEpochSecond(wbd.getRespawnDate()), "HHh MMm SSs");
                    remainingTimerDays = new RemainingTimer(DateTimeUtil.getDateTimeFromEpochSecond(wbd.getRespawnDate()), "DD Days");
                    DateTimeUtil.addTimer(remainingTimer, remainingTimerDays);
                });
            });

            childBarName.setText(cd.getUsername());
            childBarAvatar.setImageResource(avatarImages[cd.getAvatar()]);
        });

        popupMonsterButton.setOnClickListener(v -> popupMonsterMessage.setVisibility(View.GONE));
    }

    private void updateBossHealthBar(int progress) {
        monsterHealthBar.setProgress(progress);
        monsterHealthBarText.setText(progress + "/100");
    }

    private void showPopup(String message, String buttonText){
        if(remainingTimer.isPastDue()){
            popupMonsterImage.setImageResource(currBossAvatar.getUndamagedImageResId());
        }else{
            popupMonsterImage.setImageResource(currBossAvatar.getDamagedImageResId());
        }
        popupMonsterMessageText.setText(message);
        popupMonsterButton.setText(buttonText);
        popupMonsterMessage.setVisibility(View.VISIBLE);
    }

    private void bossFight(ChildData childData, WeeklyBossData weeklyBossData) {
        ScheduledExecutorService bossFightExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runnable bossFightTask;
        AtomicReference<Boolean> bossDefeated = new AtomicReference<>();
        if (
                childData.getStrength() >= weeklyBossData.getStrengthRequired()
                && childData.getIntelligence() >= weeklyBossData.getIntelligenceRequired()
        ) {
            // Win
            bossFightTask = () -> {
                // insert animation here
                bossDefeated.set(true);
            };
        } else {
            // Lose
            bossFightTask = () -> {
                // insert animation here
                bossDefeated.set(false);
            };
        }

        // Await the Boss Fight to complete
        bossFightExecutorService.execute(bossFightTask);
        bossFightExecutorService.shutdown();
        try {
            Boolean bossFightTaskIsSuccessful = bossFightExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            if (bossFightTaskIsSuccessful) {
                if (bossDefeated.get()) {
                    showPopup("You have defeated me!","Great!");
                } else {
                    showPopup("Come Back When You Are Stronger","Okay");
                }
            } else {
                showPopup("Error has occurred. Boss Fight did\nnot finish in 10 seconds.", "Okay");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}