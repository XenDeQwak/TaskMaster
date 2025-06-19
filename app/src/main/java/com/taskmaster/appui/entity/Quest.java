package com.taskmaster.appui.entity;

import com.google.firebase.firestore.DocumentReference;

public class Quest {

    private String questID;
    private String name, description, creatorUID, assignedUID;
    private long startDate, endDate;
    private String rewardStat;
    private String rewardExtra;
    private DocumentReference creatorReference;
    private DocumentReference assignedReference;


    public Quest(String questID,
                 String name,
                 String description,
                 String creatorUID,
                 DocumentReference creatorReference,
                 long startDate,
                 long endDate,
                 String rewardStat,
                 String rewardExtra) {
        this.questID = questID;
        this.name = name;
        this.description = description;
        this.creatorUID = creatorUID;
        this.creatorReference = creatorReference;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rewardStat = rewardStat;
        this.rewardExtra = rewardExtra;
    }

    public String getQuestID() {
        return questID;
    }

    public void setQuestID(String questID) {
        this.questID = questID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorUID() {
        return creatorUID;
    }

    public void setCreatorUID(String creatorUID) {
        this.creatorUID = creatorUID;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getRewardStat() {
        return rewardStat;
    }

    public void setRewardStat(String rewardStat) {
        this.rewardStat = rewardStat;
    }

    public String getRewardExtra() {
        return rewardExtra;
    }

    public void setRewardExtra(String rewardExtra) {
        this.rewardExtra = rewardExtra;
    }

    public DocumentReference getCreatorReference() {
        return creatorReference;
    }

    public void setCreatorReference(DocumentReference creatorReference) {
        this.creatorReference = creatorReference;
    }

    public String getAssignedUID() {
        return assignedUID;
    }

    public void setAssignedUID(String assignedUID) {
        this.assignedUID = assignedUID;
    }

    public DocumentReference getAssignedReference() {
        return assignedReference;
    }

    public void setAssignedReference(DocumentReference assignedReference) {
        this.assignedReference = assignedReference;
    }
}
