package com.taskmaster.appui.view.uimodule.CosmeticItemTemplate;

public class CosmeticItem {
    private int id;
    private String name;
    public String subtitle;
    public int price;
    public int imageResId;

    public CosmeticItem () {}

    public CosmeticItem(int id, String name, String subtitle, int price, int imageResId) {
        this.id = id;
        this.name = name;
        this.subtitle = subtitle;
        this.price = price;
        this.imageResId = imageResId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}