package com.qian.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qian.R;
import com.qian.entity.Dish;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/6/23.
 */


public class DishAdapter extends BaseAdapter {

    private Context mContext;
    private int i = 0;
    private ArrayList<Dish> mDishs = new ArrayList<>();
    public DishAdapter(Context context) {
        this.mContext = context;

    }
    public DishAdapter(Context context,ArrayList<Dish> dishs) {
        this.mContext = context;
        this.mDishs = dishs;
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

        DishViewHolder holder = null;
        if(convertView == null) {
            holder = new DishViewHolder();
            view = View.inflate(mContext, R.layout.item_grid,null);
            holder.dishImage = (ImageView) view.findViewById(R.id.item_grid_image);
            holder.dishName = (TextView) view.findViewById(R.id.tv_explain);
            holder.dishPrice = (TextView) view.findViewById(R.id.tv_price);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (DishViewHolder) view.getTag();
        }
        holder.dishName.setText(mDishs.get(position).getName());
        holder.dishImage.setImageResource(mDishs.get(position).getImage());
        holder.dishPrice.setText("￥:" + mDishs.get(position).getPrice());
        return view;
    }

    public final class DishViewHolder {
        public ImageView dishImage;
        public TextView dishName;
        public TextView dishPrice;
    }
}
