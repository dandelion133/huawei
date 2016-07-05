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
public class RecommendFoodFragment extends Fragment {


    private static final String TAG = "RecommendFoodFragment";
    private DishAdapter mAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.recommend_fragment, null);

        final int[] images = new int[]{R.drawable.fish,R.drawable.red_pig,R.drawable.chicken};
        final String[] names = new String[] {"特色烤鱼","红烧肉","宫保鸡丁"};
        final int[] prices = new int[] {50,30,15};
        ArrayList<Dish> dishs = new ArrayList<>();
        for (int i = 0;i < images.length;i ++) {
            Dish dish = new Dish(names[i],images[i],i,prices[i],Dish.RECOMMEND);
            dishs.add(dish);
        }



        GridView listView = (GridView) view.findViewById(R.id.recommend_lv);
        mAdapter = new DishAdapter(getActivity(),dishs);
        listView.setAdapter(mAdapter);





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedDish.addDish(new Dish(names[position],images[position],position));
                ((HomeActivity)getActivity())
                       .mBinder.addSelectedDish(new Dish(names[position],images[position],position,prices[position],Dish.RECOMMEND));
                //Toast.makeText(RecommendFoodFragment.this.getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
