package com.taskmaster.appui.entity;

import com.google.firebase.firestore.DocumentReference;
import com.taskmaster.appui.R;
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.data.WeeklyBossData;
import com.taskmaster.appui.util.GenericCallback;

import java.lang.annotation.Documented;

public class WeeklyBoss {

    WeeklyBossData weeklyBossData;
    RemainingTimer remainingTimer;

    public WeeklyBoss () {}

    public WeeklyBoss (WeeklyBossData weeklyBossData) {
        this.weeklyBossData = weeklyBossData;
    }

    public void setUpWeeklyBoss (ChildData childData, GenericCallback<WeeklyBossData> callback) {
        System.out.println();
        if (childData.getWeeklyBossReference() == null) {
            BossAvatar newBossAvatar = WeeklyBossData.bossAvatars[childData.getFloor() % WeeklyBossData.bossAvatars.length];
            weeklyBossData = WeeklyBossData.newEmptyWeeklyBoss();
            weeklyBossData.setAlive(true);
            weeklyBossData.setHealth(100);
            weeklyBossData.setBossAvatar(newBossAvatar);
            weeklyBossData.setStrengthRequired(childData.getFloor());
            weeklyBossData.setIntelligenceRequired(childData.getFloor());
            weeklyBossData.setPenalty(Math.max(1, childData.getQuestsCompleted()/10));
            weeklyBossData.setRespawnDate(0);
            weeklyBossData.setAdventurerReference(childData.getChildReference());
            childData.getParentReference().collection("Bosses").add(weeklyBossData)
                    .addOnCompleteListener(dr -> {
                        childData.setWeeklyBossReference(dr.getResult());
                        childData.uploadData();
                        weeklyBossData.setWeeklyBossReference(dr.getResult());
                        weeklyBossData.uploadData();
                        callback.onCallback(weeklyBossData);
                    });
        } else {
            childData.getWeeklyBossReference().get()
                    .addOnCompleteListener(task -> {
                        weeklyBossData = new WeeklyBossData(task.getResult().toObject(WeeklyBossData.class));
                        callback.onCallback(weeklyBossData);
                    });
        }
    }

    public WeeklyBossData getWeeklyBossData() {
        return weeklyBossData;
    }

    public void setWeeklyBossData(WeeklyBossData weeklyBossData) {
        this.weeklyBossData = weeklyBossData;
    }

    public RemainingTimer getRemainingTimer() {
        return remainingTimer;
    }

    public void setRemainingTimer(RemainingTimer remainingTimer) {
        this.remainingTimer = remainingTimer;
    }
}
