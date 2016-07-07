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
public class ColdFoodFragment extends Fragment {


    private DishAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.cold_fragment,null);


        final int[] images = new int[]{R.drawable.banzhuer,R.drawable.hongyouxinshe,
                R.drawable.huashengmi,R.drawable.shuangkoumuer,R.drawable.tiaoshuihuanggua,
                R.drawable.xiangbanzhutourou
        };
        final String[] names = new String[] {"拌猪耳","红油心舌","花生米","爽口木耳","跳水黄瓜",
                "香拌猪头肉"};
        final int[] prices = new int[] {18,18,8,10,8,16};

        ArrayList<Dish> dishs = new ArrayList<>();
        for (int i = 0;i < images.length;i ++) {
            Dish dish = new Dish(names[i],images[i],i,prices[i],Dish.CLOUD);
            dishs.add(dish);
        }


       // ArrayList<Dish> dishList = SelectedDish.getInstance().getDishs();


        GridView listView = (GridView) view.findViewById(R.id.cold_lv);
        mAdapter = new DishAdapter(getActivity(),dishs);

        listView.setAdapter(mAdapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedDish.addDish(new Dish(names[position],images[position],position));
                //Dish(String name, int image, int id,int price, int dishType)
                ((HomeActivity)getActivity())
                        .mBinder.addSelectedDish(new Dish(names[position],images[position],position,prices[position],Dish.CLOUD));
                mAdapter.notifyDataSetChanged();
                //Toast.makeText(RecommendFoodFragment.this.getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });



       // ListView listView = (ListView) view.findViewById(R.id.cold_lv);
      //  mAdapter = new DishAdapter(getActivity());
      //  listView.setAdapter(mAdapter);



        return view;
    }
}
