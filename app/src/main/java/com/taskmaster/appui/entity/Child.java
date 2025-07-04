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
    long bossTimer;
    Boolean bossAlive;
    int floor;
    int gold;
    List<String> ownedItems;

    public Child(String childEmail, String childPassword, String childUsername, String childFirstname, String childLastname, String parentUID, DocumentReference parentRef, int strength, int intelligence, int avatar, long bossTimer, Boolean bossAlive, int floor, int gold, List<String> ownedItems) {
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

    public long getBossTimer() {
        return bossTimer;
    }

    public void setBossTimer(long bossTimer) {
        this.bossTimer = bossTimer;
    }

    public Boolean getBossAlive() {
        return bossAlive;
    }

    public void setBossAlive(Boolean bossAlive) {
        this.bossAlive = bossAlive;
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

    public List<String> getOwnedItems() {
        return ownedItems;
    }

    public void setOwnedItems(List<String> ownedItems) {
        this.ownedItems = ownedItems;
    }
}
