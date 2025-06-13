package com.taskmaster.appui.entity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;
import com.taskmaster.appui.entity.enums.Stats;

import java.util.Date;

public class Quest {

    private String name, description;
    @ServerTimestamp
    private Date startDate;
    @ServerTimestamp
    private Date deadlineDate;
    private Stats rewardStat;
    private DocumentReference creatorReference;
    private String creatorUID;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public Stats getRewardStat() {
        return rewardStat;
    }

    public void setRewardStat(Stats rewardStat) {
        this.rewardStat = rewardStat;
    }

    public DocumentReference getCreatorReference() {
        return creatorReference;
    }

    public void setCreatorReference(DocumentReference creatorReference) {
        this.creatorReference = creatorReference;
    }

    public String getCreatorUID() {
        return creatorUID;
    }

    public void setCreatorUID(String creatorUID) {
        this.creatorUID = creatorUID;
    }
}
