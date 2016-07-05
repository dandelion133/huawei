package com.qian.entity;

import java.io.Serializable;

/**
 * Created by QHF on 2016/7/4.
 */
public class Dish implements Serializable {

    public static final int RECOMMEND = 0;//推荐
    public static final int MEAT = 1;//荤菜
    public static final int VEGETABLE = 2;//素菜
    public static final int SOUP = 3;//汤菜
    public static final int CLOUD = 4;//凉菜
    public static final int WINE = 5;//酒水
    public static final int OTHER = 6;//其他


    private String name;
    private int image;
    private int id;
    private int price;
    private int dishType;
    private int count = 1;//点菜数量

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getDishType() {
        return dishType;
    }

    public void setDishType(int dishType) {
        this.dishType = dishType;
    }


    public Dish() {
    }
    public Dish(int id,int dishType) {
        this.id = id;
        this.dishType = dishType;
    }

    public Dish(String name, int image, int id,int price, int dishType) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.price = price;
        this.dishType = dishType;
    }


    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", image=" + image +
                ", id=" + id +
                ", price=" + price +
                ", dishType=" + dishType +
                ", count=" + count +
                '}';
    }
}
