package com.taskmaster.appui.data;

import com.google.firebase.firestore.DocumentReference;
import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.BossAvatar;
import com.taskmaster.appui.util.GenericCallback;

import java.util.Objects;

public class WeeklyBossData {

    public static final BossAvatar[] bossAvatars = new BossAvatar[]{
            new BossAvatar("Buckler", R.drawable.bucklerbossundamaged_sprite, R.drawable.bucklerbossdamaged_sprite),
            new BossAvatar("Book", R.drawable.bookbossundamaged_sprite, R.drawable.bookbossdamaged_sprite),
            new BossAvatar("Glove", R.drawable.gloveboss_sprite, R.drawable.glovebossdamaged_sprite),
            new BossAvatar("Spirit Animal", R.drawable.spiritanimalboss, R.drawable.spiritanimalbossdamaged),
            new BossAvatar("Witch Hat", R.drawable.witchhatboss_sprite, R.drawable.witchhatbossdamaged_sprite)
    };

    private static final WeeklyBossData EmptyWeeklyBoss = new WeeklyBossData (
            0,
            0,
            0,
            0,
            0,
            false,
            0L,
            null,
            null,
            new BossAvatar("default", 0, 0)
    );
    public static WeeklyBossData newEmptyWeeklyBoss () {
        return EmptyWeeklyBoss;
    }

    int health, strengthRequired, intelligenceRequired, penalty;
    boolean isAlive;
    long respawnDate;
    DocumentReference weeklyBossReference, adventurerReference;
    BossAvatar bossAvatar;

    public WeeklyBossData () {}

    public void uploadData () {
        weeklyBossReference.set(this).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) task.getException().printStackTrace();
        });
    }
    public void updateData (GenericCallback<WeeklyBossData> callback) {
        weeklyBossReference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) task.getException().printStackTrace();
            else {
                updateObject(Objects.requireNonNull(task.getResult().toObject(WeeklyBossData.class)));
                callback.onCallback(this);
            }
        });
    }

    private void updateObject (WeeklyBossData weeklyBossData) {
        this.health = weeklyBossData.getHealth();
        this.strengthRequired = weeklyBossData.getStrengthRequired();
        this.intelligenceRequired = weeklyBossData.getIntelligenceRequired();
        this.penalty = weeklyBossData.getPenalty();
        this.isAlive = weeklyBossData.isAlive();
        this.respawnDate = weeklyBossData.getRespawnDate();
        this.weeklyBossReference = weeklyBossData.getWeeklyBossReference();
        this.adventurerReference = weeklyBossData.getAdventurerReference();
        this.bossAvatar = weeklyBossData.getBossAvatar();
    }

    public WeeklyBossData (WeeklyBossData weeklyBossData) {
        updateObject(weeklyBossData);
    }

    public WeeklyBossData(int health, int strengthRequired, int intelligenceRequired, int penalty, int bossAvatarIndex, boolean isAlive, long respawnDate, DocumentReference weeklyBossReference, DocumentReference adventurerReference, BossAvatar bossAvatar) {
        this.health = health;
        this.strengthRequired = strengthRequired;
        this.intelligenceRequired = intelligenceRequired;
        this.penalty = penalty;
        this.isAlive = isAlive;
        this.respawnDate = respawnDate;
        this.weeklyBossReference = weeklyBossReference;
        this.adventurerReference = adventurerReference;
        this.bossAvatar = bossAvatar;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrengthRequired() {
        return strengthRequired;
    }

    public void setStrengthRequired(int strengthRequired) {
        this.strengthRequired = strengthRequired;
    }

    public int getIntelligenceRequired() {
        return intelligenceRequired;
    }

    public void setIntelligenceRequired(int intelligenceRequired) {
        this.intelligenceRequired = intelligenceRequired;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public long getRespawnDate() {
        return respawnDate;
    }

    public void setRespawnDate(long respawnDate) {
        this.respawnDate = respawnDate;
    }

    public DocumentReference getWeeklyBossReference() {
        return weeklyBossReference;
    }

    public void setWeeklyBossReference(DocumentReference weeklyBossReference) {
        this.weeklyBossReference = weeklyBossReference;
    }

    public DocumentReference getAdventurerReference() {
        return adventurerReference;
    }

    public void setAdventurerReference(DocumentReference adventurerReference) {
        this.adventurerReference = adventurerReference;
    }

    public BossAvatar getBossAvatar() {
        return bossAvatar;
    }

    public void setBossAvatar(BossAvatar bossAvatar) {
        this.bossAvatar = bossAvatar;
    }
}
