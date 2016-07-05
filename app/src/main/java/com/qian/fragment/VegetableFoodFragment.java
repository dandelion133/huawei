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


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.vegetable_fragment,null);

        final int[] images = new int[]{R.drawable.lotus,R.drawable.tudou,R.drawable.shengcai};
        final String[] names = new String[] {"清炒藕片","土豆丝","生菜"};
        final int[] prices = new int[] {8,7,7};

        ArrayList<Dish> dishs = new ArrayList<>();
        for (int i = 0;i < images.length;i ++) {
            Dish dish = new Dish(names[i],images[i],i,prices[i],Dish.VEGETABLE);
            dishs.add(dish);
        }


        GridView gridView = (GridView) view.findViewById(R.id.vegetable_lv);
        DishAdapter adapter = new DishAdapter(getActivity(),dishs);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedDish.addDish(new Dish(names[position],images[position],position));
                ((HomeActivity)getActivity())
                        .mBinder.addSelectedDish(new Dish(names[position],images[position],position,prices[position],Dish.VEGETABLE));
             //   Toast.makeText(VegetableFoodFragment.this.getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
