package com.taskmaster.appui.entity;

import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

public class Child {

    String childEmail;
    String childPassword;
    String childUsername;
    String childFirstname;
    String childLastname;
    String parentUID;
    DocumentReference parentRef;
    Integer strength, intelligence;
    Integer avatar;
    Double bossTimer;
    Boolean bossAlive;
    Integer floor;
    Integer gold;

    public Child(String childEmail, String childPassword, String childUsername, String childFirstname, String childLastname, String parentUID, DocumentReference parentRef, Integer strength, Integer intelligence, Integer avatar, Double bossTimer, Boolean bossAlive, Integer floor, Integer gold) {
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

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getAvatar() {
        return avatar;
    }

    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }

    public Double getBossTimer() {
        return bossTimer;
    }

    public void setBossTimer(Double bossTimer) {
        this.bossTimer = bossTimer;
    }

    public Boolean getBossAlive() {
        return bossAlive;
    }

    public void setBossAlive(Boolean bossAlive) {
        this.bossAlive = bossAlive;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }
}
