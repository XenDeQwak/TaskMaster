package com.taskmaster.appui.view.child;

import static android.view.View.GONE;

import android.app.Activity;
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
import com.taskmaster.appui.view.uimodule.ChildStatsTab;
import com.taskmaster.appui.view.uimodule.CosmeticItemTemplate.CosmeticItem;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
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

    private final List<CosmeticItem> allItems = Arrays.asList(
            new CosmeticItem(0, "Armorless", "High-level protection", 5, R.drawable.placeholderavatar5_framed_round),
            new CosmeticItem(1, "Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar1_framed_round),
            new CosmeticItem(2, "Red Armor", "High-level protection", 3, R.drawable.placeholderavatar2_framed_round),
            new CosmeticItem(3, "Crusader Armor", "High-level protection", 4, R.drawable.placeholderavatar3_framed_round),
            new CosmeticItem(4, "Copper Crusader Armor", "High-level protection", 5, R.drawable.placeholderavatar4_framed_round)
    );

    private ChildData childData;
    private RemainingTimer remainingTimer;
    private WeeklyBoss weeklyBoss;
    ChildStatsTab childStatsTab;


    // Below arent mine -gab

    private AppCompatButton fightButton;
    private AppCompatButton statReqStr;
    private AppCompatButton statReqInt;
    private AppCompatButton monsterName;
    private AppCompatButton popupMonsterButton;
    private ImageView popupMonsterImage,monsterImage;
    private Group popupMonsterMessage;
    private ProgressBar monsterHealthBar;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView timerTxt, timerTxtDays, popupMonsterMessageText, monsterHealthBarText;
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
        fightButton = findViewById(R.id.fightButton);
        popupMonsterButton = findViewById(R.id.popupMonsterButton);
        popupMonsterMessage = findViewById(R.id.popupMonsterMessage);
        popupMonsterMessageText = findViewById(R.id.popupMonsterMessageText);
        popupMonsterImage = findViewById(R.id.popupMonsterImage);
        monsterHealthBar = findViewById(R.id.monsterHealthBar);
        monsterHealthBarText = findViewById(R.id.monsterHealthBarText);
        monsterName = findViewById(R.id.monsterName);
        statReqStr = findViewById(R.id.statReqStr);
        statReqInt = findViewById(R.id.statReqInt);
        monsterImage = findViewById(R.id.monsterImage);

        // Set up
        currentUser = CurrentUser.getInstance();
        childData = currentUser.getUserData().getUserSnapshot().toObject(ChildData.class);
        childData.updateData(cd -> {

            childStatsTab = findViewById(R.id.ChildStatsTab);
            childStatsTab.getChildStatsName().setText(cd.getUsername());
            childStatsTab.getChildStatsFloor().setText("Floor: " + cd.getFloor());
            childStatsTab.getChildStatsAvatarImage().setImageResource(cd.getOwnedItems().get(cd.getAvatar()).getImageResId());
            childStatsTab.setProgressionNav(this);

            // Boss Setup
            weeklyBoss = new WeeklyBoss();
            weeklyBoss.setUpWeeklyBoss(cd, weeklyBossData -> {

                currBossAvatar = weeklyBoss.getWeeklyBossData().getBossAvatar();
                monsterName.setText(currBossAvatar.getName());

                statReqStr.setText("STR: " + weeklyBossData.getStrengthRequired());
                statReqInt.setText("INT: " + weeklyBossData.getIntelligenceRequired()   );

                Duration duration = Duration.between(DateTimeUtil.getDateTimeNow(), DateTimeUtil.getDateTimeFromEpochSecond(weeklyBossData.getRespawnDate()));
                weeklyBossData.setAlive(duration.isZero() || duration.isNegative());
                if (weeklyBossData.isAlive()) {
                    weeklyBossData.setHealth(100);
                    monsterImage.setImageResource(currBossAvatar.getUndamagedImageResId());
                    fightButton.setOnClickListener(e -> bossFight(cd, weeklyBossData));
                } else {
                    weeklyBossData.setHealth(0);
                    monsterImage.setImageResource(currBossAvatar.getDamagedImageResId());
                    fightButton.setAlpha(0.70f);
                    fightButton.setText("Defeated");
                    fightButton.setOnClickListener(e-> showPopup("Fight the Next BossAvatar\nNext Week","Okay"));
                    // Timer Setup
                    //System.out.println("BOSS IS DEAD");
                    remainingTimer = new RemainingTimer(DateTimeUtil.getDateTimeFromEpochSecond(weeklyBossData.getRespawnDate()), "HHh MMm SSs");
                    remainingTimer.setOnTick(timer -> {
                        Duration d = Duration.between(DateTimeUtil.getDateTimeNow(), DateTimeUtil.getDateTimeFromEpochSecond(weeklyBossData.getRespawnDate()));
                        System.out.println(d.toSeconds());
                        Activity activity = (Activity) timerTxtDays.getContext();
                        activity.runOnUiThread(() -> {
                            timerTxtDays.setText(DateTimeUtil.formatDuration(d, "DD Days"));
                            timerTxt.setText(DateTimeUtil.formatDuration(d, "HHh MMMm SSSs"));
                            if (d.isZero() || d.isNegative()) {
                                timerTxt.setText(DateTimeUtil.formatDuration(d, "0h 00m 00s"));
                                timer.setPastDue(true);
                            }
                        });
                    });
                    remainingTimer.setOnFinish(timer -> {
                        weeklyBossData.getAdventurerReference().get()
                                .addOnCompleteListener(task -> {
                                    ChildData cData = new ChildData(task.getResult().toObject(ChildData.class));
                                    cData.setWeeklyBossReference(null);
                                    cData.uploadData();
                                });
                        Activity activity = (Activity) timerTxtDays.getContext();
                        activity.runOnUiThread(() -> NavUtil.instantNavigation(this, this.getClass()));
                    });
                    DateTimeUtil.addTimer(remainingTimer);
                }

                updateBossHealthBar(weeklyBossData.getHealth());
                weeklyBossData.uploadData();
            });
        });

        popupMonsterButton.setOnClickListener(v -> popupMonsterMessage.setVisibility(GONE));
    }

    private void updateBossHealthBar(int progress) {
        monsterHealthBar.setProgress(progress);
        monsterHealthBarText.setText(progress + "/100");
    }

    private void showPopup(String message, String buttonText){
        if(weeklyBoss.getWeeklyBossData().isAlive()){
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
        int cStr = childData.getStrength(), cInt = childData.getIntelligence();
        int rStr = weeklyBossData.getStrengthRequired(), rInt = weeklyBossData.getIntelligenceRequired();

        bossFightTask = () -> {
            Boolean defeated = (cStr >= rStr && cInt >= rInt);
            bossDefeated.set(defeated);
            // Insert Animation Below
        };

        // Await the Boss Fight to complete
        bossFightExecutorService.execute(bossFightTask);
        bossFightExecutorService.shutdown();
        try {
            Boolean bossFightTaskIsSuccessful = bossFightExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            if (bossFightTaskIsSuccessful) {
                if (bossDefeated.get()) {
                    weeklyBossData.setRespawnDate(DateTimeUtil.getDateTimeNow().plusWeeks(1).toEpochSecond());
                    //weeklyBossData.setRespawnDate(DateTimeUtil.getDateTimeNow().plusSeconds(30).toEpochSecond());
                    weeklyBossData.uploadData();
                    childData.setFloor(childData.getFloor()+1);
                    childData.uploadData();
                    showPopup("You have defeated me!","Great!");
                    NavUtil.instantNavigation(this, this.getClass());
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