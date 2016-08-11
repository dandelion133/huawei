package com.qian.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by QHF on 2016/7/7.
 */
public class MyMenu implements Serializable {
    private static final long serializableVersionUID = 3L;
    //菜单状态
    public static final int WAITTING = 0; //等待状态
    public static final int STARTING = 1; //正在上菜
    public static final int CASH = 2; //需要结账
    public static final int OK = 3;//完成上菜
    public static final int COMPLEMENT_DISH = 4;//完成上菜，但是还没结账状态

    private ArrayList<Dish> mDishs;
    private String seatNum;
    private String allPrice;
    private String ipAddress;//发送方ip地址  老板端用
    private int status; //订单状态
    private String submitTime;

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

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

    public MyMenu(ArrayList<Dish> dishs, String seatNum, String allPrice,String submitTime) {
        mDishs = dishs;
        this.seatNum = seatNum;
        this.allPrice = allPrice;
        this.submitTime = submitTime;
    }

    public MyMenu(ArrayList<Dish> dishs, String seatNum, String allPrice, String ipAddress,int status,String submitTime) {
        mDishs = dishs;
        this.seatNum = seatNum;
        this.allPrice = allPrice;
        this.ipAddress = ipAddress;
        this.status = status;
        this.submitTime = submitTime;
    }

    public MyMenu() {
    }

    @Override
    public String toString() {
        return "MyMenu{" +
                "mDishs=" + mDishs +
                ", seatNum='" + seatNum + '\'' +
                ", allPrice='" + allPrice + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", status=" + status +
                ", submitTime='" + submitTime + '\'' +
                '}';
    }
}
