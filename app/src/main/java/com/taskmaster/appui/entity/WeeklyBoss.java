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
        if (childData.getWeeklyBossReference() == null) {
            //BossAvatar newBossAvatar = bossAvatars[childData.getFloor() % bossAvatars.length];
            weeklyBossData = WeeklyBossData.newEmptyWeeklyBoss();
            weeklyBossData.setAlive(true);
            weeklyBossData.setHealth(100);
            weeklyBossData.setBossAvatar(new BossAvatar("Buckler", R.drawable.bucklerbossundamaged_sprite, R.drawable.bucklerbossdamaged_sprite));
            weeklyBossData.setStrengthRequired(childData.getFloor());
            weeklyBossData.setIntelligenceRequired(childData.getFloor());
            weeklyBossData.setPenalty(Math.max(1, childData.getQuestsCompleted()/10));
            weeklyBossData.setRespawnDate(0);
            weeklyBossData.setAdventurerReference(childData.getChildReference());
            childData.getParentReference().collection("Bosses").add(weeklyBossData)
                    .addOnCompleteListener(dr -> {
                        childData.setWeeklyBossReference(dr.getResult());
                        weeklyBossData.setWeeklyBossReference(dr.getResult());
                        weeklyBossData.uploadData();
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
}
