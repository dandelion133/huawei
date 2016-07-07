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
public class MeatFoodFragment extends Fragment {


    private DishAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.meat_fragment,null);
        final int[] images = new int[]{R.drawable.chaojiza,R.drawable.chaozhugan,
                R.drawable.douganchaorou,R.drawable.ganyaohechao,R.drawable.huiguorou,
                R.drawable.huobaojungan, R.drawable.huobaoyaohua, R.drawable.jiangrousi,
                R.drawable.jimiyacai, R.drawable.jiuhuangrousi, R.drawable.lanroufentiao,
                R.drawable.lanroujiangdou, R.drawable.mapodoufu, R.drawable.muerroupian,
                R.drawable.paojiaorousi, R.drawable.pingguroupian, R.drawable.qingjiaorousi,
                R.drawable.qingsunroupian, R.drawable.suantairousi, R.drawable.tianjiaorousi,
                R.drawable.tudourousi, R.drawable.xiangguroupian, R.drawable.yanjianrou,
                R.drawable.yuxiangrousi };
        final String[] names = new String[] {"炒鸡杂","炒猪肝","豆干炒肉","肝腰合炒","回锅肉","火爆郡肝"
                ,"火爆腰花","酱肉丝","鸡米芽菜","韭黄肉丝","烂肉粉条","烂肉豇豆","麻婆豆腐","木耳肉片","泡椒肉丝"
                ,"平菇肉片","青椒肉丝","青笋肉片","蒜台肉丝","甜椒肉丝","土豆肉丝","香菇肉片","盐煎肉","鱼香肉丝"};
        final int[] prices = new int[] {16,13,16,16,16,18,20,20,13,16,12,12,12,16,16,16,16,16,16,
                16,16,16,16,16,16};

        ArrayList<Dish> dishs = new ArrayList<>();
        for (int i = 0;i < images.length;i ++) {
            Dish dish = new Dish(names[i],images[i],i,prices[i],Dish.MEAT);
            dishs.add(dish);
        }

        GridView gridView = (GridView) view.findViewById(R.id.meat_lv);
        mAdapter = new DishAdapter(getActivity(),dishs);
        gridView.setAdapter(mAdapter);




/*
        GridView gridView = (GridView) view.findViewById(R.id.vegetable_lv);
        mAdapter = new DishAdapter(getActivity(),dishs);
        gridView.setAdapter(mAdapter);
*/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedDish.addDish(new Dish(names[position],images[position],position));
                ((HomeActivity)getActivity())
                        .mBinder.addSelectedDish(new Dish(names[position],images[position],position,prices[position],Dish.MEAT));
                mAdapter.notifyDataSetChanged();
                //   Toast.makeText(VegetableFoodFragment.this.getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }


}
