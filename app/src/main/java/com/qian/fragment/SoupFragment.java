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
public class SoupFragment extends Fragment {


    private DishAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.soup_fragment,null);

        final int[] images = new int[]{R.drawable.huangguapidantang,R.drawable.jiandantang,
                R.drawable.roupiantang,R.drawable.sanxiantang,R.drawable.suancaifensitang,
                R.drawable.suroutang, R.drawable.zicaidanhuatang
        };
        final String[] names = new String[] {"黄瓜皮蛋汤","煎蛋汤","肉片汤","三鲜汤","酸菜粉丝汤"
                ,"酥肉汤","紫菜蛋花汤"};
        final int[] prices = new int[] {10,8,12,12,8,15,8};

        ArrayList<Dish> dishs = new ArrayList<>();
        for (int i = 0;i < images.length;i ++) {
            Dish dish = new Dish(names[i],images[i],i,prices[i],Dish.SOUP);
            dishs.add(dish);
        }


     //   ArrayList<Dish> dishList = SelectedDish.getInstance().getDishs();


        GridView listView = (GridView) view.findViewById(R.id.soup_lv);
        mAdapter = new DishAdapter(getActivity(),dishs);

        listView.setAdapter(mAdapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedDish.addDish(new Dish(names[position],images[position],position));
                //Dish(String name, int image, int id,int price, int dishType)
                ((HomeActivity)getActivity())
                        .mBinder.addSelectedDish(new Dish(names[position],images[position],position,prices[position],Dish.SOUP));
                mAdapter.notifyDataSetChanged();
                //Toast.makeText(RecommendFoodFragment.this.getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });



       // ListView listView = (ListView) view.findViewById(R.id.soup_lv);
      //  mAdapter = new DishAdapter(getActivity());
      //  listView.setAdapter(mAdapter);






        return view;
    }
}
