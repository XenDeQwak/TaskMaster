package com.taskmaster.appui.entity;

import com.google.firebase.firestore.DocumentReference;
import com.taskmaster.appui.entity.enums.Stats;

public class Quest {

    private String name, description, creatorUID;
    private long startDate, endDate;
    private Stats rewardStat;
    private String rewardExtra;
    private DocumentReference creatorReference;


    public Quest(String name,
                 String description,
                 String creatorUID,
                 DocumentReference creatorReference,
                 long startDate,
                 long endDate,
                 Stats rewardStat,
                 String rewardExtra) {
        this.name = name;
        this.description = description;
        this.creatorUID = creatorUID;
        this.creatorReference = creatorReference;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rewardStat = rewardStat;
        this.rewardExtra = rewardExtra;
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

    public Stats getRewardStat() {
        return rewardStat;
    }

    public void setRewardStat(Stats rewardStat) {
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
}
