package com.taskmaster.appui.entity;

public class BossAvatar {
    private String name;
    private int undamagedImageResId;
    private int damagedImageResId;

    public BossAvatar () {}
    public BossAvatar(String name, int undamagedImageResId, int damagedImageResId) {
        this.name = name;
        this.undamagedImageResId = undamagedImageResId;
        this.damagedImageResId = damagedImageResId;
    }

    public BossAvatar (BossAvatar bossAvatar) {
        this.name = bossAvatar.getName();
        this.undamagedImageResId = bossAvatar.getUndamagedImageResId();
        this.damagedImageResId = bossAvatar.getDamagedImageResId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUndamagedImageResId() {
        return undamagedImageResId;
    }

    public void setUndamagedImageResId(int undamagedImageResId) {
        this.undamagedImageResId = undamagedImageResId;
    }

    public int getDamagedImageResId() {
        return damagedImageResId;
    }

    public void setDamagedImageResId(int damagedImageResId) {
        this.damagedImageResId = damagedImageResId;
    }
}