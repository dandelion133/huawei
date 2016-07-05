package com.qian.engine;

import android.util.Log;

import com.qian.entity.Dish;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/7/4.
 */
public class SelectedDish {


    private ArrayList<Dish> mDishs = new ArrayList<>();


    private static SelectedDish instance = null;
    private String  Tag = "SelectedDish" ;

    private SelectedDish() {

    }
    /**
     * 单例模式  全局共享的类
     * @return
     */
    public static SelectedDish getInstance() {
        if(instance == null) {
            return new SelectedDish();
        } else {
            return instance;
        }

    }

    public void addDish(Dish dish) {

      //  Log.e(Tag,"添加");

        if(!isHaved(dish)) {
            mDishs.add(dish);
        }
       // Log.e(Tag,"添加"+mDishs.size());
        for (Dish mDish:mDishs) {
            Log.e(Tag,mDish.toString());
        }
    }
    private boolean isHaved(Dish dish) {
        for (Dish mDish:mDishs) {
            if(mDish.getDishType() == dish.getDishType()) {
                if(mDish.getId() == dish.getId()) {
                    int cnt = mDish.getCount() + 1;
                    mDish.setCount(cnt);
                    return true;
                }
            }
        }
        return false;
    }
    public void deleteDish(Dish dish) {

        Log.e(Tag,"删除"+mDishs.size());

        for (Dish mDish:mDishs) {
            if(mDish.getDishType() == dish.getDishType()) {
                if(mDish.getId() == dish.getId()) {
                    mDishs.remove(mDish);
                    break;
                }
            }
        }
        Log.e(Tag,"删除"+mDishs.size());
    }

    public ArrayList<Dish> getDishs() {
        return mDishs;
    }

    public void setDishs(ArrayList<Dish> dishs) {
        mDishs = dishs;
    }
}
