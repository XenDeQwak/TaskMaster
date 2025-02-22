package com.taskmaster.appui;
//might be redacted - padua
public class Quest {
    private String description;
    private int difficulty;
    private String name;
    private String rewardOptional;
    private String rewardStat;
    private String roomCode;
    private String time;

    public Quest() {}

    public Quest(String description, int difficulty, String name, String rewardOptional, String rewardStat, String roomCode, String time) {
        this.description = description;
        this.difficulty = difficulty;
        this.name = name;
        this.rewardOptional = rewardOptional;
        this.rewardStat = rewardStat;
        this.roomCode = roomCode;
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }

    public String getRewardOptional() {
        return rewardOptional;
    }

    public String getRewardStat() {
        return rewardStat;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getTime() {
        return time;
    }
}