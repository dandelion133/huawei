package com.qian.entity;

import java.io.Serializable;

/**
 * Created by QHF on 2016/7/9.
 */
public class SendedMenu implements Serializable {
    private static final long serializableVersionUID = 4L;
    private MyMenu mMenu;
    private String waittingTime;


    public MyMenu getMenu() {
        return mMenu;
    }

    public void setMenu(MyMenu menu) {
        mMenu = menu;
    }

    public String getWaittingTime() {
        return waittingTime;
    }

    public void setWaittingTime(String waittingTime) {
        this.waittingTime = waittingTime;
    }

    public SendedMenu(MyMenu menu, String waittingTime) {
        mMenu = menu;
        this.waittingTime = waittingTime;
    }
    public SendedMenu() {
    }

    @Override
    public String toString() {
        return "SendedMenu{" +
                "mMenu=" + mMenu +
                ", waittingTime='" + waittingTime + '\'' +

                '}';
    }
}
