package com.taskmaster.appui.data;

import com.google.firebase.firestore.DocumentReference;

public class QuestData {

    private String id;
    private String name, description, status, failureReason;
    private long startDate, endDate, completedDate;
    private String rewardStat, rewardExtra;
    private String createdBy, assignedTo;
    private DocumentReference questReference;
    private Number difficulty;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public DocumentReference getQuestReference() {
        return questReference;
    }

    public void setQuestReference(DocumentReference questReference) {
        this.questReference = questReference;
    }

    public Number getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Number difficulty) {
        this.difficulty = difficulty;
    }
}
