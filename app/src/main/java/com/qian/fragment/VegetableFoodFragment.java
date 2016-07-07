package com.qian.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.qian.R;
import com.qian.activity.HomeActivity;
import com.qian.adapter.DishAdapter;
import com.qian.entity.Dish;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/6/23.
 */
public class VegetableFoodFragment extends Fragment {


    private DishAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.vegetable_fragment,null);

       // final int[] images = new int[]{R.drawable.lotus,R.drawable.tudou,R.drawable.shengcai};
      //  final String[] names = new String[] {"清炒藕片","土豆丝","生菜"};
     //   final int[] prices = new int[] {8,7,7};


        final int[] images = new int[]{R.drawable.shengcai,
                R.drawable.chaobaicai,R.drawable.chaofengwei,
                R.drawable.chaooupian,R.drawable.chaotudousi,R.drawable.fanqiechaojidan,
                R.drawable.ganbiansijidou, R.drawable.hupiqingjiao, R.drawable.jiucaichaodougan,
                R.drawable.jiucaichaojidan, R.drawable.jiuhuangchaojidan, R.drawable.kuguachaojidan,
                R.drawable.lianbaifensi, R.drawable.muerchaoshanyao, R.drawable.qingchaokugua,
                R.drawable.qingjiaochaojidan, R.drawable.qingjiaoyumi, R.drawable.qingsunmuer,
                R.drawable.suchaopinggu, R.drawable.tangculianbai, R.drawable.yuxiangqiezi
        };
        final String[] names = new String[] {"生菜",
                "炒白菜","炒凤尾","炒藕片","炒土豆丝","番茄炒鸡蛋",
                "干煸四季豆","虎皮青椒","韭菜炒豆干","韭菜炒鸡蛋","韭黄炒鸡蛋","苦瓜炒鸡蛋","莲白粉丝","木耳炒山药"
                ,"清炒苦瓜","青椒炒鸡蛋","青椒玉米","青笋木耳","素炒平菇","糖醋莲白","鱼香茄子"};
        final int[] prices = new int[] {7,8,8,10,8,12,10,10,10,10,10,10,10,12,10,10,10,10,10,8,12};




        ArrayList<Dish> dishs = new ArrayList<>();
        for (int i = 0;i < images.length;i ++) {
            Dish dish = new Dish(names[i],images[i],i,prices[i],Dish.VEGETABLE);
            dishs.add(dish);
        }


        GridView gridView = (GridView) view.findViewById(R.id.vegetable_lv);
        mAdapter = new DishAdapter(getActivity(),dishs);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedDish.addDish(new Dish(names[position],images[position],position));
                ((HomeActivity)getActivity())
                        .mBinder.addSelectedDish(new Dish(names[position],images[position],position,prices[position],Dish.VEGETABLE));
                mAdapter.notifyDataSetChanged();
             //   Toast.makeText(VegetableFoodFragment.this.getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
