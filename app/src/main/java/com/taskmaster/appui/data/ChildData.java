package com.taskmaster.appui.data;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.taskmaster.appui.R;
import com.taskmaster.appui.util.GenericCallback;
import com.taskmaster.appui.view.uimodule.CosmeticItemTemplate.CosmeticItem;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChildData {

    private static ChildData EmptyChildData = new ChildData(
            "uid",
            "email",
            "password",
            "username",
            "child",
            "parentUID",
            null,
            null,
            null,
            0,
            1,
            1,
            1,
            0,
            0,
            Arrays.asList(new CosmeticItem(0, "Armorless", "High-level protection", 5, R.drawable.placeholderavatar5_framed_round))
    );

    public static ChildData newEmptyChildData() {
        return EmptyChildData;
    }

    String uid;
    String email, password, username, role;
    String parentUID;
    DocumentReference childReference, parentReference, weeklyBossReference;
    int avatar, strength, intelligence, floor, gold, questsCompleted;
    List<CosmeticItem> ownedItems;

    public ChildData () {}

    public void uploadData () {
        childReference.set(this).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) task.getException().printStackTrace();
        });
    }
    public void updateData (GenericCallback<ChildData> callback) {
        childReference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) task.getException().printStackTrace();
            else {
                updateObject(Objects.requireNonNull(task.getResult().toObject(ChildData.class)));
                callback.onCallback(this);
            }
        });
    }

    private void updateObject (ChildData childData) {
        this.uid = childData.getUid();
        this.email = childData.getEmail();
        this.password = childData.getPassword();
        this.username = childData.getUsername();
        this.role = childData.getRole();
        this.parentUID = childData.getParentUID();
        this.childReference = childData.getChildReference();
        this.parentReference = childData.getParentReference();
        this.weeklyBossReference = childData.getWeeklyBossReference();
        this.avatar = childData.getAvatar();
        this.strength = childData.getStrength();
        this.intelligence = childData.getIntelligence();
        this.floor = childData.getFloor();
        this.gold = childData.getGold();
        this.questsCompleted = childData.getQuestsCompleted();
        this.ownedItems = childData.getOwnedItems();
    }

    public ChildData (ChildData childData) {
        updateObject(childData);
    }

    public ChildData(String uid, String email, String password, String username, String role, String parentUID, DocumentReference childReference, DocumentReference parentReference, DocumentReference weeklyBossReference, int avatar, int strength, int intelligence, int floor, int gold, int questsCompleted, List<CosmeticItem> ownedItems) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.parentUID = parentUID;
        this.childReference = childReference;
        this.parentReference = parentReference;
        this.weeklyBossReference = weeklyBossReference;
        this.avatar = avatar;
        this.strength = strength;
        this.intelligence = intelligence;
        this.floor = floor;
        this.gold = gold;
        this.questsCompleted = questsCompleted;
        this.ownedItems = ownedItems;
    }

    public static ChildData getEmptyChildData() {
        return EmptyChildData;
    }

    public static void setEmptyChildData(ChildData emptyChildData) {
        EmptyChildData = emptyChildData;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getParentUID() {
        return parentUID;
    }

    public void setParentUID(String parentUID) {
        this.parentUID = parentUID;
    }

    public DocumentReference getChildReference() {
        return childReference;
    }

    public void setChildReference(DocumentReference childReference) {
        this.childReference = childReference;
    }

    public DocumentReference getParentReference() {
        return parentReference;
    }

    public void setParentReference(DocumentReference parentReference) {
        this.parentReference = parentReference;
    }

    public DocumentReference getWeeklyBossReference() {
        return weeklyBossReference;
    }

    public void setWeeklyBossReference(DocumentReference weeklyBossReference) {
        this.weeklyBossReference = weeklyBossReference;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getQuestsCompleted() {
        return questsCompleted;
    }

    public void setQuestsCompleted(int questsCompleted) {
        this.questsCompleted = questsCompleted;
    }

    public List<CosmeticItem> getOwnedItems() {
        return ownedItems;
    }

    public void setOwnedItems(List<CosmeticItem> ownedItems) {
        this.ownedItems = ownedItems;
    }
}
