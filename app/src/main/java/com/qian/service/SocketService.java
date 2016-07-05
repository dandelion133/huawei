package com.qian.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.qian.engine.SelectedDish;
import com.qian.entity.Dish;
import com.qian.utils.XmlUtil;

import java.util.ArrayList;


public class SocketService extends Service {
    private SelectedDish selectedDish;
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        selectedDish = SelectedDish.getInstance();

        ArrayList<Dish> dishs =  XmlUtil.parserXmlFromLocal();
        if(dishs != null) {
            selectedDish.setDishs(dishs);
        }

    }


    public class MyBinder extends Binder {
        public void  addSelectedDish(Dish dish) {
            selectedDish.addDish(dish);
        }
        public ArrayList<Dish> getDishList() {
            return selectedDish.getDishs();
        }
        public void deleteDish(Dish dish) {
            selectedDish.deleteDish(dish);
        }
    }


}
