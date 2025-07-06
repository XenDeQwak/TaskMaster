package com.taskmaster.appui.entity;

import com.google.firebase.firestore.DocumentReference;
import com.taskmaster.appui.view.uimodule.ViewQuest;

public class Quest {

    private String questID;
    private String name, description, creatorUID, assignedUID;
    private long startDate, endDate, completedDate;
    private String rewardStat;
    private String rewardExtra;
    private DocumentReference creatorReference;
    private DocumentReference assignedReference;
    private Number difficulty;
    private String status;

    private ViewQuest qb;


    public Quest(String questID, String name, String description, String creatorUID, String assignedUID, long startDate, long endDate, long completedDate, String rewardStat, String rewardExtra, DocumentReference creatorReference, DocumentReference assignedReference, Number difficulty, String status) {
        this.questID = questID;
        this.name = name;
        this.description = description;
        this.creatorUID = creatorUID;
        this.assignedUID = assignedUID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completedDate = completedDate;
        this.rewardStat = rewardStat;
        this.rewardExtra = rewardExtra;
        this.creatorReference = creatorReference;
        this.assignedReference = assignedReference;
        this.difficulty = difficulty;
        this.status = status;
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

    public String getAssignedUID() {
        return assignedUID;
    }

    public void setAssignedUID(String assignedUID) {
        this.assignedUID = assignedUID;
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

    public long getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(long completedDate) {
        this.completedDate = completedDate;
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

    public DocumentReference getAssignedReference() {
        return assignedReference;
    }

    public void setAssignedReference(DocumentReference assignedReference) {
        this.assignedReference = assignedReference;
    }

    public Number getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Number difficulty) {
        this.difficulty = difficulty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ViewQuest getQb() {
        return qb;
    }

    public void setQb(ViewQuest qb) {
        this.qb = qb;
    }
}
