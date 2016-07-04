package com.qian.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.qian.R;
import com.qian.adapter.DishAdapter;
import com.qian.entity.Dish;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/6/23.
 */
public class RecommendFoodFragment extends Fragment {


    private static final String TAG = "RecommendFoodFragment";
    private DishAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.recommend_fragment, null);

        int[] images = new int[]{R.drawable.fish,R.drawable.red_pig,R.drawable.chicken};
        String[] names = new String[] {"特色烤鱼","红烧肉","宫保鸡丁"};
        ArrayList<Dish> dishs = new ArrayList<>();
        for (int i = 0;i < images.length;i ++) {
            Dish dish = new Dish(names[i],images[i]);
            dishs.add(dish);
        }

        GridView listView = (GridView) view.findViewById(R.id.recommend_lv);
        mAdapter = new DishAdapter(getActivity(),dishs);
        listView.setAdapter(mAdapter);

      //  Log.e(TAG,"onCreateView");
        return view;
    }

}
