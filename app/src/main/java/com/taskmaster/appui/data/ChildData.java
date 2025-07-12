package com.taskmaster.appui.data;

import com.google.firebase.firestore.DocumentReference;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChildData {

    private static ChildData EmptyChildData = new ChildData(
            "child",
            "email",
            "password",
            "username",
            "parentUID",
            null,
            null,
            0,
            0,
            0,
            1,
            0,
            0,
            Arrays.asList("Armorless"),
            0
    );

    public static ChildData newEmptyChildData() {
        return EmptyChildData;
    }

    String role;
    String email, password, username;
    String parentUID;
    DocumentReference childReference, parentReference;
    int avatar, strength, intelligence, floor, gold, questsCompleted;
    List<String> ownedItems;
    long weeklyBossRespawnDate;

    public ChildData () {}


    public void uploadData () {
        childReference.set(this).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) task.getException().printStackTrace();
        });
    }
    public void updateData () {
        childReference.get().addOnCompleteListener(task -> {
            updateObject(Objects.requireNonNull(task.getResult().toObject(ChildData.class)));
        }).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) task.getException().printStackTrace();
        });
    }

    private void updateObject (ChildData childData) {
        this.role = childData.getRole();
        this.email = childData.getEmail();
        this.password = childData.getPassword();
        this.username = childData.getUsername();
        this.parentUID = childData.getParentUID();
        this.childReference = childData.getChildReference();
        this.parentReference = childData.getParentReference();
        this.avatar = childData.getAvatar();
        this.strength = childData.getStrength();
        this.intelligence = childData.getIntelligence();
        this.floor = childData.getFloor();
        this.gold = childData.getGold();
        this.questsCompleted = childData.getQuestsCompleted();
        this.ownedItems = childData.getOwnedItems();
        this.weeklyBossRespawnDate = childData.getWeeklyBossRespawnDate();
    }

    public ChildData (ChildData childData) {
        updateObject(childData);
    }

    public ChildData(String role, String email, String password, String username, String parentUID, DocumentReference childReference, DocumentReference parentReference, int avatar, int strength, int intelligence, int floor, int gold, int questsCompleted, List<String> ownedItems, long weeklyBossRespawnDate) {
        this.role = role;
        this.email = email;
        this.password = password;
        this.username = username;
        this.parentUID = parentUID;
        this.childReference = childReference;
        this.parentReference = parentReference;
        this.avatar = avatar;
        this.strength = strength;
        this.intelligence = intelligence;
        this.floor = floor;
        this.gold = gold;
        this.questsCompleted = questsCompleted;
        this.ownedItems = ownedItems;
        this.weeklyBossRespawnDate = weeklyBossRespawnDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public List<String> getOwnedItems() {
        return ownedItems;
    }

    public void setOwnedItems(List<String> ownedItems) {
        this.ownedItems = ownedItems;
    }

    public long getWeeklyBossRespawnDate() {
        return weeklyBossRespawnDate;
    }

    public void setWeeklyBossRespawnDate(long weeklyBossRespawnDate) {
        this.weeklyBossRespawnDate = weeklyBossRespawnDate;
    }
}
