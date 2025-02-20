package com.taskmaster.appui;
//might be redacted - padua
public class Quest {
    private String name;
    private String difficulty;
    private String reward;
    private String roomCode; // Add this field

    // Default constructor required for Firestore
    public Quest() {}

    public Quest(String name, String difficulty, String reward, String roomCode) {
        this.name = name;
        this.difficulty = difficulty;
        this.reward = reward;
        this.roomCode = roomCode;
    }

    public String getName() {
        return name;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public String getReward() {
        return reward;
    }
    public String getRoomCode() {
        return roomCode;
    }
}