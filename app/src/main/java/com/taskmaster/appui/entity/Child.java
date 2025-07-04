package com.taskmaster.appui.entity;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class Child {

    String childEmail;
    String childPassword;
    String childUsername;
    String childFirstname;
    String childLastname;
    String parentUID;
    DocumentReference parentRef;
    int strength, intelligence;
    int avatar;
    Number bossTimer;
    Boolean bossAlive;
    Number floor;
    Number gold;
    Number questCompleted;
    List<String> ownedItems;

    public Child(String childEmail, String childPassword, String childUsername, String childFirstname, String childLastname, String parentUID, DocumentReference parentRef, int strength, int intelligence, int avatar, Number bossTimer, Boolean bossAlive, Number floor, Number gold, Number questCompleted, List<String> ownedItems) {
        this.childEmail = childEmail;
        this.childPassword = childPassword;
        this.childUsername = childUsername;
        this.childFirstname = childFirstname;
        this.childLastname = childLastname;
        this.parentUID = parentUID;
        this.parentRef = parentRef;
        this.strength = strength;
        this.intelligence = intelligence;
        this.avatar = avatar;
        this.bossTimer = bossTimer;
        this.bossAlive = bossAlive;
        this.floor = floor;
        this.gold = gold;
        this.questCompleted = questCompleted;
        this.ownedItems = ownedItems;
    }

    public String getChildEmail() {
        return childEmail;
    }

    public void setChildEmail(String childEmail) {
        this.childEmail = childEmail;
    }

    public String getChildPassword() {
        return childPassword;
    }

    public void setChildPassword(String childPassword) {
        this.childPassword = childPassword;
    }

    public String getChildUsername() {
        return childUsername;
    }

    public void setChildUsername(String childUsername) {
        this.childUsername = childUsername;
    }

    public String getChildFirstname() {
        return childFirstname;
    }

    public void setChildFirstname(String childFirstname) {
        this.childFirstname = childFirstname;
    }

    public String getChildLastname() {
        return childLastname;
    }

    public void setChildLastname(String childLastname) {
        this.childLastname = childLastname;
    }

    public String getParentUID() {
        return parentUID;
    }

    public void setParentUID(String parentUID) {
        this.parentUID = parentUID;
    }

    public DocumentReference getParentRef() {
        return parentRef;
    }

    public void setParentRef(DocumentReference parentRef) {
        this.parentRef = parentRef;
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

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public Number getBossTimer() {
        return bossTimer;
    }

    public void setBossTimer(Number bossTimer) {
        this.bossTimer = bossTimer;
    }

    public Boolean getBossAlive() {
        return bossAlive;
    }

    public void setBossAlive(Boolean bossAlive) {
        this.bossAlive = bossAlive;
    }

    public Number getFloor() {
        return floor;
    }

    public void setFloor(Number floor) {
        this.floor = floor;
    }

    public Number getGold() {
        return gold;
    }

    public void setGold(Number gold) {
        this.gold = gold;
    }

    public Number getQuestCompleted() {
        return questCompleted;
    }

    public void setQuestCompleted(Number questCompleted) {
        this.questCompleted = questCompleted;
    }

    public List<String> getOwnedItems() {
        return ownedItems;
    }

    public void setOwnedItems(List<String> ownedItems) {
        this.ownedItems = ownedItems;
    }
}
