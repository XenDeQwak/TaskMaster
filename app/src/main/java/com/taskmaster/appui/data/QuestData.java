package com.taskmaster.appui.data;

import com.google.firebase.firestore.DocumentReference;
import com.taskmaster.appui.entity.Quest;

import java.util.Objects;

public class QuestData {

    private static final QuestData BlankQuestData = new QuestData(
            "",
            "Blank Quest",
            "Please configure this blank quest.",
            "Awaiting Configuration",
            "",
            0L,
            0L,
            0L,
            "Set Rewards",
            "Set Rewards",
            "creator",
            "assignee",
            null,
            null,
            null,
            0
    );
    public static QuestData newBlankQuestData() {
        return new QuestData(BlankQuestData);
    }

    public void uploadData () {
        questReference.set(this).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) task.getException().printStackTrace();
        });
    }
    public void updateData () {
        questReference.get().addOnCompleteListener(task -> {
            updateObject(Objects.requireNonNull(task.getResult().toObject(QuestData.class)));
        }).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) task.getException().printStackTrace();
        });
    }

    private void updateObject (QuestData questData) {
        this.id = questData.getId();
        this.name = questData.getName();
        this.description = questData.getDescription();
        this.status = questData.getStatus();
        this.failureReason = questData.getFailureReason();
        this.startDate = questData.getStartDate();
        this.endDate = questData.getEndDate();
        this.completedDate = questData.getCompletedDate();
        this.rewardStat = questData.getRewardStat();
        this.rewardExtra = questData.getRewardExtra();
        this.createdBy = questData.getCreatedBy();
        this.assignedTo = questData.getAssignedTo();
        this.questReference = questData.getQuestReference();
        this.difficulty = questData.getDifficulty();
    }

    private String id;
    private String name, description, status, failureReason;
    private long startDate, endDate, completedDate;
    private String rewardStat, rewardExtra;
    private String createdBy, assignedTo;
    private DocumentReference questReference, creatorReference, adventurerReference;
    private Number difficulty;

    public QuestData(String id, String name, String description, String status, String failureReason, long startDate, long endDate, long completedDate, String rewardStat, String rewardExtra, String createdBy, String assignedTo, DocumentReference questReference, DocumentReference creatorReference, DocumentReference adventurerReference, Number difficulty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.failureReason = failureReason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completedDate = completedDate;
        this.rewardStat = rewardStat;
        this.rewardExtra = rewardExtra;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.questReference = questReference;
        this.creatorReference = creatorReference;
        this.adventurerReference = adventurerReference;
        this.difficulty = difficulty;
    }

    public QuestData (QuestData questData) {
        updateObject(questData);
    }

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

    public DocumentReference getCreatorReference() {
        return creatorReference;
    }

    public void setCreatorReference(DocumentReference creatorReference) {
        this.creatorReference = creatorReference;
    }

    public DocumentReference getAdventurerReference() {
        return adventurerReference;
    }

    public void setAdventurerReference(DocumentReference adventurerReference) {
        this.adventurerReference = adventurerReference;
    }

    public Number getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Number difficulty) {
        this.difficulty = difficulty;
    }
}
