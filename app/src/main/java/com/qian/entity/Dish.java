package com.qian.entity;

/**
 * Created by QHF on 2016/7/4.
 */
public class Dish {
    private String name;
    private int image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Dish(String name, int images) {
        this.name = name;
        this.image = images;
    }

    public Dish() {
    }
}
