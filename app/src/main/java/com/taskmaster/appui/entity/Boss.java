package com.taskmaster.appui.entity;

public class Boss {
    private final String name;
    private final int undamagedImageResId;
    private final int damagedImageResId;

    public Boss(String name, int undamagedImageResId, int damagedImageResId) {
        this.name = name;
        this.undamagedImageResId = undamagedImageResId;
        this.damagedImageResId = damagedImageResId;
    }

    public int getUndamagedImageResId() {
        return undamagedImageResId;
    }

    public int getDamagedImageResId() {
        return damagedImageResId;
    }

    public String getName() {
        return name;
    }
}