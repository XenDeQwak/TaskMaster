package com.taskmaster.appui.view.uimodule.CosmeticItemTemplate;

public class CosmeticItem {
    private final int id;
    private final String name;
    public String subtitle;
    public int price;
    public int imageResId;
    public CosmeticItem(int id,String name, String subtitle, int price, int imageResId) {
        this.id = id;
        this.name = name;
        this.subtitle = subtitle;
        this.price = price;
        this.imageResId = imageResId;
    }

    public int getImageResId() {
        return imageResId;
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
}