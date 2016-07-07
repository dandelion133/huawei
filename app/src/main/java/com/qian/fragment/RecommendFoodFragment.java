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
import com.qian.service.SocketService;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/6/23.
 */
public class RecommendFoodFragment extends Fragment {


    private static final String TAG = "RecommendFoodFragment";
    private DishAdapter mAdapter;
    private SocketService.MyBinder binder;
  /*  private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ((HomeActivity)getActivity())
                    .mBinder.addSelectedDish(new Dish());
            mAdapter.notifyDataSetChanged();
            Log.e(TAG,"notifyDataSetChangedl");
            super.handleMessage(msg);
        }
    };*/
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.recommend_fragment, null);


       /* final int[] images = new int[]{R.drawable.fish,R.drawable.red_pig,R.drawable.chicken};
        final String[] names = new String[] {"特色烤鱼","红烧肉","宫保鸡丁"};
        final int[] prices = new int[] {50,30,15};*/

        final int[] images = new int[]{
                R.drawable.chashugubaoniurou,R.drawable.congxiangniuliu,
                R.drawable.fengweifeiniu,R.drawable.maoxuewang,R.drawable.maoyazi,
                R.drawable.piaoxiangfeichang, R.drawable.qingjiaomenya, R.drawable.shaosurou,
                R.drawable.shuizhuroupian, R.drawable.tesejiachangyu, R.drawable.xianglajizhen,
                R.drawable.xianglaxia, R.drawable.xianglazhangzhongbao
        };
        final String[] names = new String[] {"茶树菇爆牛肉","葱香牛柳","风味肥牛","毛血旺","冒鸭子"
                ,"飘香肥肠","青椒焖鸭","烧酥肉","水煮肉片","特色家常鱼","香辣鸡胗","香辣虾","香辣掌中宝"};
        final int[] prices = new int[] {38,32,32,38,28,32,26,24,22,32,28,46,38};


        ArrayList<Dish> dishs = new ArrayList<>();
        for (int i = 0;i < images.length;i ++) {
            Dish dish = new Dish(names[i],images[i],i,prices[i],Dish.RECOMMEND);
            dishs.add(dish);
        }


      //  ArrayList<Dish> dishList = SelectedDish.getInstance().getDishs();


        GridView listView = (GridView) view.findViewById(R.id.recommend_lv);
        mAdapter = new DishAdapter(getActivity(),dishs);

        listView.setAdapter(mAdapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedDish.addDish(new Dish(names[position],images[position],position));
                //Dish(String name, int image, int id,int price, int dishType)
                ((HomeActivity)getActivity())
                       .mBinder.addSelectedDish(new Dish(names[position],images[position],position,prices[position],Dish.RECOMMEND));
                mAdapter.notifyDataSetChanged();
                //Toast.makeText(RecommendFoodFragment.this.getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

   /* public void r() {



        if(mAdapter != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mHandler.sendMessage(new Message());
                }
            }).start();

            Log.e(TAG,"not null");
        } else {
            Log.e(TAG,"null");
        }
    }*/

}
