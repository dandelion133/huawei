package com.qian.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qian.R;
import com.qian.entity.Dish;
import com.qian.utils.XmlUtil;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/6/23.
 */


public class DishAdapter extends BaseAdapter {
    public final static String TAG = "DishAdapter";
    private Context mContext;
    private int i = 0;
    private ArrayList<Dish> mDishs = new ArrayList<>();
    private ArrayList<Dish> selectDishs = new ArrayList<>();
    private SharedPreferences sp;
    public DishAdapter(Context context) {
        this.mContext = context;

    }
    public DishAdapter(Context context,ArrayList<Dish> dishs) {
        this.mContext = context;
        this.mDishs = dishs;
        selectDishs = XmlUtil.parserXmlFromLocal();
        sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);


      //  SelectedDish selectedDish = SelectedDish.getInstance();
      //  this.selectDishs = selectedDish.getDishs();
    //    selectDishs = ;

      //  selectDishs = XmlUtil.parserXmlFromLocal();

    }

    @Override
    public int getCount() {
        return mDishs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Dish dish = mDishs.get(position);
        DishViewHolder holder = null;
        if(sp.getBoolean("isModified",true)) {
            selectDishs = XmlUtil.parserXmlFromLocal();
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("isModified", false);
            edit.apply();
        }
        if(convertView == null) {
            holder = new DishViewHolder();
            view = View.inflate(mContext, R.layout.item_grid,null);
            holder.dishImage = (ImageView) view.findViewById(R.id.item_grid_image);
            holder.dishName = (TextView) view.findViewById(R.id.tv_explain);
            holder.dishPrice = (TextView) view.findViewById(R.id.tv_price);
            holder.showNum = (RelativeLayout) view.findViewById(R.id.show_num);
            holder.selectNum = (TextView) view.findViewById(R.id.select_num);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (DishViewHolder) view.getTag();
        }
        holder.showNum.setVisibility(View.INVISIBLE);//这一句不加会有大的BUG
        holder.dishName.setText(dish.getName());
        holder.dishImage.setImageResource(dish.getImage());
        holder.dishPrice.setText("￥:" + dish.getPrice());
        if(selectDishs != null && selectDishs.size() != 0) {

            for (Dish d: selectDishs) {
                if (dish.getDishType() == d.getDishType()) {
                    if(dish.getId() == d.getId()) {

                        Log.i(TAG,d.toString());
                        if(d.getCount() != 0) {

                          //  Log.i(TAG,d.toString());
                            holder.showNum.setVisibility(View.VISIBLE);
                            holder.selectNum.setText(d.getCount() + "");
                        }
                        break;
                    }
                }
            }


        }


        return view;
    }

    public final class DishViewHolder {
        public ImageView dishImage;
        public TextView dishName;
        public TextView dishPrice;
        public RelativeLayout showNum;
        public TextView selectNum;



    }
}
