package com.taskmaster.appui.Services.CosmeticItemTemplate;

public class CosmeticItem {
    public String name;
    public String subtitle;
    public int price;
    public String priceText;
    public int imageResId;
    public CosmeticItem(String name, String subtitle, int price, int imageResId) {
        this.name = name;
        this.subtitle = subtitle;
        this.price = price;
        this.imageResId = imageResId;
    }
}