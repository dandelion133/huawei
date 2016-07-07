package com.qian.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by QHF on 2016/7/7.
 */
public class MyMenu implements Serializable {

    private ArrayList<Dish> mDishs;
    private String seatNum;
    private String allPrice;

    public ArrayList<Dish> getDishs() {
        return mDishs;
    }

    public void setDishs(ArrayList<Dish> dishs) {
        mDishs = dishs;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }

    public String getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(String allPrice) {
        this.allPrice = allPrice;
    }

    public MyMenu(ArrayList<Dish> dishs, String seatNum, String allPrice) {
        mDishs = dishs;
        this.seatNum = seatNum;
        this.allPrice = allPrice;
    }

    @Override
    public String toString() {
        return "MyMenu{" +
                "mDishs=" + mDishs +
                ", seatNum='" + seatNum + '\'' +
                ", allPrice=" + allPrice +
                '}';
    }
}
